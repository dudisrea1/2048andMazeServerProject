package view;

public interface View {
	
	void displayConnectedClients(String[] clients);
	int getUserCommand();
	void displayError(String string);
	public void displayClientLog(String log);
}
