package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewModel.PostViewModel

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment) // делаем общую viewModel для двух фрагментов.

//          val text = arguments?.textArg
//          text?.let {binding.edit.setText(it)}
//          равнозначно:
//       arguments?.textArg?.let(binding.edit::setText)


        arguments?.textArg?.let(binding.edit::setText) // проставляем прищедшие данные postContent в поле редактирования

        binding.edit.requestFocus() //запрашиваем фокус на поле для редактирования

        binding.ok.setOnClickListener {
            val text = binding.edit.text
            if (!text.isNullOrBlank()) {
                val content = text.toString()
                viewModel.onAddButtonClicked(content)
            }
            findNavController().navigateUp() // закрываем фрагмент
        }
        return binding.root
    }



//    Версия максима
//    private fun onOkButtonClicked(binding: FragmentNewPostBinding) {  //не уверена
//
//        val text = binding.edit.text
//
//        if (!text.isNullOrBlank()) {
//            val resultBundle = Bundle(1)
//            resultBundle.putString(RESULT_KEY, text.toString())
//            setFragmentResult(REQUEST_KEY, resultBundle)
//
//        }
//        parentFragmentManager.popBackStack()
//    }


companion object { //формируем ключ для интента
    const val REQUEST_KEY = "requestKey"
    const val RESULT_KEY = "postNewContent"

    var Bundle.textArg: String? by StringArg
}


}