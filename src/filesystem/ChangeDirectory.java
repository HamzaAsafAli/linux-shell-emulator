package filesystem;


import javax.swing.tree.DefaultMutableTreeNode;

public class ChangeDirectory extends Command {
  private String userArgs;
  private DefaultMutableTreeNode root;
  private DefaultMutableTreeNode WD;
  private DefaultMutableTreeNode workingNode;
  private DirStack tempWD;
  private DirStack tempDirStack;
  private DefaultMutableTreeNode currentNode;
  private String someString;
  private Directory userDir;
  private String nameDir;
  private Object userObject;
  private DirStack originalWD;


  public ChangeDirectory(String userArgs, DefaultMutableTreeNode currentWD,
      DirStack WDPath, DefaultMutableTreeNode rootNode) {

    this.WD = currentWD;
    this.userArgs = userArgs;
    this.tempWD = WDPath;
    this.originalWD = WDPath;
    this.tempDirStack = new DirStack(rootNode);

  }

  public ChangeDirectory(DefaultMutableTreeNode currentWD, DirStack WDPath,
      DefaultMutableTreeNode rootNode) {
    this.WD = currentWD;
    this.tempWD = WDPath;
    this.originalWD = WDPath;
    this.tempDirStack = new DirStack(rootNode);
  }

  public void setUserArgs(String userArgs) {
    this.userArgs = userArgs;
  }

  /**
   * depending on the user given path provided that the path exists. Returns the
   * initial working directory path stack if the path does not exist
   * 
   * @returns DirStack
   * 
   */
  public DirStack cd() {
    boolean isAbsolute = false;


    if (this.userArgs.equals("..")) {
      // pops the working directory stack if argument is "/../"
      if (this.tempWD.size() == 1) {
        System.out.println("Currently at the root folder");
      }
      // returns a notification if current working directory is root folder, if
      // the working directory path stack only has one content
      if (this.tempWD.size() > 1) {
        tempWD.popped();
      }


    }

    if (this.userArgs.startsWith("/") && !this.userArgs.equals("..")) {
      // if argument is absolute path, restarts the working directory path stack
      // from the root folder
      isAbsolute = true;
    }
    if (this.tempWD.peekd().getChildCount() == 0 && isAbsolute == false) {
      // if working directory is empty and argument exist, notify that the
      // folder does not exist
      System.out.println("The specified folder does not exist");
    }

    if (isAbsolute && !this.userArgs.equals("..")) {
      // Calls the implementation for absolute path.
      while (this.originalWD.size() != 1) {
        // Acesses the root node from the working directory path stack
        currentNode = this.originalWD.popped();
        this.tempDirStack.pushed(currentNode);
      }
      this.root = this.originalWD.peekd();
      // Sets the root variable as the root of the working directory path stack
      while (this.tempDirStack.size() != 1) {
        // Restores the working directory path stack so it remains unmodified
        currentNode = this.tempDirStack.popped();
        this.originalWD.pushed(currentNode);
      }
      this.tempDirStack = new DirStack(this.root);
      // Reinitializes the temporary directory stack with the root folder

      int i = 1;
      String[] user = this.userArgs.split("/");
      while (i < user.length) {
        // Iterates through the argument to access the desired directory names
        // one by one by setting it to this.nameDir
        this.nameDir = user[i];
        int j = 0;
        int k = 0;
        while (j < this.root.getChildCount()) {
          // Iterates through the children of this.root TreeNode
          this.currentNode = (DefaultMutableTreeNode) this.root.getChildAt(j);
          this.userObject = this.currentNode.getUserObject();
          if (this.userObject instanceof Directory) {
            // Gets the name of the directory contained in the child node of
            // this.root TreeNode
            this.userDir = (Directory) this.userObject;

            this.someString = this.userDir.getDirName();
            if (this.someString.equals(this.nameDir)) {
              // If name this.nameDir matches with
              // the name of directory contained in one of the child nodes of
              // this.root TreeNode, pushes that child node with the same name
              // to this.tempDirStack
              this.tempDirStack.pushed(this.currentNode);
              k = -1;
            }
          }
          k++;
          j++;
          if (k == this.root.getChildCount()) {
            // If iteration of the children of this.root is complete without
            // finding any path, without appending any of the children nodes to
            // the working directory path stack, returns the original working
            // directory path stack and prints an error message.
            System.out.println("The specified path does not exist.");

            return this.originalWD;
          }
          this.root = this.tempDirStack.peekd();
          // Reinitializes this.root, in which the children will be iterated,
          // into the root that was appended.

          if (this.root.getChildCount() == 0
              && user[i] != user[user.length - 1]) {
            // If this.root has no children but the iteration of the argument of
            // the argument is not finished, return the original working
            // directory path stack and print folder not found message
            System.out.println("The specified path does not exist.");
            return this.originalWD;

          }


        }
        i++;

      }

      this.tempWD = this.tempDirStack;



    }

    if (!isAbsolute && !this.userArgs.equals("..")
    // if argument is not absolute path, pushes the specified tree nodes to the
    // working directory path stack
        && this.tempWD.peekd().getChildCount() != 0) {
      int i = 0;
      int l = 0;
      this.workingNode = this.tempWD.peekd();
      String[] user = this.userArgs.split("/");
      while (i < user.length) {
        // Iterates through the argument to access the desired directory names
        // one by one by setting it to this.nameDir
        this.nameDir = user[i];
        int j = 0;
        int k = 0;


        while (j < this.workingNode.getChildCount()) {
          // Iterates through the children of this.workingNode TreeNode
          this.currentNode =
              (DefaultMutableTreeNode) this.workingNode.getChildAt(j);
          this.userObject = this.currentNode.getUserObject();
          if (this.userObject instanceof Directory) {
            // Gets the name of the directory contained in the child node of
            // this.workingNode TreeNode
            this.userDir = (Directory) this.userObject;
            this.someString = this.userDir.getDirName();
            if (this.someString.equals(this.nameDir)) {
              // If string this.nameDir matches with
              // the name of directory contained in one of the child nodes of
              // this.workingNode TreeNode, pushes that child node with the same
              // name
              // to this.tempWD
              k = -1;
              l++;
              this.tempWD.pushed(this.currentNode);
            }
          }

          j++;
          k++;
          if (k == this.workingNode.getChildCount()) {
            // If this.root has no children but the iteration of the argument of
            // the argument is not finished, all the increments of the original
            // working directory path stack is reversed and prints folder not
            // found message
            while (l != 0) {
              this.originalWD.popped();
              l--;
            }
            System.out.println("The specified path does not exist.");
            return this.originalWD;
          }
          this.workingNode = this.tempWD.peekd();
          if (this.workingNode.getChildCount() == 0
              && user[i] != user[user.length - 1]) {
            // If the current working node has no children, returns the
            // appropriate message
            System.out.println("The specified path does not exist.");
            return this.originalWD;
          }


        }
        i++;

      }

    }
    return this.tempWD;
  }

  public DirStack getOriginalWD() {
    return originalWD;
  }

  public void setOriginalWD(DirStack originalWD) {
    this.originalWD = originalWD;
  }
}
