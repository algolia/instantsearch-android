package com.algolia.instantsearch.model;

import java.util.Locale;

public class NumericFilter {
    /** The lower than operator (<).*/
    public static final int OPERATOR_LT = 0;
    /** The lower or equal operator (<).*/
    public static final int OPERATOR_LE = 1;
    /** The equal operator (==).*/
    public static final int OPERATOR_EQ = 2;
    /** The not equal operator (!=).*/
    public static final int OPERATOR_NE = 3;
    /** The greater or equal operator (>=).*/
    public static final int OPERATOR_GE = 4;
    /** The greater than operator (>).*/
    public static final int OPERATOR_GT = 5;

    public static final String ERROR_INVALID_OPERATOR = "operator should be one of NumericFilter.OPERATOR_XX.";

    public final int operator;
    public final String attribute;
    public final Double value;

    public NumericFilter(String attribute, double value, int operator) {
        if (operator < OPERATOR_LT || operator > OPERATOR_GT) {
            throw new IllegalStateException(ERROR_INVALID_OPERATOR);
        }
        this.operator = operator;
        this.value = value;
        this.attribute = attribute;
    }

    private static String getOperatorSymbol(int operatorCode) {
        switch (operatorCode) {
            case OPERATOR_LT:
                return "<";
            case OPERATOR_LE:
                return "<=";
            case OPERATOR_EQ:
                return "=";
            case OPERATOR_NE:
                return "!=";
            case OPERATOR_GE:
                return ">=";
            case OPERATOR_GT:
                return ">";
            default:
                throw new IllegalStateException(ERROR_INVALID_OPERATOR);
        }
    }

    @Override
    public String toString() {
        return attribute + " " + getOperatorSymbol(operator) + " " + String.format(Locale.US, "%f", value);
    }
}
