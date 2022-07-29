package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFileImpl(val context: Context) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var posts = emptyList<Post>()

    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }
            sync()
        }
    }

 //   override fun getAll(): LiveData<List<Post>> = data

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
/*        posts = listOf(
            Post(
                id = 3,
                author = "Нетология. Пост 3",
                published = "21 мая в 18:36",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                likedCount = 1999,
                sharedCount = 0,
                viewedCount = 2678,
                videoURL = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
            ),
            Post(
                id = 2,
                author = "Нетология. Пост 2",
                published = "22 мая в 18:36",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                likedCount = 999,
                sharedCount = 9998,
                viewedCount = 13457,
                videoURL = "",
            ),
            Post(
                id = 1,
                author = "Нетология. Пост 1",
                published = "22 мая в 18:36",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                likedCount = 999,
                sharedCount = 9998,
                viewedCount = 13457,
                videoURL = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
            )
        )*/
        context.openFileOutput(filename,Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

}