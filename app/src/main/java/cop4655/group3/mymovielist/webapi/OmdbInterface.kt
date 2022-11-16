package cop4655.group3.mymovielist.webapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val OMDB_URL = "http://www.omdbapi.com/"
const val API_KEY = "18f98b60"

interface OmdbInterface {
    @GET(" ")
    fun searchMovies(
        @Query("s") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apikey") apiKey: String = API_KEY,
    ) : Call<MovieSearch>
}