package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun getAll_http(): List<Post>
    fun likeById(id : Long)
    fun likeById_http(id : Long) : Post
    fun shareById(id : Long)
    fun removeById(id : Long)
    fun save(post: Post)
}