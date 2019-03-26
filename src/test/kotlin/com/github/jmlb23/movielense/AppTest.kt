package com.github.jmlb23.movielense

import com.beust.klaxon.Klaxon
import com.github.jmlb23.movielense.graphql.schema.initGraphQLSchema
import graphql.GraphQL
import graphql.schema.idl.SchemaPrinter
import org.junit.Assert
import kotlin.test.Test
import kotlin.test.assertTrue

class AppTest {

    val movieLensService = MovieLensService(
            MockRepository(),
            MockRepository(),
            MockRepository(),
            MockRepository(),
            MockRepository()
    )
    val expediaSchema = initGraphQLSchema(movieLensService)
    val expediaGraphQL = GraphQL.newGraphQL(expediaSchema).build()
    val schemaTxt = SchemaPrinter().print(expediaSchema)

    init {
        movieLensService.createUser(10, "M", 1, "22331")
        movieLensService.createUser(40, "F", 1, "22331")

        movieLensService.createRating(0, 1, 3)
    }

    @Test
    fun `schema should have ratings queries`() {
        assertTrue(expediaSchema.queryType.children.any { it.name == "allRatings" })
    }

    @Test
    fun testQueryAllUsers() {
        println(expediaGraphQL.execute("""query {allUsers{
                        id
                        age
                        gender
                        }
            }
                """.trimIndent()))
        println(expediaGraphQL.execute("""query {user(id: 1){
                        id
                        age
                        gender
                        }
            }
                """.trimIndent()))
    }

    @Test
    fun testMutationOnUser() {
        println(expediaGraphQL.execute("""mutation{
                createUser(age: 30, gender: "M", occupationId: 5, zipCode: "15704") {
                    id
                }
            }
            """.trimIndent()
        ))
        println(expediaGraphQL.execute("""mutation{
                deleteUser(id: 2)
            }

            """.trimIndent()
        ))
    }

    @Test
    fun testMutationOnRating() {
        data class Element(val id: Int)
        data class Data(val allRates: List<Element>)
        data class Obj(val data: Data)

        data class Data2(val createRate: Int)
        data class Obj2(val data: Data2)

        val idJson = expediaGraphQL.execute("""
                    mutation{
                        createRate(userId: 18, movieId: 1, rating: 5)
                    }
                """.trimIndent())

        val it = expediaGraphQL.execute("""
            query {
                allRatings {
                    id
                }
            }""")

        println(it.toString())
        val list = Klaxon().parse<Obj>(it.toString())
        println(idJson.toString())
        val id = Klaxon().parse<Obj2>(idJson.toString())

        val last = list?.data?.allRates?.last()

        Assert.assertTrue(id?.data?.createRate == last?.id)


    }

    @Test
    fun testMutationOnUserForDelete(){
        data class Data(val getUser: String? = null)
        data class Response(val data: Data?)

        expediaGraphQL.execute("mutation {deleteUser(id: 944)}")
        Assert.assertNull(
        Klaxon().parse<Response>(expediaGraphQL.execute(
                """query{
                        |getUser(id: 944){
                            | id
                            | gender
                        |}
                    |}""".trimMargin()).toString())?.data?.getUser)
    }

    @Test
    fun testMutationOnUserForUpdate(){
        data class User(val id: Int, val age: Int, val gender: String, val occupationId: Long, val zipCode: String)
        data class Data(val getUser: User)
        data class Response(val data: Data)
        val id = 942
        val updateObject = User(id, 25, "M", 4, "14500")

        val response = expediaGraphQL.execute("mutation{updateUser(id: $id, age: ${updateObject.age}, gender: \"${updateObject.gender}\", occupationId: ${updateObject.occupationId}, zipCode: \"${updateObject.zipCode}\" )}")

        val ResponseObj = Klaxon().parse<Response>(expediaGraphQL.execute("""query{getUser(id: $id){
            |id
            |age
            |gender
            |occupationId
            |zipCode
            |}}""".trimMargin()).toString())
        Assert.assertEquals(ResponseObj!!.data.getUser,updateObject)

    }
}
