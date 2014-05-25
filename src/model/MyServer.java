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

import solvers.AIsolver;
import solvers.Solver;

public class MyServer extends Observable implements ServerModel {

	private int port;
	private ServerSocket server;
	private boolean stop;
	private int NumberOfClients;
	private ClientHandler clientHandler;
	private ServerConfiguration config;
	private List<String> clientsList = new ArrayList<String>();
	private Map<String, String> logMap;
	private Solver solver;
	
	public MyServer() {
		config = new ServerConfiguration();
		config.Load();
		this.port = config.getPort();
		stop = false;
		this.NumberOfClients = config.getNumberOfClients();
		logMap = new HashMap<String, String>();

	}

	@Override
	public void SetServerConfiguration(ServerConfiguration config) {
		this.config=config;
		this.config.Save();
		this.port = config.getPort();
		this.NumberOfClients = config.getNumberOfClients();
	}
	@Override
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
						ExecutorService ThreadPool = Executors
								.newFixedThreadPool(NumberOfClients);

						while (!stop) {
							try {
								final Socket someClient = server.accept();
								ClientIP = someClient.getRemoteSocketAddress()
										.toString().substring(1);

								AddLogToMap(ClientIP, "Client Connected");
								WriteLog("Client Connected: " + ClientIP);

								clientsList.add(ClientIP);
								setChanged();
								notifyObservers();
								ThreadPool.execute(new Runnable() {

									@Override
									public void run() {
										clientHandler.handleClient(someClient);
									}
								});

							} catch (SocketTimeoutException e) {
							} catch (IOException e1) {
								e1.printStackTrace();
							}

						}
						try {
							ThreadPool.shutdown();
							server.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						WriteLog("Server Closed");
					}
				});
				t.start();
				setChanged();
				notifyObservers();

			} else {
				WriteLog("error: illegal NumberOfClients. server closed");
				setChanged();
				notifyObservers();
			}
		} catch (Exception e) {
		}
	}

	public void close() {

		stop = true;
	}

	@Override
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
		} else {
			logMap.put(clientIP, dateFormat.format(date) + " - " + log + "\r\n");
		}
	}

	@Override
	public String GetClientLog(String clientIP) {
		return logMap.get(clientIP);
	}

	@Override
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

	@Override
	public void InitServer() {

		solver=new AIsolver();
		
		clientHandler = new ClientHandler() {
			@Override
			public void handleClient(Socket someClient) {
				ClientRequest request;
				ObjectInputStream inFromClient = null;
				ObjectOutputStream out2client = null;
				try {
					while (!someClient.isClosed()) {
						inFromClient = new ObjectInputStream(
								someClient.getInputStream());
						out2client = new ObjectOutputStream(
								someClient.getOutputStream());
						request = (ClientRequest) (inFromClient.readObject());
						//AIsolver ai = new AIsolver();
						//Integer[] move = ai.findBestMove(request.getBoard(),request.getDepth(), request.getMethod(),request.getModel());
						Integer[] move = solver.Run(request);
						
						AddLogToMap(someClient.getRemoteSocketAddress()
								.toString().substring(1), request.getModel()
								.ArrayToString(move));
						// Double check if the client didn't disconnect in the
						// middle
						if (!someClient.isClosed()) {
							out2client.writeObject(move);
							out2client.flush();
						}
					}
//					AddLogToMap(someClient.getRemoteSocketAddress().toString()
//							.substring(1), "Client Disconnected");
//					someClient.close();
				} catch (Exception e) {
					try {
						AddLogToMap(someClient.getRemoteSocketAddress().toString()
								.substring(1), "Client Disconnected");
						someClient.close();
					} catch (IOException e1) {
					}
				}
			}
		};

	}
	@Override
	public boolean RunServer() {
		try {
			start();
			WriteLog("server started");
			return true;

		} catch (Exception e) {
			return false;
		}
	}
	@Override
	public boolean StopServer() {
		try {
			close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
