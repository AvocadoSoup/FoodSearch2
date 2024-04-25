package com.example.foodsearch

data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val authorName: String = "",
    val rating: Int = 0,
    val genre: String = "",
    val uemail: String = ""
)
