// pilaje PilajeStack class
// Jacob Peck
// 20120812

package suschord.pilaje;

import suschord.pilaje.*;
import java.util.*;

public class PilajeStack {
  private Stack<Object> data = new Stack<Object>();
  private Stack<Object> backup_data = null;
  public String name = "";
  
  public PilajeStack(String name) {
    this.name = name;
  }
  
  public Object peek() {
    return data.peek();
  }
  
  public Object pop() {
    return data.pop();
  }
  
  public void empty() {
    data.clear();
  }
  
  public void push(Object val) {
    data.push(val);
  }
  
  public int size() {
    return data.size();
  }
  
  public String toString() {
    Object[] stackval = data.toArray();
    StringBuilder sb = new StringBuilder("[");
    for(int i = 0; i < data.size(); i++) {
      sb.append(stackval[i]);
      if(i != data.size() - 1) sb.append(", ");
    }
    sb.append("]<=");
    return sb.toString();
  }
  
  @SuppressWarnings("unchecked")
  public void backup() {
    backup_data = (Stack<Object>)data.clone();
  }
  
  public void rollback() {
    data = backup_data;
    backup_data = null;
  }
}
