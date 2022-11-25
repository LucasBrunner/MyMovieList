package cop4655.group3.mymovielist.recyclerviewutilities

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.webapi.OmdbController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDataContainer(movieData: MovieData) : ViewModel() {

    var hasFullData: ObservableBoolean = ObservableBoolean(false)

    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var showDropDown: ObservableBoolean = ObservableBoolean(false)

    private var listPosition: Int = 0
    private var mdra: MovieDataRecyclerAdapter? = null

    val movieData: MutableLiveData<MovieData> = MutableLiveData()

    init {
        hasFullData.set(!movieData.Plot.isNullOrBlank())
        this.movieData.postValue(movieData)
    }

    fun toggleDropDown() {
        showDropDown.set(!showDropDown.get())
        if (showDropDown.get() && !hasFullData.get()) {
            getFullData()
        }
    }

    fun setRecyclerData(listPosition: Int, mdra: MovieDataRecyclerAdapter) {
        this.listPosition = listPosition
        this.mdra = mdra
    }

    private fun getFullData() {
        Log.i("MovieDataContainer: ", "get full data")
        movieData.value?.imdbID?.let { imdbID ->
            movieData.value?.Year?.let { year ->
                isLoading.set(true)
                OmdbController
                    .getService()
                    .getFulldata(
                        imdbID,
                        year
                    )
                    .enqueue(object : Callback<MovieData> {
                        override fun onResponse(call: Call<MovieData>, response: Response<MovieData>) {
                            response.body()?.let { body ->
                                movieData.postValue(body)
                                hasFullData.set(true)
                                mdra?.notifyItemChanged(listPosition)
                            }
                            isLoading.set(false)
                        }

                        override fun onFailure(call: Call<MovieData>, t: Throwable) {
                            isLoading.set(false)
                            Log.i("Movie search error: ", "Callback called onFailure")
                            Log.e("Movie search error: ", t.message.toString())
                        }

                    })
            }
        }

    }
}