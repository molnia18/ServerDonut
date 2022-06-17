package com.example.data.model

import org.jetbrains.exposed.sql.statements.api.ExposedBlob


data class Recipes(
    val id:String,
    val recipeTitle:String,
    val cookingTime:String,
    val count:String,
    val content:String
)
