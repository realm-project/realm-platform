package net.objectof.connector.parameter;


class PasswordParameter extends TextParameter {

    public PasswordParameter(String title) {
        super(title);
    }

    @Override
    public Type getType() {
        return Type.PASSWORD;
    }

}
