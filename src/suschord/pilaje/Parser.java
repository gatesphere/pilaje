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
    token_map.put("...", new Builtin("...") {
      public void exec() {
        System.out.println(Parser.currentStack);
      }
    });
    token_map.put("!bye", new Builtin("!bye") {
      public void exec() {
        System.out.println("goodbye");
        REPL.running = false;
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
      if(w.endsWith("\"")) {
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
  
  private static boolean is_string(String word) {
    return word.startsWith("\"") && word.endsWith("\"");
  }
  
  private static String quote(String word) {
    return word;
  }
  
}
