package view;

import helper.ViewUtilities;

import java.util.Observable;

import model.ServerConfiguration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import Screens.OptionScreen;

public class ServerView extends Observable implements View, Runnable {
	private Menu menuBar, fileMenu;
	private MenuItem fileMenuHeader, fileOptionsItem;
	private Text logText, status;
	private Button runServer_btn, stopServer_btn;
	private Display display;
	private Shell shell;
	private OptionScreen optionsScr;

	private List clientsList;
	private int userCommand;
	boolean serverRunning = false;

	@Override
	public int getUserCommand() {
		return userCommand;
	}

	private void setUserCommand(int cmd) {
		userCommand = cmd;
		updateObservers();
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
		display.getFocusControl();

		initButtonsandLabels();

		shell.open();
	}

	private void initButtonsandLabels() {

		menuBar = new Menu(shell, SWT.BAR);
		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("File");

		fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);

		fileOptionsItem = new MenuItem(fileMenu, SWT.PUSH);
		fileOptionsItem.setText("Options");

		optionsScr = new OptionScreen(shell);
		fileOptionsItem.addSelectionListener(new fileOptionsItemListener());

		shell.setMenuBar(menuBar);

		GridData buttonGrid = new GridData(SWT.FILL, SWT.FILL, false, false, 1,
				1);

		runServer_btn = new Button(shell, SWT.BUTTON1);
		runServer_btn.setText("Run Server");
		runServer_btn.setLayoutData(buttonGrid);
		runServer_btn.addSelectionListener(new RunServerItemListener());

		logText = new Text(shell, SWT.NONE | SWT.V_SCROLL | SWT.H_SCROLL);
		logText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5));
		logText.setEditable(false);

		status = new Text(shell, SWT.BORDER | SWT.CENTER);
		status.setText("                                         ");
		// label.setBounds(shell.getClientArea());
		status.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1,
				1));
		status.setEnabled(false);

		stopServer_btn = new Button(shell, SWT.BUTTON1);
		stopServer_btn.setText("Stop Server");
		stopServer_btn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		stopServer_btn.addSelectionListener(new StopServerItemListener());

		clientsList = new List(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		clientsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1));
		clientsList.addSelectionListener(new ClientItemListener());
		clientsList.addListener(SWT.MouseDoubleClick, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				new ClientItemListener().widgetSelected(null);
			}
		});

		ServerConfiguration config = new ServerConfiguration();
		config.Load();
		String invalid = "";
		int port = ViewUtilities.verifyNumberInRange(
				String.valueOf(config.getPort()), 1, 65535);
		if (port == -1)
			invalid += "Invalid Port number, must be number between 1-65535.\n";

		if (config.getNumberOfClients() < 1 || config.getNumberOfClients() > 20)
			invalid += "Invalid number of clients, must be a number between 0-20.";

		if (invalid.isEmpty()) {
			runServer_btn.setEnabled(true);
		} else {
			ViewUtilities.displayMessage(Display.getDefault(), shell,
					"Can not start server", "Change configuration parameters at File -> Options", SWT.ERROR);
			runServer_btn.setEnabled(false);
		}
		stopServer_btn.setEnabled(false);

	}

	class fileOptionsItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			optionsScr.open();
			if (optionsScr.serverEnabled()) {
				setUserCommand(4, new ServerConfiguration(optionsScr.getPort(),
						optionsScr.getNumberOfClient()));
				runServer_btn.setEnabled(true);
			} else
				runServer_btn.setEnabled(false);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
		}
	}

	@Override
	public void run() {
		initComponents();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		setUserCommand(-1);
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

			status.setText("Server Running");
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

			status.setText("Server Stop");
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
			if (clientsList.getSelectionIndex() != -1)
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