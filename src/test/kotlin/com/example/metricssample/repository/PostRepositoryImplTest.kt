package com.example.metricssample.repository

import com.example.bookmanagement.db.jooq.gen.tables.references.POST
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional

@JooqTest
@Transactional
@Import(PostRepositoryImpl::class)
class PostRepositoryImplTest
    @Autowired
    constructor(
        private val create: DSLContext,
        private val postRepository: PostRepositoryImpl,
    ) : RepositorySpec({
            test("Create") {
                val result = postRepository.create("タイトル", "内容")

                val post = create.fetchOne(POST, POST.ID.eq(result))

                post.shouldNotBeNull()
                post.title shouldBe "タイトル"
                post.body shouldBe "内容"
            }

            test("Update") {
                val post = create.newRecord(POST)
                post.title = "タイトル"
                post.body = "内容"
                post.store()
                val id = post.id!!

                val updateCount = postRepository.update(id, "変更タイトル", "変更内容")
                updateCount shouldBe 1

                val updatePost =
                    create.selectFrom(POST)
                        .where(POST.ID.eq(id))
                        .fetchOne()

                updatePost.shouldNotBeNull()

                updatePost.title shouldBe "変更タイトル"
                updatePost.body shouldBe "変更内容"
            }

            test("Delete") {
                val post = create.newRecord(POST)
                post.title = "タイトル"
                post.body = "内容"
                post.store()
                val id = post.id!!

                val result = postRepository.delete(id)

                result shouldBe 1
            }
        })
