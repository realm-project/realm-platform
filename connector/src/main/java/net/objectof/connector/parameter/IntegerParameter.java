package net.objectof.connector.parameter;


class IntegerParameter extends AbstractParameter {

    Integer value;

    public IntegerParameter(String title) {
        super(title);
    }

    @Override
    public Type getType() {
        return Type.INT;
    }

    @Override
    public String getValue() {
        if (value == null) { return ""; }
        return value.toString();
    }

    @Override
    public boolean setValue(String value) {
        try {
            this.value = Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }


}
