package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.data.observe(this) { post -> binding.render(post) }


        binding.repost.setOnClickListener {
            vm.onRepostClicked()

        }

        binding.like.setOnClickListener {
            vm.onLikeClicked()

        }

    }

    private fun ActivityMainBinding.render(post: Post) {
        authorName.text = post.author
        content.text = post.content
        date.text = post.published
        likeCount.text = checkForThousand(post.likes)
        repostCount.text = checkForThousand(post.reposts)
        viewsCount.text = checkForThousand(post.views)
        like.setImageResource(getLikeIconResId(post))
    }


    @DrawableRes
    fun getLikeIconResId(post: Post) =
        if (post.likedByMe) R.drawable.ic_outline_favorite_24
        else R.drawable.ic_sharp_favorite_border_24


    private fun checkForThousand(count: Int) = when {
        count in 1000..9999 && ((count / 100) % 10 == 0) -> (count / 1000).toString() + "K"
        count in 1000..9999 && ((count / 100) % 10 != 0) -> (kotlin.math.floor((count / 1000.0) * 10) / 10).toString() + "K"
        count in 10000..999999 -> (count / 1000).toString() + "K"
        count >= 1_000_000 && ((count / 100000) % 10 == 0) -> (count / 1_000_000).toString() + "M"
        count >= 1_000_000 && ((count / 100000) % 10 != 0) -> (kotlin.math.floor((count / 1_000_000.0) * 10) / 10).toString() + "M"
        else -> count.toString()
    }
}

