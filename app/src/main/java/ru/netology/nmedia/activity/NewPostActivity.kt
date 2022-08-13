package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edit.requestFocus() //запрашиваем фокус на поле для редактирования
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding.edit.text?.toString())
        }
    }

    private fun onOkButtonClicked(postContent: String?) {
        val intent = Intent() //результирующий интент

        if (postContent.isNullOrBlank()) {
            setResult(Activity.RESULT_CANCELED, intent)
        } else {
            intent.putExtra(POST_CONTENT_EXTRA_KEY, postContent)
            setResult(Activity.RESULT_OK, intent)

        }
        finish() //завершаем активити
    }

    private companion object {
        private const val POST_CONTENT_EXTRA_KEY = "postContent"
    }

    object ResultContract: ActivityResultContract<Unit, String?>() {  //поправить входной параметр на string?
        override fun createIntent(context: Context, input: Unit) =
            Intent(context, NewPostActivity::class.java)   //сформировали явный интент для запуска активити

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            if (resultCode != Activity.RESULT_OK) return null
            intent ?: return null
            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
        }



    }
}