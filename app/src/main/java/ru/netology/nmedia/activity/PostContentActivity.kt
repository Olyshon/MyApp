package ru.netology.nmedia.activity


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding


class PostContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.edit.text

            if (text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    class ResultContract : ActivityResultContract<Unit, String?>() {
        override fun createIntent(context: Context, input: Unit) =
            Intent(context, PostContentActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?): String? =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)
            } else null

    }

    private companion object {
        private const val RESULT_KEY = "postNewContent"
    }

}