package net.objectof.connector.parameter;


import java.io.File;


class FileParameter extends AbstractParameter {

    File file;

    public FileParameter(String title) {
        super(title);
    }

    @Override
    public Type getType() {
        return Type.FILE;
    }

    @Override
    public String getValue() {
        if (file == null) { return ""; }
        return file.getAbsolutePath();
    }

    @Override
    public boolean setValue(String value) {
        file = new File(value);
        return true;
    }

}
