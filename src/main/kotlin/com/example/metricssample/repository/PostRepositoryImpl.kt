package com.example.metricssample.repository

import com.example.metricssample.db.jooq.gen.tables.references.POST
import com.example.metricssample.model.Post
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(
    private val dslContext: DSLContext,
) : PostRepository {
    override fun create(
        title: String,
        body: String,
    ): Int {
        val post = dslContext.newRecord(POST)
        post.title = title
        post.body = body
        post.store()
        return post.id!!
    }

    override fun update(
        id: Int,
        title: String,
        body: String,
    ): Int =
        dslContext
            .update(POST)
            .set(POST.TITLE, title)
            .set(POST.BODY, body)
            .where(POST.ID.eq(id))
            .execute()

    override fun delete(id: Int): Int =
        dslContext
            .delete(POST)
            .where(POST.ID.eq(id))
            .execute()

    override fun findById(id: Int): Post? =
        dslContext
            .selectFrom(POST)
            .where(POST.ID.eq(id))
            .fetchOne()
            ?.let { Post(it.id!!, it.title, it.body) }

    override fun findAll(): List<Post> =
        dslContext
            .selectFrom(POST)
            .orderBy(POST.ID)
            .fetch()
            .map { Post(it.id!!, it.title, it.body) }
}
