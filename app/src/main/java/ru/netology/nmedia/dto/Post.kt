package ru.netology.nmedia.dto

data class Post(
    val id : Long = 0L,
    val author : String = "",
    val authorAvatar : String = "",
    val published : String,
    val content : String = "",
    val likedByMe : Boolean = false,
    val likedCount : Long = 0L,
    val sharedCount : Long = 0L,
    val viewedCount : Long = 0L,
    val videoURL : String = ""
)
