package filesystem;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import driver.JShell;

public class Path extends Command {
  private String path;
  private String[] pathSplit, userArgs;
  private Object obj1;
  private DirStack tempPath;
  private DirStack copyPath;
  private DirStack wdPath;
  private DirStack wdPath1;
  private DefaultMutableTreeNode currentWD;
  private DefaultMutableTreeNode parentNode;
  private DefaultMutableTreeNode treeNode;
  private ArrayList<String[]> userPaths = new ArrayList<String[]>();

  /**
   * This constructor creates a new path object which provides the user with the
   * ability to print a single or multiple files
   * 
   * @param path the string of files to be shown
   * @param currentWD the current working directory
   * @param parentNode the parent node of the file
   * @param wdPath the current directory stack of the program
   */
  public Path(String path, DefaultMutableTreeNode currentWD,
      DefaultMutableTreeNode parentNode, DirStack wdPath) {
    this.path = path;
    this.wdPath = wdPath;
    this.currentWD = currentWD;
    this.parentNode = parentNode;
  }

  /**
   * This command creates a new file at the user's specified location if the
   * user would like to redirect the output.
   */
  public void reDirect() {
    // makes a copy of the stack
    if (this.wdPath.size() > 1) {
      this.tempPath = new DirStack(this.wdPath.popped());
      while (this.wdPath.size() != 1) {
        this.tempPath.pushed(this.wdPath.popped());
      }
      if (this.wdPath.size() == 1) {
        this.copyPath = new DirStack(this.wdPath.peekd());
      }
      while (!this.tempPath.isEmpty()) {
        this.treeNode = this.tempPath.popped();
        this.copyPath.pushed(this.treeNode);
        this.wdPath.pushed(this.treeNode);
      }
    }
    if (this.wdPath.size() == 1) {
      this.copyPath = new DirStack(this.wdPath.peekd());
    }
    // splits the string based on the files to be shown, and the location to
    // saved at
    pathSplit = path.split(">");
    // creates an array of files to show the uiser
    userArgs = pathSplit[0].split(" ");
    String tt = pathSplit[1].trim();

    String[] folders = tt.split("/");
    int numFolders = folders.length;

    // gets the filename that the user would like created
    String fileToCreate = folders[numFolders - 1];

    // builds the path to where the file has to be created
    String pathToGet = "";
    for (int i = 0; i < numFolders - 1; i++) {
      pathToGet += folders[i] + "/";
    }

    // navigates to the path the user specified
    DefaultMutableTreeNode curr;
    if (pathToGet.length() != 0) {
      ChangeDirectory something = new ChangeDirectory(pathToGet, this.currentWD,
          this.copyPath, this.parentNode);
      wdPath1 = something.cd();
      curr = wdPath1.peekd();
    } else {
      curr = wdPath.peekd();
    }

    // creates the file to the location the user specified
    File f = new File("test", showFiles(), curr);
    f.addChildToParent(f);
    String[] nameArray = pathSplit[1].split("/");
    String name = nameArray[nameArray.length - 1];
    f.setFileName(name.trim());
  }

  /**
   * This method shows the files that the user has specified. If the user would
   * like the output redirected, it is created in the specified file
   */
  public void executeCommand() {
    // determines if the user would like to redirect the output
    if (path.contains(">")) {
      reDirect();
    } else {
      userArgs = path.split(" ");
      System.out.println(showFiles());
    }
    JShell.useHistory = false;
  }

  /**
   * This method returns a string representing the contents of all files the
   * user specified
   * 
   * @return toReturn represents the contents of the file(s) the user specified
   */
  public String showFiles() {
    String toReturn = "";
    // builds an array list of paths to retrieve files from
    String[] currentPath;
    for (int i = 0; i < userArgs.length; i++) {
      currentPath = userArgs[i].split("/");
      userPaths.add(currentPath);
    }
    userPaths.remove(0);

    // loops through every file in the path
    for (String[] s : userPaths) {
      // makes a copy of the dirstack
      if (this.wdPath.size() > 1) {
        this.tempPath = new DirStack(this.wdPath.popped());
        while (this.wdPath.size() != 1) {
          this.tempPath.pushed(this.wdPath.popped());
        }
        if (this.wdPath.size() == 1) {
          this.copyPath = new DirStack(this.wdPath.peekd());
        }
        while (!this.tempPath.isEmpty()) {
          this.treeNode = this.tempPath.popped();
          this.copyPath.pushed(this.treeNode);
          this.wdPath.pushed(this.treeNode);
        }
      }
      if (this.wdPath.size() == 1) {
        this.copyPath = new DirStack(this.wdPath.peekd());
      }
      // gets the name of the file the user would like
      String targetName = s[s.length - 1];
      int numChildren = s.length;
      String pathToGet = "";
      // builds a path to navigate to
      for (int i = 0; i < numChildren - 1; i++) {
        pathToGet += s[i] + "/";
      }
      // changes directory if the file is not contained in the current directory
      if (pathToGet.contains("/")) {
        ChangeDirectory something = new ChangeDirectory(pathToGet,
            this.currentWD, this.copyPath, this.parentNode);
        wdPath1 = something.cd();
        currentWD = wdPath1.peekd();
      } else {
        currentWD = copyPath.peekd();
      }
      // gets the file the user needs
      File f = getFileByName(targetName, currentWD);
      // error message if the file is not found
      if (f == null) {
        String errorMessage =
            "The file: \"" + targetName + "\", does not exist.";
        toReturn += errorMessage + "\n";
      } else {
        toReturn += f.getFileName() + ": \n" + f.getFileContent() + "\n";
      }
    }
    JShell.useHistory = false;
    return toReturn;
  }


  /**
   * This method returns the file that is located in the specified node
   * with the given filename
   * @param fileName the name of the file to look for
   * @param parent the node of the parent directory
   * @return the file that is found with that name
   */
  public File getFileByName(String fileName, DefaultMutableTreeNode parent) {
    File f = null;
    
    DefaultMutableTreeNode currentNode;
    int j = 0;
    //loops through all children of the parent node
    while (j < parent.getChildCount()) {
      
      currentNode = (DefaultMutableTreeNode) parent.getChildAt(j);
      Object currentObject = currentNode.getUserObject();
      //checks if the current object is an instance of a directory
      if (!(currentObject instanceof Directory)) {
        //returns the file if it is found
        File currentFile = (File) currentObject;
        if (currentFile.getFileName().equals(fileName)) {
          f = currentFile;
        }
      }

      j++;
    }
    return f;
  }
}

