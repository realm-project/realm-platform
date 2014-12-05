package net.objectof.rt;

import net.objectof.Named;
import net.objectof.ext.Target;

public interface Member extends Named
{
  public Interface getInterface();

  /**
   * @param aUniqueName
   *          The unique name of the property.
   * @return The property matching the name or null when not defined.
   */
  public Object getProperty(String aUniqueName);

  public String getSelector();

  public Visibility getVisibility();

  public Object invoke(Invocation aMessage);

  public boolean isPure();

  public Object target(Target aTarget, Invocation aMessage);
}
