package cop4655.group3.mymovielist.boundfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.gson.Gson
import cop4655.group3.mymovielist.MainActivity
import cop4655.group3.mymovielist.MovieAppFragment
import cop4655.group3.mymovielist.data.FullMovieData
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.data.SearchWrapper
import cop4655.group3.mymovielist.databinding.FragmentMovieSearchBinding
import cop4655.group3.mymovielist.databinding.MovieInfoBinding
import cop4655.group3.mymovielist.webapi.OmdbController
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieSearch(main: MainActivity) : MovieAppFragment(main) {

//    private var searchBox: EditText? = null
//    private var searchButton: Button? = null
//    private var movieViewTools: ConstraintLayout? = null
//    private var searchResult: ConstraintLayout? = null

    private var binding: FragmentMovieSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val v = inflater.inflate(R.layout.fragment_movie_search, container, false)
        binding = FragmentMovieSearchBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding?.let { b ->
            b.movieSearchButton.setOnClickListener{ this.searchMovie() }
        }

//        searchBox = v.findViewById(R.id.movie_search_box)
//        searchButton = v.findViewById(R.id.movie_search_button)
//        movieViewTools = v.findViewById(R.id.movie_view_tools)
//        searchResult = v.findViewById(R.id.search_result)

//        val model = ViewModelProvider(searchResult)[MovieInfoViewModel::class.java]

//        searchButton?.setOnClickListener{ this.searchMovie() }

        return binding?.root
    }

    private fun searchMovie() {
        OmdbController
            .getService()
            .searchMovies(
                binding?.movieSearchBox?.text.toString()
            )
            .enqueue(object : Callback<SearchWrapper> {
            override fun onResponse(call: Call<SearchWrapper>, response: Response<SearchWrapper>) {
                Log.d("my activity", "request successful")
                response.body()?.let { body ->
                    binding?.let { b ->
                        b.movieViewTools.visibility = VISIBLE
                        b.searchResult.vm?.setNewMovie(body.results[0])
                    }
                }
            }

            override fun onFailure(call: Call<SearchWrapper>, t: Throwable) {
                t.printStackTrace()
                Log.d("my activity", t.message.toString())
            }

        })
    }

    override fun startup() {} // Is run once when the fragment is created
    override fun refresh() {
        binding?.let { b ->
            b.movieSearchBox.text.clear()
            b.movieViewTools.visibility = INVISIBLE
        }
    }
}