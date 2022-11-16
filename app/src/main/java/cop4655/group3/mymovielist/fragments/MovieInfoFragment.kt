package cop4655.group3.mymovielist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cop4655.group3.mymovielist.R
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.databinding.FragmentMovieInfoBinding

class MovieInfoFragment : Fragment() {

    private var movieData: MovieData? = null

    private var binding: FragmentMovieInfoBinding? = null

    private var showExtraInfo: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieInfoBinding.inflate(layoutInflater, container, false)

        binding?.let { b ->
            b.extraInfoButton.setOnClickListener { toggleExtraInfo() }
        }

        return binding?.root
    }

    fun setMovie(movieData: MovieData) {
        this.movieData = movieData
        binding?.let { b ->
            b.movieTitle.text = movieData.Title
            b.movieYear.text = movieData.Year
            b.movieDescription.text = movieData.Plot
        }
    }

    private fun toggleExtraInfo() {
        binding?.let { b ->
            showExtraInfo = !showExtraInfo
            if (showExtraInfo) {
                b.sometimesShow.visibility = View.VISIBLE
                b.extraInfoButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
            } else {
                b.sometimesShow.visibility = View.GONE
                b.extraInfoButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
            }
        }
    }
}