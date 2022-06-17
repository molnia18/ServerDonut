package com.example.routes

import com.example.data.model.Recipes
import com.example.data.model.SimpleResponse
import com.example.data.model.User
import com.example.repository.Repo
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val RECIPES = "$API_VERSION/recipes"
const val CREATE_RECIPES = "$RECIPES/create"
const val UPDATE_RECIPES = "$RECIPES/update"
const val DELETE_RECIPES = "$RECIPES/delete"

@Location(CREATE_RECIPES)
class RecipeCreateRoute

@Location(RECIPES)
class RecipeGetRoute

@Location(UPDATE_RECIPES)
class RecipeUpdateRoute

@Location(DELETE_RECIPES)
class RecipeDeleteRoute



fun Route.RecipeRoutes(
    db:Repo,
    hashFunction: (String)->String
){
    authenticate("jwt") {

        post<RecipeCreateRoute>{

            val reсipes = try {
                call.receive<Recipes>()
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Отсутствуют поля"))
                return@post
            }

            try {
                val email = call.principal<User>()!!.email
                db.addReсipes(reсipes,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Ваш рецепт добавлен успешно!"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message?:"Произошла какая-то ошибка"))
            }
        }

        get<RecipeGetRoute>{
            try{
                val email = call.principal<User>()!!.email
                val recipes = db.getAllRecipes(email)
                call.respond(HttpStatusCode.OK,recipes)
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict, e.message?:"Произошла какая-то ошибка")
            }
        }

        post<RecipeUpdateRoute>{
            val reсipes = try {
                call.receive<Recipes>()
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Отсутствуют поля"))
                return@post
            }

            try {
                val email = call.principal<User>()!!.email
                db.updateRecipes(reсipes,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Ваш рецепт изменен успешно!"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message?:"Произошла какая-то ошибка"))
            }
        }

        delete<RecipeDeleteRoute>{
            val recipeid = try{
                call.request.queryParameters["id"]!!
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,""))
                return@delete
            }

            try {
                val email = call.principal<User>()!!.email
                db.deleteRecipes(recipeid,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Рецепт успешно удален"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message?:"Произошла какая-то ошибка"))
            }
        }
    }
}