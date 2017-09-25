package filesystem;

import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

public class DirStack {

  private Stack<DefaultMutableTreeNode> CurDir;
  private int size;

  public void setCurDir(Stack<DefaultMutableTreeNode> curDir) {
    CurDir = curDir;
  }

  public DirStack() {

  }



  public DirStack(DefaultMutableTreeNode node) {
    CurDir = new Stack<DefaultMutableTreeNode>();
    size++;
    CurDir.push(node);
  }

  public void pushed(DefaultMutableTreeNode node) {
    CurDir.push(node);
    size++;
  }

  public DefaultMutableTreeNode popped() {
    if (CurDir.isEmpty() == false) {
      size--;
    }
    return CurDir.pop();
  }

  public DefaultMutableTreeNode peekd() {
    return CurDir.peek();
  }

  public boolean isEmpty() {
    return CurDir.isEmpty();
  }

  public int size() {
    return size;

  }

}

