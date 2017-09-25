package filesystem;

import driver.JShell;

public class ExecuteHistory extends Command {
  private String userCommand;
  private History histLog;
  
  /**
   * This constructor creates a new instance of the history executor 
   * @param command the raw input from user including "!"
   * @param histLog the current history log
   */

  public ExecuteHistory(String command, History histLog) {
    this.userCommand = command;
    this.histLog = histLog;
  }
  
  /**
   * This command gets the user's command at the specified index
   * and returns that command to JSHell for execution
   * @return userInput a string of the command to be executed
   */
  public String execute(){
    //remove the "!" character
    String command = userCommand.replaceFirst("!", "");
    //get the index of the command
    int numCommand = Integer.parseInt(command) - 1;
    //get the command the user wants
    String userInput = histLog.getCommandAtInt(numCommand);
    System.out.println("--" +userInput);
    //Let JShell know that a command from the log is being executed
    JShell.useHistory = true;
    return userInput;
    

    
  }

}
