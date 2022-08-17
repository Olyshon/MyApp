package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.ActivityShareBinding

class ShareActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent ?: return // если интента по какой-то причине нет - возвращаемся из активити
        if (intent.action != Intent.ACTION_SEND) return // если тип action не sand, то возвращаемся в активити

        val text = intent.getStringExtra(Intent.EXTRA_TEXT) //проверяем по тому же ключу, что отправляли в MainActivity
        if (text.isNullOrBlank()) {
            Snackbar.make(binding.root, "присланный текст пустой", Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok) {
                    finish()  //прилепили кнопку Ок для закрытия, чтобы снэкбар не висел вечно
                }.show() // показываем снэкбар
        } else {
            binding.shareContent.text = text
        }

        binding.okShare.setOnClickListener {
            finish()
        }
    }

}
