package filesystem;

import javax.swing.tree.DefaultMutableTreeNode;

public class PrintWorkingDirectory extends Command {

  private DefaultMutableTreeNode userNode;
  private DirStack originalPath;
  private DirStack invStack;
  private DirStack returnStack;
  private Directory userDirectory;
  private DirStack someStack;
  private String stringPath = "";


  public PrintWorkingDirectory(DirStack path) {
    super(null);
    this.originalPath = path;

  }

  /***
   * Generates a path from the root folder to the current working directory in
   * String provided that the path exists, else, returns the appropriate error.
   * 
   * @return returns the path from the root to current working directory
   */
  public String getPath() {

    DirStack invStack = new DirStack(this.originalPath.popped());
    while (!this.originalPath.isEmpty()) {
      // Inverts the whole working directory stack
      invStack.pushed(this.originalPath.popped());
    }
    if (invStack.size() == 1) {
      // If the working directory is the root directory, then simply return the
      // name of the root directory
      userNode = invStack.peekd();
      returnStack = new DirStack(userNode);
      userDirectory = ((Directory) userNode.getUserObject());
      stringPath += userDirectory.getDirName();
      // Restores working directory stack so it remains unchanged
      this.originalPath = returnStack;
      return stringPath;
    }
    if (invStack.size() > 1) {

      DirStack returnStack = new DirStack(invStack.popped());
      while (!invStack.isEmpty()) {
        // Iterates through the children of the working directory
        userNode = invStack.popped();
        // The popped element is returned to returnStack so that it is not gone
        returnStack.pushed(userNode);
        if (!invStack.isEmpty()) {
          userDirectory = ((Directory) userNode.getUserObject());
          // Creates the working directory path by adding the directory name one
          // by one, seperated by a slash, to stringPath"
          stringPath = stringPath + "/" + userDirectory.getDirName();

        }
        if (invStack.isEmpty()) {
          userDirectory = ((Directory) userNode.getUserObject());
          // When the last node in the working directory stack is reached, it
          // reinstates the original path by replacing it with returnStack so
          // that it remains unmodified.
          stringPath = stringPath + "/" + userDirectory.getDirName();
          this.originalPath = returnStack;
          return stringPath;

        }


      }
      someStack = returnStack;
    }
    this.originalPath = someStack;
    return stringPath;
  }

  public DirStack returnStack() {
    return this.originalPath;
  }
}

