package net.objectof.connector.parameter;


import java.io.File;


class IDirectoryParameter extends AbstractParameter {

    File file;

    public IDirectoryParameter(String title) {
        super(title);
    }

    @Override
    public Type getType() {
        return Type.DIRECTORY;
    }

    @Override
    public String getValue() {
        if (file == null) { return ""; }
        return file.getAbsolutePath();
    }

    @Override
    public boolean setValue(String value) {
        File newFile = new File(value);
        // don't let users set value to existing file (not dir)
        if (newFile.exists() && !newFile.isDirectory()) { return false; }
        file = newFile;
        return true;
    }
}
