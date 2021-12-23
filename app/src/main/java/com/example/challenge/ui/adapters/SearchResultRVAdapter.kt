package com.example.challenge.ui.adapters

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challenge.R
import com.example.challenge.api.omdb.dto.Search
import com.example.challenge.ui.ResultClickListener

class SearchResultRVAdapter(private val resultClickListener: ResultClickListener?) :
    RecyclerView.Adapter<SearchResultRVAdapter.ViewHolder>() {
    var items: List<Search> = listOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imgCover: ImageView
        var tvTrackerName: TextView
        var tvGenre: TextView
        var tvYear: TextView

        fun bindData(item: Search) {
            tvTrackerName.text = item.title
            tvGenre.text = item.type
            tvYear.text = item.year.toString()
            Glide.with(imgCover.context)
                .load(item.poster)
                .placeholder(R.drawable.img_placeholder)
                .into(imgCover)
        }

        override fun onClick(v: View) {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION && resultClickListener != null) {
                resultClickListener.onResultItemClick(items!![pos])
            }
        }

        init {
            imgCover = itemView.findViewById(R.id.img_item)
            tvTrackerName = itemView.findViewById(R.id.tv_trackname)
            tvGenre = itemView.findViewById(R.id.tv_genre)
            tvYear = itemView.findViewById(R.id.tv_year)
            itemView.setOnClickListener(this)
        }
    }

    private var dataVersion = 0


    @Suppress("DEPRECATION")
    @MainThread
    fun replace(update: List<Search>) {
        dataVersion++
        if (items.isEmpty()) {
            items = update
            notifyDataSetChanged()
        } else if (update.isEmpty()) {
            val oldSize = items.size
            items = listOf()
            notifyItemRangeRemoved(0, oldSize)
        } else {
            val startVersion = dataVersion
            val oldItems = ArrayList(items)

            object : AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                override fun doInBackground(vararg voids: Void): DiffUtil.DiffResult {
                    return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                        override fun getOldListSize(): Int = oldItems.size
                        override fun getNewListSize(): Int = update.size

                        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                            val oldItem = oldItems[oldItemPosition]
                            val newItem = update[newItemPosition]
                            return oldItem.imdbID == newItem.imdbID
                        }

                        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                            val oldItem = oldItems[oldItemPosition]
                            val newItem = update[newItemPosition]
                            return oldItem == newItem
                        }
                    })
                }

                override fun onPostExecute(diffResult: DiffUtil.DiffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return
                    }
                    items = update
                    diffResult.dispatchUpdatesTo(this@SearchResultRVAdapter)
                }
            }.execute()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_search_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(items!![position])
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    fun clearResults() {
        this.items = listOf()
        notifyDataSetChanged()
    }
}