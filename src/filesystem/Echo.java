package filesystem;

import javax.swing.tree.DefaultMutableTreeNode;

public class Echo extends Command {
  private String[] userCommands;
  private DefaultMutableTreeNode parentNode;
  private String copy;
  private String[] userString;
 
  

  /**
   * This constructor creates a new instance of a echo object. Then splits the
   * users command so that it is easy to access specific's.
   * 
   * @param userCommand takes the command the user types.
   * @param parentNode the parent node of the current working directory.
   */

  public Echo(String userCommand, DefaultMutableTreeNode parentNode) {
    super(userCommand);
    userCommands = userCommand.split(" ");
    this.parentNode = parentNode;
    copy = userCommand;
    userString = userCommand.split("\"");
    
    
    
    
  }

  /**
   * This is the main method behind echo. There is several error handling here
   * to account for any amount of lines the user types. The method checks if
   * there is already a file with the same name before appending or or
   * overwriting any string and this makes sure that there are no duplicate
   * files being created.
   */
  
  public void EchoOverwrite() {
    int numStr = userString.length;
    if (numStr == 2){
      String subs = copy.substring(5);
      String noquotes = subs.replace("\"", "");
      System.out.println(noquotes);
      }

     else if (numStr == 1) {
      System.out.print("");
    } else {  
      String extensions = userString[2].trim();
      String[] finalArray = extensions.split(" ");
      int catchNum = finalArray.length;
      if (catchNum!= 2){
        System.out.print("");
      }
      
      else if (finalArray[0].equals(">")) {
        Directory parentDir = (Directory) parentNode.getUserObject();
        File fileRefer = getFileByName(finalArray[1], parentNode);
        if (fileRefer != null) {
          fileRefer.deleteContent();
          fileRefer.setFileContent(userString[1]);
        } else {
          File readerFile =
              new File(finalArray[1], userString[1], parentNode);
          readerFile.addChildToParent(readerFile);
          parentDir.addFiles(finalArray[1]);


        }

      } else if (finalArray[0].equals(">>")) {
        Directory parentDir = (Directory) parentNode.getUserObject();
        File fileRefer = getFileByName(finalArray[1], parentNode);
        if (fileRefer != null) {
          String existingContent = fileRefer.getFileContent();
          String newContent = existingContent + "\n" + userString[1];
          fileRefer.setFileContent(newContent);
        } else {
          File readerFile =
              new File(finalArray[1], userString[1], parentNode);
          readerFile.addChildToParent(readerFile);
          parentDir.addFiles(finalArray[1]);

        }



      }
      
      
    } 
    }


  

  /**
   * External storage of the string the user wishes to append/overwrite.
   */
  public String getuserString() {
    String store = "";
    store = userCommands[1];
    return store;
  }

  /**
   * This is the method which is responsible for checking if a file with the
   * same name already exists in the mock file system. Its job is to check the
   * existence of a particular file.
   * 
   * @param fileName
   * @param parent
   * @return
   */

  public File getFileByName(String fileName, DefaultMutableTreeNode parent) {
    File f = null;
    DefaultMutableTreeNode currentNode;
    int j = 0;
    while (j < parent.getChildCount()) {
      currentNode = (DefaultMutableTreeNode) parent.getChildAt(j);
      Object currentObject = currentNode.getUserObject();

      if (!(currentObject instanceof Directory)) {
        File currentFile = (File) currentObject;
        if (currentFile.getFileName().equals(fileName)) {
          f = currentFile;
        }
      }

      j++;
    }
    return f;
  }

  public void executeCommand() {
    EchoOverwrite();
    

  }

  public void checkArguements() {

  }



}
