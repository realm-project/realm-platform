package net.objectof.corc;

/**
 * A Handler provides the interface for execution of a Request Action. Multiple
 * Handlers can be chained, aggregated, and composed to create a composite
 * orchestrated service pipeline.
 * <p>
 * A Handler differs from a Service in that there is no return value from its
 * execute() interface therefore enabling non-blocking asynchronous execution of
 * actions. Whether or not the Handler executes asynchronously or not is
 * determined by implementations based on specific (functional) requirements and
 * system non-functional requirements and topology. For example a given Handler
 * may be required to execute synchronously when the action must be completed
 * prior to performing the next step due to functional (business) constraints.
 * <p>
 * In general, a Handler instance handles multiple actions in its lifetime
 * therefore implementations must be thread-safe and the design contract is for
 * per-action state to be put on the Object argument, possibly by wrapping the
 * action object within a user-defined envelope/wrapper. Extensions to the
 * Action interface are supported for extension of overall action state for a
 * given service.
 * <p>
 * The expected pattern for implementation is a concrete Handler will be wired
 * through a dependency injection mechanism.
 * 
 * @param <T>
 *          The Type of object processed through the Handler.
 * 
 * @author jdh
 * 
 */
public interface Handler<T>
{
  /**
   * Executes an Action on a request chain. There is no return value, the
   * Handler forwards the result action to the next Handler on the request
   * chain. This design promotes Handlers being able to determine whether the
   * execution path is synchronous or asynchronous.
   * 
   * @param aAction
   *          The Action to perform.
   * @param aObject
   *          The object being processed by this Handler.
   */
  public void execute(Action aAction, T aObject);

  Interface<T> getType();
}
