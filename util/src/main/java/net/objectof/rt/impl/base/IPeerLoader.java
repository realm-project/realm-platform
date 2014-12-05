package net.objectof.rt.impl.base;

import net.objectof.rt.impl.IType;

public class IPeerLoader extends IRuntime.Loader<IPeer<?>>
{
  @Override
  public IPeer<?> load(IRuntime<IPeer<?>> aRuntime, String aClassName)
  {
    Class<?> cl = IType.fromName(aClassName);
    return cl == null ? null : new IPeer<>(cl);
  }
}