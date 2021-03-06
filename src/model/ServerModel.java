package model;

public interface ServerModel {
	
	/**
	 * Set Server Configurations including port & number of clients allow
	 * @param ServerConfiguration object
	 */
	void SetServerConfiguration(ServerConfiguration config);
	
	/**
	 * Get the port the server listening to
	 * @return number of port
	 */
	int GetPort();
	
	
	//void start();
	
	
	//void close();
	
	/**
	 * Get all client that connected to server
	 * @return array of clients's IP
	 */
	String[] GetClientsList();
	
	/**
	 * Get specific client log
	 * @param client's IP
	 * @return client log
	 */
	String GetClientLog(String clientIP);
	
	/**
	 * Write server log to log file
	 * @param server log
	 * @return true if write succeed or false if write failed
	 */
	boolean WriteLog(String log);
	
	/**
	 * Initialize server params - server client handler
	 */
	void InitServer();
	
	/**
	 * Start server listening to requests
	 */
	boolean RunServer();
	
	/**
	 * Stop server listening 
	 */
	boolean StopServer();
}
