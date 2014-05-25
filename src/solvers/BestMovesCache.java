package solvers;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class BestMovesCache {

	/**
	 * Saves a given HashMap to file
	 * 
	 * @param cache
	 *            - represents the HashMap
	 * @param FileName
	 *            - represents the location of the file
	 * @return return true if the file saved successfully, otherwise returns
	 *         false
	 */
	public boolean ToFile(HashMap<String, String> cache, String FileName) {
		XStream xstream = new XStream(new StaxDriver());
		try {
			FileOutputStream outputStream = new FileOutputStream(FileName);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			xstream.toXML(cache, writer);
			return true;

		} catch (Exception exp) {
			return false;
		}
	}

	/**
	 * Loads an HashMap from a file
	 * 
	 * @param FileName
	 *            - location of the XML file contains the HashMap
	 * @return a new HashMap if the operation was finished successfully, else
	 *         returns a null object
	 */
	public HashMap<String, String> FromFile(String FileName) {
		XStream xstream = new XStream(new StaxDriver());
		try {
			BufferedReader br = new BufferedReader(new FileReader(FileName));
			StringBuffer buff = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				buff.append(line);
			}

			HashMap<String, String> LoadedMap = (HashMap<String, String>) xstream
					.fromXML(buff.toString());
			br.close();
			return LoadedMap;

		} catch (Exception exp) {
			return null;
		}
	}

}
