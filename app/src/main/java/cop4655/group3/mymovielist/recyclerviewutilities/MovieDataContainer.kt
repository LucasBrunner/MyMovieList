package cop4655.group3.mymovielist.recyclerviewutilities

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.database.DatabaseInterface
import java.util.*

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
        updateMovieData()
    }

    fun togglePlanList(view: View) {
        movieData.value?.let { movieData ->
            movieData.rawMovieData.imdbID?.let { imdbId ->
                if (movieData.myMovieData.planList == null) {
                    val now = Calendar.getInstance()
                    DatabaseInterface(view.context).setPlan(now, imdbId)
                    movieData.myMovieData.planList = now
                    updateMovieData()
                } else {
                    DatabaseInterface(view.context).setPlan(null, imdbId)
                    movieData.myMovieData.planList = null
                    updateMovieData()
                }
            }
        }
    }

    fun toggleHistoryList(view: View) {
        movieData.value?.let { movieData ->
            movieData.rawMovieData.imdbID?.let { imdbId ->
                if (movieData.myMovieData.historyList == null) {
                    val now = Calendar.getInstance()
                    DatabaseInterface(view.context).setHistory(now, imdbId)
                    movieData.myMovieData.historyList = now
                    updateMovieData()
                } else {
                    DatabaseInterface(view.context).setHistory(null, imdbId)
                    movieData.myMovieData.historyList = null
                    updateMovieData()
                }
            }
        }
    }

    fun toggleHeart(view: View) {
        movieData.value?.let { movieData ->
            movieData.rawMovieData.imdbID?.let { imdbId ->
                if (movieData.myMovieData.heart == null) {
                    val now = Calendar.getInstance()
                    DatabaseInterface(view.context).setHeart(now, imdbId)
                    movieData.myMovieData.heart = now
                    updateMovieData()
                } else {
                    DatabaseInterface(view.context).setHeart(null, imdbId)
                    movieData.myMovieData.heart = null
                    updateMovieData()
                }
            }
        }
    }

    fun setStarCount(view: View, starPosition: Int) {
        movieData.value?.let { movieData ->
            movieData.rawMovieData.imdbID?.let { imdbId ->
                if (movieData.myMovieData.stars == starPosition) {
                    DatabaseInterface(view.context).setRating(0, imdbId)
                    movieData.myMovieData.stars = 0
                    updateMovieData()
                } else {
                    DatabaseInterface(view.context).setRating(starPosition, imdbId)
                    movieData.myMovieData.stars = starPosition
                    updateMovieData()
                }
            }
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