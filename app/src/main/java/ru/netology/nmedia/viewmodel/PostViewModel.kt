package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryHttpImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

val empty = Post(
    id = 0L,
    author = "",
    authorAvatar = "",
    published = "",
    content = "",
    likedByMe = false,
    videoURL = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryHttpImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll_http()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    fun likeById(id: Long) {
        thread {
            try {
                val post = repository.likeById_http(id)

                val posts = _data.value?.posts.orEmpty().map { postnew ->
                    if (postnew.id == id) {
                        postnew.copy(
                            likedByMe = post.likedByMe,
                            likedCount = post.likedCount
                        )
                    } else {
                        postnew
                    }
                }
                FeedModel(posts = posts)
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also(_data::postValue)

        }
    }

    fun shareById(id: Long) {
        thread { repository.shareById(id) }
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            val posts = old
                .filter { it.id != id }
            _data.postValue(
                _data.value?.copy(
                    posts = posts, empty = posts.isEmpty()
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = content)
    }
}