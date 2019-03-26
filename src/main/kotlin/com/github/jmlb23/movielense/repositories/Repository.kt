package com.github.jmlb23.movielense.repositories


interface Repository<D>{
    fun add(element: D): Long
    fun getAll(): Sequence<D>
    fun remove(indexer: Long): Long
    fun replace(indexer: Long, element: D): Long
    fun getElement(indexer: Long): D?
}