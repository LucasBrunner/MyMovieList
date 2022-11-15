package cop4655.group3.mymovielist.boundfragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import cop4655.group3.mymovielist.data.FullMovieData
import cop4655.group3.mymovielist.data.MovieData
import cop4655.group3.mymovielist.databinding.MovieInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MovieInfoFragment(var movie: FullMovieData) : Fragment() {

    private lateinit var binding: MovieInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieInfoBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        binding = MovieInfoBinding.inflate(
//            layoutInflater,
//            container,
//            false
//        )

        binding.let { b ->
            b.vm = this
            putPoster(
                b.vm!!.movie,
                viewLifecycleOwner.lifecycleScope,
                b.posterImage
            )
        }

        return binding.root;
    }

    fun setNewMovie(newMovie: FullMovieData) {
        this.movie = newMovie
    }

    private fun putPoster(
        movieData: FullMovieData,
        currentScope: CoroutineScope,
        imageLocation: ImageView
    ) {
        currentScope.launch(Dispatchers.IO) {
            var image: Bitmap? = null
            try {
                val imageStream = java.net.URL(movieData.Poster).openStream()
                image = BitmapFactory.decodeStream(imageStream)
            } catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            imageLocation.setImageBitmap(image)
        }
    }
}