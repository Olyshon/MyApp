package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.impl.SharedPrefPostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application), PostInteractionListener {

   // private val repository: PostRepository = InMemoryPostRepository()
   // private val repository: PostRepository = SharedPrefPostRepository(application)
   private val repository: PostRepository = FilePostRepository(application)

    val data by repository::data

    val shareEvent = SingleLiveEvent<String>()
    val editEvent = SingleLiveEvent<String>()
    val playVideoEvent = SingleLiveEvent<String?>()
    val onePostEvent = SingleLiveEvent<Post>()

    private val currentPost = MutableLiveData<Post?>(null)

    fun onAddButtonClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "кто-то",
            content = content,
            published = "01.01.2021",
            video = "https://www.youtube.com/watch?v=yrTtU9TXI9M"

        )
        repository.save(post)
        currentPost.value = null
    }

    //region PostInteractionListener
    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        shareEvent.value = post.content
    }

    override fun onRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        editEvent.value = post.content
    }

    override fun onPlayVideoClicked(post: Post) {
        currentPost.value = post
        playVideoEvent.value = post.video
    }

    override fun onCancelEditingClicked() {
        currentPost.value = null
        return
    }

    override fun onPostClicked(post: Post) {
        currentPost.value = post
        onePostEvent.value = post
    }


    //endregion PostInteractionListener

}