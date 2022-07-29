package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {

    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val key = "posts"
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private var posts = emptyList<Post>()

    private val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let{
            posts = gson.fromJson(it, type)
            data.value = posts
        }
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun getAll_http(): List<Post> {
        TODO("Not yet implemented")
    }

    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likedCount = if (post.likedByMe) post.likedCount - 1 else post.likedCount + 1
                )
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }

    override fun likeById_http(id: Long): Post {
        TODO("Not yet implemented")
    }

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    sharedCount = post.sharedCount + 1
                )
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter {
            it.id != id
        }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val newId = posts.firstOrNull()?.id ?: post.id
            posts = listOf(
                post.copy(id = newId + 1)
            ) + posts
            data.value = posts
            sync()
            return
        }

        posts = posts.map {
            if (it.id != post.id) {
                it
            } else {
                it.copy(content = post.content)
            }
        }
        data.value = posts
        sync()
    }

    private fun sync(){
         with(prefs.edit()){
            putString(key, gson.toJson(posts))
            apply()
        }
    }

}