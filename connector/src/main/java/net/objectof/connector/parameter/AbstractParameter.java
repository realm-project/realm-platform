package net.objectof.connector.parameter;


abstract class AbstractParameter implements Parameter {

    private String title;

    public AbstractParameter(String title) {
        this.title = title;
    }

    @Override
    public final String getTitle() {
        return title;
    }

}
