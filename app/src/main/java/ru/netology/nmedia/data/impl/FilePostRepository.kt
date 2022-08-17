package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import android.provider.Settings.Global.putString
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import kotlin.properties.Delegates


class FilePostRepository(
    private val application: Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type



    private val prefs = application.getSharedPreferences(  //получили референсы в репозитории
        "repo", Context.MODE_PRIVATE
    )

    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }


    private var posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            application.openFileOutput(
                FILE_NAME, Context.MODE_PRIVATE
            ).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val postsFile = application.filesDir.resolve((FILE_NAME))
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use { gson.fromJson(it, type) }
        } else emptyList()

        data = MutableLiveData(posts)

    }

    override fun like(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) {
                post.copy(likedByMe = !post.likedByMe, likes = getLikeCount(post))
            } else post
        }
        posts = posts

    }

    override fun share(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) {
                post.copy(reposts = post.reposts + 1)
            } else post
        }
        posts = posts
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post)
        else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(
                id = ++nextId
            )
        ) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filter { it.id != postId }
    }


    private fun getLikeCount(post: Post) =
        if (!post.likedByMe) post.likes + 1
        else post.likes - 1


    private companion object {
        const val POSTS_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "nextId"
        const val FILE_NAME = "posts.json"
    }

}