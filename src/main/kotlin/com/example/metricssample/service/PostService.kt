package com.example.metricssample.service

import com.example.metricssample.model.Post

interface PostService {
    fun create(
        title: String,
        body: String,
    )

    fun update(
        id: Int,
        title: String,
        body: String,
    )

    fun delete(id: Int)

    fun findById(id: Int): Post?

    fun findAll(): List<Post>
}
