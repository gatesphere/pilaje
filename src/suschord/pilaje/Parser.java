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
    
  }
  
  public static void run_input(String input) {
    // die on empty input
    if(input == null || input.trim().length() == 0) return;
    input = input.trim();
    
    // check for macro definition
    
    // discard comments
    
    // selectively split (preserve strings and anonmacros)
    
    // run each word
    for(PilajeStack ps : stack_map.values()) ps.backup();
    try{
      
    } catch (Exception ex) {
      for(PilajeStack ps : stack_map.values()) ps.rollback();
      System.out.println("  >> ERROR: Something went wrong.  Reverting all stacks to previous state.");
    }
    
  }
  
  private static void run_word(String word) throws Exception {
    
  }
}