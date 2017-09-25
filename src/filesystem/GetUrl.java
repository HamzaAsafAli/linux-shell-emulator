package filesystem;

import java.net.*;
import java.io.*;
import javax.swing.tree.DefaultMutableTreeNode;


public class GetUrl extends Command {
  private String[] userCommands;
  private URL userLink;
  private DefaultMutableTreeNode parentNode;

  public GetUrl(String userCommand, DefaultMutableTreeNode parentNode) {
    super(userCommand);
    userCommands = userCommand.split(" ");
    this.parentNode = parentNode;
  }


  public void checkUrl() throws MalformedURLException {
    try {
      userLink = new URL(userCommands[1]);
    } catch (IndexOutOfBoundsException e) {
      System.err.println("Please provide a URL");
    }

  }

  public void get() throws MalformedURLException {
    try {
      userLink = new URL(userCommands[1]);
      String path = userLink.getFile();
      String fileName =
          path.substring(path.lastIndexOf('/') + 1, path.length());
      File reffile = getFileByName(fileName, parentNode);
      // create a URLconnection object
      URLConnection urlConnection = userLink.openConnection();
      // establish connection to the url
      urlConnection.connect();
      // using bufferedreader to read from the input stream
      BufferedReader in = new BufferedReader(
          new InputStreamReader(urlConnection.getInputStream()));

      if (reffile != null) {
        String inputLine;
        while ((inputLine = in.readLine()) != null)
          reffile.appendContent(inputLine);
        in.close();
      } else {
        File newFile = new File(fileName, "", parentNode);
        Directory parentDir = (Directory) parentNode.getUserObject();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
          newFile.appendContent(inputLine);
        in.close();
        newFile.addChildToParent(newFile);
        parentDir.addFiles(fileName);
      }

    } catch (MalformedURLException e) {
      System.err.println("Invalid URL");
    } catch (IOException e) {
      System.err.println("Couldnt connect");
    }


  }

  public void checkRedirect() throws MalformedURLException {
    int commandNum = userCommands.length;
    if (commandNum == 4) {
      try {
        userLink = new URL(userCommands[1]);
        String path = userLink.getFile();
        String fileName =
            path.substring(path.lastIndexOf('/') + 1, path.length());
        File reffile = getFileByName(fileName, parentNode);
        // create a URLconnection object
        URLConnection urlConnection = userLink.openConnection();
        // establish connection to the url
        urlConnection.connect();
        // using bufferedreader to read from the input stream
        BufferedReader in = new BufferedReader(
            new InputStreamReader(urlConnection.getInputStream()));
        String prevFile = userCommands[3];
        Directory parentDir = (Directory) parentNode.getUserObject();
        if (userCommands[2].equals(">")) {
          File fileRefer = getFileByName(userCommands[3], parentNode);
          if (fileRefer != null) {
            fileRefer.removeString();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
              fileRefer.appendContent(inputLine);
            in.close();
          } else {
            File newFile = new File(userCommands[3], "", parentNode);
            String inputLine;
            while ((inputLine = in.readLine()) != null)
              newFile.appendContent(inputLine);
            in.close();
            newFile.addChildToParent(newFile);
            parentDir.addFiles(userCommands[3]);
          }
        } else if (userCommands[2].equals(">>")) {
          File fileRefer = getFileByName(userCommands[3], parentNode);
          if (fileRefer != null) {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
              fileRefer.appendContent(inputLine);
            in.close();
          } else {
            File newFile = new File(userCommands[3], "", parentNode);
            String inputLine;
            while ((inputLine = in.readLine()) != null)
              newFile.appendContent(inputLine);
            in.close();
            newFile.addChildToParent(newFile);
            parentDir.addFiles(userCommands[3]);
          }
        } else {
          System.out.println("Redirection failed, try again");
        }

      } catch (MalformedURLException e) {
        System.err.println("Invalid URL");
      } catch (IOException e) {
        System.err.println("Couldnt connect");
      }
    }
  }

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

}
