package com.algolia.instantsearch.migration2to3

public interface Index :
    EndpointSearch,
    EndpointSettings,
    EndpointAdvanced,
    EndpointIndex,
    EndpointIndexing,
    EndpointSynonym,
    EndpointRule,
    EndpointAnswers {

    /**
     * Index name.
     */
    override val indexName: IndexName

    /**
     * Iterate over all [Rule] in the index.
     *
     * @see [searchRules]
     *
     * @param query The [RuleQuery] used to search.
     * @param requestOptions Configure request locally with [RequestOptions]
     */
    public suspend fun browseRules(
        query: RuleQuery = RuleQuery(),
        requestOptions: RequestOptions? = null,
    ): List<ResponseSearchRules>

    /**
     * Iterate over all [Synonym] in the index.
     *
     * @see [searchSynonyms]
     *
     * @param query The [SynonymQuery] used to search.
     * @param requestOptions Configure request locally with [RequestOptions]
     */
    public suspend fun browseSynonyms(
        query: SynonymQuery = SynonymQuery(),
        requestOptions: RequestOptions? = null,
    ): List<ResponseSearchSynonyms>

    /**
     * Iterate over all objects in the index.
     *
     * @see [browse]
     *
     * @param query The [Query] used to search.
     * @param requestOptions Configure request locally with [RequestOptions]
     */
    public suspend fun browseObjects(
        query: Query = Query(),
        requestOptions: RequestOptions? = null,
    ): List<ResponseSearch>

    public companion object
}
