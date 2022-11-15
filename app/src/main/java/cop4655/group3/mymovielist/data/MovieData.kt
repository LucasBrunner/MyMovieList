package cop4655.group3.mymovielist.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

data class MovieData (
    @SerializedName("imdbID")
    val id: String,
    @SerializedName("Title")
    val Title: String,
    @SerializedName("Year")
    val Year: String,
    @SerializedName("Plot")
    val plot: String,
    @SerializedName("Poster")
    private val posterUrl: String,
) {
    fun putPoster(
        currentScope: CoroutineScope,
        imageLocation: ImageView
    ) {
        currentScope.launch(Dispatchers.IO) {
            var image: Bitmap? = null
            try {
                val imageStream = java.net.URL(posterUrl).openStream()
                image = BitmapFactory.decodeStream(imageStream)
            } catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            imageLocation.setImageBitmap(image)
        }
    }
}