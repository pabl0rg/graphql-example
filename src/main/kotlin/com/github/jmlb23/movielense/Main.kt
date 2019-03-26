package com.github.jmlb23.movielense

import com.github.jmlb23.movielense.graphql.helper.Gateway
import com.github.jmlb23.movielense.graphql.schema.initGraphQLSchema
import com.github.jmlb23.movielense.repositories.*
import graphql.GraphQL
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main(args: Array<String>) {

    val movieLensService = MovieLensService(MovieRepository, GenresRepository, OccupationRepository, RatingRepository, UserRepository)
    val schema = initGraphQLSchema(movieLensService)
    val graphQL = GraphQL.newGraphQL(schema).build()

    Gateway.start(graphQL).asServer(Jetty(8080)).start()
}
