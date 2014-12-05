package net.objectof.rt.impl.base;

import java.io.PrintWriter;

public class Util
{
  public static final void encodeXMLString(String aString, PrintWriter aWriter)
  {
    int len = aString.length();
    for (int i = 0; i < len; i++)
    {
      char ch = aString.charAt(i);
      switch (ch)
      {
      case '&':
        aWriter.print("&amp;");
        break;
      case '>':
        aWriter.print("&gt;");
        break;
      case '<':
        aWriter.print("&lt;");
        break;
      case '\'':
        aWriter.print("&apos;");
        break;
      case '"':
        aWriter.print("&quot;");
        break;
      default:
        if (ch < ' ' || ch > '~')
        {
          aWriter.print("&#");
          aWriter.print((int) ch);
          aWriter.print(';');
        }
        else
        {
          aWriter.print(ch);
        }
      }
    }
  }

  public static final String initCap(String aString)
  {
    if (aString == null || aString.length() == 0) return "";
    StringBuilder b = new StringBuilder();
    b.append(Character.toUpperCase(aString.charAt(0)));
    if (aString.length() > 1)
    {
      b.append(aString.subSequence(1, aString.length()));
    }
    return b.toString();
  }

  public static String selector(String aPath)
  {
    int index = aPath.lastIndexOf('.');
    return aPath.substring(index + 1);
  }
}
