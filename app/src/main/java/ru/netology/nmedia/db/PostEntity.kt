package ru.netology.nmedia.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts") //лучше явно прописывать названия таблиц и полей
class PostEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    @ColumnInfo(name = "liked_by_me")
    val likedByMe: Boolean,
    val likes: Int = 0,
    var reposts: Int = 0,
    var views: Int = 0,
    val video: String?
)