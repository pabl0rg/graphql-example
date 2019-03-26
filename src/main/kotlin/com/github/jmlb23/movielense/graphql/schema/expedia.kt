package com.github.jmlb23.movielense.graphql.schema

import com.expedia.graphql.SchemaGeneratorConfig
import com.expedia.graphql.TopLevelObject
import com.expedia.graphql.toSchema
import com.github.jmlb23.movielense.MovieLensService
import graphql.schema.GraphQLSchema

class MovieLensQueries(private val movieLensService: MovieLensService) {
    fun allUsers() = movieLensService.getAllUsers()
    fun user(id: Int) = movieLensService.getUser(id)

    val allRatings = movieLensService::getAllRates
    val rating = movieLensService::getRate

    val allGenres = movieLensService::getAllGenres
    val genre = movieLensService::getGenre

    val allOccupations = movieLensService::getAllOccupations
    val occupation = movieLensService::getOccupation
}

class MovieLensMutations(private val movieLensService: MovieLensService) {
    fun createUser(age: Int, gender: String, occupationId: Long, zipCode: String) =
            movieLensService.createUser(age, gender, occupationId, zipCode)

    fun deleteUser(id: Long) = movieLensService.deleteUser(id)
    fun updateUser(id: Long, age: Int, gender: String, occupationId: Long, zipCode: String) = movieLensService

    val createRating = movieLensService::createRating
    val deleteRating = movieLensService::deleteRating
    val updateRating = movieLensService::updateRating

    val createGenre = movieLensService::createGenre
    val deleteGenre = movieLensService::deleteGenre
    val updateGenre = movieLensService::updateGenre

    val createOccupation = movieLensService::createOccupation
    val deleteOccupation = movieLensService::deleteOccupation
    val updateOccupation = movieLensService::updateOccupation
}

fun initGraphQLSchema(movieLensService: MovieLensService): GraphQLSchema {

    val config = SchemaGeneratorConfig(supportedPackages = listOf(
            "com.github.jmlb23.movielense.domain",
            "com.github.jmlb23.movielense"
    ))

    val queries = listOf(TopLevelObject(MovieLensQueries(movieLensService)))
    val mutations = listOf(TopLevelObject(MovieLensMutations(movieLensService)))

    return toSchema(config, queries, mutations)
}
