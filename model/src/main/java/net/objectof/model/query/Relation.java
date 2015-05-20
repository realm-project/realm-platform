package net.objectof.model.query;


public enum Relation {
    EQUAL {

        @Override
        public String toString() {
            return "=";
        }
    },
    UNEQUAL {

        @Override
        public String toString() {
            return "!=";
        }
    },
    CONTAINS {

        @Override
        public String toString() {
            return "contains";
        }
    },
    GT {

        @Override
        public String toString() {
            return ">";
        }
    },
    LT {

        @Override
        public String toString() {
            return "<";
        }
    },
    GTE {

        @Override
        public String toString() {
            return ">=";
        }
    },
    LTE {

        @Override
        public String toString() {
            return "<=";
        }
    },
    MATCHES {

        @Override
        public String toString() {
            return "=~";
        }
    },
    NOTMATCHES {

        @Override
        public String toString() {
            return "!~";
        }
    };
}
