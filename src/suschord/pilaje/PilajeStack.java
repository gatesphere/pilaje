// pilaje PilajeStack class
// Jacob Peck
// 20120812

package suschord.pilaje;

import suschord.pilaje.*;
import java.util.*;

public class PilajeStack {
  private Stack<Object> data = new Stack<Object>();
  private Stack<Object> backup_data = new Stack<Object>();
  public String name = "";
  
  public PilajeStack(String name) {
    this.name = name;
  }
  
  public Object peek() {
    if(size() > 0) return data.peek();
    else return null;
  }
  
  public Object pop() {
    if(size() > 0) return data.pop();
    else return null;
  }
  
  public void empty() {
    data.clear();
  }
  
  public void push(Object val) {
    if(val != null) data.push(val);
  }
  
  public int size() {
    return data.size();
  }
  
  @SuppressWarnings("unchecked")
  public Stack<Object> get_clone_data() {
    return (Stack<Object>)data.clone();
  }
  
  public void set_data(Stack<Object> d) {
    data = d;
  }
  
  public void push_whole_stack(Stack<Object> d) {
    Object[] a = d.toArray();
    for(int i = 0; i < a.length; i++)
      data.push(a[i]);
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
  
  public void reverse() {
    Object[] a = data.toArray();
    data = new Stack<Object>();
    for(int i = a.length - 1; i >= 0; i--)
      data.push(a[i]);
  }
}
