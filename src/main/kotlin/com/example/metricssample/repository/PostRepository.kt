package com.example.metricssample.repository

import com.example.metricssample.model.Post

interface PostRepository {
    fun create(
        title: String,
        body: String,
    ): Int

    fun update(
        id: Int,
        title: String,
        body: String,
    ): Int

    fun delete(id: Int): Int

    fun findById(id: Int): Post?
}
