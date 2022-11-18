package com.aink.submission1.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aink.submission1.data.ListStoryItem
import com.aink.submission1.databinding.ItemRowStoryBinding
import com.bumptech.glide.Glide


class ListStoryAdapter:
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK)
{
    private lateinit var binding: ItemRowStoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataStory = getItem(position)

        if (dataStory != null) {
            holder.bind(dataStory, holder)
        }
    }

    class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataStory: ListStoryItem, holder: ListViewHolder) {
            binding.tvItemIdUser.text = dataStory.id
            binding.tvItemNama.text = dataStory.name

            Glide.with(holder.itemView.context)
                .load(dataStory.photoUrl)
                .circleCrop()
                .into(binding.imgItemPhoto)

            val data = ListStoryItem(dataStory.photoUrl, dataStory.name, dataStory.description, dataStory.id,null,null)

            holder.itemView.setOnClickListener {
                val detailStoryIntent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
                detailStoryIntent.putExtra(DetailStoryActivity.EXTRA_USER, data)

                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.imgItemPhoto, "photo"),
                    Pair(holder.binding.tvItemIdUser, "id"),
                    Pair(holder.binding.tvItemNama, "name")
                )

                holder.itemView.context.startActivity(detailStoryIntent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}