package presenter;

import java.util.Observable;
import java.util.Observer;

import model.ServerConfiguration;
import model.ServerModel;
import view.View;

public class Presenter implements Observer {
	private View ui;
	private ServerModel myServer;

	public Presenter(ServerModel m,View ui) {
		this.ui = ui;
		myServer = m;
		myServer.InitServer();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 == ui) {
			switch (ui.getUserCommand()) {
			case -1:
				myServer.StopServer();
				break;
			// run server
			case 1:
				myServer.RunServer();
				break;
			case 2:
				myServer.StopServer();
				break;
			case 3: // show client log
				ui.displayClientLog(myServer.GetClientLog((String) arg1));
				break;
			case 4: // set server configuration
				myServer.SetServerConfiguration((ServerConfiguration) arg1);
				break;
			}

		} else {
			ui.displayConnectedClients(myServer.GetClientsList());

		}
	}
}
