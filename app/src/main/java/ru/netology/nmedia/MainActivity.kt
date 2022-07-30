package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vm by viewModels<PostViewModel>()

        val adapter = PostsAdapter(
            onLikeClicked = { post -> vm.onLikeClicked(post) },
            onRepostClicked = { post -> vm.onRepostClicked(post) }
        )

        binding.postsRecyclerView.adapter = adapter
        vm.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }


}

