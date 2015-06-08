package net.objectof.model.impl.facets;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import net.objectof.facet.Property;
import net.objectof.model.Kind;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.IPackage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


// import net.objectof.model.impl.res.IKind;
public class ISourcePackage extends IPackage {

    public static Document parseFile(File aFile) {
        try {
            if (!aFile.exists()) { throw new FileNotFoundException(aFile.getCanonicalPath()); }
            return parseStream(new FileInputStream(aFile));
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot parse '" + aFile + "'", e);
        }
    }

    public static Document parseStream(InputStream aStream) {
        try {
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            return fac.newDocumentBuilder().parse(aStream);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot read input", e);
        }
    }

    private final IMetamodel theMetamodel;

    public ISourcePackage(IMetamodel aMetamodel, Document aDocument) {
        this(aMetamodel, aDocument.getDocumentElement().getAttribute("id"));
        aDocument.getDocumentElement().getChildNodes();
        NodeList models = aDocument.getDocumentElement().getElementsByTagName("model");
        if (models.getLength() != 1) { throw new RuntimeException(aDocument.getDocumentURI()
                + " must have one '<model>' Element"); }
        process(null, (Element) models.item(0));
        // set up reference targets from the m:href properties
        for (Kind<?> kind : this) {
            if (kind.getStereotype() != Stereotype.REF) {
                continue;
            }
            Property href = kind.getProperty("ans://objectof.net:1401/facets/model/href");
            if (href == null) { throw new NullPointerException("Cannot find Reference target for "
                    + kind.getComponentName()); }
            IKind<?> target = (IKind<?>) forName(href.getSource());
            ((IKind<?>) kind).getParts().add(target);
        }
    }

    public ISourcePackage(IMetamodel aMetamodel, File aFile) {
        this(aMetamodel, parseFile(aFile));
    }

    protected ISourcePackage(IMetamodel aMetamodel, String aUniformName) {
        super(aUniformName);
        theMetamodel = aMetamodel;
    }

    @Override
    public IMetamodel getMetamodel() {
        return theMetamodel;
    }

    protected IKind<?> createKind(String aPathname, Stereotype aStereotype) {
        return new IKind<>(this, aPathname, aStereotype);
    }

    protected void process(IKind<?> aParent, Element e) {
        for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) n;
                processElement(aParent, element);
            }
        }
    }

    protected Map<String, Property> processAttributes(Kind<?> aType, Element aElement) {
        NamedNodeMap attrs = aElement.getAttributes();
        int len = attrs.getLength();
        Map<String, Property> map = null;
        for (int i = 0; i < len; i++) {
            Node node = attrs.item(i);
            String ns = node.getNamespaceURI();
            if (ns != null && ns.startsWith("ans:")) {
                if (map == null) {
                    map = new HashMap<String, Property>();
                }
                String un = ns + '/' + node.getLocalName();
                String val = node.getNodeValue();
                Property p = createProperty(ns, node.getLocalName(), aType, val);
                map.put(un, p);
            }
        }
        return map;
    }

    protected void processElement(IKind<?> aParent, Element aElement) {
        Stereotype stereotype = getStereotype(aElement.getTagName());
        if (stereotype == null) { throw new RuntimeException("Invalid element '" + aElement.getTagName()
                + "': No Stereotype defined."); }
        String selector = aElement.getAttribute("selector");
        String pathname = (aParent != null ? aParent.getComponentName() + "." : "") + selector;
        IKind<?> type = createKind(pathname, stereotype);
        Map<String, Property> properties = processAttributes(type, aElement);
        type.setProperties(properties);
        getMap().put(type.getComponentName(), type);
        if (aParent != null) {
            aParent.getParts().add(type);
        }
        process(type, aElement);
    }
}
