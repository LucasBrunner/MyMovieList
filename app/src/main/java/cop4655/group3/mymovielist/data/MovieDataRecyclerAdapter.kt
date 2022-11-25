package cop4655.group3.mymovielist.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cop4655.group3.mymovielist.R

class MovieDataRecyclerAdapter(private val movieDataList: List<MovieData>): RecyclerView.Adapter<MovieDataRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(movieInfoView: View) : RecyclerView.ViewHolder(movieInfoView) {
        lateinit var poster:      ImageView
        lateinit var title:       TextView
        lateinit var year:        TextView
        lateinit var description: TextView

        init {
            poster      = movieInfoView.findViewById(R.id.poster_image)
            title       = movieInfoView.findViewById(R.id.title)
            year        = movieInfoView.findViewById(R.id.year)
            description = movieInfoView.findViewById(R.id.description)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val movieInfoView = LayoutInflater.from(parent.context).inflate(R.layout.movie_data_list_item, parent, false)
        return ViewHolder(movieInfoView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text       = movieDataList[position].Title
        holder.year.text        = "Released in " + movieDataList[position].Year
        holder.description.text = movieDataList[position].Plot
    }

    override fun getItemCount(): Int {
        return movieDataList.size
    }
}