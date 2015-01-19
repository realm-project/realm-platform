package net.objectof.connector.parameter;


class IFloatParameter extends AbstractParameter {

    Double value;

    public IFloatParameter(String title) {
        super(title);
    }

    @Override
    public Type getType() {
        return Type.REAL;
    }

    @Override
    public String getValue() {
        if (value == null) { return ""; }
        return value.toString();
    }

    @Override
    public boolean setValue(String value) {
        try {
            this.value = Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

}
