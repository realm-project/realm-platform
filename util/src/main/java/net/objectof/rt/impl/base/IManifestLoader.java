package net.objectof.rt.impl.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.StringTokenizer;

import net.objectof.aggr.Mapping;
import net.objectof.rt.Member;

public abstract class IManifestLoader extends IBaseLoader
{
  protected void forEntry(ILoadableInterface aType, String entry,
      Map<String, Member> aMap)
  {
    StringTokenizer t = new StringTokenizer(entry, " ");
    if (t.hasMoreElements())
    {
      String selector = t.nextToken();
      if (!selector.startsWith("."))
      {
        Method method = t.hasMoreElements() ? fromManifestEntry(aType,
            selector, t) : null;
        Member member = defineMember(aType, selector, method);
        aMap.put(selector, member);
      }
    }
  }

  @Override
  protected void loadMembers(ILoadableInterface aType,
      Mapping<String, Member> aMap)
  {
    Class<?> peer = aType.peer();
    File file = new File("manifests/" + peer.getName() + ".MethodTypes");
    if (!file.exists())
    {
      return;
    }
    try
    {
      BufferedReader r = new BufferedReader(new FileReader(file));
      for (String entry = r.readLine(); entry != null; entry = r.readLine())
      {
        forEntry(aType, entry, aMap);
      }
      r.close();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  private Method fromManifestEntry(ILoadableInterface aType, String aSelector,
      StringTokenizer aTokens)
  {
    String declaringName = aTokens.nextToken();
    Class<?> declaring = IPeer.fromName(declaringName);
    String methodName = aTokens.nextToken();
    int length = aTokens.countTokens();
    Class<?>[] args = new Class<?>[length];
    for (int i = 0; i < length; i++)
    {
      args[i] = IPeer.fromName(aTokens.nextToken());
    }
    try
    {
      return declaring.getMethod(methodName, args);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
