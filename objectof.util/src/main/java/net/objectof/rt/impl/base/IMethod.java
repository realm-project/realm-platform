package net.objectof.rt.impl.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.objectof.Type;
import net.objectof.aggr.Aggregate;
import net.objectof.ext.Target;
import net.objectof.ext.Vector;
import net.objectof.rt.Argument;
import net.objectof.rt.Interface;
import net.objectof.rt.Member;
import net.objectof.rt.Invocation;
import net.objectof.rt.Visibility;
import net.objectof.rt.impl.IType;

public abstract class IMethod extends IType implements Interface, Member,
    Vector<Interface>
{
  private final String theSelector;
  private final Method theMethod;

  public IMethod(String aSelector, Method aMethod)
  {
    theSelector = aSelector;
    theMethod = aMethod;
  }

  @Override
  public Interface getEvaluation()
  {
    return runtime().forClass(theMethod.getReturnType());
  }

  @Override
  public Interface getInterface()
  {
    return this;
  }

  @Override
  public Aggregate<String, ? extends Member> getMembers()
  {
    return runtime().forClass(peer()).getMembers();
  }

  @Override
  public abstract Vector<? extends Interface> getParameters();

  @Override
  public Object getProperty(String aUniqueName)
  {
    Annotation annot = null;
    @SuppressWarnings("unchecked")
    Class<Annotation> cls = (Class<Annotation>) fromName(runtime()
        .getPackageName(aUniqueName));
    if (cls != null)
    {
      annot = theMethod.getAnnotation(cls);
    }
    return annot;
  }

  @Override
  public String getSelector()
  {
    return theSelector;
  }

  @Override
  public String getUniqueName()
  {
    return runtime().forClass(peer()).getUniqueName() + '/' + theSelector;
  }

  @Override
  public Visibility getVisibility()
  {
    int mod = theMethod.getModifiers();
    if (Modifier.isPublic(mod))
    {
      return Visibility.PUBLIC;
    }
    if (Modifier.isProtected(mod))
    {
      return Visibility.PROTECTED;
    }
    return Visibility.PRIVATE;
  }

  @Override
  public Object invoke(Invocation aMessage)
  {
    Vector<? extends Argument> args = aMessage.getArguments();
    int len = args.size();
    Object[] m = new Object[len];
    for (int i = 0; i < len; i++)
    {
      m[i] = args.get(i).getValue();
    }
    return invoke(aMessage.getReceiver().getValue(), m);
  }

  public abstract Object invoke(Object aReceiver, Object[] aMessage);

  @Override
  public boolean isAssignable(Type aType)
  {
    return aType == this || aType instanceof IMethod
        && generalizes((IMethod) aType);
  }

  @Override
  public boolean isPure()
  {
    return true;
  }

  @Override
  public Class<?> peer()
  {
    return theMethod.getDeclaringClass();
  }

  @Override
  public Object target(Target aTarget, Invocation aMessage)
  {
    // TODO Auto-generated stub
    throw new UnsupportedOperationException();
  }

  protected Method getMethod()
  {
    return theMethod;
  }

  private final boolean generalizes(IMethod aType)
  {
    return isValidReceiver(aType) && isValidDomain(aType)
        && getEvaluation().isAssignable(aType.getEvaluation());
  }

  private final boolean isValidDomain(IMethod aType)
  {
    int arity = size();
    if (arity != aType.getParameters().size())
    {
      return false;
    }
    for (int i = 0; i < arity; i++)
    {
      if (!aType.getParameters().get(i).isAssignable(getParameters().get(i)))
      {
        return false;
      }
    }
    return true;
  }

  private boolean isValidReceiver(IMethod aType)
  {
    return peer().isAssignableFrom(aType.peer());
  }
}
