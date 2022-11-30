package cop4655.group3.mymovielist.data

import android.util.Log
import cop4655.group3.mymovielist.webapi.MovieSearchResults
import cop4655.group3.mymovielist.webapi.OmdbController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class RawMovieData(
    var Actors: String? = null,
    var Awards: String? = null,
    var BoxOffice: String? = null,
    var Country: String? = null,
    var DVD: String? = null,
    var Director: String? = null,
    var Genre: String? = null,
    var Language: String? = null,
    var Metascore: String? = null,
    var Plot: String? = null,
    var Poster: String? = null,
    var Production: String? = null,
    var Rated: String? = null,
    var Ratings: MutableList<Rating> = ArrayList<Rating>(),
    var Released: String? = null,
    var Response: String? = null,
    var Runtime: String? = null,
    var Title: String? = null,
    var Type: String? = null,
    var Website: String? = null,
    var Writer: String? = null,
    var Year: String? = null,
    var imdbID: String? = null,
    var imdbRating: String? = null,
    var imdbVotes: String? = null
)