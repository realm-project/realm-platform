package net.objectof.connector;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.objectof.connector.parameter.Parameter;
import net.objectof.connector.parameter.Parameter.Type;
import net.objectof.model.Package;

import org.xml.sax.SAXException;


public abstract class AbstractConnector implements Connector {

    protected static final String KEY_DOMAIN = "Domain";
    protected static final String KEY_REPOSITORY = "Repository";
    protected static final String KEY_VERSION = "Version";
    private String name = "";
    private List<Parameter> parameters = new ArrayList<>();

    public AbstractConnector() {
        addParameter(Type.STRING, KEY_DOMAIN);
        addParameter(Type.STRING, KEY_REPOSITORY);
        addParameter(Type.INT, KEY_VERSION);
    }

    @Override
    public String getPackageName() {
        return value(KEY_DOMAIN) + ":" + value(KEY_VERSION) + "/" + value(KEY_REPOSITORY);
    }

    protected void addParameter(Parameter param) {
        parameters.add(param);
    }

    protected void addParameter(Parameter.Type type, String title) {
        addParameter(type.create(title));
    }

    protected String value(String key) {
        return getParameter(key).getValue();
    }

    @Override
    public Package createPackage(InputStream schema) throws ConnectorException {
        try {
            return createPackage(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(schema));
        }
        catch (SAXException | IOException | ParserConfigurationException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public List<Parameter> getParameters() {
        return new ArrayList<>(parameters);
    }

    @Override
    public Parameter getParameter(String name) {
        for (Parameter p : parameters) {
            if (name.equals(p.getTitle())) { return p; }
        }
        return null;
    }

    public void setParameter(String parameter, String value) {
        Parameter p = getParameter(parameter);
        if (p == null) { return; }
        p.setValue(value);
    }

    public void setParameters(Map<String, String> values) {
        for (String parameter : values.keySet()) {
            setParameter(parameter, values.get(parameter));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!Connector.class.isAssignableFrom(o.getClass())) { return false; }
        Connector other = (Connector) o;
        if (!other.getName().equals(getName())) { return false; }
        if (!other.getType().equals(getType())) { return false; }
        if (!other.getPackageName().equals(getPackageName())) { return false; }
        for (Parameter p : getParameters()) {
            Parameter op = other.getParameter(p.getTitle());
            if (!p.getValue().equals(op.getValue())) { return false; }
        }
        return true;
    }
}
