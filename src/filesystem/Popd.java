package filesystem;

import java.util.Stack;

import driver.JShell;

public class Popd extends Command {
  private Stack<DirStack> pushDir;
  private DirStack curDir;

  /**
   * Creates a Popd object
   * 
   * @param Stack<DirStack> a pushdDir stack is taken
   * @param DirStack the current working directory is taken in
   * 
   */
  public Popd(Stack<DirStack> pushDir, DirStack curDir) {
    super();
    this.pushDir = pushDir;
    this.curDir = curDir;
    JShell.useHistory = false;
    if (pushDir.size() == 0) {
      System.out.println("No directory to pop");
    }
  }

  /**
   * Removes the top item from the pushdDir stack and returns it
   * 
   * @returns DirStack
   * 
   */
  public DirStack popd() {
    DirStack a = null; 
    try {
      a = pushDir.pop();
    } catch (Exception e) {
      System.out.println("Stack is empty");
    }
    return a;
  }

}
