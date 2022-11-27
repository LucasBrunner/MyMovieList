package cop4655.group3.mymovielist.data

import java.util.*

/**
 * If watchList or planList are null then they this movie is not in those list.
 */
data class MyMovieData(
    var stars: Int? = null,
    var heart: Boolean = false,
    /**
     * If watchList is null then they this movie is not in the watchlist.
     */
    var watchList: Calendar? = null,
    /**
     * If planList is null then they this movie is not in the plan-to-watch list.
     */
    var planList: Calendar? = null
)
