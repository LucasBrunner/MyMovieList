package cop4655.group3.mymovielist.data

import java.util.*

/**
 * If watchList or planList are null then they this movie is not in those list.
 */
data class MyMovieData(
    var stars: Int = 0,
    /**
     * If heart is null then they this movie is not in the watchlist.
     */
    var heart: Calendar? = null,
    /**
     * If watchList is null then they this movie is not in the watchlist.
     */
    var historyList: Calendar? = null,
    /**
     * If planList is null then they this movie is not in the plan-to-watch list.
     */
    var planList: Calendar? = null
)