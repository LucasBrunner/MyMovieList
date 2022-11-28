package cop4655.group3.mymovielist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cop4655.group3.mymovielist.MainActivity
import cop4655.group3.mymovielist.MovieDataUtilities.getMoviesFromSearch
import cop4655.group3.mymovielist.databinding.FragmentMovieSearchBinding
import cop4655.group3.mymovielist.recyclerviewutilities.MovieDataContainer
import cop4655.group3.mymovielist.recyclerviewutilities.MovieDataRecyclerAdapter

class MovieSearch(main: MainActivity) : MovieAppFragment(main) {

    private var binding: FragmentMovieSearchBinding? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MovieDataRecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieSearchBinding.inflate(layoutInflater, container, false)

        binding?.let { b ->
            b.movieSearchButton.setOnClickListener { findMovie() }

            layoutManager = LinearLayoutManager(context)
            b.recyclerView.layoutManager = layoutManager
        }

        setDataVisibility(false)
        setLoading(false)

        return binding?.root
    }

    private fun findMovie() {
        setDataVisibility(false)
        setLoading(true)
        this.context?.let { context ->
            getMoviesFromSearch(
                binding?.movieSearchBox?.text.toString(),
                context,
                { movieData ->
                    setDataVisibility(true)
                    setLoading(false)
                    setMovie(movieData.map { data -> MovieDataContainer(data) })
                },
                { setLoading(false) },
            )
        }
    }

    private fun setDataVisibility(value: Boolean) {
        if (value) {
            binding?.recyclerView?.visibility = View.VISIBLE
        } else {
            binding?.recyclerView?.clearAnimation()
            binding?.recyclerView?.visibility = View.GONE
        }
    }

    private fun setLoading(value: Boolean) {
        if (value) {
            binding?.loadingSpinner?.clearAnimation()
            binding?.loadingSpinner?.visibility = View.VISIBLE
        } else {
            binding?.loadingSpinner?.clearAnimation()
            binding?.loadingSpinner?.visibility = View.GONE
        }

    }

    private fun setMovie(movieData: List<MovieDataContainer>) {
        adapter = MovieDataRecyclerAdapter(movieData)
        binding?.recyclerView?.adapter = adapter
    }

//    private fun toggleExtraInfo() {
//        binding?.let { b ->
//            showExtraInfo = !showExtraInfo
//            if (showExtraInfo) {
//                b.searchResult.sometimesShow.clearAnimation()
//                b.searchResult.sometimesShow.visibility = View.VISIBLE
//                b.searchResult.extraInfoButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
//            } else {
//                b.searchResult.sometimesShow.clearAnimation()
//                b.searchResult.sometimesShow.visibility = View.GONE
//                b.searchResult.extraInfoButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
//            }
//        }
//    }

    override fun startup() {} // Is run once when the fragment is created
    override fun refresh() {} // Is run every time the fragment is swapped to
}