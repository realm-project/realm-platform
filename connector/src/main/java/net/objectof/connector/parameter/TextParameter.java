package net.objectof.connector.parameter;


class TextParameter extends AbstractParameter {

    String value = "";

    public TextParameter(String title) {
        super(title);
    }

    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean setValue(String value) {
        this.value = value;
        return true;
    }


}
