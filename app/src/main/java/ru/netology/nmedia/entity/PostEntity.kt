package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String = "Пусто",
    val authorAvatar: String,
    val content: String,
    val published: String =  SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ROOT).format(Date()),
    val likedByMe: Boolean = false,
    val likedCount: Long = 0,
    val sharedCount: Long = 0,
    val viewedCount: Long = 0,
    val videoURL: String = "",
) {
    fun toDto() = Post(id, author, authorAvatar, published, content, likedByMe,
        likedCount, sharedCount, viewedCount, videoURL )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.authorAvatar, dto.content, dto.published, dto.likedByMe,
                dto.likedCount, dto.sharedCount, dto.viewedCount, dto.videoURL)
    }
}