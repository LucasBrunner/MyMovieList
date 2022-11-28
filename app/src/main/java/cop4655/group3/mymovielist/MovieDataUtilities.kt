package cop4655.group3.mymovielist

import android.content.Context
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.data.RawMovieData
import cop4655.group3.mymovielist.database.DatabaseInterface
import cop4655.group3.mymovielist.webapi.MovieSearchResults
import cop4655.group3.mymovielist.webapi.OmdbController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MovieDataUtilities {
    fun getMoviesFromSearch(
        search: String,
        context: Context,
        success: (List<MovieData>) -> Unit,
        failure: () -> Unit,
    ) {
        OmdbController.getService()
            .searchMovies(search)
            .enqueue(object : Callback<MovieSearchResults> {
                override fun onResponse(call: Call<MovieSearchResults>, response: Response<MovieSearchResults>) {
                    response.body()?.let { callbackResults ->
                        success(fillMovieDataList(callbackResults.Search, context))
                    }
                }

                override fun onFailure(call: Call<MovieSearchResults>, t: Throwable) {
                    failure()
                }

            })
    }

    fun fillMovieDataList(
        rawMovieSearchResults: List<RawMovieData>,
        context: Context,
    ): List<MovieData> {
        val movieSearchResults = ArrayList<MovieData>()
        dataLoop@ for (rawMovieData in rawMovieSearchResults) {
            rawMovieData.imdbID?.also { imdbID ->
                movieSearchResults.add(
                    DatabaseInterface(context).getFullMovieData(imdbID) ?: MovieData(
                        rawMovieData
                    )
                )
            } ?: continue@dataLoop
        }
        return movieSearchResults
    }
}