package com.example.metricssample.controller

import com.example.metricssample.api.PostsApi
import com.example.metricssample.api.model.CreatePostRequest
import com.example.metricssample.api.model.Post
import com.example.metricssample.api.model.UpdatePostRequest
import com.example.metricssample.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PostApiController(private val postService: PostService) : PostsApi {
    override fun createPost(createPostRequest: CreatePostRequest): ResponseEntity<Unit> {
        postService.create(createPostRequest.title, createPostRequest.body)
        return ResponseEntity.ok().build()
    }

    override fun updatePost(
        id: Int,
        updatePostRequest: UpdatePostRequest,
    ): ResponseEntity<Unit> {
        postService.update(id, updatePostRequest.title, updatePostRequest.body)
        return ResponseEntity.ok().build()
    }

    override fun deletePost(id: Int): ResponseEntity<Unit> {
        postService.delete(id)
        return ResponseEntity.ok().build()
    }

    override fun getPostId(id: Int): ResponseEntity<Post> {
        return postService.findById(id)?.let { ResponseEntity.ok(Post(it.id, it.title, it.body)) } ?: ResponseEntity(
            HttpStatus.NOT_FOUND,
        )
    }

    override fun getPost(): ResponseEntity<List<Post>> {
        return ResponseEntity.ok(postService.findAll().map { Post(it.id, it.title, it.body) })
    }
}
