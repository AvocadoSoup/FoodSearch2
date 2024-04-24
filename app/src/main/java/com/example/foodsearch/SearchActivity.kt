package com.example.foodsearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {

    private lateinit var genreEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var searchHintTextView : TextView
    private lateinit var searchResultsTextView: TextView
    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        genreEditText = findViewById(R.id.genreEditText)
        searchButton = findViewById(R.id.searchButton)
        searchHintTextView = findViewById(R.id.searchHintTextView)
        searchResultsTextView = findViewById(R.id.searchResultsTextView)
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)

        recipeAdapter = RecipeAdapter { recipeDocumentId ->
            val intent = Intent(this, RecipeActivity::class.java).apply {
                putExtra("recipeDocumentId", recipeDocumentId)
            }
            startActivity(intent)
        }
        recyclerViewSearch.adapter = recipeAdapter
        recyclerViewSearch.layoutManager = LinearLayoutManager(this)

        searchButton.setOnClickListener {
            val genre = genreEditText.text.toString().trim()
            if (genre.isNotEmpty()) {
                searchRecipesByGenre(genre)
            } else {
                searchHintTextView.text = "Please enter a genre to search"
            }
        }
    }

    private fun searchRecipesByGenre(genre: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("recipes")
            .whereEqualTo("genre", genre)
            .get()
            .addOnSuccessListener { result ->
                val recipes = mutableListOf<Recipe>()
                for (document in result) {
                    val recipe = document.toObject(Recipe::class.java)
                    recipes.add(recipe)
                }
                if (recipes.isNotEmpty()) {
                    searchHintTextView.text = "Results found for genre: $genre"
                } else {
                    searchHintTextView.text = "No results found for genre: $genre"
                }
                recipeAdapter.submitList(recipes)
            }
            .addOnFailureListener { exception ->
                searchHintTextView.text = "Failed to search recipes: ${exception.message}"
            }
    }
}
