/**
 * This method handles the creation of a new directory
 */

package filesystem;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

public class Directory extends File {
  private String dirName;
  private ArrayList<String> dirContent; // 0 - 5
  private ArrayList<String> filContent; // 0 - 7
  private ArrayList<Directory> childrenDirectories;

  /**
   * This constructor creates a new directory object and initializes three
   * arraylists to keep track of the directory's contents.
   */
  public Directory() {
    super();
    this.dirContent = new ArrayList<String>();
    this.filContent = new ArrayList<String>();
    this.childrenDirectories = new ArrayList<Directory>();
  }

  /**
   * This constructor creates a file based on the given paramenters
   * 
   * @param dirName a name of the new directory to be created
   * @param dirContent a string representing the directory's content
   * @param filContent an arraylist listing the directory's contents
   */
  public Directory(String dirName, ArrayList<String> dirContent,
      ArrayList<String> filContent) {
    this.dirName = dirName;
    this.dirContent = new ArrayList<String>();
    this.filContent = new ArrayList<String>();
    this.childrenDirectories = new ArrayList<Directory>();
  }

  /**
   * Returns an arraylist of the directory's content
   * 
   * @return an arraylist of the directory's contents
   */
  public ArrayList<String> getDirContent() {
    return dirContent;
  }

  /**
   * This method sets a directory's content if necessary
   * 
   * @param dirContent an arraylist of the content
   */
  public void setDirContent(ArrayList<String> dirContent) {
    this.dirContent = dirContent;
  }

  /**
   * This method adds the given string into the array list of the file
   * 
   * @param name the name of the child to add
   */
  public void addChildName(String name) {
    dirContent.add(name);

  }

  /**
   * This method prints out the given children of a directory
   */
  public void getChildren() {
    for (String s : dirContent) {
      System.out.println(s);
    }
  }

  /**
   * This method adds the given filename into th arraylist of files
   * 
   * @param fileName the filename to be added
   */
  public void addFiles(String fileName) {
    filContent.add(fileName);
  }

  /**
   * this method returns the array list of files
   * 
   * @return arraylist of the files within this directory
   */
  public ArrayList<String> getFileListing() {
    return filContent;
  }

  /**
   * this method returns the directory's name
   * 
   * @return return a string of the directorys name
   */
  public String getDirName() {
    return dirName;
  }

  /**
   * this method sets the directory name
   * 
   * @param dirName a string representing the directory's name
   */
  public void setDirName(String dirName) {
    this.dirName = dirName;
  }

  /**
   * this method returns the directory within the current directory that has the
   * same filename
   * 
   * @param dirName the directory name to search for
   * @return a directory object that has the name the user would like
   */
  public Directory getDirectoryByName(String dirName) {
    Directory d = null;
    for (Directory s : childrenDirectories) {
      if (s.getDirName().equals(dirName)) {
        d = s;
      }
    }

    return d;
  }

  /**
   * this method adds the given directory into the array list of directories as
   * given by the user
   * 
   * @param d the directory to add into the array list
   */
  public void addDirectory(Directory d) {
    childrenDirectories.add(d);
  }

  /***
   * this method returns the file within the specified node that has the name
   * the user is looking for
   * 
   * @param fileName file name to look for
   * @param parent parent of the file
   * @return returns a file object which has the name that the user is looking
   *         for
   */
  public File getFileByName(String fileName, DefaultMutableTreeNode parent) {
    File f = null;

    DefaultMutableTreeNode currentNode;
    int j = 0;
    // loops through children of the parent
    while (j < parent.getChildCount()) {
      currentNode = (DefaultMutableTreeNode) parent.getChildAt(j);
      Object currentObject = currentNode.getUserObject();
      // checks if the current child is a file instance
      if (!(currentObject instanceof Directory)) {
        File currentFile = (File) currentObject;
        System.out.println(currentFile.getFileContent());
        // checks the name of the current file
        if (currentFile.getFileName().equals(fileName)) {
          // if it is a match, it returns the current file
          f = currentFile;
        }
      }

      j++;
    }
    return f;
  }


}
