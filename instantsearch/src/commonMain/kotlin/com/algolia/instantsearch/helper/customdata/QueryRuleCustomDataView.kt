package com.algolia.instantsearch.helper.customdata

/**
 * View that will render query rule custom data.
 */
public interface QueryRuleCustomDataView<in T> {

    /**
     * Sets the model to be rendered by the view.
     */
    public fun setModel(model: T)
}
