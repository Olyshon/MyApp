package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    val shareEvent = SingleLiveEvent<String>()
    val editEvent = SingleLiveEvent<String>()
    val playVideoEvent = SingleLiveEvent<String?>()

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
            video = null

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


    //endregion PostInteractionListener

}