package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(vm)
        binding.postsRecyclerView.adapter = adapter
        vm.data.observe(this) { posts ->
            adapter.submitList(posts)
        }


        vm.shareEvent.observe(this) { postContent ->
            val intent = Intent().apply { //устанавливаем поля интенту далее
                action = Intent.ACTION_SEND
                type = "text/plain"  // можно поставить text/*  - те текст неизвестно какого типа
                putExtra(
                    Intent.EXTRA_TEXT,
                    postContent
                ) // кладем контент пос та :первый параметр - ключ,второй -значение
                //  аналогично записи  extras?.putString(Intent.EXTRA_TEXT,postContent)
            }

            val shareIntent =
                Intent.createChooser(  //интент оборачиваем в другой интент, чтобы применить красивую выбирашку
                    intent, getString(R.string.chooser_share_post)
                )
            startActivity(shareIntent)
        }


        val activityLauncher =
            registerForActivityResult( //регистрируемся на результат выполнения активити NewPostActivity
                NewPostActivity.ResultContract
            ) { postContent: String? ->
                postContent?.let(vm::onAddButtonClicked)
            }


        vm.editEvent.observe(this) { postContent ->
            activityLauncher.launch(postContent)
        }

        binding.addButton.setOnClickListener {
            activityLauncher.launch(null)
        }

    }


}

