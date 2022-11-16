package cop4655.group3.mymovielist.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cop4655.group3.mymovielist.MainActivity
import cop4655.group3.mymovielist.R
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.databinding.FragmentMovieSearchBinding
import cop4655.group3.mymovielist.webapi.MovieSearchResults
import cop4655.group3.mymovielist.webapi.OmdbController
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieSearch(main: MainActivity) : MovieAppFragment(main) {

    private var movieData: MovieData? = null

    private var binding: FragmentMovieSearchBinding? = null

    private var showExtraInfo: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieSearchBinding.inflate(layoutInflater, container, false)

        binding?.let { b ->
            b.movieSearchButton.setOnClickListener { findMovie() }
            b.searchResult.extraInfoButton.setOnClickListener { toggleExtraInfo() }
        }

        setDataVisibility(false)
        isLoading(false)

        return binding?.root
    }

    private fun findMovie() {
        setDataVisibility(false)
        isLoading(true)
        OmdbController
            .getService()
            .searchMovies(
                binding?.movieSearchBox?.text.toString()
            )
            .enqueue(object : Callback<MovieSearchResults> {
                override fun onResponse(call: Call<MovieSearchResults>, response: Response<MovieSearchResults>) {
                    response.body()?.let { body ->
                        setDataVisibility(true)
                        isLoading(false)
                        setMovie(body.Search[0])
                    }
                }

                override fun onFailure(call: Call<MovieSearchResults>, t: Throwable) {
                    Log.i("Movie search error: ", "Callback called onFailure")
                    Log.e("Movie search error: ", t.message.toString())
                }

            })
    }

    fun setDataVisibility(setVisible: Boolean) {
        if (setVisible) {
            binding?.movieViewTools?.visibility = View.VISIBLE
        } else {
            binding?.movieViewTools?.clearAnimation()
            binding?.movieViewTools?.visibility = View.GONE
        }
    }

    fun isLoading(setLoading: Boolean) {
        if (setLoading) {
            binding?.loadingSpinner?.clearAnimation()
            binding?.loadingSpinner?.visibility = View.VISIBLE
        } else {
            binding?.loadingSpinner?.clearAnimation()
            binding?.loadingSpinner?.visibility = View.GONE
        }

    }

    fun setMovie(movieData: MovieData) {
        this.movieData = movieData
        binding?.let { b ->
            b.searchResult.movieTitle.text = movieData.Title
            b.searchResult.movieYear.text = movieData.Year
            b.searchResult.movieDescription.text = movieData.Plot
        }
    }

    private fun toggleExtraInfo() {
        binding?.let { b ->
            showExtraInfo = !showExtraInfo
            if (showExtraInfo) {
                b.searchResult.sometimesShow.clearAnimation()
                b.searchResult.sometimesShow.visibility = View.VISIBLE
                b.searchResult.extraInfoButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
            } else {
                b.searchResult.sometimesShow.clearAnimation()
                b.searchResult.sometimesShow.visibility = View.GONE
                b.searchResult.extraInfoButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
            }
        }
    }

    override fun startup() {} // Is run once when the fragment is created
    override fun refresh() {} // Is run every time the fragment is swapped to
}