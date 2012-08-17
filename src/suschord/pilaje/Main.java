// pilaje main class
// Jacob Peck
// 20120811

package suschord.pilaje;

import suschord.pilaje.*;

public class Main {
  public static final String version = "pilaje 20120817"; 
 
  private static boolean display_version = true;
  private static boolean exit_after_version = false;
  private static boolean help = false;
  private static String scriptfile = null;

  public static void main(String[] args) {
    parseargs(args);
    if(display_version && scriptfile == null) System.out.println(version);
    if(exit_after_version) System.exit(0);
    if(help) print_help();
    if(scriptfile == null) REPL.start();
    else REPL.run_script(scriptfile);
  }

  private static void parseargs(String[] args) {
    int i = 0;
    String arg;
    while (i < args.length && args[i].startsWith("-")) {
      arg = args[i];
      if(arg.equals("-h") | arg.equals("-?") | arg.equals("--help")) help = true;
      if(arg.equals("-v") | arg.equals("--version")) exit_after_version = true;
      i++;
    }
    if (i < args.length && !args[i].startsWith("-"))
      scriptfile = args[i];
  }
  
  private static void print_help() {
    System.out.println("The following command line arguments are valid:");
    System.out.println("\t-h\t\tPrint this help and exit.");
    System.out.println("\t-?\t\tSame as -h.");
    System.out.println("\t--help\t\tSame as -h.");
    System.out.println("\t-v\t\tPrint version info and exit.");
    System.out.println("\t--version\tSame as -v.");
    
    System.exit(0);
  }
}

