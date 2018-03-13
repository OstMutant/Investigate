package org.ost.investigate.test.database.jpa.dictionary;

public enum OperatorType {
    MOBILE(OperatorType.Values.MOBILE);

    private final String type;

    OperatorType(String s) {
        type = s;
    }

    public String toString() {
        return this.type;
    }

    public static class Values {
        public static final String MOBILE = "MOBILE";
    }
}
