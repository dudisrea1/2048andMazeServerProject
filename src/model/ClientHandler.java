package model;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public interface ClientHandler {

	public void handleClient(ObjectInputStream inFromClient,ObjectOutputStream out2client);

}
