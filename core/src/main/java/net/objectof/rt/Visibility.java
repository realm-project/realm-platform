package net.objectof.rt;

public enum Visibility
{
  PRIVATE, PROTECTED, PUBLIC;
  public static Visibility visibilityFor(char aChar)
  {
    switch (aChar)
    {
    case '+':
      return PUBLIC;
    case '#':
      return PROTECTED;
    case '-':
      return PRIVATE;
    default:
      return null;
    }
  }
}
