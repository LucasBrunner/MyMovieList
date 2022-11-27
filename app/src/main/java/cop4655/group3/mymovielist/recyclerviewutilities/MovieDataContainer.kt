package cop4655.group3.mymovielist.recyclerviewutilities

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.data.RawMovieData
import cop4655.group3.mymovielist.webapi.OmdbController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDataContainer(movieData: MovieData) : ViewModel() {
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var showDropDown: ObservableBoolean = ObservableBoolean(false)

    private var listPosition: Int = 0
    private var recycler: MovieDataRecyclerAdapter? = null

    val movieData: MutableLiveData<MovieData> = MutableLiveData(movieData)

    fun toggleDropDown() {
        showDropDown.set(!showDropDown.get())
        if (showDropDown.get() && movieData.value?.hasFullData != true) {
            getFullData()
        }
    }

    fun setRecyclerData(listPosition: Int, mdra: MovieDataRecyclerAdapter) {
        this.listPosition = listPosition
        this.recycler = mdra
    }

    private fun getFullData() {
        Log.i("MovieDataContainer: ", "get full data")
        movieData.value?.rawMovieData?.imdbID?.let { imdbID ->
            movieData.value?.rawMovieData?.Year?.let { year ->
                isLoading.set(true)
                OmdbController
                    .getService()
                    .getFulldata(
                        imdbID,
                        year
                    )
                    .enqueue(object : Callback<RawMovieData> {
                        override fun onResponse(call: Call<RawMovieData>, response: Response<RawMovieData>) {
                            response.body()?.let { body ->
                                movieData.value?.rawMovieData = body
                                movieData.value?.hasFullData = true
                                movieData.postValue(movieData.value)
                                recycler?.notifyItemChanged(listPosition)
                            }
                            isLoading.set(false)
                        }

                        override fun onFailure(call: Call<RawMovieData>, t: Throwable) {
                            isLoading.set(false)
                            Log.i("Movie search error: ", "Callback called onFailure")
                            Log.e("Movie search error: ", t.message.toString())
                        }

                    })
            }
        }

    }
}