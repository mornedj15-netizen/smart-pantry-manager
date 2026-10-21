package com.example.smartpantrymanager.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;

/** Shows the full ingredient list and method for one recipe. */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Read the recipe id that was passed in the Intent
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = new DatabaseHelper(this).getRecipe(recipeId);
        if (recipe == null) {
            finish(); // nothing to show
            return;
        }

        ((TextView) findViewById(R.id.tvRecipeTitle)).setText(recipe.getName());

        StringBuilder ingredients = new StringBuilder();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            ingredients.append("\u2022 ")
                    .append(formatQuantity(ing.getQuantity())).append(" ")
                    .append(ing.getUnit()).append(" ")
                    .append(ing.getIngredientName()).append("\n");
        }
        ((TextView) findViewById(R.id.tvIngredients)).setText(ingredients.toString().trim());
        ((TextView) findViewById(R.id.tvSteps)).setText(recipe.getSteps());
    }

    private String formatQuantity(double q) {
        return (q % 1 == 0) ? String.valueOf((long) q) : String.valueOf(q);
    }
}