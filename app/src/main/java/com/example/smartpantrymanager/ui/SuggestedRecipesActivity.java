package com.example.smartpantrymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.adapter.RecipeAdapter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.util.NavigationHelper;
import com.example.smartpantrymanager.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

/** Lists only the recipes the user can make right now (strict matching). */
public class SuggestedRecipesActivity extends AppCompatActivity
        implements RecipeAdapter.OnRecipeClickListener {

    private DatabaseHelper db;
    private RecipeAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvNoMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerRecipes);
        tvNoMatches = findViewById(R.id.tvNoMatches);

        adapter = new RecipeAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        NavigationHelper.setup(this, findViewById(R.id.bottomNav), R.id.nav_suggested);
    }

    /** Re-run the matching every time the screen is shown, so it reflects the latest pantry. */
    @Override
    protected void onResume() {
        super.onResume();
        List<Recipe> matches = RecipeMatcher.getStrictMatches(
                db.getAllRecipes(), db.getAllPantryItems());
        adapter.setRecipes(matches);

        boolean empty = matches.isEmpty();
        tvNoMatches.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        // Explicit Intent: pass the recipe id so the detail screen can load it
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}