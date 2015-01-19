package net.objectof.connector.parameter;


public interface Parameter {

    // TODO: Feels like re-implementing stereotype, but not sure how to handle
    // file types
    public enum Type {
        INT {

            @Override
            public Parameter create(String title) {
                return new IIntegerParameter(title);
            }
        },
        REAL {

            @Override
            public Parameter create(String title) {
                return new IFloatParameter(title);
            }
        },
        STRING {

            @Override
            public Parameter create(String title) {
                return new ITextParameter(title);
            }
        },
        FILE {

            @Override
            public Parameter create(String title) {
                return new IFileParameter(title);
            }
        },
        PASSWORD {

            @Override
            public Parameter create(String title) {
                return new IPasswordParameter(title);
            }
        };

        public Parameter create(String title) {
            return new ITextParameter(title);
        }

    }

    Type getType();

    String getValue();

    boolean setValue(String value);

    String getTitle();



}
