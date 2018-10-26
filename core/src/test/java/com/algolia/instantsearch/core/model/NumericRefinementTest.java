package com.algolia.instantsearch.core.model;

import junit.framework.Assert;

import org.junit.Test;

import static com.algolia.instantsearch.core.R.attr.operator;
import static junit.framework.Assert.fail;

public class NumericRefinementTest {
    @Test
    public void getOperatorCodeValid() {
        Assert.assertEquals(NumericRefinement.OPERATOR_LT, NumericRefinement.getOperatorCode("lt"));
        Assert.assertEquals(NumericRefinement.OPERATOR_LE, NumericRefinement.getOperatorCode("le"));
        Assert.assertEquals(NumericRefinement.OPERATOR_EQ, NumericRefinement.getOperatorCode("eq"));
        Assert.assertEquals(NumericRefinement.OPERATOR_NE, NumericRefinement.getOperatorCode("ne"));
        Assert.assertEquals(NumericRefinement.OPERATOR_GE, NumericRefinement.getOperatorCode("ge"));
        Assert.assertEquals(NumericRefinement.OPERATOR_GT, NumericRefinement.getOperatorCode("gt"));
    }

    @Test(expected = IllegalStateException.class)
    public void getOperatorCodeInvalid() {
        NumericRefinement.getOperatorCode("foo");
    }

    @Test
    public void getOperatorSymbolValid() {
        Assert.assertEquals("<", NumericRefinement.getOperatorSymbol(NumericRefinement.OPERATOR_LT));
        Assert.assertEquals("<=", NumericRefinement.getOperatorSymbol(NumericRefinement.OPERATOR_LE));
        Assert.assertEquals("=", NumericRefinement.getOperatorSymbol(NumericRefinement.OPERATOR_EQ));
        Assert.assertEquals("!=", NumericRefinement.getOperatorSymbol(NumericRefinement.OPERATOR_NE));
        Assert.assertEquals(">=", NumericRefinement.getOperatorSymbol(NumericRefinement.OPERATOR_GE));
        Assert.assertEquals(">", NumericRefinement.getOperatorSymbol(NumericRefinement.OPERATOR_GT));
    }

    @Test(expected = IllegalStateException.class)
    public void getOperatorSymbolInvalid() {
        NumericRefinement.getOperatorSymbol(-1);
    }

    @Test
    public void checkOperatorIsValid() {
        try {
            int operator = NumericRefinement.OPERATOR_LT;
            NumericRefinement.checkOperatorIsValid(operator);
            operator = NumericRefinement.OPERATOR_LE;
            NumericRefinement.checkOperatorIsValid(operator);
            operator = NumericRefinement.OPERATOR_EQ;
            NumericRefinement.checkOperatorIsValid(operator);
            operator = NumericRefinement.OPERATOR_NE;
            NumericRefinement.checkOperatorIsValid(operator);
            operator = NumericRefinement.OPERATOR_GE;
            NumericRefinement.checkOperatorIsValid(operator);
            operator = NumericRefinement.OPERATOR_GT;
            NumericRefinement.checkOperatorIsValid(operator);
        } catch (IllegalStateException e) {
            fail("Operator " + operator + "should be considered valid");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void checkOperatorIsInvalid() {
        int operator = -1;
        NumericRefinement.checkOperatorIsValid(operator);
    }
}