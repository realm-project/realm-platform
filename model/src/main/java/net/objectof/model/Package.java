package net.objectof.model;

import net.objectof.Context;
import net.objectof.facet.Annotated;
import net.objectof.facet.Faceted;
import net.objectof.model.Kind;

public interface Package extends Faceted, Context<Kind<?>>
{
  /**
   * Connects an &lt;&lt;Actor&gt;&gt; to this Package's resource manager.
   *
   * @param aActor
   *          A object examined by the resource manager to determine and track
   *          changes to resources. Each concrete implementation determines what
   *          is required to be passed. The Base implementation allows any
   *          object to be the actor.
   * @return An Tx instance or null when the Tx cannot be created, such as the
   *         connection to the resource manager isn't available.
   * @throws IllegalArgumentException
   *           The resource manager rejects the actor for some reason, such as
   *           the wrong type of object has been passed, the actor's credentials
   *           are rejected, or the actor is not authorized for this package.
   */
  public Transaction connect(Object aActor) throws IllegalArgumentException;
  
  @Override
	public Iterable<? extends Kind<?>> getParts();
  
}