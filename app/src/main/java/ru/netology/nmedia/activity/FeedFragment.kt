package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R

import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.activity.OnePostFragment.Companion.idArg
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment) // ищем контроллер навигации и у него вызываем метод navigate, передаем ему id перехода
        }

        viewModel.editEvent.observe(viewLifecycleOwner) { postContent ->
            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply { textArg = postContent })
        }

        viewModel.onePostEvent.observe(viewLifecycleOwner) { post ->
            findNavController().navigate(
                R.id.action_feedFragment_to_onePostFragment,
                Bundle().apply { idArg = post.id })
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


}



