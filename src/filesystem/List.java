package filesystem;


import javax.swing.tree.DefaultMutableTreeNode;

public class List extends Command {
  private DefaultMutableTreeNode parentNode;
  private DefaultMutableTreeNode childNode;
  private Object userObject;
  private String[] userParameters;
  private String directoryList;
  private ChangeDirectory newWorkingDirectory;
  private DirStack stackPath;
  private DirStack desiredPath;
  private DirStack inversePath;
  private DefaultMutableTreeNode rootNode;
  private String stringOutput;

  public List(String userCommand, DefaultMutableTreeNode currentWD,
      DirStack path, DefaultMutableTreeNode rootNode) {
    this.stackPath = path;
    this.parentNode = path.peekd();
    this.userParameters = userCommand.split(" ");
    this.rootNode = rootNode;
    this.inversePath = new DirStack(rootNode);
    this.directoryList = "";



  }

  /**
   * Generates a list of all the names of files and directories contained within
   * the current working directory, or depending on the given path. If the
   * working directory is empty, or the given path does not exist, returns the
   * appropriate message.
   * 
   * @return A string with each file name appearing on different lines
   */
  public String list() {
    this.inversePath.popped();
    if (this.stackPath.size() > 1) {
      while (this.stackPath.size() != 1) {
        // inverts the working directory stack
        this.inversePath.pushed(this.stackPath.popped());

      }
      if (this.stackPath.size() == 1) {
        this.desiredPath = new DirStack(this.stackPath.peekd());
      }
      while (!this.inversePath.isEmpty()) {
        // reinstates the original working directory stack while also
        // duplicating it in the process so it remains unchanged
        this.rootNode = this.inversePath.popped();
        this.desiredPath.pushed(this.rootNode);
        this.stackPath.pushed(this.rootNode);
      }
    }
    if (this.stackPath.size() == 1) {
      // copies the working directory stack if it has only one node
      this.desiredPath = new DirStack(this.stackPath.peekd());
    }
    if (this.userParameters.length == 1) {
      // if the user provides no parameters, it calls ls without any
      this.stringOutput = this.executeCode();
    }
    if (this.userParameters.length == 2) {
      // if the user provides the parameters, it changes the working directory
      // stack according to the specified path
      this.newWorkingDirectory = new ChangeDirectory(this.userParameters[1],
          this.parentNode, this.desiredPath, rootNode);
      this.desiredPath = this.newWorkingDirectory.cd();
      int stackLength = this.stackPath.size();
      if (stackLength == this.desiredPath.size()) {
        // If cd returns the current working directory stack, then the path does
        // not exist the function terminates and returns a notification
        this.stringOutput = "Path Does not exist, try another path";
      }
      if (stackLength != this.desiredPath.size()) {
        // If cd changes the current working directory successfully, then
        // proceed with listing all the files
        this.parentNode = this.desiredPath.peekd();
        this.stringOutput = this.executeCode();
      }
    }
    System.out.println(this.stringOutput);
    return this.stringOutput;
  }

  /***
   * Iterates through the children of the current working directory and
   * combining their names to one string.
   * 
   * @return @return A string with each file name appearing on different lines
   */
  public String executeCode() {
    int counter = 0;
    if (this.parentNode.getChildCount() != 0) {
      // If the working node is not empty, Iterate through the children of that
      // node
      while (counter < this.parentNode.getChildCount()) {
        this.childNode =
            (DefaultMutableTreeNode) this.parentNode.getChildAt(counter);
        this.userObject = this.childNode.getUserObject();
        if (this.userObject instanceof Directory) {
          // if the object in the child node is a folder, perform a getDirName
          // method
          Directory userDir = (Directory) this.userObject;
          // Combines all the name of the directories in one string, seperated
          // by a lineSeparator
          directoryList += userDir.getDirName() + System.lineSeparator();

        }
        if (this.userObject instanceof File) {
          // if the object in the child node is a file, perform a getFileName
          // method
          File userFile = (File) this.userObject;
          if (userFile.getFileName() != null) {
            // Combines all the name of the files in one string, seperated by a
            // lineSeparator
            this.directoryList +=
                userFile.getFileName() + System.lineSeparator();
          }
        }
        counter++;
      }
    }
    if (parentNode.getChildCount() == 0) {
      // If the working node is empty, returns the appropriate notification
      this.directoryList = "The current directory is empty";
    }
    return this.directoryList;



  }


}

