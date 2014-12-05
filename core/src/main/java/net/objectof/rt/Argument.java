package net.objectof.rt;

import net.objectof.EvaluationException;
import net.objectof.InvalidNameException;
import net.objectof.ext.Target;

/**
 * An Argument is a canonical object that supports conversion to zero or more
 * targets.
 *
 * @author jdh
 *
 */
public interface Argument
{
  /**
   * @return The value of the model when isPure() answers true. Note that null
   *         is a valid pure value.
   */
  // @Override
  // public Object value();
  /**
   * @return The Interface of the model.
   */
  public Interface getInterface();

  /**
   * Converts ("transcodes") the model into a serialized target form. Invocation
   * of this method can:
   * <ul>
   * <li>define a target element through Target.append().
   * <li>emit an expression or target element reference through the return
   * value.
   * <li>both define and emit the reference to the target element when the
   * element being defined can be referenced.
   * </ul>
   *
   * @param aTarget
   *          The target to convert to.
   * @return The expression or reference aspect of transcoding.
   * @throws InvalidNameException
   *           The target doesn't define a required method.
   * @throws EvaluationException
   *           An exception occurred during conversion.
   * @throws IllegalArgumentException
   *           The target is unknown or otherwise unsupported by this model.
   * @throws RuntimeException
   *           Another exception occurred.
   */
  public Object target(Target aTarget);

  /**
   * @return The Argument's value.
   */
  public Object getValue();
}
