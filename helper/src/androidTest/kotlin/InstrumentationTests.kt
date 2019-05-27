
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch

private val mockResponse = ResponseSearch()
private val mockClient = mockClient(mockResponse, ResponseSearch.serializer())
public val mockIndex = mockClient.initIndex(IndexName("stub"))
public val applicationContext: Context = ApplicationProvider.getApplicationContext()