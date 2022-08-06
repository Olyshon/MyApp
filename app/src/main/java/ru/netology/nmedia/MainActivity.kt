package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(vm)
        binding.postsRecyclerView.adapter = adapter
        vm.data.observe(this) { posts ->
            adapter.submitList(posts)
        }


        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                vm.onSaveButtonClicked(content)
                binding.groupCancelEditing.visibility = View.GONE
                clearFocus()
                hideKeyboard()
            }
        }

        binding.cancelEditButton.setOnClickListener {
            with(binding.contentEditText) {
                vm.onCancelEditingClicked()
                binding.groupCancelEditing.visibility = View.GONE
                clearFocus()
                hideKeyboard()
            }
        }
        vm.currentPost.observe(this) { currentPost ->
            binding.contentEditText.setText(currentPost?.content)
            binding.cancelEditText.setText(currentPost?.content)
            binding.groupCancelEditing.visibility = View.VISIBLE
        }
    }


}

