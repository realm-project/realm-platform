package net.objectof.corc.dev;

/**
 * A Response provides a common interface for encapsulating data regarding a
 * response object. It is provided as utility - there is no inherent requirement
 * that all responses be modeled as such.
 * <p>
 * Responses have zero or more child Responses as indicated by size() and
 * accessible through its Iterator.
 * 
 * @author jdh
 * 
 */
public interface Response extends Iterable<Response>
{
  /**
   * Categorizes responses with respect to success of execution.
   * 
   * @author jdh
   */
  public enum Category
  {
    /**
     * The action was not be performed due a condition or exception.
     */
    Error,
    /**
     * The action did not encounter an error, however there was a condition that
     * should be noted. The action may or may not have been performed.
     */
    Warning,
    /**
     * The action completed without errors or warnings.
     */
    Success;
  }

  /**
   * @return The category of the response.
   */
  public Category getCategory();

  /**
   * @return The message associated to the id.
   */
  public String getMessage();

  /**
   * @return The identifier of the response message.
   */
  public String getMessageId();

  /**
   * @return The response object.
   */
  public Object getObject();

  /**
   * @return The number of child Responses.
   */
  public int size();
}
