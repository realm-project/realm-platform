package net.objectof.rt.impl.base;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import net.objectof.InvalidNameException;
import net.objectof.rt.Interface;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.ILoadableContext;

public class IRuntime<T extends Interface> extends ILoadableContext<T>
implements Runtime<T>
{
  public abstract static class Loader<C extends Interface>
  {
    public abstract C load(IRuntime<C> aContext, String aClassName);
  }

  private final Loader<T> theLoader;

  public IRuntime(String aName)
  {
    super(aName);
    theLoader = initLoader(aName);
  }

  @Override
  public T forClass(Class<?> aClass)
  {
    String className = aClass.getName();
    if (getAdaptors().containsKey(className))
    {
      className = getAdaptors().getString(className);
      className = toPeerName(className);
    }
    return super.forName(className);
  }

  @Override
  public T forInterfaceName(String aUniqueName)
  {
    String peerName = toPeerName(aUniqueName);
    return super.forName(peerName);
  }

  @Override
  public String getLocation(String aUniformName)
  {
    try
    {
      return "http://" + InetAddress.getLocalHost().getHostAddress() + "/res/"
          + aUniformName;
    }
    catch (UnknownHostException e)
    {
    }
    return "res/" + aUniformName;
  }

  // TODO format checks. Might want to use a full grammar/parser.
  @Override
  public String getPackageName(String aContextName)
  {
    int registrationIndex = aContextName.indexOf(':');
    int pathIndex = aContextName.indexOf('/');
    String domain = aContextName.substring(0, registrationIndex);
    String date = aContextName.substring(registrationIndex + 1, pathIndex);
    String path = aContextName.substring(pathIndex + 1).replace('/', '.');
    StringTokenizer tokenizer = new StringTokenizer(domain, ".");
    StringBuilder pkg = new StringBuilder();
    while (tokenizer.hasMoreTokens())
    {
      pkg.insert(0, tokenizer.nextToken() + '.');
    }
    pkg.setLength(pkg.length() - 1); // remove trailing '.'
    return resolve(pkg.toString(), date, path);
  }

  @Override
  public String getUniqueName()
  {
    return IRuntimes.RUNTIMES + '/' + getName();
  }

  protected Loader<T> initLoader(String aName)
  {
    try
    {
      String cls = ResourceBundle.getBundle("rt." + aName).getString("loader");
      @SuppressWarnings("unchecked")
      Loader<T> loader = (Loader<T>) Class.forName(cls).newInstance();
      return loader;
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new Ex(this, e);
    }
  }

  @Override
  protected T load(String aClassName)
  {
    return theLoader.load(this, aClassName);
  }

  private final String authority(String aContextName)
  {
    int releaseIndex = aContextName.indexOf(':');
    String domain = aContextName.substring(0, releaseIndex);
    StringTokenizer tokenizer = new StringTokenizer(domain, ".");
    StringBuilder pkg = new StringBuilder();
    while (tokenizer.hasMoreTokens())
    {
      pkg.insert(0, tokenizer.nextToken() + '.');
    }
    pkg.setLength(pkg.length() - 1); // remove trailing '.'
    return pkg.toString();
  }

  private ResourceBundle getAdaptors()
  {
    return ResourceBundle.getBundle("adaptors");
  }

  private final String path(String aContextName)
  {
    int pathIndex = aContextName.indexOf('/');
    String path = aContextName.substring(pathIndex + 1).replace('/', '.');
    return path;
  }

  private final int release(String aUniqueName)
  {
    try
    {
      int releaseIndex = aUniqueName.indexOf(':');
      int pathIndex = aUniqueName.indexOf('/');
      return Integer.parseInt(aUniqueName
          .substring(releaseIndex + 1, pathIndex));
    }
    catch (Exception e)
    {
      throw new InvalidNameException(aUniqueName);
    }
  }

  private String resolve(String aAuthority, int aRelease, String aPathname)
  {
    return aAuthority + ".R" + aRelease + "." + aPathname;
  }

  private String resolve(String aPackage, String aRelease, String aPathname)
  {
    return aPackage + '.' + aPathname;
  }

  private final String toPeerName(String aUniqueName)
  {
    String authority = authority(aUniqueName);
    int release = release(aUniqueName);
    String pathname = path(aUniqueName);
    return resolve(authority, release, pathname);
  }
}
