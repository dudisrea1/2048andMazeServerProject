package view;

import java.util.Observable;

import model.MyServer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ServerView extends Observable implements View, Runnable {
	
	private Label label,logText;
//	private Text logText;
	private Button runServer_btn, stopServer_btn;
	private Display display;
	private Shell shell;
	
	private List clientsList;
	private int userCommand;
	boolean serverRunning = false;

	@Override
	public int getUserCommand() {
		return userCommand;
	}

	private void setUserCommand(final int cmd) {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				userCommand = cmd;
				updateObservers();
			}
		});

	}

	private void setUserCommand(final int cmd, final Object args) {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				userCommand = cmd;
				updateObservers(args);
			}
		});
	}

	private void initComponents() {
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(600, 600);
		shell.setMinimumSize(600, 600);
		shell.setText("Game Server");
		shell.addShellListener(new ShellListener() {
			
			@Override
			public void shellIconified(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellDeiconified(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellDeactivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellClosed(ShellEvent arg0) {
				setUserCommand(2);
				
			}
			
			@Override
			public void shellActivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		display.getFocusControl();

		initButtonsandLabels();

				
		shell.open();
	}

	private void initButtonsandLabels() {

		
		GridData buttonGrid = new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1);

		runServer_btn = new Button(shell, SWT.BUTTON1);
		runServer_btn.setText("Run Server");
		runServer_btn.setLayoutData(buttonGrid);
		runServer_btn.addSelectionListener(new RunServerItemListener());

		stopServer_btn = new Button(shell, SWT.BUTTON1);
		stopServer_btn.setText("Stop Server");
		stopServer_btn.setLayoutData(buttonGrid);
		stopServer_btn.addSelectionListener(new StopServerItemListener());
		
		clientsList = new List(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		clientsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,	2, 1));
		clientsList.addSelectionListener(new ClientItemListener());
		
//		logText = new Text(shell, SWT.BORDER);
		logText = new Label(shell,SWT.BORDER);
		
		logText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,	2, 1));
		

		label = new Label(shell, SWT.CENTER);
		label.setBounds(shell.getClientArea());
		label.setLayoutData(buttonGrid);
		
	}

	@Override
	public void run() {
		initComponents();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// ADD SOMETHING THAT CLOSES THE SERVER
		display.dispose();
	}

	@Override
	public void displayError(String string) {
		MessageBox mb = new MessageBox(shell, SWT.ERROR);
		mb.setText("Error");
		mb.setMessage(string);
		mb.open();
	}

	private void updateObservers() {
		setChanged();
		notifyObservers();

	}

	private void updateObservers(Object args) {
		setChanged();
		notifyObservers(args);

	}

	
	class RunServerItemListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			widgetSelected(arg0);
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {

			label.setText("Server Running");
			setUserCommand(1);
			stopServer_btn.setEnabled(true);
			runServer_btn.setEnabled(false);
		}

	}

	class StopServerItemListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			widgetSelected(arg0);
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {

			label.setText("Server Stop");
			setUserCommand(2);
			stopServer_btn.setEnabled(false);
			runServer_btn.setEnabled(true);
		}

	}
	class ClientItemListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			widgetSelected(arg0);
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {

			setUserCommand(3, clientsList.getSelection()[0]);
			
		}

	}
	
	

	@Override
	public void displayClientLog(final String log) {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				logText.setText(log);
			}
		});

	}

	@Override
	public void displayConnectedClients(final String[] clients) {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (clients != null && clients.length > 0)
					clientsList.setItems(clients);
			}
		});

	}
}