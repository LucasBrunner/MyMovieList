package cop4655.group3.mymovielist.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cop4655.group3.mymovielist.MainActivity
import cop4655.group3.mymovielist.R
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.database.DatabaseInterface
import cop4655.group3.mymovielist.databinding.FragmentMovieSortListBinding
import cop4655.group3.mymovielist.recyclerviewutilities.MovieDataContainer
import cop4655.group3.mymovielist.recyclerviewutilities.MovieDataRecyclerAdapter

class MovieSortListFragment(
    main: MainActivity,
    private val listShowType: ListShowType,
) : MovieAppFragment(main), AdapterView.OnItemSelectedListener {
    enum class ListShowType {
        HISTORY,
        PLAN,
        HEARTED,
    }

    enum class SortMethod {
        TITLE,
        STARS,
        DATE_ADDED,
        METASCORE,
        RELEASE_YEAR,
    }

    private var binding: FragmentMovieSortListBinding? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MovieDataRecyclerAdapter.ViewHolder>? = null
    private var movieList: MutableList<MovieDataContainer>? = null

    private var sortMethod = SortMethod.TITLE
    private var sortAscending = false

    private var enumAdapter: ArrayAdapter<SortMethod>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieSortListBinding.inflate(layoutInflater, container, false)

        binding?.let { b ->
            layoutManager = LinearLayoutManager(context)
            b.recyclerView.layoutManager = layoutManager

            this.context?.let { context ->
                enumAdapter = ArrayAdapter(context, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, SortMethod.values())
            }
            b.sortSelector.adapter = enumAdapter
            b.sortSelector.onItemSelectedListener = this
            b.sortOrderToggle.setOnClickListener {
                sortAscending = !sortAscending
                if (sortAscending) {
                    b.sortOrderToggle.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                } else {
                    b.sortOrderToggle.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                }
                sortMovieList()
            }
        }

        getMovies()

        return binding?.root
    }

    private fun getMovies() {
        this.context?.let { context ->
            when (listShowType) {
                ListShowType.HISTORY -> setMovies(DatabaseInterface(context).getMoviesInHistory())
                ListShowType.PLAN -> setMovies(DatabaseInterface(context).getMoviesInPlan())
                ListShowType.HEARTED -> setMovies(DatabaseInterface(context).getMoviesHearted())
            }
        }
        sortMovieList()
    }

    private fun setMovies(movieDataList: List<MovieData>) {
        movieList = movieDataList.map { movieData ->
            Log.i("movie", "setMovies: " + movieData.rawMovieData.Title)
            MovieDataContainer(movieData)
        }.toMutableList()
        movieList?.let { movieList ->
            adapter = MovieDataRecyclerAdapter(movieList)
        }
        binding?.recyclerView?.adapter = adapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        enumAdapter?.getItem(position)?.let { enumAdapter ->
            sortMethod = enumAdapter
        }
        sortMovieList()
    }

    private fun sortMovieList() {
        movieList?.sortBy { when (sortMethod) {
            SortMethod.TITLE -> it.movieData.value?.rawMovieData?.Title
            SortMethod.STARS -> it.movieData.value?.myMovieData?.stars?.toString()
            SortMethod.DATE_ADDED -> {
                when (listShowType) {
                    ListShowType.HISTORY -> it.movieData.value?.myMovieData?.historyList?.toString()
                    ListShowType.PLAN -> it.movieData.value?.myMovieData?.planList?.toString()
                    ListShowType.HEARTED -> it.movieData.value?.rawMovieData?.Title
                }
            }
            SortMethod.METASCORE -> it.movieData.value?.rawMovieData?.Metascore
            SortMethod.RELEASE_YEAR -> it.movieData.value?.rawMovieData?.Year
        } }

        if (!sortAscending) {
            movieList?.reverse()
        }
        adapter?.notifyDataSetChanged()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun startup() {} // Is run once when the fragment is created
    override fun refresh() {} // Is run every time the fragment is swapped to
}
