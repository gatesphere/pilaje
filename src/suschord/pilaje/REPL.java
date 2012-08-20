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
    
    while (running)
      run_input(get_input());
      
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
    boolean go_again = true;
    do {
      try {
        line = line + inreader.readLine();
        if (line.endsWith("\\")) {
          line = line.substring(0, line.length() - 1);
          System.out.print("> ");
        } else go_again = false;
      } catch (Exception ex) {}
    } while (go_again);
    return line;
  }
  
  public static void get_script_input(String filename) throws Exception {
    Scanner sc = new Scanner(new File(filename));
    String line;
    boolean go_again;
    while (sc.hasNextLine()) {
      go_again = true;
      line = "";
      do {
        if(sc.hasNextLine()) {
          line = line + sc.nextLine();
          if(line.endsWith("\\"))
            line = line.substring(0, line.length() - 1);
          else go_again = false;
        } else go_again = false;
      } while (go_again);
      Parser.run_input(line);
    }
  }
  
  public static void prompt() {
    System.out.print(Parser.currentStack.name + "[" + Parser.currentStack.size() + "]> ");
  }
  
  public static void run_input(String input) {
    Parser.run_input(input);
  }
}
