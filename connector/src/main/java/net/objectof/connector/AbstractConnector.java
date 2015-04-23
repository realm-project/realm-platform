package net.objectof.connector;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.objectof.connector.Parameter.Hint;


public abstract class AbstractConnector implements Connector {

    private String name = "";
    private List<Parameter> parameters = new ArrayList<>();

    protected void addParameter(Parameter param) {
        parameters.add(param);
    }

    protected void addParameter(String title) {
        addParameter(new Parameter(title));
    }

    protected void addParameter(String title, Hint hint) {
        addParameter(new Parameter(title, hint));
    }

    protected String value(String key) {
        return getParameter(key).getValue();
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
}
