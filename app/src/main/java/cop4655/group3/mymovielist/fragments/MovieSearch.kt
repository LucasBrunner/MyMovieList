package cop4655.group3.mymovielist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cop4655.group3.mymovielist.MainActivity
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.databinding.FragmentMovieSearchBinding

class MovieSearch(main: MainActivity) : MovieAppFragment(main) {

    private var movieData: MovieData? = null

    private var binding: FragmentMovieSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieSearchBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }

    fun setMovie(movieData: MovieData) {
        this.movieData = movieData
        binding?.let { b ->
            b.searchResult.movieTitle.text = movieData.Title
            b.searchResult.movieYear.text = movieData.Year
        }
    }

    override fun startup() {} // Is run once when the fragment is created
    override fun refresh() {} // Is run every time the fragment is swapped to
}