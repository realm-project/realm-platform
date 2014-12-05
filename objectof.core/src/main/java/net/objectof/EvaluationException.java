package net.objectof;


public class EvaluationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EvaluationException() {
        super();
    }

    public EvaluationException(Exception aException) {
        super(aException);
    }

    public EvaluationException(String aMessage) {
        super(aMessage);
    }

    public EvaluationException(String aMessage, Exception aException) {
        super(aMessage, aException);
    }
}
