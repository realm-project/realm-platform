package net.objectof.impl.corc;

import net.objectof.corc.Action;

public class IAction implements Action
{
  private final String theName;
  private final Action theAction;

  /**
   * Creates a new Action from an exiting one with only the request name
   * changed.
   * 
   * @param aRequest
   * @param aRequestName
   */
  public IAction(Action aAction, String aName)
  {
    theAction = aAction;
    theName = aName;
  }

  @Override
  public Object getActor()
  {
    return theAction.getActor();
  }

  @Override
  public String getName()
  {
    return theName;
  }

  @Override
  public String getRequestId()
  {
    return theAction.getRequestId();
  }

  @Override
  public String toString()
  {
    return getRequestId() + '?' + getName() + '/' + getActor();
  }
}
