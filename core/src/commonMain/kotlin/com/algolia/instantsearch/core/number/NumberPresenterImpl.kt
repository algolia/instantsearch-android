package com.algolia.instantsearch.core.number


public object NumberPresenterImpl : NumberPresenter<Number> {

    override fun invoke(number: Number?): String = present(number)

    /**
     * Presents the given number, transforming it into a [String].
     *
     * @param number the value to present.
     *
     * @return the number's string representation, or `"-"` if `null`.
     */
    fun present(number: Number?): String {
        return number?.toString() ?: "-"
    }
}