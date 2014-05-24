package model;

import Converter.ServerXMLManager;

public class ServerConfiguration {

	public int Port;
	public int NumberOfClients;
	private ServerXMLManager XMLManager = new ServerXMLManager();
	
	public ServerConfiguration(){}
	public ServerConfiguration(int Port,int NumberOfClients)
	{
		this.Port=Port;
		this.NumberOfClients=NumberOfClients;
	
	}
	
	public boolean Save() {
		
		return XMLManager.ToFile(this, "Configuration.xml");
		
	}
	public boolean Load() {
		try{
			ServerConfiguration tmpConfig = (ServerConfiguration)XMLManager.FromFile("Configuration.xml");
			this.Port=tmpConfig.Port;
			this.NumberOfClients=tmpConfig.NumberOfClients;
			return true;
		}
		catch(Exception e)
		{return false;}
		
	}
}
