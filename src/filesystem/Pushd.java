package filesystem;

import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

import driver.JShell;

public class Pushd extends Command {
  private Stack<DirStack> pushDir;
  private DirStack curDir;

  /**
   * Constructs a pushd object
   * 
   * @param DirStack which contains the current working directory
   * @param Stack<DirStack> a stack which holds DirStacks
   */
  public Pushd(DirStack curDir, Stack<DirStack> pushDir) {
    super();
    this.curDir = curDir;
    this.pushDir = pushDir;
    this.pushd();
    JShell.useHistory = false;
  }

  /**
   * Pushes the current working directory on the stack
   * 
   */
  public void pushd() {

    pushDir.push(curDir);
  }

  /**
   * Returns the pushdDir stack
   * 
   * @returns Stack<DirStack>
   * 
   */
  public Stack<DirStack> getPushDir() {
    return pushDir;
  }

}
