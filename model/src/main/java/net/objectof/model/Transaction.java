package net.objectof.model;

import java.io.Reader;

import net.objectof.Selector;
import net.objectof.model.query.Query;

/**
 * A Transaction provides the interface to resource management for a single
 * package. With a Transaction, the consumer can obtain new or existing objects
 * and post changes to those objects as a unit of work. How the changes are
 * posted is based on a concrete implementation.
 * <p>
 * Transactions may be implemented with an intention to be short-lived or
 * long-lived.
 * <ul>
 * <li>Short Lived: All Modifications are performed 'in batch'. As an example, a
 * web service that is posting changes to one or more objects will obtain a
 * Transaction and interact with it within a single timespan. Short-lived
 * Transactions may use 'pessimistic locking' and hold the resource through its
 * lifetime.
 * <li>Long Lived: Modifications are performed as a series of postings. As an
 * example, a web service that creates a Transaction for potentially multiple
 * web postings that lives until the client issues a "save" request initiating a
 * post() on the Transaction. Long-lived transactions should be 'optimistic' and
 * not lock resources until the Transaction.post() is initiated. In this case,
 * the Transaction should ensure the version being posted has not been changed
 * by another user/process/thread.
 *
 * @author jdh
 */
public interface Transaction
{
  /**
   * State transitions
   * <ul>
   * <li>OPEN -> CLOSED
   * <li>OPEN -> POSTING -> POSTED
   * <li>OPEN -> POSTING -> FAILED
   * <li>POSTED -> CLOSED
   * <li>FAILED -> CLOSED
   * </ul>
   *
   * @author jdh
   *
   */
  public enum Status
  {
    OPEN, POSTING, POSTED, FAILED, CLOSED
  }

  /**
   * Closes the Transaction. Resources may be freed. Upon normal completion, the
   * state will be Closed. Close cannot be called while posting. Once closed,
   * the Transaction must not support any activity.
   */
  public void close();

  /**
   * Creates a new instance of the kind specified by the kind's path within the
   * Transaction's package.
   *
   * @param aKind
   * @return a new instance.
   */
  public <T> T create(String aKind);

  /**
   * Retrieves all elements of a {@link Kind} specified by this Class's
   * {@link Selector}
   *
   * @param aActor
   *          A object examined by the resource manager to determine and track
   *          changes to resources. Each concrete implementation determines what
   *          is required to be passed. The Base implementation allows any
   *          object to be the actor.
   * @param kind
   *          String representation of the {@link Kind} of elements to examine
   * @return An {@link Iterable} {@link QueryResult} containing the elements.
   * @throws UnsupportedOperationException
   */
  public <T> Iterable<T> enumerate(String kind)
      throws UnsupportedOperationException;

  /**
   * @return The Package on which this transaction will operate on.
   */
  public Package getPackage();

  /**
   * @return The Transaction.Status.
   */
  public Status getStatus();

  /**
   * Posts changes managed by this Transaction (optional operation). A
   * Transaction supports 1 post. Changes required after a post must be
   * performed through a new Transaction. The Transaction.Status is POSTING
   * while this method is executing. Upon completion, the status will be POSTED
   * or FAILED.
   *
   * @throws UnsupportedOperationException
   *           Posting is not supported by this Transaction.
   * @throws IllegalStateException
   *           The Status is not valid for posting.
   * @throws RuntimeException
   *           An undefined exception occurred posting.
   */
  public void post() throws IllegalStateException;

  /**
   * Retrieves all elements of a {@link Kind} specified by this Class's
   * {@link Selector} matching the given {@link Query}
   *
   * @param kind
   *          String representation of the {@link Kind} of elements to examine
   * @param query
   *          the specific query to use to find elements in this Package.
   * @return An {@link Iterable} {@link QueryResult} containing the elements, or
   *         an empty QueryResult if an empty Query is submitted.
   * @throws UnsupportedOperationException
   */
  public <T> Iterable<T> query(String kind, Query query)
      throws IllegalArgumentException, UnsupportedOperationException;

  
  /**
   * Convenience method for {@link Transaction#query(String, Query)}
   * 
   * This method will build a Query object from the given String.
   *
   * @param kind
   *          String representation of the {@link Kind} of elements to examine
   * @param query
   *          the specific query string to use to find elements in this Package.
   * @return An {@link Iterable} {@link QueryResult} containing the elements, or
   *         an empty QueryResult if an empty Query is submitted.
   * @throws UnsupportedOperationException, IllegalArgumentException
   */
  public <T> Iterable<T> query(String kind, String query)
      throws IllegalArgumentException, UnsupportedOperationException;
  
  /**
   * Receives an object on the argument stream to be added to the transaction.
   * If the object contains parts, they are added as well.
   *
   * @param aMediaType
   *          The format of the input stream.
   * @param aReader
   *          The input stream.
   * @return The (root) object.
   */
  public <T> T receive(String aMediaType, Reader aReader);

  /**
   * Retrieves an existing object or establishes a reference to it, such as a
   * Resource reference.
   *
   * @param aKind
   *          The object's type.
   * @param aLabel
   *          The object's label, i.e. the unique value, within the kind, of the
   *          object.
   * @return the object
   */
  public <T> T retrieve(String aKind, Object aLabel);
  
  
  /**
   * Retrieves an existing object or establishes a reference to it, such as a
   * Resource reference.
   *
   * @param aId
   *          the Id of the object.
   * @return the object
   */
  public <T> T retrieve(Id<?> aId);
  
  
  public void setLimit(int aLimit);
}