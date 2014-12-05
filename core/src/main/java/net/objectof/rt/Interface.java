package net.objectof.rt;

import net.objectof.Type;
import net.objectof.aggr.Aggregate;
import net.objectof.ext.Vector;

public interface Interface extends Type
{
  public Interface getEvaluation();

  public Aggregate<String, ? extends Member> getMembers();

  public Vector<? extends Interface> getParameters();

  /**
   * Determines the assignability of an instance of the argument type to an
   * instance of this type. This method is similar in purpose to
   * Class.isAssignableFrom().
   * <p>
   * The strictest form of assignability is an identity match (this == aType).
   * All implementations must answer true for an identity match.
   * <p>
   * Note that assignability might be determined through the uniform name or a
   * portion thereof. For example, given types with names of
   * "ns.acme.com/2015/ctx1/app.ui.Widget" and
   * "ns.rrinc.net/2014/afw/app.ui.Widget", either may answer true or false
   * based on the common path suffix. Also note there is no requirement that
   * there be commonality in the two names. One system may simply map names,
   * paths, etc. into its type system.
   *
   * @return true when aType is assignable to this.
   */
  public boolean isAssignable(Type aType);

  public Runtime<?> runtime();
}
