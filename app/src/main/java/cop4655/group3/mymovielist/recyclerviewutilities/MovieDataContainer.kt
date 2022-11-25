package cop4655.group3.mymovielist.recyclerviewutilities

import android.util.Log
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.webapi.OmdbController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDataContainer(public var movieData: MovieData) {

    var hasFullData: Boolean = false

    init {
        hasFullData = movieData.Plot.isNullOrBlank()
    }

    fun getFullData() {
        movieData.imdbID?.let { imdbID ->
            movieData.Year?.let { year ->
                OmdbController
                    .getService()
                    .getFulldata(
                        imdbID,
                        year
                    )
                    .enqueue(object : Callback<MovieData> {
                        override fun onResponse(call: Call<MovieData>, response: Response<MovieData>) {
                            response.body()?.let { body ->
                                movieData = body
                                hasFullData = true
                            }
                        }

                        override fun onFailure(call: Call<MovieData>, t: Throwable) {
                            Log.i("Movie search error: ", "Callback called onFailure")
                            Log.e("Movie search error: ", t.message.toString())
                        }

                    })
            }
        }

    }
}