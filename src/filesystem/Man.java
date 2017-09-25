/**
 * Handles all activities related to the manual
 */

package filesystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

import driver.JShell;

public class Man extends Command {
  private BufferedReader reader;
  private FileReader fileReader;
  private String fileName;
  private String userCommand;
  private String commandName;
  private String line;
  private String[] pathSplit, userArgs;
  private DirStack tempPath;
  private DirStack copyPath;
  private DirStack wdPath;
  private DirStack wdPath1;
  private DefaultMutableTreeNode parent;
  private DefaultMutableTreeNode currentWD;
  private DefaultMutableTreeNode parentNode;
  private DefaultMutableTreeNode treeNode;

  /**
   * This constructor takes a string of the user commands and creates a manual
   * object
   * 
   * @param userCommand string representing user commands and arguments
   * @throws FileNotFoundException
   */
  public Man(String userCommand, DefaultMutableTreeNode parent, DirStack wdPath)
      throws FileNotFoundException {
    super(userCommand);
    this.parent = parent;
    this.wdPath = wdPath;
    this.userCommand = userCommand;
    initialize();
    // TODO Auto-generated constructor stub
  }

  /**
   * This method initializes the file reader and buffered reader to the command
   * as specified by the user
   * 
   * @throws FileNotFoundException
   */
  public void initialize() throws FileNotFoundException {

    String[] userArgs = userCommand.split(" ");
    commandName = userArgs[1] + ".txt";
    // sets the filename to the specified command
    fileName = "./Documentation/" + commandName;
    // opens a file reader and buffered reader to the given file
    fileReader = new FileReader(fileName);
    reader = new BufferedReader(fileReader);
  }

  /**
   * This method prints out the contents of the given file for the specified
   * command by the user
   * 
   * @throws IOException
   */
  public String getManual() throws IOException {
    String gline = "";
    while ((line = reader.readLine()) != null) {
      // System.out.println(line);
      gline += line + "\n";
    }
    return gline;

  }

  /**
   * this method calls the appropriate method to get the command's manual
   * printed
   */
  public void executeCommand() throws IOException {
    // determines if redirection is needed
    if (userCommand.contains(">")) {
      // creates a copy of the dir stack
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

      pathSplit = userCommand.split(">");
      userArgs = pathSplit[0].split(" ");
      String tt = pathSplit[1].trim();
      String[] folders = tt.split("/");
      int numFolders = folders.length;

      // gets the folder name that the user would like created
      String fileToCreate = folders[numFolders - 1];
      String pathToGet = "";
      for (int i = 0; i < numFolders - 1; i++) {
        pathToGet += folders[i] + "/";
      }
      // changes the directory if the output file does not go in the current WD
      DefaultMutableTreeNode curr;
      if (pathToGet.length() != 0) {
        ChangeDirectory something = new ChangeDirectory(pathToGet,
            this.currentWD, this.copyPath, this.parentNode);
        wdPath1 = something.cd();
        curr = wdPath1.peekd();
      } else {
        curr = wdPath.peekd();
      }
      //creates the new file at the given location
      File f = new File("test", getManual(), curr);
      f.addChildToParent(f);
      String[] nameArray = pathSplit[1].split("/");
      String name = nameArray[nameArray.length - 1];
      f.setFileName(name.trim());
    } else {
      System.out.println(getManual());
    }
    JShell.useHistory = false;
  }

}
