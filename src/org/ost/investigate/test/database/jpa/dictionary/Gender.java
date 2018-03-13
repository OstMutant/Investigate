package org.ost.investigate.test.database.jpa.dictionary;

public enum Gender {
    MALE(Values.MALE), FEMALE(Values.FEMALE);

    private final String gender;

    Gender(String s) {
        gender = s;
    }

    public String toString() {
        return this.gender;
    }

    public static class Values {
        public static final String MALE = "MALE";
        public static final String FEMALE = "FEMALE";
    }
}
