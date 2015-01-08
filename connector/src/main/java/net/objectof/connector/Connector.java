package net.objectof.connector;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.objectof.connector.parameter.Parameter;
import net.objectof.model.Package;

import org.w3c.dom.Document;


public interface Connector {

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
    Package createPackage(Document schema) throws ConnectorException;

    /**
     * Returns a {@link Package} based on the {@link Parameter} values of this
     * Connector, creating a new Package if it does not already exist.
     * 
     * @param schema
     *            an InputStram of a {@link Document} representing a Package
     *            schema
     * @throws Exception
     */
    Package createPackage(InputStream schema) throws ConnectorException;

    /**
     * Returns the qualified name of this {@link Package}
     */
    String getPackageName();

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
}
