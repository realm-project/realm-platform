package net.objectof.repo.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.objectof.aggr.Listing;
import net.objectof.aggr.impl.IListing;
import net.objectof.model.Kind;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IKind;

public class IRepoType<T> extends IKind<T>
{
  private final int theIdx;

  public IRepoType(IRepo aRepo, String aPath, Stereotype aStereotype, int aIdx)
  {
    super(aRepo, aPath, aStereotype);
    theIdx = aIdx;
  }

  @Override
  public IRepo getPackage()
  {
    return (IRepo) super.getPackage();
  }

  public int idx()
  {
    return theIdx;
  }

  @Override
  protected Listing<IKind<?>> initializeParts()
  {
    if (datatype().isAggregate())
    {
      Listing<IKind<?>> children = new IListing<>(IKind.class);
      for (Kind<?> type : getPackage())
      {
        if (type.getPartOf() == this)
        {
          children.add((IKind<?>) type);
        }
      }
      return children;
    }
    return IKind.EMPTY;
  }
  
}
