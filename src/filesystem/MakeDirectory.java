/**
 * This class handles the creation of new directories in a file system
 */

package filesystem;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import driver.JShell;

public class MakeDirectory extends Command {
	private DefaultMutableTreeNode newNode;
	private DefaultMutableTreeNode originalNode;
	private DefaultMutableTreeNode parentNode;
	private Directory newDirectory;
	private String[] userCommands;
	private ArrayList<String> existingFolders;
	private boolean isPath = false;
	private DefaultMutableTreeNode currentWD;
	private DirStack wdPath;
	private DirStack wdPath1;
	private String originalPath;
	private String userCommand;
	private DirStack tempPath;
	private DirStack copyPath;
	private DefaultMutableTreeNode treeNode;

	/**
	 * This constructor creates a new folder in the specified directory
	 * 
	 * @param userCommand
	 *            string of user folders to be created
	 * @param parent
	 *            parent node of the folder to be created
	 * @param currentWD
	 *            node of the current working directory
	 * @param wdPath
	 *            current directory stack
	 */
	public MakeDirectory(String userCommand, DefaultMutableTreeNode parent, DefaultMutableTreeNode currentWD,
			DirStack wdPath) {
		super(userCommand);
		parentNode = parent;
		this.userCommand = userCommand;
		userCommands = userCommand.split(" ");
		existingFolders = new ArrayList<String>();
		this.currentWD = currentWD;
		this.wdPath = wdPath;
		this.originalPath = originalPath;
		originalNode = currentWD;
		if (userCommand.contains("/")) {
			isPath = true;
		}

	}

	/**
	 * This method creates a new folder at a given path
	 */
	public void mkdir() {
		//makes a copy of the current working directory so the original is not altered
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

		//gets the path of the path that the user would like the folder created at
		String[] folders = userCommands[1].split("/");
		int numFolders = folders.length;
		
		//gets the folder name that the user would like created
		String folderToCreate = folders[numFolders - 1];
		String pathToGet = "";
		for (int i = 0; i < numFolders - 1; i++) {
			pathToGet += folders[i] + "/";
		}
		//temporarily changes the CWD to the specified path
		ChangeDirectory something = new ChangeDirectory(pathToGet, this.currentWD, this.copyPath, this.parentNode);

		wdPath1 = something.cd();
		currentWD = wdPath1.peekd();
		Directory parent = (Directory) currentWD.getUserObject();

		//checks to see if the folder already exists
		if (checkFolderExists(folderToCreate) == true) {
			System.out.println("Folder: " + folderToCreate + " already exists");
		} else {
			//creates the new folder
			newDirectory = new Directory();
			newDirectory.setDirName(folderToCreate);
			//places the new folder in a node
			newNode = new DefaultMutableTreeNode(newDirectory);
			
			//adds the folder name into the parent's arraylist
			parent.addChildName(folderToCreate);
			existingFolders.add(folderToCreate);
			parent.addDirectory(newDirectory);
			currentWD.add(newNode);
		}
		currentWD = wdPath1.peekd();

	}

	/**
	 * This command makes new folders given that the current working directory
	 * is the location of where the user would like the folders created
	 */
	public void makeNewFolder() {
		int numCommands = userCommands.length;
		Directory parentDir = (Directory) parentNode.getUserObject();
		for (int i = 1; i < numCommands; i++) {
			//checks if the folder exists
			if (checkFolderExists(userCommands[i]) == true) {
				System.out.println("Folder: " + userCommands[i] + " already exists");
			} else {
				//creates the new folder if it does not exists
				newDirectory = new Directory();
				newDirectory.setDirName((userCommands[i]));
				
				//places the new folder in a node
				newNode = new DefaultMutableTreeNode(newDirectory);
				//adds the name of the folder onto the parent's array list
				parentDir.addChildName(userCommands[i]);
				existingFolders.add(userCommands[i]);
				parentDir.addDirectory(newDirectory);
				parentNode.add(newNode);
			}
		}
	}

	/**
	 * This command determines if the user would like to create the folder in a
	 * path, and calls the appropriate method to execute the folder creation, or
	 * it calls the appropriate method to create the folder in the current
	 * working directory
	 */
	public void executeCommand() {
		if (isPath) {
			mkdir();
		} else {
			makeNewFolder();
			
		}
		JShell.useHistory = false;
	}

	/**
	 * This method checks the current working directory to see if the user is
	 * attemping to create duplicate methods
	 * 
	 * @param currentFolder
	 *            string name of the current working directory
	 * @return folderExists returns a boolean values indicating if the given
	 *         folder exists
	 */
	public boolean checkFolderExists(String currentFolder) {
		boolean folderExists = false;
		Directory parentDir = (Directory) parentNode.getUserObject();
		//checks if the new folder is already contained in the parent folder
		for (String existingFolder : parentDir.getDirContent()) {
			if (existingFolder.equals(currentFolder)) {
				folderExists = true;
			}
		}

		return folderExists;
	}

}
