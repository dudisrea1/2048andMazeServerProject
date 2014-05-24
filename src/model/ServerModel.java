package model;

public interface ServerModel {
	int GetPort();
	void start();
	void close();
	String[] GetClientsList();
	String GetClientLog(String clientIP);
	boolean WriteLog(String log);
	void InitServer();
	boolean RunServer();
	boolean StopServer();
}
