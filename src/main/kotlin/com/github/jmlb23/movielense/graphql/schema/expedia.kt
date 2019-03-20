package com.github.jmlb23.movielense.graphql.schema

import com.expedia.graphql.SchemaGeneratorConfig
import com.expedia.graphql.TopLevelObject
import com.expedia.graphql.toSchema
import com.github.jmlb23.movielense.MovieLensService
import com.github.jmlb23.movielense.repositories.*
import graphql.GraphQL

val config = SchemaGeneratorConfig(supportedPackages = listOf("com.github.jmlb23.movielense.domain"))

val movieLensService = MovieLensService(MovieRepository, GenresRepository, OccupationRepository, RatingRepository, UserRepository)

class MovieLensQueries(private val movieLensService: MovieLensService) {
    fun allUsers() = movieLensService.getAllUsers()
    fun user(id: Int) = movieLensService.getUser(id)
}

class MovieLensMutations(private val movieLensService: MovieLensService) {
    fun createUser(age: Int, gender: String, occupationId: Long, zipCode: String) =
            movieLensService.createUser(age, gender, occupationId, zipCode)
    fun deleteUser(id: Long) = movieLensService.deleteUser(id)
    fun updateUser(id: Long, age: Int, gender: String, occupationId: Long, zipCode: String) = movieLensService
}

val queries = listOf(TopLevelObject(MovieLensQueries(movieLensService)))
val mutations = listOf(TopLevelObject(MovieLensMutations(movieLensService)))

val expediaSchema = GraphQL.newGraphQL(toSchema(config, queries, mutations)).build()