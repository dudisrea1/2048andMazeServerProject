package controller;

import java.io.FileWriter;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import presenter.Presenter;
import view.ServerView;
import view.View;
import model.GameModel;
import model.MyServer;
import model.ServerConfiguration;
import model.ServerModel;


public class RunServer{

	
	
	
	public static void main(String[] args) throws Exception {
		ServerView ui = new ServerView();
		ServerModel m = new ServerModel();
		Presenter p = new Presenter(m, ui);
		m.addObserver(p);
		ui.addObserver(p);
		Thread t = new Thread(ui);
		t.run();
		
		
	}
	

	

}
