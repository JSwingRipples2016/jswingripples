package org.incha.core;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
/**
 * Stores information and methods for the parser of XML files.
 */
public class ModelSerializer {
    /**
     * XML attribute name.
     */
    private static final String NAME_ATTR = "name";
    /**
     * XML element 'file'.
     */
    private static final String FILE = "file";
    /**
     * XML element 'resource'.
     */
    private static final String SOURCES = "sources";
    /**
     * XML element 'project'.
     */
    private static final String PROJECT = "project";
    /**
     * XML element 'application'.
     */
    private static final String APPLICATION = "application";

    /**
     * Default constructor.
     */
    public ModelSerializer() {
        super();
    }

    public JavaProjectsModel parse(final Reader r) throws SAXException, IOException {
        final DocumentBuilder db = newDocumentBuilder();
        final Document dom = db.parse(new InputSource(r));

        final JavaProjectsModel model = new JavaProjectsModel();
        for (final Element p: getChildElements(dom.getDocumentElement(), PROJECT)) {
            parseProject(model, p);
        }

        return model;
    }
    /**
     * @param model model.
     * @param projectItem XML item.
     */
    private void parseProject(final JavaProjectsModel model, final Element projectItem) {
        final JavaProject project = new JavaProject(projectItem.getAttribute(NAME_ATTR));
        model.addProject(project);

        //process sources
        final Element sources = getFirstChildElement(projectItem, SOURCES);

        List<Element> files = getChildElements(sources, FILE);
        for (final Element e : files) {
            project.getBuildPath().addSource(new File(e.getTextContent()));
        }

    }

    /**
     * @param element the element.
     * @param tagName the tag name.
     * @return
     */
    private List<Element> getChildElements(final Element element, final String tagName) {
        final NodeList nodes = element.getChildNodes();
        final List<Element> result = new LinkedList<Element>();

        final int len = nodes.getLength();
        for (int i = 0; i < len; i++) {
            final Node n = nodes.item(i);
            if (n instanceof Element) {
                final Element e = (Element) n;
                if (tagName.equals(e.getTagName())) {
                    result.add(e);
                }
            }
        }

        return result;
    }
    /**
     * @param element the element.
     * @param tagName the tag name.
     * @return the first found child element by given child name.
     */
    private Element getFirstChildElement(final Element element, final String tagName) {
        final List<Element> els = getChildElements(element, tagName);
        return els.size() == 0 ? null : els.get(0);
    }

    /**
     * @param model saves model.
     * @param out output writer.
     * @throws TransformerException
     */
    public void save(final JavaProjectsModel model, final Writer out) throws TransformerException {
        final Document dom = newDocumentBuilder().newDocument();
        final Element application = dom.createElement(APPLICATION);
        dom.appendChild(application);

        for (final JavaProject p : model.getProjects()) {
            final Element project = dom.createElement(PROJECT);
            project.setAttribute(NAME_ATTR, p.getName());
            application.appendChild(project);

            //add build path
            //add sources
            final Element sources = dom.createElement(SOURCES);
            project.appendChild(sources);
            for (final File f : p.getBuildPath().getSources()) {
                final Element src = dom.createElement(FILE);
                sources.appendChild(src);
                src.appendChild(dom.createTextNode(f.getPath()));
            }

        }

        saveDom(dom, out);
    }

    /**
     * @return
     * @throws ParserConfigurationException
     */
    private DocumentBuilder newDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param node the node to save.
     * @param out the output.
     * @throws TransformerException
     */
    private void saveDom(final Node node, final Writer out) throws TransformerException {
        final TransformerFactory f = TransformerFactory.newInstance();
        final Transformer transformer = f.newTransformer();

        final DOMSource source = new DOMSource(node);
        final StreamResult result = new StreamResult(out);
        transformer.transform(source, result);
    }
}
