package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer extends Observable {

	private int port;
	private ServerSocket server;
	private boolean stop;
	private int NumberOfClients;
	private ClientHandler clientHandler;
	private ServerConfiguration config;
	private List<String> clientsList = new ArrayList<String>();
	private Map<String, String> logMap;

	public MyServer(ClientHandler clientHandler) {
		config = new ServerConfiguration();
		config.Load();
		this.port = config.Port;
		stop = false;
		this.NumberOfClients = config.NumberOfClients;
		this.clientHandler = clientHandler;

		logMap = new HashMap<String, String>();

	}

	public int GetPort() {
		return port;
	}

	public void start() {
		try {
			stop = false;
			server = new ServerSocket(port);
			server.setSoTimeout(2000);

			if (NumberOfClients > 0) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						String ClientIP;

						ExecutorService ThredPool = Executors.newFixedThreadPool(NumberOfClients); // Defined thread pool to run multiple clients same time

						while (!stop) {
							try {

								final Socket someClient = server.accept();
								ClientIP = someClient.getRemoteSocketAddress().toString().substring(1);
								
								AddLogToMap(ClientIP, "client connected");
								WriteLog("client connected: "+ someClient.getLocalSocketAddress());

								clientsList.add(someClient.getRemoteSocketAddress().toString().substring(1));
								setChanged();
								notifyObservers();

								final ObjectInputStream inFromClient = new ObjectInputStream(someClient.getInputStream());
								final ObjectOutputStream out2client = new ObjectOutputStream(someClient.getOutputStream());						
	
								ThredPool.execute(new Runnable() {
	
										@Override
										public void run() {
																							
										clientHandler.handleClient(inFromClient, out2client);
										try{
											inFromClient.close();
											out2client.close();
											someClient.close();
										} catch(Exception e){}
										
										}
									});
																
								System.out.println("out to client");
														
								System.out.println("client disconnected");
								AddLogToMap(ClientIP, "client disconnected");
								
							} catch (SocketTimeoutException e) {
							} catch (IOException e1) {
								e1.printStackTrace();
							}

						}
						try {
							server.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						WriteLog("server closed");
						System.out.println("server closed");
					}
				});
				t.start();

				setChanged();
				notifyObservers();

			} else {
				// return "error: illegal NumberOfClients";
				server.close();
				WriteLog("error: illegal NumberOfClients. server closed");
				System.out
						.println("error: illegal NumberOfClients. server closed");

				setChanged();
				notifyObservers();
			}

			// return "server closed";

		} catch (Exception e) {
			// return e.getMessage();
			System.out.println(e.getMessage());
		}
	}

	public void close() {
		stop = true;
	}

	public String[] GetClientsList() {
		return clientsList.toArray(new String[clientsList.size()]);
	}

	public void AddLogToMap(String clientIP, String log) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();

		String value = logMap.get(clientIP);
		if (value != null) {
			logMap.put(clientIP, value + dateFormat.format(date) + " - " + log
					+ "\r\n");
			System.out.println(value + "\r\n" + log + "\r\n");
		} else {
			logMap.put(clientIP, dateFormat.format(date) + " - " + log + "\r\n");
			System.out.println(log + "\r\n");
		}
	}

	public String GetClientLog(String clientIP) {
		return logMap.get(clientIP);
	}

	public boolean WriteLog(String log) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();

			FileWriter out = new FileWriter("ServerLog.log", true);
			out.write(dateFormat.format(date) + " - " + log + "\r\n");

			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
