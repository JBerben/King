package src.main.org.updater.xml;

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

import src.main.org.updater.Updater;
import src.main.org.updater.obj.GameField;
import src.main.org.updater.obj.GameMethod;

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
			Element rootTag = dom.createElement("Injector");
			Element classesTag = dom.createElement("Classes");
			Element fieldsTag = dom.createElement("Fields");
			Element methodsTag = dom.createElement("Methods");
			// Element classNodeTag = dom.createElement("classnode");
			// Element classNameTag = dom.createElement("className");

			String classNode;
			String className;

			// create data elements and place them under root
			// e = dom.createElement("role1");
			// e.appendChild(dom.createTextNode("role1"));
			// root.appendChild(e);

			// Creates the structure of the XML File
			dom.appendChild(rootTag);

			rootTag.appendChild(classesTag);

			rootTag.appendChild(methodsTag);

			rootTag.appendChild(fieldsTag);

			/*
			 * Writes classes to XML
			 */
			Iterator<Map.Entry<String, String>> classIterator = Updater.processedParsedClasses.entrySet().iterator();
			while (classIterator.hasNext()) {
				Map.Entry<String, String> entry = classIterator.next();

				if (entry.getKey() != null && entry.getValue() != null) {
					Element classNameTag = dom.createElement("classname");
					Element classNodeTag = dom.createElement("classnode");
					Element addClassTag = dom.createElement("class");

					classesTag.appendChild(addClassTag);
					addClassTag.appendChild(classNameTag);
					addClassTag.appendChild(classNodeTag);

					// Adds class to hook file
					className = entry.getKey();
					classNode = entry.getValue();
					classNameTag.appendChild(dom.createTextNode(className));
					classNodeTag.appendChild(dom.createTextNode(classNode));
				}
			}

			/*
			 * This is where all the methods get added to the hook file
			 */
			for (GameMethod gm : Updater.processedGameMethods.values()) {

				if (gm.getClassNode().name != null && gm.getMethodNode().name != null && gm.getMethodNode().desc != null
						&& gm.getName() != null && Updater.processedParsedClasses.get(gm.getClassNode().name) != null) {

					Element addMethodsTag = dom.createElement("method");
					Element accessorTag = dom.createElement("accessor");
					Element methodTag = dom.createElement("methodname");
					Element methodNameTag = dom.createElement("methodnode");
					Element descTag = dom.createElement("desc");

					methodsTag.appendChild(addMethodsTag);

					addMethodsTag.appendChild(accessorTag);
					addMethodsTag.appendChild(methodTag);
					addMethodsTag.appendChild(methodNameTag);
					addMethodsTag.appendChild(descTag);

					String modifiedName = Updater.processedParsedClasses.get(gm.getClassNode().name);
					// System.out.println("NNNNNNNNNNNNN: " + modifiedName);
					// modifiedName = modifiedName.replace("/", ".");
					accessorTag.appendChild(dom.createTextNode(modifiedName));

					/*
					 * This conditional statement is used to get the old field
					 * name and display it as well as the new field name so that
					 * we have a reference to how the fields have changed. The
					 * oldFieldNames HashMap is updated in the Manipulate()
					 * method in each transform.
					 */
					methodTag.appendChild(dom.createTextNode(gm.getMethodNode().name));
					methodNameTag.appendChild(dom.createTextNode(gm.getName()));
					descTag.appendChild(dom.createTextNode(gm.getMethodNode().desc));

				}

			}
			/*
			 * This is where all the fields (accessors) get added to the hook
			 * file
			 */
			Iterator<Map.Entry<GameField, GameField>> fieldIterator = Updater.matchedFields.entrySet().iterator();
			while (fieldIterator.hasNext()) {
				Map.Entry<GameField, GameField> entry = fieldIterator.next();

				if (entry.getKey().getDesc() != null && entry.getValue().getName() != null
						&& entry.getKey().getName() != null) {

					Element addFieldsTag = dom.createElement("field");
					Element accessorTag = dom.createElement("accessor");
					Element fieldTag = dom.createElement("fieldname");
					Element fieldNameTag = dom.createElement("fieldnode");
					Element descTag = dom.createElement("desc");

					fieldsTag.appendChild(addFieldsTag);

					addFieldsTag.appendChild(accessorTag);
					addFieldsTag.appendChild(fieldTag);
					addFieldsTag.appendChild(fieldNameTag);
					addFieldsTag.appendChild(descTag);

					String modifiedName = Updater.processedParsedClasses.get(entry.getKey().getAcessor().name);
					// modifiedName = modifiedName.replace("/", ".");
					accessorTag.appendChild(dom.createTextNode(modifiedName));

					/*
					 * This conditional statement is used to get the old field
					 * name and display it as well as the new field name so that
					 * we have a reference to how the fields have changed. The
					 * oldFieldNames HashMap is updated in the Manipulate()
					 * method in each transform.
					 */
					fieldTag.appendChild(dom.createTextNode(entry.getValue().getName()));

					fieldNameTag.appendChild(dom.createTextNode(entry.getKey().getName()));
					descTag.appendChild(dom.createTextNode(entry.getKey().getDesc()));

					if (entry.getKey().getDesc() == null || entry.getValue().getName() == null
							|| entry.getKey().getName() == null) {
						System.out.println(entry.getValue().getName() + " : " + entry.getKey().getName() + " : "
								+ entry.getKey().getDesc());
					}

				}
			}

			try {
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
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