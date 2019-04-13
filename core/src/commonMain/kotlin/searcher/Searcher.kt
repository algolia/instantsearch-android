package searcher

import kotlinx.coroutines.Job


public interface Searcher {

    fun search(): Job
    fun cancel()
}