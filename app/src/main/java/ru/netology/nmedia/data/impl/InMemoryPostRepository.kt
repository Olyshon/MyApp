package ru.netology.nmedia.data.impl

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post


class InMemoryPostRepository : PostRepository {

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val initialPosts = listOf(
            Post(
                id = 0L,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Тестовый пост номер 1",
                published = "21 мая в 22:12",
                likes = 999,
                reposts = 996,
                views = 1345,
                likedByMe = false
            ),
            Post(
                id = 1L,
                author = "Ольга Бушмина",
                content = "Тестовый пост номер 2",
                published = "23 мая в 12:00",
                likes = 99,
                reposts = 199,
                views = 45,
                likedByMe = false
            ),
            Post(
                id = 2L,
                author = "Вася Пупкин",
                content = "Тестовый пост номер 3",
                published = "25 мая в 2:12",
                likes = 9,
                reposts = 12,
                views = 13,
                likedByMe = true
            )
        )
        data = MutableLiveData(initialPosts)
    }

    override fun like(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) {
                post.copy(likedByMe = !post.likedByMe, likes = getLikeCount(post))
            }
            else post
        }
        data.value = posts

    }

    override fun repost(postId: Long) {
          posts = posts.map { post ->
            if (post.id == postId) {
                post.copy(reposts = post.reposts + 1)
            }
            else post
        }
        data.value = posts
    }


    private fun getLikeCount(post: Post) =
        if (!post.likedByMe) post.likes + 1
        else post.likes - 1

}