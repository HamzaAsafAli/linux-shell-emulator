package filesystem;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import driver.JShell;
import filesystem.Command;

public class History extends Command {
  private String[] userCommand;
  private int numCommand;
  private ArrayList<String> userHistory;
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
   * This constructor creates a new instance of a history object and inserts the
   * first element. An array list is initialized
   * 
   * @param userCommand the user's command
   */
  public History(String userCommand) {
    super(userCommand);
    this.userCommand = userCommand.split(" ");
    userHistory = new ArrayList<String>();
  }

  public History() {
    super();
    userHistory = new ArrayList<String>();
  }

  /**
   * This method takes an integer n which represents the number of commands that
   * the user would like to view.
   * 
   * @param n the number of past commands the user would like to view
   */

  public String getHistory(int n) {
    String toReturn = "";
    int numCommands = userHistory.size() - 1;
    int toPrint = numCommands - n;
    while (toPrint < numCommands) {
      toReturn += userHistory.get(toPrint) + "\n";
      // System.out.println(userHistory.get(toPrint));
      toPrint++;
    }
    return toReturn;
  }

  /**
   * Returns the last element of the array list
   * 
   * @return returns a string of the last element in the list
   */
  public String getTopCommand() {
    return userHistory.get(userHistory.size() - 1);
  }

  /**
   * Sets the number of commands the user would like to be shown
   * 
   * @param n the number of commands the user would like to view
   */
  public void setNumCommands(int n) {
    n = Integer.parseInt(userCommand[1]);
  }

  /**
   * Adds a user command to the existing arraylist
   * 
   * @param command the command the user would like saved onto the array list
   */
  public void addCommand(String command) {
    userHistory.add(command);
  }

  /**
   * This method taken an integer representing the number of commands the user
   * would like printed out and calls the appropriate method. Determines if the
   * user would like output redirection
   * 
   * @param n the number of arguments the user would like printed out
   */
  public void executeCommand(int n, String input, DirStack wdPath) {
    this.wdPath = wdPath;
    // makes a copy of the dir stack
    if (input.contains(">")) {
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
      // splits the user input by the '>' character to separate commands from
      // the output file
      String path = input;
      pathSplit = path.split(">");
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
      // switches directory if the user would like the file in a different
      // directory
      DefaultMutableTreeNode curr;
      if (pathToGet.length() != 0) {
        ChangeDirectory something = new ChangeDirectory(pathToGet,
            this.currentWD, this.copyPath, this.parentNode);
        wdPath1 = something.cd();
        curr = wdPath1.peekd();
      } else {
        curr = wdPath.peekd();
      }
      // creates a file with the history log
      File f = new File("test", getHistory(n), curr);
      f.addChildToParent(f);
      String[] nameArray = pathSplit[1].split("/");
      String name = nameArray[nameArray.length - 1];
      f.setFileName(name.trim());
    } else {
      System.out.println(getHistory(n));
    }
    JShell.useHistory = false;
  }

  /**
   * returns the number of commands in the array list
   * 
   * @return an integer of the number of commands in the log
   */
  public int getHistoryLength() {
    return userHistory.size();
  }

  /**
   * returns the command and n index in the history log
   * 
   * @param n the command to be returned
   * @return a string of the command at the specified index
   */
  public String getCommandAtInt(int n) {
    return userHistory.get(n);
  }

}
