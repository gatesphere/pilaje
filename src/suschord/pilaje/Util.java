// pilaje Util class
// Jacob Peck
// 20120816

package suschord.pilaje;

import suschord.pilaje.*;
import java.util.*;
import java.io.*;

public class Util {
  
  static boolean is_xfer(String word) {
    String[] components = word.split("\\$", 2);
    return components.length == 2 && 
           (components[0].equals("-->") || 
            components[0].equals("->")  ||
            components[0].equals("<--") || 
            components[0].equals("<-")) &&
           (is_valid_name(components[1]) || components[1].equals("!"));      
  }
  
  static boolean is_relocation(String word) {
    if(word.startsWith("&&"))
      return (is_valid_name(word.substring(3)) ||
              word.substring(3).equals("!"))   && 
             word.substring(2,3).equals("$");
    else if (word.startsWith("&"))
      return (is_valid_name(word.substring(2)) ||
              word.substring(2).equals("!"))   && 
             word.substring(1,2).equals("$");
    else
      return false;
  }
  
  static boolean is_valid_name(String name) {
    return (!(name.contains(" ")  || name.contains("!")  || 
              name.contains(":")  || name.contains("$")  ||
              name.contains("(")  || name.contains(")")  ||
              name.contains("#")  || name.contains("\"") ||
              name.contains("~")  || name.contains("->") ||
              name.contains("<-") || name.contains("\\") ||
              name.contains("&")  ||
              name.length() < 1   || is_number(name)     || 
              is_bool(name)       || is_anonmacro(name)  || 
              is_string(name)));
  }
  
  static boolean is_deletion(String word) {
    return word.startsWith("~") && 
           (is_valid_name(word.substring(1)) || 
            is_stack_name(word.substring(1)));
  }
  
  static boolean is_number(String word) {
    return is_double(word) || is_binary(word) || is_octal(word) || is_hex(word);
  }
  
  static boolean is_double(String word) {
    try {
      Double.parseDouble(word);
    } catch (Exception ex) {
      return false;
    }
    return true;
  }
  
  static boolean is_binary(String word) {
    try {
      Integer.parseInt(word.substring(2), 2);
      return word.startsWith("0b");
    } catch (Exception ex) {
        return false;
    }
  }
  
  static boolean is_octal(String word) {
    try {
      Integer.parseInt(word.substring(2), 8);
      return word.startsWith("0o");
    } catch (Exception ex) {
        return false;
    }
  }
  
  static boolean is_hex(String word) {
    try {
      Integer.parseInt(word.substring(2), 16);
      return word.startsWith("0x");
    } catch (Exception ex) {
        return false;
    }
  }
  
  static double to_num(String word) {
    int radix = 10;
    if(is_binary(word)) radix = 2;
    else if(is_octal(word)) radix = 8;
    else if(is_hex(word)) radix = 16;
    if(radix != 10) {
      try {
        return new Double(Integer.parseInt(word.substring(2), radix));
      } catch (Exception ex) {}
    } else {
      try {
        return Double.parseDouble(word);
      } catch (Exception ex) {}
    }
    return 0;
  }
  
  static boolean is_bool(String word) {
    return word.equals("true") || word.equals("false");
  }
  
  static boolean to_bool(String word) {
    return word.equals("true");
  }
  
  static boolean is_anonmacro(String word) {
    return word.startsWith("#(") && word.endsWith(")");
  }
  
  static String to_anonmacro(String word) {
    return word.substring(2,word.length()-1);
  }
  
  static boolean is_stack_name(String word) {
    return word.startsWith("$") && is_valid_name(word.substring(1));
  }
  
  static boolean is_string(String word) {
    return word.startsWith("\"") && word.endsWith("\"");
  }
  
  static String quote(String word) {
    return "\"" + word + "\"";
  }
  
  static String unquote(String word) {
    return word.substring(1,word.length() - 1);
  }
  
  static String unescape(String word) {
    return word.replace("\\t","\t").replace("\\r","\r")
               .replace("\\f", "\f").replace("\\n", "\n")
               .replace("\\\"", "\"");
  }
}
