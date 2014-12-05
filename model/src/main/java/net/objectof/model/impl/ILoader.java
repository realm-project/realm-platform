package net.objectof.model.impl;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import net.objectof.aggr.Aggregate;
import net.objectof.aggr.impl.IListing;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.ResourceException;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;
import net.objectof.rt.impl.base.Ex;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ILoader extends IListing<Object>
{
  private static final long serialVersionUID = 1L;

  public static Document parseDocument(File aFile) throws ResourceException
  {
    try
    {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(aFile);
    }
    catch (Exception e)
    {
      throw new Ex(aFile, e);
    }
  }

  private final Transaction theTx;

  public ILoader(Transaction aTransaction)
  {
    super(Object.class);
    theTx = aTransaction;
  }

  public int load(Element aElement)
  {
    int count = 0;
    for (Node n = aElement.getFirstChild(); n != null; n = n.getNextSibling())
    {
      if (n.getNodeType() == Node.ELEMENT_NODE)
      {
        Element element = (Element) n;
        String pathname = element.getTagName();
        Kind<?> type = theTx.getPackage().forName(pathname);
        if (type == null)
        {
          System.out.println("Unrecognized path: " + pathname);
        }
        Resource<?> value = toInstance(type, element);
        // Class<?> eleType = IPrototype.fromName(getElementType().getPeer());
        // Class<?> valType = IPrototype.fromName(value.getKind().getPeer());
        if (!type().getEvaluation().isInstance(value))
        {
          throw new RuntimeException("Instance '" + value
              + "' is not an instance of '" + type().getEvaluation() + "'");
        }
        add(value);
        count++;
      }
    }
    return count;
  }

  public int load(File aFile)
  {
    Element ele;
    try
    {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(aFile);
      ele = doc.getDocumentElement();
    }
    catch (Exception e)
    {
      throw new Ex(aFile, e);
    }
    return load(ele);
  }

  public int load(InputStream aStream)
  {
    Element ele;
    try
    {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(aStream);
      ele = doc.getDocumentElement();
    }
    catch (Exception e)
    {
      throw new Ex(aStream, e);
    }
    return load(ele);
  }

  public int load(String aURL)
  {
    Element ele;
    try
    {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(aURL);
      ele = doc.getDocumentElement();
    }
    catch (Exception e)
    {
      throw new Ex(aURL, e);
    }
    return load(ele);
  }

  protected Resource<?> toInstance(Kind<?> aType, Element aElement)
  {
    Resource<?> object = (Resource<?>) theTx.create(aType.getComponentName());
    int count = 0;
    for (Node n = aElement.getFirstChild(); n != null; n = n.getNextSibling())
    {
      if (n.getNodeType() == Node.ELEMENT_NODE)
      {
        Element element = (Element) n;
        loadElement(object, count++, element);
      }
    }
    return object;
  }

  protected Object toObject(Kind<?> aType, Element aElement)
  {
    switch (aType.getStereotype())
    {
    case MOMENT:
      return toDate(aType, aElement);
    case NUM:
      return toDouble(aType, aElement);
    case INT:
      return toLong(aType, aElement);
    case COMPOSED:
    case INDEXED:
      return toInstance(aType, aElement);
    case REF:
      return dereference(aType, aElement);
    case TEXT:
    default:
      return toString(aType, aElement);
    }
  }

  private Object dereference(Kind<?> aType, Element aElement)
  {
    String name = aElement.getTextContent();
    int idx = name.indexOf('/');
    String kind = name.substring(0, idx);
    String id = name.substring(idx + 1);
    Object object = theTx.retrieve(kind, id);
    if (object == null)
    {
      System.out.println("Warning: Instance '" + name + "' not found.");
    }
    return object;
  }

  private void loadElement(Resource<?> aParent, int aCount, Element aElement)
  {
    String pathname = aParent.id().kind().getComponentName() + '.'
        + aElement.getTagName();
    Kind<?> type = theTx.getPackage().forName(pathname);
    if (type == null)
    {
      System.out.println("Unrecognized path: " + pathname);
    }
    Object key = aParent.id().kind().getStereotype() == Stereotype.INDEXED ? aCount
        : aElement.getTagName();
    Object value = toObject(type, aElement);
    @SuppressWarnings("unchecked")
    Aggregate<Object, Object> val = (Aggregate<Object, Object>) aParent.value();
    val.set(key, value);
  }

  private Object toDate(Kind<?> aType, Element aElement)
  {
    try
    {
      return new IMoment(aElement.getTextContent());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private Object toDouble(Kind<?> aType, Element aElement)
  {
    return Double.parseDouble(aElement.getTextContent());
  }

  private Object toLong(Kind<?> aType, Element aElement)
  {
    return Long.parseLong(aElement.getTextContent());
  }

  private Object toString(Kind<?> aType, Element aElement)
  {
    return aElement.getTextContent();
  }
}
