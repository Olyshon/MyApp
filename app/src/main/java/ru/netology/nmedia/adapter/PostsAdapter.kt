package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post

class PostsAdapter(
    private val onLikeClicked: (Post) -> Unit,
    private val onRepostClicked: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, onLikeClicked, onRepostClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostListItemBinding,
        private val onLikeClicked: (Post) -> Unit,
        private val onRepostClicked: (Post) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.like.setOnClickListener {
                onLikeClicked(post)
            }

            binding.repost.setOnClickListener {
                onRepostClicked(post)
            }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                authorName.text = post.author
                content.text = post.content
                date.text = post.published
                likeCount.text = checkForThousand(post.likes)
                repostCount.text = checkForThousand(post.reposts)
                viewsCount.text = checkForThousand(post.views)
                like.setImageResource(getLikeIconResId(post))

                repost.setOnClickListener {
                    onRepostClicked(post)
                }

                like.setOnClickListener {
                    onLikeClicked(post)
                }
            }
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


    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }

}