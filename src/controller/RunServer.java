package controller;

import model.MyServer;
import view.ServerView;

public class RunServer {
	public static void main(String[] args) throws Exception {
		ServerView ui = new ServerView();
		MyServer m = new MyServer();
		Controller p = new Controller(m, ui);
		ui.addObserver(p);
		m.addObserver(p);
		Thread t = new Thread(ui);
		t.run();

	}
}
