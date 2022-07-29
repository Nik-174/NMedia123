package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory : PostRepository {

    private var posts = listOf(
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
    )

    private val data = MutableLiveData(posts)

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
    }

    override fun removeById(id: Long) {
        posts = posts.filter {
            it.id != id
        }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val newId = posts.firstOrNull()?.id ?: post.id
            posts = listOf(
                post.copy(id = newId + 1)
            ) + posts
            data.value = posts
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
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun getAll_http(): List<Post> {
        TODO("Not yet implemented")
    }
}