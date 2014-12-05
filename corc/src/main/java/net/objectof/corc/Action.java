package net.objectof.corc;

/**
 * An Action encapsulates a Request action.
 * 
 * @author jdh
 * 
 */
public interface Action
{
  /**
   * @return The consumer requesting the Action. Sub-interfaces may specify a
   *         more specific return type. It is recommended that the
   *         Object.toString() method return a distinct handle to the actor.
   */
  public Object getActor();

  /**
   * @return The name of this action. The name is used to route actions and
   *         responses.
   */
  public String getName();

  /**
   * The request id uniquely identifies a request within a system. It is used as
   * a correlation id.
   * <p>
   * In multi-server systems, it is recommended that the request id be a
   * compound identifier consisting of a location id to identify the origin
   * server/service for routing responses back to the origin and to
   * simplify/ensure numbers are unique.
   * 
   * @return a unique identifier of the originating Request.
   */
  public String getRequestId();
}
