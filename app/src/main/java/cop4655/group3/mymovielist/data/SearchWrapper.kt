package cop4655.group3.mymovielist.data

import com.google.gson.annotations.SerializedName

data class SearchWrapper(
    @SerializedName("Search")
    val results: List<FullMovieData>
)
