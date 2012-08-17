// pilaje REPL class
// Jacob Peck
// 20120811

package suschord.pilaje;

import suschord.pilaje.*;
import java.util.*;
import java.io.*;

public class REPL {
  static boolean running = true;
  private static final BufferedReader inreader = new BufferedReader(new InputStreamReader(System.in));
  
  public static void start() {
    Parser.initialize();
    
    while (running) {
      run_input(get_input());
    }
    System.exit(0);
  }
  
  public static void run_script(String scriptfile) {
    Parser.initialize();
    Parser.run_input(Util.quote(scriptfile) + " !import");
    System.exit(0);
  }
      
  public static String get_input() {
    prompt();
    String line = "";
    try {
      line = inreader.readLine();
    } catch (Exception ex) {}
    return line;
  }
  
  public static void prompt() {
    System.out.print(Parser.currentStack.name + "[" + Parser.currentStack.size() + "]> ");
  }
  
  public static void run_input(String input) {
    Parser.run_input(input);
  }
}
