package com.example.repository

import com.example.data.model.Recipes
import com.example.data.model.User
import com.example.data.table.RecipesTable
import com.example.data.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class Repo {

    suspend fun addUser(user: User){
        dbQuery{
            UserTable.insert { ut->
                ut[UserTable.email] = user.email
                ut[UserTable.hashPassword] = user.hashPassword
                ut[UserTable.name] = user.userName
            }
        }
    }

    suspend fun findUserByEmail(email:String) = dbQuery {
         UserTable.select { UserTable.email.eq(email)}
             .map { rowToUser(it) }
             .singleOrNull()
    }


    private fun rowToUser(row:ResultRow?):User?{
        if(row == null){
            return null
        }

        return User(
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.name]
        )
    }


    suspend fun addResipes(recipes: Recipes,email: String){
        dbQuery {
            RecipesTable.insert { rt->
                rt[RecipesTable.id] = recipes.id
                rt[RecipesTable.userEmail] = email
                rt[RecipesTable.recipeTitle] = recipes.recipeTitle
                rt[RecipesTable.cookingTime] = recipes.cookingTime
                rt[RecipesTable.count] = recipes.count
                rt[RecipesTable.content] = recipes.content
                rt[RecipesTable.photo] = recipes.photo
            }
        }
    }


    suspend fun getAllRecipes(email:String):List<Recipes> = dbQuery {

        RecipesTable.select{
            RecipesTable.userEmail.eq(email)
        }.mapNotNull { rowToRecipes(it)}
    }


    suspend fun updateRecipes(recipes: Recipes,email: String){
        dbQuery {

            RecipesTable.update(
                where = {
                    RecipesTable.userEmail.eq(email) and RecipesTable.id.eq(recipes.id)
                }
            ){ rt->
                rt[RecipesTable.recipeTitle] = recipes.recipeTitle
                rt[RecipesTable.cookingTime] = recipes.cookingTime
                rt[RecipesTable.count] = recipes.count
                rt[RecipesTable.content] = recipes.content
                rt[RecipesTable.photo] = recipes.photo
            }
        }
    }

    suspend fun deleteRecipes(id:String,email:String){
        dbQuery {
            RecipesTable.deleteWhere { RecipesTable.userEmail.eq(email) and RecipesTable.id.eq(id) }
        }
    }

    private fun rowToRecipes(row: ResultRow?):Recipes? {
        if(row == null){
            return null
        }

        return Recipes(
            id = row[RecipesTable.id],
            recipeTitle = row[RecipesTable.recipeTitle],
            cookingTime = row[RecipesTable.cookingTime],
            count = row[RecipesTable.count],
            content = row[RecipesTable.content],
            photo = row[RecipesTable.photo]
        )
    }


}


