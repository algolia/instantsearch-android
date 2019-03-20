class TestSearcherSingleIndex {
//TODO: Test using mockk.io
//    lateinit var query : Query
//    lateinit var searcher: Searcher
//
//    var mockClient = mockk()
//    internal class MockClient : ClientSearch(ApplicationID("foo"), APIKey("bar")) {
//
//        var count = 0
//
//        override fun search() {
//            throw BadResponseStatusException(HttpStatusCode(403, "forbidden"), )
//        }
//
//        override fun cancel() {
//        }
//    }
//
//    internal class MockResponse: HttpResponse {
//        override val call: HttpClientCall
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val content: ByteReadChannel
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val coroutineContext: CoroutineContext
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val requestTime: GMTDate
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val responseTime: GMTDate
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val status: HttpStatusCode
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val version: HttpProtocolVersion
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val headers: Headers
//            get() = Headers.Empty
//    }
//
//    fun setup() {
//        query = Query()
//        val mockClient = object : ClientSearch() {
//
//        }
//        searcher = SearcherSingleIndex(mockClient, query)
//    }
//    @Test
//    fun `error listeners are called on error`() {
//        setup()
//    searcher.errorListeners +=
//    {
//        //TODO: Assert listener is called
//    }
//    }
}