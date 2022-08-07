package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post


internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostListItemBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menu).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }

                }
            }
        }

        init {
            binding.like.setOnClickListener {
                listener.onLikeClicked(post)
            }

            binding.repost.setOnClickListener {
                listener.onRepostClicked(post)
            }

            binding.menu.setOnClickListener {
                popupMenu.show()
            }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                authorName.text = post.author
                content.text = post.content
                date.text = post.published
                like.text = checkForThousand(post.likes)
                repost.text = checkForThousand(post.reposts)
                viewsCount.text = checkForThousand(post.views)
                like.isChecked = post.likedByMe


            }
        }


        @DrawableRes
        fun getLikeIconResId(post: Post) =
            if (post.likedByMe) R.drawable.ic_liked24
            else R.drawable.ic_like24


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