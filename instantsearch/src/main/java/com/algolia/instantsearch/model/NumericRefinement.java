package com.algolia.instantsearch.model;

import android.support.annotation.NonNull;

import java.util.Locale;

@SuppressWarnings("WeakerAccess")
public class NumericRefinement {
    /** The lower than operator {@literal (<)}. */
    public static final int OPERATOR_LT = 0;
    /** The lower or equal operator {@literal (<=)}. */
    public static final int OPERATOR_LE = 1;
    /** The equal operator (==). */
    public static final int OPERATOR_EQ = 2;
    /** The not equal operator (!=). */
    public static final int OPERATOR_NE = 3;
    /** The greater or equal operator {@literal (>=)}. */
    public static final int OPERATOR_GE = 4;
    /** The greater than operator {@literal (>)}. */
    public static final int OPERATOR_GT = 5;

    private static final String ERROR_INVALID_CODE = "operator (%d) should be one of NumericRefinement.OPERATOR_XX.";
    private static final String ERROR_INVALID_NAME = "operator (%s) should be one of 'lt', 'le', 'eq', 'ne', 'ge', 'gt'.";

    public final int operator;
    public final String attribute;
    public final Double value;

    public NumericRefinement(@NonNull String attribute, int operator, double value) {
        checkOperatorIsValid(operator);
        this.operator = operator;
        this.value = value;
        this.attribute = attribute;
    }

    public static int getOperatorCode(String operatorName) {
        switch (operatorName) {
            case "lt":
                return OPERATOR_LT;
            case "le":
                return OPERATOR_LE;
            case "eq":
                return OPERATOR_EQ;
            case "ne":
                return OPERATOR_NE;
            case "ge":
                return OPERATOR_GE;
            case "gt":
                return OPERATOR_GT;
            default:
                throw new IllegalStateException(String.format(ERROR_INVALID_NAME, operatorName));
        }
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
                throw new IllegalStateException(String.format(Locale.US, ERROR_INVALID_CODE, operatorCode));
        }
    }

    public static void checkOperatorIsValid(int operatorCode) {
        switch (operatorCode) {
            case OPERATOR_LT:
            case OPERATOR_LE:
            case OPERATOR_EQ:
            case OPERATOR_NE:
            case OPERATOR_GE:
            case OPERATOR_GT:
                return;
            default:
                throw new IllegalStateException(String.format(Locale.US, ERROR_INVALID_CODE, operatorCode));
        }
    }

    @Override
    public String toString() {
        return attribute + getOperatorSymbol(operator) + String.format(Locale.US, "%f", value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NumericRefinement that = (NumericRefinement) o;
        return operator == that.operator && attribute.equals(that.attribute) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = operator;
        result = 31 * result + attribute.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
