package com.github.jmlb23.movielense.graphql.helper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jmlb23.movielense.graphql.schema.expediaSchema
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes

object Gateway{
    val jackson = jacksonObjectMapper()

    fun start(): HttpHandler = routes(
            "/graphql" bind Method.POST to { req: Request ->
                val res = expediaSchema.execute(req.bodyString())
                val response = res.toSpecification()
                Response(Status.OK).body(jackson.writeValueAsString(response))
            }

    )
}