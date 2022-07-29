package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adaptor.convertCount2String
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.CompanionArg.Companion.longArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(layoutInflater)

        val viewModel: PostViewModel by viewModels(::requireParentFragment)

        with(binding.postContent) {
            viewModel.data.observe(viewLifecycleOwner) { posts ->
                val post = posts.posts
                    .find { it.id == arguments?.longArg }
                if (post != null) {
                    author.text = post.author
                    published.text = post.published
                    content.text = post.content
                    shared.text = convertCount2String(post.sharedCount)
                    viewed.text = convertCount2String(post.viewedCount)
                    liked.isChecked = post.likedByMe
                    liked.text = convertCount2String(post.likedCount)

                    if (post.videoURL != "") {
                        videoPreview.setImageResource(R.mipmap.ic_video_preview_foreground)
                        playButton.visibility = View.VISIBLE
                    } else {
                        playButton.visibility = View.INVISIBLE
                    }
                }
            }
        }

        return binding.root
    }
}