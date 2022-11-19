package com.aink.submission1.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aink.submission1.R
import com.aink.submission1.databinding.ActivityStoriesBinding
import com.aink.submission1.utils.UserPreference
import com.aink.submission1.utils.util.logout
import com.aink.submission1.utils.util.selectItem
import com.aink.submission1.viewmodel.ListStoryViewModel

class StoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoriesBinding
    private val listStoryViewModel: ListStoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStories.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        showAllStories()

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@StoriesActivity, StoryActivity::class.java)
            startActivity(intent)
        }

        binding.fabMaps.setOnClickListener {
            val intentMaps = Intent(this@StoriesActivity, MapsActivity::class.java)
            startActivity(intentMaps)
        }

    }

    private fun showAllStories() {
        val mUserPrefernces = UserPreference(this)

        val token: String? = mUserPrefernces.getUser()
        if (token == "") {
            logout(this)
        }
        else {

            val adapter = ListStoryAdapter()
            binding.rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )

            listStoryViewModel.listStory(token!!).observe(this, {
                adapter.submitData(lifecycle, it)
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return selectItem(this, item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}