package cop4655.group3.mymovielist.data

import android.content.Context
import cop4655.group3.mymovielist.database.DatabaseInterface
import cop4655.group3.mymovielist.webapi.OmdbController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieData (
    rawMovieData: RawMovieData,
    var myMovieData: MyMovieData = MyMovieData()
) {
    var rawMovieData: RawMovieData = rawMovieData
        set(value) {
            this.hasFullData = value.Plot != null
            field = value
        }
    var hasFullData: Boolean = false
        private set
    val me = this

    init {
        hasFullData = rawMovieData.Plot != null
    }

    fun getFullData(
        context: Context,
        success: (MovieData) -> Unit,
        failure: () -> Unit,
    ) {
        if (hasFullData) {
            rawMovieData.imdbID?.let { imdbId ->
                myMovieData = DatabaseInterface(context).getMyMovieData(imdbId) ?: myMovieData
            }
            success(this)
        } else {
            rawMovieData.imdbID?.let { imdbID ->
                rawMovieData.Year?.let { year ->
                    OmdbController
                        .getService()
                        .getFulldata(
                            imdbID,
                            year
                        )
                        .enqueue(object : Callback<RawMovieData> {
                            override fun onResponse(call: Call<RawMovieData>, response: Response<RawMovieData>) {
                                response.body()?.let { body ->
                                    rawMovieData = body
                                }
                                success(me)
                            }

                            override fun onFailure(call: Call<RawMovieData>, t: Throwable) {
                                failure()
                            }

                        })
                }
            }
        }
    }
}

