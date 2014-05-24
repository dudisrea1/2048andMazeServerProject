package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import model.ClientRequest;
import Game2048.Board2048;
import Game2048.Game2048Model;


public class RunClient {

	public static void main(String[] args) throws Exception{
		System.out.println("client side");
		Socket myServer = new Socket(InetAddress.getLocalHost(), 5001);
		System.out.println("connected to server");
		
		
		
		ObjectOutputStream out2server = new ObjectOutputStream(myServer.getOutputStream());
		ObjectInputStream inFromServer =new ObjectInputStream(myServer.getInputStream());
			
		
		System.out.println("sending to server: ");
		Board2048 g2048 = new Board2048(new int[4][4], 0, 4);
//		Game2048Model gModel = new Game2048Model(g2048);
//		gModel.InitBoard();
//		out2server.writeObject(new ClientRequest(g2048, 6, 2, gModel));
		out2server.flush();
		Integer[] movement = (Integer[])inFromServer.readObject();
		System.out.println(movement+"get: "+movement[0]+","+movement[1]);		
		
		out2server.close();
		myServer.close();
		
		
		
	}
}
