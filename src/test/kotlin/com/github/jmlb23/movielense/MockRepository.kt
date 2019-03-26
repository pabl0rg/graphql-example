package com.github.jmlb23.movielense

import com.github.jmlb23.movielense.repositories.Repository

class MockRepository<T>: Repository<T> {
    private val repo = mutableMapOf<Long, T>()

    override fun add(element: T): Long {
        val newId = repo.size.toLong()
        repo[newId] = element
        return newId
    }

    override fun getAll(): Sequence<T> = repo.values.asSequence()


    override fun remove(indexer: Long): Long = if (repo.remove(indexer) != null) 1L else 0L

    override fun replace(indexer: Long, element: T): Long = if (repo.replace(indexer, element) != null) 1L else 0L

    override fun getElement(indexer: Long): T? = repo[indexer]

}