package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    var likes: Int = 0,
    var reposts: Int = 0,
    var views: Int = 0,
    val video: String?
)