package src.main.org.bot.hooks;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

	File inputFile;
	Document doc;
	NodeList classes;
	NodeList fields;
	NodeList methods;
	
	public XMLParser(String file) {
		try {
			this.inputFile = new File(file + ".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			this.classes = doc.getElementsByTagName("class");
			this.fields = doc.getElementsByTagName("field");
			this.methods = doc.getElementsByTagName("method");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getClass(String name) {
		String className = null;
		for (int i = 0; i < this.classes.getLength(); i++) {
			Node n = this.classes.item(i);
			if (n.getChildNodes().item(1).getTextContent().equals(name)) {
				className = (n.getChildNodes().item(3).getTextContent());
			}
			
		}
		return className;
	}
	
	public String getMethod(String name) {
		String methodName = null;
		for (int i = 0; i < this.methods.getLength(); i++) {
			Node n = this.methods.item(i);
			//System.out.println(n.getChildNodes().item(3).getTextContent());
			if (n.getChildNodes().item(3).getTextContent().equals(name)) {
				methodName = n.getChildNodes().item(1).getTextContent() + "." + (n.getChildNodes().item(3).getTextContent());
			}
			
		}
		return methodName;
	}
	
	public String getField(String name) {
		String fieldName = null;
		for (int i = 0; i < this.fields.getLength(); i++) {
			Node n = this.fields.item(i);
			if (n.getChildNodes().item(3).getTextContent().equals(name)) {
				fieldName = n.getChildNodes().item(3).getTextContent();
			}
		}
		return fieldName;
	}
	
}
