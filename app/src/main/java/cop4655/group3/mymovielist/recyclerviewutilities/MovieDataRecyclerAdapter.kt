package cop4655.group3.mymovielist.recyclerviewutilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import cop4655.group3.mymovielist.R
import cop4655.group3.mymovielist.databinding.MovieDataListItemBinding

class MovieDataRecyclerAdapter(private val movieDataList: MutableList<MovieDataContainer>): RecyclerView.Adapter<MovieDataRecyclerAdapter.ViewHolder>() {

    init {
        var i = 0
        for (mdc in movieDataList) {
            mdc.setRecyclerData(i, this)
            i += 1
        }
    }

    inner class ViewHolder(private val binding: MovieDataListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieDataContainer) {
            binding.apply {
                container = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.movie_data_list_item,
            parent,
            false,
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieDataList[position])
    }

    override fun getItemCount(): Int {
        return movieDataList.size
    }
}