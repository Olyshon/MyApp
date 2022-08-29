package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    reposts = reposts,
    views= views,
    video = video

)
internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    reposts = reposts,
    views = views,
    video = video

)