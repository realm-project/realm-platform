package net.objectof.connector.parameter;


class IPasswordParameter extends ITextParameter {

    public IPasswordParameter(String title) {
        super(title);
    }

    @Override
    public Type getType() {
        return Type.PASSWORD;
    }

}
