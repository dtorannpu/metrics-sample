package com.example.metricssample.service

import com.example.metricssample.model.Post
import com.example.metricssample.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostServiceImpl(private val postRepository: PostRepository) : PostService {
    @Transactional
    override fun create(
        title: String,
        body: String,
    ) {
        postRepository.create(title, body)
    }

    @Transactional
    override fun update(
        id: Int,
        title: String,
        body: String,
    ) {
        postRepository.update(id, title, body)
    }

    @Transactional
    override fun delete(id: Int) {
        postRepository.delete(id)
    }

    override fun findById(id: Int): Post? {
        return postRepository.findById(id)
    }

    override fun findAll(): List<Post> {
        return postRepository.findAll()
    }
}
