package net.objectof.connector;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.objectof.model.Package;

import org.w3c.dom.Document;


public interface Connector {

    /**
     * Enumeration indicating when the Connector should initialize an empty
     * database.
     *
     */
    public enum Initialize {
        NEVER, WHEN_EMPTY;
    }

    /**
     * Returns a {@link Package} based on the {@link Parameter} values of this
     * Connector
     * 
     * @throws Exception
     */
    Package getPackage() throws ConnectorException;

    /**
     * Returns a {@link Package} based on the {@link Parameter} values of this
     * Connector, creating a new Package if it does not already exist.
     * 
     * @param schema
     *            a {@link Document} representing a Package schema
     * @throws Exception
     */
    Package createPackage(Document schema, Initialize initialize) throws ConnectorException;

    /**
     * Returns a {@link Package} based on the {@link Parameter} values of this
     * Connector, creating a new Package if it does not already exist.
     * 
     * @param schema
     *            an InputStram of a {@link Document} representing a Package
     *            schema
     * @param initialize
     *            Indicates if this database (not repository) should be
     *            initialized if it is empty as determined by
     *            {@link Connector#isContainerEmpty()}
     * @throws Exception
     */
    Package createPackage(InputStream schema, Initialize initialize) throws ConnectorException;

    /**
     * Returns the qualified name of this {@link Package}
     */
    String getPackageName();

    /**
     * Checks to see if the container (eg database, not repository) contains
     * <i>any</i> tables, views, etc... (not just ones used by objectof).
     * 
     * @return true if the database is empty, false otherwise.
     * @throws ConnectorException
     */
    boolean isContainerEmpty() throws ConnectorException;

    /**
     * When creating a new container (eg database, not repository), this can be
     * used to populate it with the required tables and views.
     */
    void initializeContainer() throws ConnectorException;

    /**
     * Attempts to connect to the configured database and read a list of all
     * repository names.
     * 
     * @return A list of all repository names in the configured database, if
     *         provided.
     * @throws ConnectorException
     */
    List<String> getPackageNames() throws ConnectorException;

    /**
     * Returns a {@link List} of all {@link Parameter}s for this Connector
     */
    List<Parameter> getParameters();

    /**
     * Returns the {@link Parameter} with the given name. If no such Parameter
     * exists, it returns null.
     * 
     * @param name
     *            The name of the Parameter to retrieve
     */
    Parameter getParameter(String name);

    /**
     * Set the value for a {@link Parameter} with the given name. If no
     * parameter with the given name exists, no action is performed.
     * 
     * @param parameter
     *            the name of the {@link Parameter} to set
     * @param value
     *            the value for the Parameter
     */
    void setParameter(String parameter, String value);

    /**
     * A convenience method for {@link Connector#setParameter(String, String)}
     * which accepts a map from parameter name to parameter value
     * 
     * @param values
     *            a map of parameter name->value
     */
    void setParameters(Map<String, String> values);

    /**
     * Gets the name of this Connector. The name should default to the type, but
     * can be anything.
     */
    String getName();

    /**
     * Sets the name of this Connector
     */
    void setName(String name);

    /**
     * Returns a String identifying the type of backend this Connector uses.
     * This should be unique across all Connector implementations.
     */
    String getType();

    /**
     * Checks to see if the configured database contains a repository with the
     * given name
     * 
     * @param name
     *            the name of the repository to check for
     * @return true if the repository exists, false otherwise
     * @throws ConnectorException
     */
    boolean hasPackage(String name) throws ConnectorException;
}
