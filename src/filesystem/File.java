/**
 * This class creates a new file
 */

package filesystem;

import java.io.IOException;
import java.nio.CharBuffer;

import javax.swing.tree.DefaultMutableTreeNode;


public class File {
  private String fileName;
  private String fileContent;
  private DefaultMutableTreeNode parent;

  /**
   * The constructor creates a new instance of a file
   * 
   * @param fileName a string representing the name of the file
   * @param fileContent a string representing the file's content
   * @param parent the parent node
   */

  public File(String fileName, String fileContent,
      DefaultMutableTreeNode parent) {
    this.fileName = fileName;
    this.fileContent = fileContent;
    this.parent = parent;

  }

  public File() {

  }

  /**
   * This method takes in a file and places the file in a new node and adds it
   * to the parent.
   * 
   * @param f represents a file the user would like to add to the file system
   */
  public void addChildToParent(File f) {
    // creates a new node
    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(f);
    // adds the node which contains the file onto the parent
    parent.add(newNode);
  }
  
  /**
   * This method takes in a file and places the file in a new node and adds it
   * to a specified parent.
   * 
   * @param f represents a file the user would like to add to the file system
   * @param parent is the node to which the file is added
   */
  public void addChildToParent(File f, DefaultMutableTreeNode parentNode){
    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(f);
    parentNode.add(newNode);
  }

  /**
   * This method sets the filename of the given file
   * 
   * @param fileName a string representing the new file name
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * This method returns the contents of a given file
   * 
   * @return returns a string of the file's content
   */
  public String getFileContent() {
    return fileContent;
  }

  /***
   * This method sets the content of the given file
   * 
   * @param content a string reprsenting the new content
   */
  public void setFileContent(String content) {
    this.fileContent = content;
  }

  /**
   * This method allows large walls of text to be added to files.
   * 
   * @param content takes in the content to be added.
   */
  public void appendContent(String content) {
    String str = this.getFileContent();
    String strTotal = str + "\n" + content;
    this.setFileContent(strTotal);
  }

  /**
   * This method returns the file name of the given file
   * 
   * @return a string representing the filename
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * This method deletes the content of the given file
   */
  public void deleteContent() {
    fileContent = null;
  }

  /**
   * This method removes pieces of string
   */
  public void removeString() {
    fileContent = "";
  }

}
