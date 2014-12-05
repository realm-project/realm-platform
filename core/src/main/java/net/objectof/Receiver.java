package net.objectof;


/**
 * A Receiver can evaluate messages in a generic way. Subinterfaces provide the
 * means to interrogate a receiver's interface.
 * 
 * @author john
 *
 */
@Selector
public interface Receiver {

    /**
     * 
     * @param aSelector
     *            The selector to route aMessage to the appropriate method or
     *            function.
     * @param aMessage
     *            The message. Subinterfaces may place contracts on the message
     *            content.
     * @return The result of evaluation.
     * @throws InvalidNameException
     *             when aSelector isn't defined for this receiver.
     * @throws EvaluationException
     *             when aMessage cannot be evaluated.
     */
    @Selector("perform:with:")
    public Object perform(String aSelector, Object... aMessage) throws InvalidNameException, EvaluationException;
}
