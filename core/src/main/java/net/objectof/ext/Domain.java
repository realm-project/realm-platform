package net.objectof.ext;

import net.objectof.InvalidNameException;
import net.objectof.Named;

/**
 * A Domain can find objects from a relative name argument, i.e. descendant
 * children can be returned. Compare to a Context that is contracted to return
 * immediate children.
 * <p>
 * Domains can delegate to child Domains and/or Contexts to achieve the desired
 * result. Domains play a key role in implementing a <em>Dominion</em>; see
 * {@link Named}.
 *
 * @author jdh
 *
 */
public interface Domain extends Named
{
  /**
   * @param aName
   *          A relative name identifying the object to return.
   * @return An object for the name or null. This interface doesn't specify any
   *         constraints on the object that is returned for each invocation:
   *         i.e. there is no requirement that the same object or null or a
   *         different object will be returned for repeated calls to this even
   *         when the <b>same name</b> is specified.
   * @throws InvalidNameException
   *           When aName is not known.
   */
  public Object forPath(String aName);
}
