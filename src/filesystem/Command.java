package filesystem;

import java.io.IOException;

public class Command {
	private String userCommand;

	public Command(String userCommand) {
		this.userCommand = userCommand;
	}
	public Command(){
		
	}

	public void checkArguements() {

	}

	public void executeCommand() throws IOException {

	}

	public void getArguementName() {

	}

	public String getUserCommand() {
		return userCommand;
	}

	public void setUserCommand(String userCommand) {
		this.userCommand = userCommand;
	}
	

}
