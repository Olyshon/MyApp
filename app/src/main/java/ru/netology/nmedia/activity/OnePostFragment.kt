package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.databinding.FragmentOnePostBinding
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel
import java.util.Locale.filter

class OnePostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val binding = FragmentOnePostBinding.inflate(inflater, container, false)
        val viewHolder = PostsAdapter.ViewHolder(binding.onePostLayout, viewModel)

        val curId = arguments?.idArg
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == curId } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(post)
        }

        viewModel.editEvent.observe(viewLifecycleOwner) { postContent ->
            findNavController().navigate(
                R.id.action_onePostFragment_to_newPostFragment,
                Bundle().apply { textArg = postContent })
        }
        viewModel.playVideoEvent.observe(viewLifecycleOwner) { videoURL ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
            val videoIntent =
                Intent.createChooser(
                    intent, getString(R.string.chooser_play_video)
                )
            startActivity(videoIntent)
        }
        viewModel.shareEvent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(    //  аналогично записи  extras?.putString(Intent.EXTRA_TEXT,postContent)
                    Intent.EXTRA_TEXT,
                    postContent
                )
            }

            val shareIntent =
                Intent.createChooser(
                    intent, getString(R.string.chooser_share_post)
                )
            startActivity(shareIntent)
        }

        return binding.root

    }

    companion object {
        var Bundle.textArg: String? by StringArg
        var Bundle.idArg: Long? by LongArg
    }


}