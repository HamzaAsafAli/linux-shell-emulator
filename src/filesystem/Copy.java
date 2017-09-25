package filesystem;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import driver.JShell;


public class Copy {
  private String oldPath;
  private String newPath;
  private DefaultMutableTreeNode parentNode;
  private DefaultMutableTreeNode targetNode;
  private String FDName;

  public Copy(String oldPath, String newPath,
      DefaultMutableTreeNode parentNode, String FDName,
      DefaultMutableTreeNode targetNode) {
    this.newPath = newPath;
    this.oldPath = oldPath;
    this.parentNode = parentNode;
    this.targetNode = targetNode;
    this.FDName = FDName;
    JShell.useHistory = false; 
  }

  public void cp() {
    Directory dir = (Directory) parentNode.getUserObject();
    ArrayList<String> dirContent = dir.getDirContent();
    ArrayList<String> filList = dir.getFileListing();
    boolean isFile = false;
    for (int i = 0; i < filList.size(); i++) {
      if (filList.get(i).equals(FDName)) {
        isFile = true;
      }
    }
    // if the path is a file, we clone the file and place it at the target node
    if (isFile == true) {
      File fileCP = dir.getFileByName(FDName, parentNode);
      this.cloneFileToNode(fileCP, targetNode);
    } else {
      // otherwise it is a directory in which case we do the same for all the
      // files
      // and directories in the directory
      this.cloneDir(parentNode, targetNode);
    }
  }

  /**
   * Clones the contents inside a given directory and moves the copies to 
   * the target node
   * 
   * @param DefaultMutableTreeNode parent 
   * @param DefaultMutableTreeNode target
   * 
   */
  public void cloneDir(DefaultMutableTreeNode parent,
      DefaultMutableTreeNode target){
    try {
    target.add(parent);
    }
    catch (Exception e){
      System.out.println("Invalid paths"); 
    }
    // files in parent node clone
    Directory a = (Directory) parent.getUserObject();
    ArrayList<String> fileLst = a.getFileListing();
    // clone all the files in the directory
    if (fileLst.size() > 0) {
      for (int i = 0; i < fileLst.size(); i++) {
        this.cloneFileToNode(a.getFileByName(fileLst.get(i), parent), target);
      }
    }
    // directories in parent node clone recursively
    /*
     * ArrayList<String> dirLst = a.getDirContent(); if (dirLst.size() > 0){ for
     * (int i = 0; i < dirLst.size(); i++){ parent.g
     */
    // couldn't get this to work recursively
    // algorithm is:
    // get directory object from the information in the list in the parent node.
    // using that get the directory object's node, recursively send that node as
    // the new parent. For the new target, use mkdir at the old target location.
    // }
  }

  /**
   * Makes a copy of a file at the given directory
   * 
   * @param requires a file
   * @param requires a DefaultMutableTreeNode
   * 
   */
  public void cloneFileToNode(File f, DefaultMutableTreeNode target) {
    File a = new File();
    a.setFileContent(f.getFileContent());
    a.setFileName(f.getFileName());
    a.addChildToParent(a, target);
  }


}
