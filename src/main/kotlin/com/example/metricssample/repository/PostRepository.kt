package com.example.metricssample.repository

interface PostRepository {
    fun create(
        title: String,
        body: String,
    ): Int

    fun update()

    fun delete(id: Int): Int
}
