import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch

private val response = ResponseSearch()
private val client = mockClient(response, ResponseSearch.serializer())
public val index = client.initIndex(IndexName("stub"))
