package net.objectof.ext;

public interface Factory<T>
{
  /**
   * Creates an object of T identified by a logical key to the type.
   *
   * @param aName
   *          The mapping key to an implementation of T.
   * @param aArguments
   *          The constructor arguments.
   * @return a new instance of T.
   * @throws RuntimeException
   *           when an exception occurs obtaining a constructor or during
   *           construction.
   */
  public T create(String aName, Object... aArguments) throws RuntimeException;
}
