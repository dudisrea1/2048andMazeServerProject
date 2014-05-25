package Screens;

import model.ServerConfiguration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class OptionScreen extends Dialog {

	private int port, numberOfClients;
	private Button OK_btn;
	private Text port_text, numberOfClients_text;
	private Group ServerGroup;
	private Shell optinsScreen_shell;
	private ServerConfiguration config;

	public OptionScreen(Shell parent) {
		super(parent, 0);
	}

	/**
	 * Opens the new window shell
	 * 
	 * @return String that represent the chosen game
	 */
	public void open() {
		config = new ServerConfiguration();
		config.Load();

		optinsScreen_shell = new Shell(Display.getCurrent(), (SWT.CLOSE
				| SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE)
				& (~SWT.RESIZE));
		optinsScreen_shell.setLayout(new GridLayout(1, true));
		optinsScreen_shell.setText("Options");

		ServerGroup = new Group(optinsScreen_shell, SWT.SHADOW_OUT);
		ServerGroup.setText("Server configurations:");
		ServerGroup.setLayout(new GridLayout(2, true));
		ServerGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		Label serverPort = new Label(ServerGroup, SWT.NONE);
		serverPort.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		serverPort.setText("Port:");

		port_text = new Text(ServerGroup, SWT.BORDER);
		port_text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		port_text.setText("" + config.getPort());
		Label serverNumberOfClients = new Label(ServerGroup, SWT.NONE);
		serverNumberOfClients.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		serverNumberOfClients.setText("number of clients:");

		numberOfClients_text = new Text(ServerGroup, SWT.BORDER);
		numberOfClients_text.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		numberOfClients_text.setText("" + config.getNumberOfClients());

		OK_btn = new Button(optinsScreen_shell, SWT.PUSH);
		OK_btn.setText("OK");
		OK_btn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		OK_btn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				port = Integer.parseInt(port_text.getText());
				numberOfClients = Integer.parseInt(numberOfClients_text
						.getText());
				optinsScreen_shell.dispose();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				widgetSelected(arg0);

			}
		});
		optinsScreen_shell.pack();
		optinsScreen_shell.open();

		while (!optinsScreen_shell.isDisposed()) {
			if (!Display.getCurrent().readAndDispatch()) {
				Display.getCurrent().sleep();
			}
		}

	}
	public int getPort() {
		return port;
	}

	public int getNumberOfClient() {
		return numberOfClients;
	}

}
