package gr.jvoyatz.android.poc.mvi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import dev.jvoyatz.newarch.mvipoc.databinding.ItemMovieBinding
import dev.jvoyatz.newarch.mvipoc.databinding.ProgressbarBinding

import gr.jvoyatz.android.poc.mvi.domain.Movie


class MoviesAdapter(val loadMore: () -> Unit): ListAdapter<Movie, RecyclerView.ViewHolder>(MovieItemDiffCallback) {


    companion object {
        val TYPE_DATA = 0
        val TYPE_PROGRESS = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MovieViewHolder) {
            val movie = getItem(position)!!
            holder.view.name.text = movie.title
            Glide.with(holder.itemView.context)
                .load("https://image.tmdb.org/t/p/w300" + movie.poster)
                .into(holder.view.imageview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == TYPE_DATA) {
            val binding = ItemMovieBinding.inflate(inflater, parent, false)
            return MovieViewHolder(binding)
        }
        val loadingBinding = ProgressbarBinding.inflate(inflater, parent, false)
        return ProgressViewHolder(loadingBinding)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                var layoutManager = recyclerView.layoutManager as GridLayoutManager
                val isLoading = currentList.isNotEmpty() && currentList.last().id == TYPE_PROGRESS

                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if( !isLoading && totalItemCount == lastVisibleItemPosition + 1){
                    showLoading()
                    loadMore()
                }
            }
        })
    }

    fun showLoading(){
        val mutableList = currentList.toMutableList()
        mutableList.add(Movie("", "", "").apply {
            id = TYPE_PROGRESS
        })
        submitList(mutableList)
    }

    override fun getItemViewType(position: Int): Int {
        if(currentList[position].id == TYPE_PROGRESS){
            return TYPE_PROGRESS
        }
        return TYPE_DATA
    }

    class MovieViewHolder(val view: ItemMovieBinding): RecyclerView.ViewHolder(view.root)
    inner class ProgressViewHolder(view: ProgressbarBinding): ViewHolder(view.root)


    object MovieItemDiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            // Id is unique.
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
