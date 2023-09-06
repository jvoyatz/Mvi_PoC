package dev.jvoyatz.newarch.mvipoc.presentation.screen2

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import dev.jvoyatz.newarch.mvipoc.databinding.ItemMovieBinding
import dev.jvoyatz.newarch.mvipoc.databinding.ProgressbarBinding


class MoviesReduceAdapter(val onSelection: (String) -> Unit): ListAdapter<MovieUiModel, RecyclerView.ViewHolder>(
    MovieItemDiffCallback
) {


    companion object {
        val TYPE_DATA = 0
        val TYPE_PROGRESS = 667
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MovieViewHolder) {
            val movie = getItem(position)!!
            holder.binding.name.text = movie.title
            Glide.with(holder.itemView.context)
                .load("https://image.tmdb.org/t/p/w300" + movie.poster)
                .into(holder.binding.imageview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == TYPE_DATA) {
            val binding = ItemMovieBinding.inflate(inflater, parent, false)
            return MovieViewHolder(binding.root, binding){
                onSelection(getItem(it).title)
            }
        }
        val loadingBinding = ProgressbarBinding.inflate(inflater, parent, false)
        return ProgressViewHolder(loadingBinding)
    }

//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                var layoutManager = recyclerView.layoutManager as GridLayoutManager
//                val isLoading = currentList.isNotEmpty() && currentList.last().id == TYPE_PROGRESS
//
//                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//                val totalItemCount = layoutManager.itemCount
//
//                if( !isLoading && totalItemCount == lastVisibleItemPosition + 1){
//                    showLoading()
//                    loadMore()
//                }
//            }
//        })
//    }

    fun showLoading(){
        val mutableList = currentList.toMutableList()
        mutableList.add(MovieUiModel("", "", "", id = TYPE_PROGRESS))
        submitList(mutableList)
    }

    override fun getItemViewType(position: Int): Int {
        if(currentList[position].id == TYPE_PROGRESS){
            return TYPE_PROGRESS
        }
        return TYPE_DATA
    }

    inner class MovieViewHolder(val view: View, val binding: ItemMovieBinding, listener: (Int) -> Unit) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                listener(adapterPosition)
            }
        }
    }
    inner class ProgressViewHolder(view: ProgressbarBinding): ViewHolder(view.root)

    object MovieItemDiffCallback: DiffUtil.ItemCallback<MovieUiModel>() {
        override fun areItemsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
