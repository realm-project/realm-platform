package net.objectof.connector.parameter;


public interface Parameter {

    // TODO: Feels like re-implementing stereotype, but not sure how to handle
    // file types
    public enum Type {
        INT {

            @Override
            public Parameter create(String title) {
                return new IntegerParameter(title);
            }
        },
        REAL {

            @Override
            public Parameter create(String title) {
                return new FloatParameter(title);
            }
        },
        STRING {

            @Override
            public Parameter create(String title) {
                return new TextParameter(title);
            }
        },
        FILE {

            @Override
            public Parameter create(String title) {
                return new FileParameter(title);
            }
        },
        PASSWORD {

            @Override
            public Parameter create(String title) {
                return new PasswordParameter(title);
            }
        };

        public Parameter create(String title) {
            return new TextParameter(title);
        }

    }

    Type getType();

    String getValue();

    boolean setValue(String value);

    String getTitle();



}
