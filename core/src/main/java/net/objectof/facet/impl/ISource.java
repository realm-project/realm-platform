package net.objectof.facet.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import net.objectof.facet.Facet;
import net.objectof.facet.Faceted;
import net.objectof.facet.Property;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class ISource<T extends Faceted> extends IFaceted<T>
{
  public static Document parseFile(File aFile)
  {
    try
    {
      DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
      fac.setNamespaceAware(true);
      if (!aFile.exists())
      {
        throw new FileNotFoundException(aFile.getCanonicalPath());
      }
      return fac.newDocumentBuilder().parse(aFile);
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException("Cannot parse '" + aFile + "'", e);
    }
  }

  public ISource(Document aDocument)
  {
    this(aDocument.getDocumentElement().getAttribute("id"));
    aDocument.getDocumentElement().getChildNodes();
    NodeList models = aDocument.getDocumentElement().getElementsByTagName(
        "model");
    if (models.getLength() != 1)
    {
      throw new RuntimeException(aDocument.getDocumentURI()
          + " must have one '<model>' Element");
    }
    process(null, (Element) models.item(0));
  }

  public ISource(File aFile)
  {
    this(parseFile(aFile));
  }

  protected ISource(String aUniformName)
  {
    super(aUniformName);
  }

  // TODO make sure that the parent adds the child:
  /**
   * 
   * <pre>
   * if (aParent != null)
   * {
   *   aParent.getParts().add(component);
   * }
   * </pre>
   * 
   * @param aParent
   * @param aSelector
   * @param aTag
   * @return
   */
  protected abstract T create(Faceted aParent, String aSelector, String aTag);

  protected abstract Facet<?> getFacet(String aUniqueName);

  protected void process(Faceted aParent, Element e)
  {
    for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling())
    {
      if (n.getNodeType() == Node.ELEMENT_NODE)
      {
        Element element = (Element) n;
        processElement(aParent, element);
      }
    }
  }

  protected Map<String, Property> processAttributes(Faceted aType,
      Element aElement)
  {
    NamedNodeMap attrs = aElement.getAttributes();
    int len = attrs.getLength();
    Map<String, Property> map = null;
    for (int i = 0; i < len; i++)
    {
      Node node = attrs.item(i);
      String ns = node.getNamespaceURI();
      if (ns != null && ns.startsWith("ans:"))
      {
        if (map == null)
        {
          map = new HashMap<String, Property>();
        }
        String selector = node.getLocalName();
        String val = node.getNodeValue();
        Facet<?> facet = getFacet(ns);
        Property p = createProperty(facet, selector, aType, val);
        String un = ns + '/' + selector;
        System.out.println(aElement.getAttribute("selector")
            + "\tmetadata name: " + un + " value: " + val);
        map.put(un, p);
      }
    }
    return map;
  }

  protected void processElement(Faceted aParent, Element aElement)
  {
    String tag = aElement.getTagName();
    String selector = aElement.getAttribute("selector");
    T component = create(aParent, selector, tag);
    Map<String, Property> properties = processAttributes(component, aElement);
    setProperties(component, properties);
    getMap().put(component.getComponentName(), component);
    process(component, aElement);
  }

  /**
   * Override when creating components that are not IFaceted.
   * 
   * @param aFaceted
   * @param aProperties
   */
  protected void setProperties(T aFaceted, Map<String, Property> aProperties)
  {
    IFaceted<?> f = (IFaceted<?>) aFaceted;
    f.setProperties(aProperties);
  }
}
