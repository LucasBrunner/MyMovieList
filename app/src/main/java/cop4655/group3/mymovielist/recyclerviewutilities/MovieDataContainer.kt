package cop4655.group3.mymovielist.recyclerviewutilities

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.data.RawMovieData
import cop4655.group3.mymovielist.database.DatabaseInterface
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

    fun toggleDropDown(view: View) {
        showDropDown.set(!showDropDown.get())
        if (showDropDown.get() && movieData.value?.hasFullData != true) {
            getFullData(view.context)
        }
    }

    fun setRecyclerData(listPosition: Int, mdra: MovieDataRecyclerAdapter) {
        this.listPosition = listPosition
        this.recycler = mdra
    }

    private fun updateMovieData(data: MovieData? = movieData.value) {
        movieData.postValue(data)
        recycler?.notifyItemChanged(listPosition)
    }

    private fun getFullData(context: Context) {
        Log.i("MovieDataContainer: ", "get full data")
        isLoading.set(true)
        movieData.value?.getFullData(
            context,
            { data ->
                updateMovieData(data)
                isLoading.set(false)
                putInDatabase(context)
            },
            { isLoading.set(false) },
        )
    }

    private fun putInDatabase(context: Context) {
        this.movieData.value?.let { data ->
            DatabaseInterface(context).insertFullMovieData(data)
        }
    }
}