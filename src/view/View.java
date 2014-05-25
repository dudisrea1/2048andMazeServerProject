package view;

public interface View {
	/**
	 * Display Connected Clients List
	 * @param array of clients
	 */
	void displayConnectedClients(String[] clients);
	
	/**
	 * Get user action from GUI
	 * @return id of user action
	 */
	int getUserCommand();
	
	/**
	 * Display message box of an error
	 * @param error message
	 */
	void displayError(String string);
	
	/**
	 * Display Client Log (connected,disconnected,response action to user)
	 * @param client log
	 */
	public void displayClientLog(String log);
}
