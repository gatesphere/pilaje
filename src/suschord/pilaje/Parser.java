// pilaje Parser class
// Jacob Peck
// 20120812

package suschord.pilaje;

import suschord.pilaje.*;
import java.util.*;

public class Parser {
  private static HashMap<String, Object> token_map = new HashMap<String, Object>();
  private static HashMap<String, PilajeStack> stack_map = new HashMap<String, PilajeStack>();
  public static PilajeStack currentStack = null;
  
  public static void initialize() {
    // create initial stack
    stack_map.put("$main", new PilajeStack("$main"));
    currentStack = stack_map.get("$main");
    
    // populate builtins
    // printing
    token_map.put(".", new Builtin(".") {
      public void exec() {
        if(Parser.currentStack.size() > 0) System.out.println(Parser.currentStack.peek());
      }
    });
    token_map.put("...", new Builtin("...") {
      public void exec() {
        System.out.println(Parser.currentStack);
      }
    });
    
    // stack manip
    token_map.put("cls", new Builtin("cls") {
      public void exec() {
        Parser.currentStack.empty();
      }
    });
    token_map.put("sz", new Builtin("sz") {
      public void exec() {
        double i = Parser.currentStack.size();
        Parser.currentStack.push(i);
      }
    });
    token_map.put("dup", new Builtin("dup") {
      public void exec() {
        if(Parser.currentStack.size() > 0) Parser.currentStack.push(Parser.currentStack.peek());
      }
    });
    token_map.put("pop", new Builtin("pop") {
      public void exec() {
        if(Parser.currentStack.size() > 0) Parser.currentStack.pop();
      }
    });
    token_map.put("swap", new Builtin("swap") {
      public void exec() {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          Parser.currentStack.push(a);
          Parser.currentStack.push(b);
        }
      }
    });
    token_map.put("rot", new Builtin("rot") {
      public void exec() {
        if(Parser.currentStack.size() > 2) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          Object c = Parser.currentStack.pop();
          Parser.currentStack.push(b);
          Parser.currentStack.push(a);
          Parser.currentStack.push(c);
        }
      }
    });
    token_map.put("-rot", new Builtin("rot") {
      public void exec() {
        Parser.run_input("rot rot");
      }
    });
    token_map.put("over", new Builtin("over") {
      public void exec() {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          Parser.currentStack.push(b);
          Parser.currentStack.push(a);
          Parser.currentStack.push(b);
        }
      }
    });
    token_map.put("nip", new Builtin("nip") {
      public void exec() {
        Parser.run_input("swap pop");
      }
    });
    token_map.put("tuck", new Builtin("tuck") {
      public void exec() {
        Parser.run_input("swap over");
      }
    });
    token_map.put("2dup", new Builtin("2dup") {
      public void exec() {
        Parser.run_input("over over");
      }
    });
    token_map.put("2pop", new Builtin("2pop") {
      public void exec() {
        Parser.run_input("pop pop");
      }
    });
    token_map.put("2swap", new Builtin("2swap") {
      public void exec() {
        if(Parser.currentStack.size() > 3) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          Object c = Parser.currentStack.pop();
          Object d = Parser.currentStack.pop();
          Parser.currentStack.push(b);
          Parser.currentStack.push(a);
          Parser.currentStack.push(d);
          Parser.currentStack.push(c);
        }
      }
    });
    token_map.put("2rot", new Builtin("2rot") {
      public void exec() {
        if(Parser.currentStack.size() > 5) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          Object c = Parser.currentStack.pop();
          Object d = Parser.currentStack.pop();
          Object e = Parser.currentStack.pop();
          Object f = Parser.currentStack.pop();
          Parser.currentStack.push(d);
          Parser.currentStack.push(c);
          Parser.currentStack.push(b);
          Parser.currentStack.push(a);
          Parser.currentStack.push(f);
          Parser.currentStack.push(e);
        }
      }
    });
    token_map.put("2-rot", new Builtin("2rot") {
      public void exec() {
        Parser.run_input("2rot 2rot");
      }
    });
    token_map.put("2over", new Builtin("2over") {
      public void exec() {
        if(Parser.currentStack.size() > 3) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          Object c = Parser.currentStack.pop();
          Object d = Parser.currentStack.pop();
          Parser.currentStack.push(d);
          Parser.currentStack.push(c);
          Parser.currentStack.push(b);
          Parser.currentStack.push(a);
          Parser.currentStack.push(d);
          Parser.currentStack.push(c);
        }
      }
    });
    token_map.put("2nip", new Builtin("2nip") {
      public void exec() {
        Parser.run_input("2swap 2pop");
      }
    });
    token_map.put("2tuck", new Builtin("2tuck") {
      public void exec() {
        Parser.run_input("2swap 2over");
      }
    });
    
    // arithmetic and string manip
    token_map.put("+", new Builtin("+") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof String || b instanceof String) {
            String astr = a.toString();
            String bstr = b.toString();
            if(Parser.is_string(astr)) astr = Parser.unquote(astr);
            if(Parser.is_string(bstr)) bstr = Parser.unquote(bstr);
            Parser.currentStack.push("\"" + astr + bstr + "\"");
          } else if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)a + (double)b);
          } else { 
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put("-", new Builtin("-") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)b - (double)a);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put("*", new Builtin("*") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof String ^ b instanceof String) {
            String astr = "";
            double bdbl = 0;
            StringBuilder bldr = new StringBuilder();
            if(a instanceof String && b instanceof Double) {
              astr = a.toString();
              bdbl = (double)b;
            } else if(a instanceof Double && b instanceof String) {
              astr = b.toString();
              bdbl = (double)a;
            } else {
              System.out.println("  >> ERROR: Incorrect types.");
              throw new Exception();
            }
            if(Parser.is_string(astr)) astr = Parser.unquote(astr);
            for(int i = 0; i < bdbl; i++) bldr.append(astr);
            Parser.currentStack.push("\"" + bldr.toString() + "\"");
          } else if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)a * (double)b);
          } else { 
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put("/", new Builtin("/") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)b / (double)a);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put("%", new Builtin("%") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)b % (double)a);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put("<<", new Builtin("<<") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)(((Double)b).intValue() << ((Double)a).intValue()));
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put(">>", new Builtin(">>") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)(((Double)b).intValue() >> ((Double)a).intValue()));
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    
    // boolean logic
    token_map.put("=", new Builtin("=") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)a == (double)b);
          } else if(a instanceof String && b instanceof String) {
            Parser.currentStack.push(a.toString().equals(b.toString()));
          } else if(a instanceof Boolean && b instanceof Boolean) {
            Parser.currentStack.push((boolean)a == (boolean)b);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put(">", new Builtin(">") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)a < (double)b);
          } else if(a instanceof String && b instanceof String) {
            Parser.currentStack.push(a.toString().compareTo(b.toString()) < 0);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put("<", new Builtin("<") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)a > (double)b);
          } else if(a instanceof String && b instanceof String) {
            Parser.currentStack.push(a.toString().compareTo(b.toString()) > 0);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put(">=", new Builtin(">=") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)a <= (double)b);
          } else if(a instanceof String && b instanceof String) {
            Parser.currentStack.push(a.toString().compareTo(b.toString()) <= 0);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put("<=", new Builtin("<=") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Double && b instanceof Double) {
            Parser.currentStack.push((double)a >= (double)b);
          } else if(a instanceof String && b instanceof String) {
            Parser.currentStack.push(a.toString().compareTo(b.toString()) >= 0);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    token_map.put("not", new Builtin("not") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 0) {
          Object a = Parser.currentStack.pop();
          if(a instanceof Boolean) {
            Parser.currentStack.push(!(boolean)a);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    }); 
    token_map.put("and", new Builtin("and") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Boolean && b instanceof Boolean) {
            Parser.currentStack.push((boolean)a && (boolean)b);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    }); 
    token_map.put("or", new Builtin("or") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 1) {
          Object a = Parser.currentStack.pop();
          Object b = Parser.currentStack.pop();
          if(a instanceof Boolean && b instanceof Boolean) {
            Parser.currentStack.push((boolean)a || (boolean)b);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });             
    
    // flow control
    token_map.put("if", new Builtin("if") {
      public void exec() throws Exception {
        if(Parser.currentStack.size() > 2) {
          String t = Parser.currentStack.pop().toString();
          String e = Parser.currentStack.pop().toString();
          Object c = Parser.currentStack.pop();
          if(c instanceof Boolean) {
            boolean condition = (boolean)c;
            if(condition)  Parser.run_input(t);
            else Parser.run_input(e);
          } else {
            System.out.println("  >> ERROR: Incorrect types.");
            throw new Exception();
          }
        }
      }
    });
    
    // direct calls
    token_map.put("nop", new Builtin("nop") {
      public void exec() {}
    });
    token_map.put("call", new Builtin("call") {
      public void exec() {
        if(Parser.currentStack.size() > 0) {
          Parser.run_input(Parser.currentStack.pop().toString());
        }
      }
    });
    
    // meta
    token_map.put("!bye", new Builtin("!bye") {
      public void exec() {
        System.out.println("goodbye");
        REPL.running = false;
      }
    });
    token_map.put("!macros", new Builtin("!macros") {
      public void exec() {
        System.out.println("registered macros:");
        for(Object o : token_map.values()) {
          if(o instanceof Macro) {
            Macro m = (Macro)o;
            System.out.println(m.name + " := " + m.contents);
          }
        }
      }
    });
  }
  
  public static void run_input(String input) {
    // die on empty input
    if(input == null || input.trim().length() == 0) return;
    input = input.trim();
    
    // check for macro definition
    if(input.startsWith(":")) {
      Scanner sc = new Scanner(input);
      String name = sc.next().substring(1);
      StringBuilder contents = new StringBuilder("");
      while(sc.hasNext()) contents.append(sc.next() + " ");
      Object e = token_map.get(name);
      if(e != null && e instanceof Builtin) {
        System.out.println("  >> ERROR: Cannot redefine builtin " + name);
        return;
      }
      else if(e != null && e instanceof Macro)
        System.out.println("  >> WARNING: Redefining macro " + name);
      token_map.put(name, new Macro(name, contents.toString().trim()));
      return;
    }
    
    // discard comments
    input = input.split("//", 2)[0];
    
    // selectively split (preserve strings and anonmacros)
    ArrayList<String> words = new ArrayList<String>();
    String[] input_split = input.split(" ");
    StringBuilder gather = new StringBuilder();
    boolean anonmacro = false;
    int macrodepth = 0;
    for(int i = 0; i < input_split.length; i++) {
      String w = input_split[i];
      if(w.startsWith("#(")) {
        anonmacro = true;
        macrodepth++;
      }
      if(anonmacro) gather.append(w + " ");
      else words.add(w);
      if(w.endsWith(")")) macrodepth--;
      if(macrodepth == 0 && anonmacro) {
        anonmacro = false;
        words.add(gather.toString().trim());
        gather = new StringBuilder();
      }
    }
    if(anonmacro) {
      System.out.println("  >> ERROR: Unterminated anonymous macro. Ignoring all input.");
      return;
    }
    
    ArrayList<String> words2 = new ArrayList<String>();
    gather = new StringBuilder();
    boolean quote = false;
    for(String w : words) {
      if(w.startsWith("\"")) quote = true;
      if(quote) gather.append(w + " ");
      else words2.add(w);
      if(w.endsWith("\"") && w.length() > 1) {
        quote = false;
        words2.add(gather.toString().trim());
        gather = new StringBuilder();
      }
    }
    if(quote) {
      System.out.println("  >> ERROR: Untermintated string literal. Ignoring all input.");
      return;
    }
    
    // run each word
    for(PilajeStack ps : stack_map.values()) ps.backup();
    try{
      for(String word : words2) run_word(word);
    } catch (Exception ex) {
      for(PilajeStack ps : stack_map.values()) ps.rollback();
      ex.printStackTrace();
      System.out.println("  >> ERROR: Something went wrong.  Reverting all stacks to previous state.");
    }
    
  }
  
  private static void run_word(String word) throws Exception {
    // look up the token (macro/builtin?)
    Object token = token_map.get(word);
    if(token != null) {
      if(token instanceof Builtin) ((Builtin)token).exec();
      else if(token instanceof Macro) run_input(((Macro)token).contents);
      
      return;
    }
    
    // not a token, must be data
    if(is_number(word)) currentStack.push(to_num(word));
    else if(is_bool(word)) currentStack.push(to_bool(word));
    else if(is_anonmacro(word)) currentStack.push(to_anonmacro(word));
    else if(is_string(word)) currentStack.push(quote(word));
    else if(is_stack_name(word)) change_stack(word);
    else System.out.println("  >> ERROR: Unknown word, ignoring " + word );
    
    return;
  }
  
  private static boolean is_number(String word) {
    return is_double(word);
  }
  
  private static boolean is_double(String word) {
    try {
      double d = Double.parseDouble(word);
    } catch (Exception ex) {
      return false;
    }
    return true;
  }
  
  private static double to_num(String word) {
    try {
      return Double.parseDouble(word);
    } catch (Exception ex) {}
    return 0;
  }
  
  private static boolean is_bool(String word) {
    return word.equals("true") || word.equals("false");
  }
  
  private static boolean to_bool(String word) {
    return word.equals("true");
  }
  
  private static boolean is_anonmacro(String word) {
    return word.startsWith("#(") && word.endsWith(")");
  }
  
  private static String to_anonmacro(String word) {
    return word.substring(2,word.length()-1);
  }
  
  private static boolean is_stack_name(String word) {
    return word.startsWith("$");
  }
  
  private static void change_stack(String word) {
    PilajeStack s = stack_map.get(word);
    if(s == null) {
      s = new PilajeStack(word);
      stack_map.put(word, s);
    }
    currentStack = s;
  }
  
  public static boolean is_string(String word) {
    return word.startsWith("\"") && word.endsWith("\"");
  }
  
  private static String quote(String word) {
    return word;
  }
  
  public static String unquote(String word) {
    return word.substring(1,word.length() - 1);
  }
  
}
