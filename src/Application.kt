package com.example

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.repository.DatabaseFactory
import com.example.repository.Repo
import com.example.routes.RecipeRoutes
import com.example.routes.UserRoutes
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseFactory.init()
    val db = Repo()
    val jwtService = JwtService()
    val hashFunction = { s:String -> hash(s)}


    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {

        jwt("jwt") {
            verifier(jwtService.varifier)
            realm = "Donut Server"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUserByEmail(email)
                user
            }

        }

    }

    install(ContentNegotiation) {
        gson {
        }
    }

    install(Locations)

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        UserRoutes(db,jwtService,hashFunction)
        RecipeRoutes(db,hashFunction)


    }
}

data class MySession(val count: Int = 0)

