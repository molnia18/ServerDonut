package com.example.data.table

import org.jetbrains.exposed.sql.Table

object RecipesTable:Table() {
    val id = varchar("id", 512)
    val userEmail = varchar("userEmail", 512).references(UserTable.email)
    val recipeTitle = text("recipeTitle")
    val cookingTime = text("cookingTime")
    val count = text("count")
    val content = text("content")


    override val primaryKey: PrimaryKey = PrimaryKey(id)
}