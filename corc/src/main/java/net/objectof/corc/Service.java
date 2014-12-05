package net.objectof.corc;

/**
 * A Service provides the interface for execution of an Action. A Service
 * differs from a Handler in that its execution provides a Response value from
 * its service() interface.
 * <p>
 * In general, a Service instance handles multiple requests in its lifetime
 * therefore implementations must be thread-safe and the design contract is for
 * per-request state to be put on the Object argument, possibly by wrapping the
 * object within a user-defined envelope/wrapper. Extensions to the Action
 * interface are supported for extension of overall action state for a given
 * service. A concrete example of this is the Request interface.
 * <p>
 * The expected pattern for implementation is a concrete Service will be
 * configured and wired through a dependency injection mechanism.
 * 
 * @param <T>
 *          The type of request being serviced.
 * @param <R>
 *          The type of response returned.
 * 
 * @author jdh
 */
public interface Service<T, R>
{
  /**
   * Performs a service.
   * 
   * @param aRequest
   *          An Action encapsulating meta data regarding the request.
   * @param aObject
   *          An object encapsulating a request.
   * @return The result of execution.
   */
  public R service(Action aRequest, T aObject);

  Interface<T> getType();
}
