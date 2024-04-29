package com.example.metricssample.repository

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
}
