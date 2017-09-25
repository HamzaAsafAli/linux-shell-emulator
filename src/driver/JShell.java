package driver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Stack;
import java.net.*;
import java.io.*;

import javax.swing.tree.DefaultMutableTreeNode;

import filesystem.ChangeDirectory;
import filesystem.Copy;
import filesystem.DirStack;
import filesystem.History;
import filesystem.List;
import filesystem.Command;
import filesystem.Directory;
import filesystem.Echo;
import filesystem.ExecuteHistory;
import filesystem.GetUrl;
import filesystem.MakeDirectory;
import filesystem.Man;
import filesystem.Path;
import filesystem.Popd;
import filesystem.PrintWorkingDirectory;
import filesystem.Pushd;
import filesystem.ShowFile;

public class JShell {
  public static boolean useHistory = false;

  public static void main(String[] args) throws IOException,
      MalformedURLException, Exception {
    // TODO Auto-generated method stub
    History userLog = new History();
    Directory root = new Directory();
    root.setDirName("/");
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
    DirStack stack = new DirStack(rootNode);
    Stack<DirStack> pushDir = new Stack<DirStack>();
    ChangeDirectory pushPopCD;
    Scanner user_prompt = new Scanner(System.in);
    String user_input = "";
    String[] user;

    // continuously loops until the user enters exit
    do {
      // prompts the user

      // accepts the user's input and adds to history
      if (useHistory == false) {
        System.out.print("/#");
        user_input = user_prompt.nextLine();
      }

      userLog.addCommand(user_input);
      Command userCommand;


      // splits the user command and arguments
      user = user_input.split(" ");

      // checks to determine which commands the user entered
      if (user[0].startsWith("!")) {
        ExecuteHistory h = new ExecuteHistory(user[0], userLog);
        user_input = h.execute();
      }
      if (user[0].equals("mkdir")) {
        userCommand =
            new MakeDirectory(user_input, stack.peekd(), rootNode, stack);
        userCommand.executeCommand();
      }
      if (user[0].equals("cat")) {
        userCommand = new Path(user_input, stack.peekd(), rootNode, stack);
        userCommand.executeCommand();
      }
      if (user[0].equals("history")) {
        userLog.executeCommand(Integer.parseInt(user[1]), user_input, stack);
      }
      if (user[0].equals("pwd")) {
        PrintWorkingDirectory item = new PrintWorkingDirectory(stack);
        System.out.println(item.getPath());
        stack = item.returnStack();
        useHistory = false;
      }
      if (user[0].equals("cd")) {
        int numArgs = user.length;
        if (numArgs == 1) {
          System.out.println("No file specified");
        } else {
          ChangeDirectory something =
              new ChangeDirectory(user[1], stack.peekd(), stack, rootNode);
          stack = something.cd();
        }

      }
      if (user[0].equals("get")) {
        GetUrl url = new GetUrl(user_input, stack.peekd());
        url.executeCommand();


      }
      if (user[0].equals("echo")) {
        Echo test = new Echo(user_input, stack.peekd());
        test.executeCommand();
      }
      if (user[0].equals("ls")) {
        List fileList = new List(user_input, stack.peekd(), stack, rootNode);
        fileList.list();
        useHistory = false;
      }
      if (user[0].equals("man")) {
        userCommand = new Man(user_input, stack.peekd(), stack);
        userCommand.executeCommand();
      }
      if (user[0].equals("pushd") || user[0].equals("popd")) {
        pushPopCD = new ChangeDirectory(stack.peekd(), stack, rootNode);

        if (user[0].equals("pushd")) {
          if (user.length == 2) {
            Pushd a = new Pushd(stack, pushDir);
            pushPopCD.setUserArgs(user[1]);
            stack = pushPopCD.cd();
          } else {
            System.out.println("Invalid path");
          }
        }

        if (user[0].equals("popd")) {
          if (user.length == 1) {
            Popd popInstance = new Popd(pushDir, stack);
            stack = popInstance.popd();
          } else {
            System.out.println("Invalid");
          }
        }
      }

      if (user[0].equals("cp")) {
        if (user.length == 3) {
          String oldPath = user[1];
          String newPath = user[2];
          String[] splitOldPath = oldPath.split("/");
          String oldPathParent = "";
          for (int i = 0; i < splitOldPath.length - 1; i++) {
            oldPathParent = oldPathParent + splitOldPath[i];
          }
          ChangeDirectory a =
              new ChangeDirectory(oldPathParent, stack.peekd(), stack, rootNode);
          DirStack stackCopy = a.cd();
          DefaultMutableTreeNode parentNode = stackCopy.peekd();
          ChangeDirectory b =
              new ChangeDirectory(newPath, stack.peekd(), stack, rootNode);
          DirStack stackCopy2 = b.cd();
          DefaultMutableTreeNode targetNode = stackCopy2.peekd();
          Copy copyInstance =
              new Copy(oldPath, newPath, parentNode,
                  splitOldPath[splitOldPath.length - 1], targetNode);
          copyInstance.cp();
        } else {
          System.out.println("Invalid");
        }
      }
      // loop exists and program terminates when the user says exit
    } while (!user_input.equals("exit"));

  }

}
