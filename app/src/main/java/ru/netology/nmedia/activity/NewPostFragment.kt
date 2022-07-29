package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.CompanionArg.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel


class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(layoutInflater)

        arguments?.textArg?.let {
            binding.content.setText(it)
        }
        binding.content.requestFocus()

        val viewModel: PostViewModel by viewModels(::requireParentFragment)

        binding.layoutFabSave.visibility = View.INVISIBLE
        binding.layoutFabCancel.visibility = View.INVISIBLE
        binding.saveButton.setOnClickListener {

            if (binding.layoutFabSave.visibility == View.VISIBLE) {
                binding.layoutFabSave.visibility = View.INVISIBLE
                binding.layoutFabCancel.visibility = View.INVISIBLE
            } else {
                binding.layoutFabSave.visibility = View.VISIBLE
                binding.layoutFabCancel.visibility = View.VISIBLE
            }
        }

        binding.fabSave.setOnClickListener {
            if (!binding.content.text.isNullOrBlank()) {
                val content = binding.content.text.toString()
                viewModel.changeContent(content)
                viewModel.save()
            }
            binding.layoutFabSave.visibility = View.INVISIBLE
            binding.layoutFabCancel.visibility = View.INVISIBLE
        }

        binding.fabCancel.setOnClickListener {
            binding.layoutFabSave.visibility = View.INVISIBLE
            binding.layoutFabCancel.visibility = View.INVISIBLE
            findNavController().navigateUp()
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }



}