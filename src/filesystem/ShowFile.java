/**
 * This class is responsible for handling the cat command.
 */

package filesystem;

import javax.swing.tree.DefaultMutableTreeNode;

public class ShowFile extends Command {
  private File f;
  private String fileName;
  private DefaultMutableTreeNode parent;
  private boolean isPath;
  private DefaultMutableTreeNode rootNode;
  private DirStack stack;
  private String content;

  /**
   * This constructor takes the name of the file the user would like to see, the
   * parent node of the directory containing the file, the current directory
   * stack and the root node.
   * 
   * @param fileName name of file the user would like to see
   * @param parent parent node of the directory within which the specified file
   *        is contained
   * @param stack current directory stack
   * @param rootNode root node of the tree
   */
  public ShowFile(String fileName, DefaultMutableTreeNode parent,
      DirStack stack, DefaultMutableTreeNode rootNode) {
    this.fileName = fileName;
    if (this.fileName.contains("/")) {
      isPath = true;
    }
    this.parent = parent;
    this.stack = stack;
    this.rootNode = rootNode;

  }

  public ShowFile(String filePath) {

  }

  /**
   * This command displays the contents of a file on the screen
   */
  public String cat() {
    // executes if the file is contained within the current working
    // directory
    if (!isPath) {

      // attempts to look for the file in the specified directory
      Directory d = (Directory) parent.getUserObject();
      //gets the file object at the location
      File f = d.getFileByName(fileName, parent);
      // informs the users if the file is not found
      if (f == null) {
        System.out.println("File not found");
      } else {
        //gets the file content
        content = f.getFileContent();
      }
      // execute if the user specifies a path to the file
    } else {
      // gets the path to the file the user specified
      String[] folders = fileName.split("/");
      int numFolders = folders.length;
      //gets the name of the folder to create
      String folderToCreate = folders[numFolders - 1];
      String pathToGet = "";
      for (int i = 0; i < numFolders - 1; i++) {
        pathToGet += folders[i] + "/";
      }
      // navigates to the specified directory
      ChangeDirectory something =
          new ChangeDirectory(pathToGet, parent, stack, rootNode);
      something.cd();
      parent = stack.peekd();
      // gets the file at the specified directory
      Directory two = (Directory) parent.getUserObject();
      File f = two.getFileByName(folderToCreate, parent);
      // informs the user if the file is not found, or prints the content
      // if it is found
      if (f == null) {
        System.out.println("File not found.");
      } else {
        content = f.getFileContent();
      }
    }
    return content;
  }

  /**
   * This command overwrites the parent command and simply calls on the cat
   * command to return the contents of a file
   */
  public void executeCommand() {
    System.out.println(cat());
  }

}
