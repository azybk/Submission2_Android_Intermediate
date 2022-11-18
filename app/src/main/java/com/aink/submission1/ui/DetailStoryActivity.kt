package com.aink.submission1.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aink.submission1.data.ListStoryItem
import com.aink.submission1.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem = intent.getParcelableExtra<ListStoryItem>(EXTRA_USER) as ListStoryItem
        val photo = storyItem.photoUrl.toString()
        val id = storyItem.id.toString()
        val nama = storyItem.name.toString()
        val description = storyItem.description.toString()

        binding.tvItemIdUserDetail.text = id
        binding.tvItemNamaDetail.text = nama
        binding.tvItemDescriptionDetail.text = description

        Glide.with(this)
            .load(photo)
            .into(binding.imgItemPhotoDetail)

    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}