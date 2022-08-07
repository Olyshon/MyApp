package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post


class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }


    override val data = MutableLiveData(
        List(GENERATED_POSTS_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Какой-то автор",
                content = "Тестовый пост №$index",
                published = "21 мая в 22:12",
                likes = 999,
                reposts = 996,
                views = 1345,
                likedByMe = false
            )
        }
    )

    override fun like(postId: Long) {
        data.value = posts.map { post ->
            if (post.id == postId) {
                post.copy(likedByMe = !post.likedByMe, likes = getLikeCount(post))
            } else post
        }
        data.value = posts

    }

    override fun repost(postId: Long) {
        data.value = posts.map { post ->
            if (post.id == postId) {
                post.copy(reposts = post.reposts + 1)
            } else post
        }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post)
        else update(post)
    }

    private fun insert(post: Post) {
        data.value = listOf(
            post.copy(
                id = ++nextId
            )
        ) + posts
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    override fun delete(postId: Long) {
        data.value = posts.filter { it.id != postId }
    }


    private fun getLikeCount(post: Post) =
        if (!post.likedByMe) post.likes + 1
        else post.likes - 1


    private companion object {
        const val GENERATED_POSTS_AMOUNT = 1000
    }

}