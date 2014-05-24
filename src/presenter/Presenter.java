package presenter;

import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import model.ClientHandler;
import model.ClientRequest;
import model.MyServer;
import model.ServerModel;
import solvers.AIsolver;
import view.View;

public class Presenter implements Observer {
	private View ui;
	private ServerModel model;
	private MyServer myServer;

	public Presenter(ServerModel model, View ui) {
		this.ui = ui;
		this.model = model;
		InitServer();
		myServer.addObserver(this);
	}

	public void InitServer() {

		myServer = new MyServer(new ClientHandler() { // Defined server with 2
					// clients same time
					@Override
					public void handleClient(ObjectInputStream inFromClient,ObjectOutputStream out2client) {
						ClientRequest request;
						try {
						request = (ClientRequest) (inFromClient.readObject());
						
							
							
							Integer[] stam = AIsolver.findBestMove(request.getBoard(), request.getDepth(),request.getMethod(), request.getModel());
							System.out.println(stam[0]+","+stam[1]);
							out2client.writeObject(stam);
							

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}

	public boolean RunServer() {
		try {
			myServer.start();
			System.out.println("server started");
			WriteLog("server started");
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public boolean StopServer() {
		try {
			myServer.close();
			return true;
		} catch (Exception e) {
			return false;
		}
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

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 == ui) {
			switch (ui.getUserCommand()) {
			// run server
			case 1:
				RunServer();
				break;
			case 2:
				StopServer();
				break;
			case 3: // show client log
				ui.displayClientLog(myServer.GetClientLog((String) arg1));
			}

		} else {
			ui.displayConnectedClients(myServer.GetClientsList());

		}
	}
}
