package ru.netology.nmedia.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface ActionListener {
    fun onLikeClick(post: Post)
    fun onShareClick(post: Post)
    fun onRemoveClick(post: Post)
    fun onEditClick(post: Post)
    fun onPlayMedia(post: Post)
    fun onPreviewPost(post: Post)
}

fun convertCount2String(count: Long): String {
    val result = when {
        count < 1_000 -> count.toString()
        count in 1_000..1_099 -> "1K"
        count in 1_100..9_999 -> String.format("%.1fK", (count / 100).toDouble() / 10)
        count in 10_000..999_999 -> (count / 1000).toString() + "K"
        count in 1_000_000..1_999_999 -> "1M"
        else -> String.format("%.1fМ", (count / 100_000).toDouble() / 10)
    }
    return result
}

class PostAdaptor(
    private val actionListener: ActionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            binding = CardPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            actionListener
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val actionListener: ActionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        with(binding) {
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

            liked.setOnClickListener {
                actionListener.onLikeClick(post)
            }
            shared.setOnClickListener {
                actionListener.onShareClick(post)
            }
            menu.setOnClickListener {
                PopupMenu(binding.root.context, binding.menu).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.remove -> {
                                actionListener.onRemoveClick(post)
                                true
                            }
                            R.id.edit -> {
                                actionListener.onEditClick(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            playButton.setOnClickListener {
                actionListener.onPlayMedia(post)
            }

            videoPreview.setOnClickListener {
                actionListener.onPlayMedia(post)
            }

            content.setOnClickListener {
                actionListener.onPreviewPost(post)
            }
        }
    }

}

class PostDiffItemCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem
}