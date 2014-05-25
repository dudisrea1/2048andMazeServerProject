package model;

import Converter.ServerXMLManager;

public class ServerConfiguration {

	private int Port;
	private int NumberOfClients;
	private ServerXMLManager XMLManager = new ServerXMLManager();
	
	public ServerConfiguration(){}
	public ServerConfiguration(int Port,int NumberOfClients)
	{
		this.Port=Port;
		this.NumberOfClients=NumberOfClients;
	
	}
	public int getPort()
	{
		return Port;
	}
	public int getNumberOfClients()
	{
		return NumberOfClients;	
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
