package Converter;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class ServerXMLManager {

	public boolean ToFile(Object object, String FileName){
		XStream xstream = new XStream(new StaxDriver());
		try {
			FileOutputStream outputStream = new FileOutputStream(FileName);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			xstream.toXML(object, writer);
			return true;

		} catch (Exception exp) {
			return false;
		}
	}
	
	

	
	public Object FromFile(String FileName) {
		XStream xstream = new XStream(new StaxDriver());
		try {
			BufferedReader br = new BufferedReader(new FileReader(FileName));
			StringBuffer buff = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				buff.append(line);
			}

			Object LoadedModel = xstream.fromXML(buff
					.toString());
			br.close();
			return LoadedModel;

		} catch (Exception exp) {
			return null;
		}
	}
}
