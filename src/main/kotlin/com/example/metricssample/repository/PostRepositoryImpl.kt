package com.example.metricssample.repository

import com.example.bookmanagement.db.jooq.gen.tables.references.POSTS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(private val dslContext: DSLContext) : PostRepository {
    override fun create(
        title: String,
        body: String,
    ): Int {
        val post = dslContext.newRecord(POSTS)
        post.title = title
        post.body = body
        post.store()
        return post.id!!
    }

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun delete() {
        TODO("Not yet implemented")
    }
}
