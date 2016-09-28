package com.algolia.instantsearch.filters;

public class CheckBoxDescription extends FilterDescription {
    final boolean checkedIsTrue;

    CheckBoxDescription(String attribute, String name, boolean checkedIsTrue, int position) {
        super(attribute, name, position);
        this.checkedIsTrue = checkedIsTrue;
    }

    protected void create(FilterResultsWindow window) {
        window.createCheckBox(this);
    }
}
