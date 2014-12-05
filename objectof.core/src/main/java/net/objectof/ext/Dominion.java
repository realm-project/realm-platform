package net.objectof.ext;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import net.objectof.InvalidNameException;
import net.objectof.Type;

public class Dominion
{
  private final HashMap<String, Domain> theAuthorities = new HashMap<>();
  private final ResourceBundle theDirectory;
  public static final Dominion INSTANCE = create("Dominion");

  public static final Dominion create(String aId)
  {
    try
    {
      ResourceBundle bundle = ResourceBundle.getBundle(aId);
      String impl = bundle.getString("implementation");
      return (Dominion) Class.forName(impl).getConstructor(String.class)
          .newInstance(aId);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public Dominion(String aId)
  {
    ResourceBundle b = ResourceBundle.getBundle(aId);
    // Now get the directory from the object bundle...
    String dir = b.getString("directory");
    b = ResourceBundle.getBundle(dir);
    // Get the initial authorities...
    Enumeration<String> enumer = b.getKeys();
    while (enumer.hasMoreElements())
    {
      String ctx = enumer.nextElement();
      theAuthorities.put(ctx, null);
    }
    theDirectory = b;
  }

  public Object find(String aUniqueName)
  {
    int idx = aUniqueName.indexOf('/');
    String authName = idx > 0 ? aUniqueName.substring(0, idx) : aUniqueName;
    Domain auth = getAuthority(authName);
    if (idx < 1)
    {
      return auth;
    }
    String pathname = aUniqueName.substring(idx + 1);
    return auth.forPath(pathname);
  }

  public <T> T find(String aUniqueName, Type aType)
  {
    @SuppressWarnings("unchecked")
    T object = (T) find(aUniqueName);
    if (!aType.isInstance(object))
    {
      throw new ClassCastException("'" + aUniqueName
          + " must be an instance of '" + aType + "'.");
    }
    return object;
  }

  private final Domain getAuthority(String aAuthority)
  {
    Domain auth = theAuthorities.get(aAuthority);
    if (auth == null)
    {
      auth = loadAuthority(aAuthority);
    }
    return auth;
  }

  private final synchronized Domain loadAuthority(String aAuthority)
  {
    if (!theAuthorities.containsKey(aAuthority))
    {
      throw new InvalidNameException(aAuthority);
    }
    Domain auth = theAuthorities.get(aAuthority);
    if (auth == null)
    {
      try
      {
        String impl = theDirectory.getString(aAuthority);
        @SuppressWarnings("unchecked")
        Class<Domain> cls = (Class<Domain>) Class.forName(impl);
        auth = cls.getConstructor(String.class).newInstance(aAuthority);
        theAuthorities.put(aAuthority, auth);
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
    return auth;
  }
}
