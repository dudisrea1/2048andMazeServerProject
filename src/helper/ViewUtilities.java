package helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Utilities for view
 * 
 * @author Dudi
 * 
 */
public class ViewUtilities {
	/**
	 * Verifies that the given string is a valid number - contains only numbers
	 * and between the range of min to max
	 * 
	 * @param number
	 *            - the string that needs to be converted to number
	 * @param min
	 *            - the lowest possible value (included)
	 * @param max
	 *            - the highest possible value (included)
	 * @return the String as int data type, -1 if the string is not a valid
	 *         number
	 */

	public static int verifyNumberInRange(String number, int min, int max) {
		char[] chars = new char[number.length()];
		number.getChars(0, chars.length, chars, 0);
		for (int i = 0; i < chars.length; i++) {
			if (!('0' <= chars[i] && chars[i] <= '9')) {
				return -1;
			}
		}
		int tmpNumber = Integer.parseInt(number);
		if (tmpNumber >= min && tmpNumber <= max)
			return tmpNumber;
		return -1;
	}

	/**
	 * Returns true if the string input in address is a valid IPv4 or IPv6
	 * 
	 * @param IP
	 *            - the IP address to verify
	 * @return boolean
	 */
	public static boolean VerifyAddress(String ipAddress) {
		Pattern VALID_IPV4_PATTERN = null;
		Pattern VALID_IPV6_PATTERN = null;
		String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
		String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";

		try {
			VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern,
					Pattern.CASE_INSENSITIVE);
			VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern,
					Pattern.CASE_INSENSITIVE);
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
		}
		Matcher m1 = VALID_IPV4_PATTERN.matcher(ipAddress);
		if (m1.matches()) {
			return true;
		}
		Matcher m2 = VALID_IPV6_PATTERN.matcher(ipAddress);
		return m2.matches();
	}

	/**
	 * Displays an message box based on given string and event type
	 * 
	 * @param shell
	 * @param string
	 * @param event
	 *            ie, event can be SWT.ERROR or SWT.ICON_INFORMATION
	 */
	public static void displayMessage(Display dsp, final Shell shell,
			final String text, final String message, final int event) {
		dsp.syncExec(new Runnable() {
 
			@Override
			public void run() {
				if (!shell.isDisposed()) {
					MessageBox mb = new MessageBox(shell, event);
					mb.setText(text);
					mb.setMessage(message);
					mb.open();
				}
			}
		});
	}
}
