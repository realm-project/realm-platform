package net.objectof.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.objectof.InvalidNameException;
import net.objectof.Type;
import net.objectof.aggr.Listing;
import net.objectof.aggr.impl.IFieldset;
import net.objectof.aggr.impl.IListing;
import net.objectof.ext.Target;
import net.objectof.facet.Property;
import net.objectof.model.Kind;
import net.objectof.model.Stereotype;
import net.objectof.rt.Argument;
import net.objectof.rt.Interface;
import net.objectof.rt.Member;
import net.objectof.rt.Invocation;
import net.objectof.rt.Visibility;
import net.objectof.rt.impl.base.IPeer;

import org.springframework.expression.EvaluationException;

public class IKind<T> extends IPeer<T> implements Kind<T>, Member
{
  private static final Iterable<String> NO_PROPERTIES = Collections.emptyList();
  private final IPackage thePackage;
  private final String theParentName;
  private final String theName;
  private final IDatatype<T> theDatatype;
  private Map<String, Property> theProperties;
  private Listing<IKind<?>> theParts;
  private long theLastTransientId = 0;
  private IFieldset theFields;

  @SuppressWarnings("unchecked")
  public IKind(IPackage aPackage, String aComponentName, Stereotype aStereotype)
  {
    super((Class<T>) aPackage.getDatatype(aStereotype).peer());
    theDatatype = aPackage.getDatatype(aStereotype);
    thePackage = aPackage;
    int index = aComponentName.lastIndexOf('.');
    theParentName = index > 0 ? aComponentName.substring(0, index) : null;
    theName = aComponentName.substring(index + 1);
  }

  public final IDatatype<T> datatype()
  {
    return theDatatype;
  }

  public IKind<?> findMember(String aSelector)
  {
    for (IKind<?> kind : getParts())
    {
      if (kind.getSelector().equals(aSelector))
      {
        return kind;
      }
    }
    throw new InvalidNameException(aSelector);
  }

  @Override
  public String getComponentName()
  {
    String parent = theParentName;
    String name = theName;
    return parent == null ? name : parent + '.' + name;
  }

  public IFieldset getFields()
  {
    IFieldset fields = theFields;
    if (fields == null && getStereotype() == Stereotype.COMPOSED)
    {
      fields = createFields();
    }
    return fields;
  }

  @Override
  public Interface getInterface()
  {
    return this;
  }

  public String getLocation()
  {
    return thePackage.getLocation() + '/' + getComponentName();
  }

  public String getModifier()
  {
    Property p = getProperty(IProperties.MODIFIER);
    return p == null ? null : p.getSource();
  }

  @Override
  public IPackage getPackage()
  {
    return thePackage;
  }

  @Override
  public Kind<?> getPartOf()
  {
    return thePackage.forName(theParentName);
  }

  @Override
  public Listing<IKind<?>> getParts()
  {
    Listing<IKind<?>> parts = theParts;
    if (parts == null)
    {
      parts = initializeParts();
      theParts = parts;
    }
    return parts;
  }

  @Override
  public Property getProperty(String aName)
  {
    return theProperties == null ? null : theProperties.get(aName);
  }

  public Iterable<String> getPropertyNames()
  {
    return theProperties == null ? NO_PROPERTIES : theProperties.keySet();
  }

  @Override
  public String getSelector()
  {
    // TODO needs review.
    String label = theName;
    return Character.toLowerCase(label.charAt(0)) + label.substring(1);
  }

  @Override
  public Stereotype getStereotype()
  {
    return theDatatype.getStereotype();
  }

  public String getTarget(String aRuntime)
  {
    return theDatatype.getTarget(aRuntime, this);
  }

  public String getTitle()
  {
    String selector = getSelector();
    return Character.toTitleCase(selector.charAt(0)) + selector.substring(1);
  }

  @Override
  public String getUniqueName()
  {
    return thePackage.getUniqueName() + '/' + getComponentName();
  }

  @Override
  public Visibility getVisibility()
  {
    return Visibility.PUBLIC;
  }

  @Override
  public Object invoke(Invocation aMessage)
  {
    Argument r = aMessage.getReceiver();
    if (isInstance(r.getValue()))
    {
      return r.getValue();
    }
    throw new EvaluationException("Illegal receiver for invoke()");
  }

  @Override
  public boolean isAssignable(Type aType)
  {
    return aType instanceof Kind && super.isAssignable(aType);
  }

  @Override
  public boolean isPure()
  {
    return false;
  }

  public T newInstance(ITransaction aTx, Object aLabel)
  {
    return datatype().newInstance(aTx, this, aLabel);
  }

  public synchronized Object nextTransientLabel()
  {
    return --theLastTransientId;
  }

  /**
   *
   * @param aProperties
   * @throws IllegalStateException
   *           When the type already has properties defined.
   */
  public void setProperties(Map<String, Property> aProperties)
      throws IllegalStateException
  {
    if (theProperties != null)
    {
      throw new IllegalStateException("Properties are already defined.");
    }
    theProperties = aProperties;
  }

  @Override
  public Object target(Target aTarget, Invocation aMessage)
  {
    // TODO Auto-generated stub
    throw new UnsupportedOperationException();
  }

  @Override
  public final String toString()
  {
    return getUniqueName();
  }

  /**
   * To work correctly, this method shouldn't be called until all the kinds in
   * the package have been created.
   *
   * @return
   */
  protected Listing<IKind<?>> initializeParts()
  {
    if (theDatatype.isAggregate())
    {
      Listing<IKind<?>> children = new IListing<>(IKind.class);
      return children;
    }
    return EMPTY;
  }
  //TODO: Should be immutable
  public static Listing<IKind<?>> EMPTY = new IListing<>(IKind.class);
  
  private final synchronized IFieldset createFields()
  {
    IFieldset fields = theFields;
    // Check again, while synchronized, to handle race conditions.
    if (fields == null)
    {
      List<IKind<?>> parts = getParts();
      int len = parts.size();
      String[] names = new String[len];
      for (int i = 0; i < len; i++)
      {
        names[i] = parts.get(i).getSelector();
      }
      fields = new IFieldset(names);
      theFields = fields;
    }
    return fields;
  }
}
