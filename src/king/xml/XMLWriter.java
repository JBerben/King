package king.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import king.org.Updater;

public class XMLWriter {

	public static void CreateFile(String xml) {
		
		Document dom;
		Element e = null;

		// instance of a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use factory to get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// create instance of DOM
			dom = db.newDocument();

			// create the root element
			Element rootTag = dom.createElement("injector");
			Element classesTag = dom.createElement("classes");
			Element gettersTag = dom.createElement("getters");
			//Element classNodeTag = dom.createElement("classnode");
			//Element classNameTag = dom.createElement("className");
			
			String classNode;
			String className;

			// create data elements and place them under root
			//e = dom.createElement("role1");
			//e.appendChild(dom.createTextNode("role1"));
			//root.appendChild(e);

			// Creates the structure of the XML File
			dom.appendChild(rootTag);
			
			rootTag.appendChild(classesTag);
			
			rootTag.appendChild(gettersTag);
			
			Iterator<Map.Entry<String, ClassNode>> classIterator = Updater.processedClasses.entrySet().iterator();
			while (classIterator.hasNext()) {
				Map.Entry<String, ClassNode> entry = classIterator.next();
				
				Element classNameTag = dom.createElement("classname");
				Element classNodeTag = dom.createElement("classnode");
				Element addClassTag = dom.createElement("add");
				
				classesTag.appendChild(addClassTag);
				
				addClassTag.appendChild(classNameTag);
				addClassTag.appendChild(classNodeTag);
				
				// Adds class to hook file
				className = entry.getKey();
				classNode = entry.getValue().name;
				classNameTag.appendChild(dom.createTextNode(className));
				classNodeTag.appendChild(dom.createTextNode(classNode));				
			}
			
			/* This is where all the fields (accessors) get added
			 * to the hook file
			 */
			Iterator<Map.Entry<String, ClassNode>> fieldIterator = Updater.processedClasses.entrySet().iterator();
			while (fieldIterator.hasNext()) {
				Map.Entry<String, ClassNode> entry = fieldIterator.next();
				for (FieldNode fn : (List<FieldNode>) entry.getValue().fields) {
					
					Element addGettersTag = dom.createElement("add");
					Element accessorTag = dom.createElement("accessor");
					Element fieldTag = dom.createElement("field");
					Element fieldNameTag = dom.createElement("fieldname");
					Element descTag = dom.createElement("desc");
					
					gettersTag.appendChild(addGettersTag);
					
					addGettersTag.appendChild(accessorTag);
					addGettersTag.appendChild(fieldTag);
					addGettersTag.appendChild(fieldNameTag);
					addGettersTag.appendChild(descTag);
					
					accessorTag.appendChild(dom.createTextNode(entry.getValue().name));
					
					/* This conditional statement is used to get the old field name
					 * and display it as well as the new field name so that we have a
					 * reference to how the fields have changed. The oldFieldNames
					 * HashMap is updated in the Manipulate() method in each transform.
					 */
					if (Updater.oldFieldNames.get(fn.name) != null) {
						fieldTag.appendChild(dom.createTextNode(Updater.oldFieldNames.get (fn.name)));
					} else {
						fieldTag.appendChild(dom.createTextNode(fn.name));
					}
					
					fieldNameTag.appendChild(dom.createTextNode(fn.name));
					descTag.appendChild(dom.createTextNode(fn.desc));
				
				}
			}
			
			
			try {
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
				tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "8");

				// send DOM to file
				tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(xml)));

			} catch (TransformerException te) {
				System.out.println(te.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		} catch (ParserConfigurationException pce) {
			System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		}
	}
	
	public static void ParseNode(ClassNode cn) {
		
		
	}
	
}