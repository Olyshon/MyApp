package ru.netology.nmedia.data.impl

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData(
        Post(
            id = 0L,
            author = "Olya",
            content = "hohoho",
            published = "01.01.2022",
            likes = 999,
            reposts = 996,
            views = 1345
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value should not be null"
        }

        val likedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe,
        )
        getLikeCount(likedPost)
        data.value = likedPost
    }

    override fun repost() {
        val currentPost = checkNotNull(data.value) {
            "Data value should not be null"
        }
        val repostedPost = currentPost.copy(
            reposts = currentPost.reposts + 1
        )
        data.value = repostedPost
    }


    private fun getLikeCount(post: Post) =
        if (post.likedByMe) post.likes++
        else post.likes--


}