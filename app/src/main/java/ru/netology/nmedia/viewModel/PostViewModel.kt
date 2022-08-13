package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.currentCoroutineContext
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()

    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "кто-то",
            content = content,
            published = "01.01.2021"
        )
        repository.save(post)
        currentPost.value = null
    }

    //region PostInteractionListener
    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }

    override fun onRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }

    override fun onCancelEditingClicked() {
        currentPost.value = null
        return
    }
//
//    override fun onCreateNewPost(post: Post) {
//        currentPost.value = post
//    }

    //endregion PostInteractionListener

}