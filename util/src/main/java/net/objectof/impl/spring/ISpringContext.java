package net.objectof.impl.spring;

import net.objectof.Context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Provides an object that is both a Spring BeanFactory and an objectof Context.
 * 
 * Within the definition of objectof's Context contracts:
 * <ul>
 * <li>This can be both a factory and a domain.
 * <li>forName will throw an IllegalArgumentException when a member name isn't
 * recognized.
 * <li>This is a reified context. The context holds a runtime reference to the
 * generic class and a ClassCastException will be thrown if requesting a Bean
 * that is not of this Instance's reified type.
 * </ul>
 * 
 * @author jdh
 * 
 * @param <T>
 */
public class ISpringContext<T> implements Context<T>, BeanFactory
{
  private final BeanFactory theFactory;
  private final Class<? extends T> thePeer;

  public ISpringContext(Class<? extends T> aComponentType, BeanFactory aFactory)
  {
    thePeer = aComponentType;
    theFactory = aFactory;
  }

  public ISpringContext(Class<? extends T> aComponentType, String aFileName)
  {
    this(aComponentType, new ClassPathXmlApplicationContext(aFileName));
  }

  @Override
  public boolean containsBean(String aName)
  {
    return theFactory.containsBean(aName);
  }

  /**
   * Uses the reified type via getComponentType() for calling &lt;X&gt; X
   * getBean(String aName, Class<X> aClass)
   * 
   * @throws IllegalArgumentException
   *           When aName is not defined or defines an object that is not a
   *           subclass of this Context's (reified) type. Null <em>will not</em>
   *           be returned for unknown names and the object will [probably?] be
   *           a subclass of getComponentPeer().
   * @throws BeansException
   *           The BeanFactory cannot instantiate the object.
   */
  @Override
  public T forName(String aName) throws IllegalArgumentException,
      BeansException
  {
    try
    {
      return theFactory.getBean(aName, getComponentPeer());
    }
    catch (BeanNotOfRequiredTypeException e)
    {
      throw new IllegalArgumentException(aName + " must be a "
          + getComponentPeer().getName());
    }
    catch (NoSuchBeanDefinitionException e)
    {
      throw new IllegalArgumentException(aName);
    }
  }

  @Override
  public String[] getAliases(String aName)
  {
    return theFactory.getAliases(aName);
  }

  @Override
  public <X> X getBean(Class<X> aClass) throws BeansException
  {
    return theFactory.getBean(aClass);
  }

  @Override
  public Object getBean(String aName) throws BeansException
  {
    return forName(aName);
  }

  @Override
  public <X> X getBean(String aName, Class<X> aClass) throws BeansException
  {
    return theFactory.getBean(aName, aClass);
  }

  @Override
  public Object getBean(String aName, Object... aArguments)
      throws BeansException
  {
    return theFactory.getBean(aName, aArguments);
  }

  public final Class<? extends T> getComponentPeer()
  {
    return thePeer;
  }

  @Override
  public Class<?> getType(String aName) throws NoSuchBeanDefinitionException
  {
    return theFactory.getType(aName);
  }

  @Override
  public String getUniqueName()
  {
    return "objectof.net:1401/context/spring/" + theFactory.toString();
  }

  @Override
  public boolean isPrototype(String aName) throws NoSuchBeanDefinitionException
  {
    return theFactory.isPrototype(aName);
  }

  @Override
  public boolean isSingleton(String aName) throws NoSuchBeanDefinitionException
  {
    return theFactory.isSingleton(aName);
  }

  @Override
  public boolean isTypeMatch(String aName, Class<?> aImpl)
      throws NoSuchBeanDefinitionException
  {
    return theFactory.isTypeMatch(aName, aImpl);
  }
}
