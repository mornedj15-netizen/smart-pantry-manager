package com.example.smartpantrymanager.database;

import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantrymanager.model.Recipe;

/** Pre-loads the recipe collection the first time the database is created. */
public class RecipeSeeder {

    // Ingredient names are stored lowercase and singular so matching stays simple.
    // Units used: g, ml, pcs (pieces)
    public static void seed(SQLiteDatabase db) {
        DatabaseHelper.insertRecipe(db, new Recipe("Scrambled Eggs on Toast",
                "1. Toast the bread.\n2. Whisk eggs with salt.\n3. Melt butter in a pan and scramble the eggs.\n4. Serve on the toast.")
                .addIngredient("egg", 3, "pcs").addIngredient("bread", 2, "pcs")
                .addIngredient("butter", 10, "g").addIngredient("salt", 2, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Cheese Omelette",
                "1. Whisk eggs with salt.\n2. Melt butter in a pan and pour in the eggs.\n3. Add grated cheese, fold and cook until set.")
                .addIngredient("egg", 3, "pcs").addIngredient("cheese", 50, "g")
                .addIngredient("butter", 10, "g").addIngredient("salt", 2, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Tomato Pasta",
                "1. Boil the pasta until tender.\n2. Fry chopped onion and garlic in olive oil.\n3. Add chopped tomatoes and simmer for 15 minutes.\n4. Toss with the pasta.")
                .addIngredient("pasta", 200, "g").addIngredient("tomato", 4, "pcs")
                .addIngredient("onion", 1, "pcs").addIngredient("garlic", 2, "pcs")
                .addIngredient("olive oil", 15, "ml"));

        DatabaseHelper.insertRecipe(db, new Recipe("Egg Fried Rice",
                "1. Cook the rice and let it cool.\n2. Fry chopped onion in oil.\n3. Scramble in the eggs.\n4. Add rice and soy sauce and stir-fry for 5 minutes.")
                .addIngredient("rice", 200, "g").addIngredient("egg", 2, "pcs")
                .addIngredient("onion", 1, "pcs").addIngredient("soy sauce", 15, "ml")
                .addIngredient("oil", 15, "ml"));

        DatabaseHelper.insertRecipe(db, new Recipe("Cheese Toastie",
                "1. Butter the outside of the bread.\n2. Put cheese between the slices.\n3. Fry on both sides until golden.")
                .addIngredient("bread", 2, "pcs").addIngredient("cheese", 40, "g")
                .addIngredient("butter", 10, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Pancakes",
                "1. Mix flour, sugar, egg and milk into a smooth batter.\n2. Melt butter in a pan.\n3. Cook spoonfuls of batter until golden on both sides.")
                .addIngredient("flour", 150, "g").addIngredient("milk", 250, "ml")
                .addIngredient("egg", 1, "pcs").addIngredient("sugar", 20, "g")
                .addIngredient("butter", 20, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("French Toast",
                "1. Whisk eggs, milk and sugar.\n2. Soak the bread slices in the mixture.\n3. Fry in butter until golden on both sides.")
                .addIngredient("bread", 4, "pcs").addIngredient("egg", 2, "pcs")
                .addIngredient("milk", 100, "ml").addIngredient("sugar", 10, "g")
                .addIngredient("butter", 10, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Mashed Potato",
                "1. Peel and boil the potatoes until soft.\n2. Drain and mash.\n3. Stir in butter, warm milk and salt.")
                .addIngredient("potato", 4, "pcs").addIngredient("butter", 30, "g")
                .addIngredient("milk", 50, "ml").addIngredient("salt", 3, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Cheesy Baked Potato",
                "1. Bake the whole potatoes at 200C for about 1 hour.\n2. Cut open and add butter and salt.\n3. Top with grated cheese.")
                .addIngredient("potato", 2, "pcs").addIngredient("cheese", 60, "g")
                .addIngredient("butter", 15, "g").addIngredient("salt", 2, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Tuna Pasta",
                "1. Boil the pasta.\n2. Fry chopped onion in olive oil.\n3. Add the drained tuna and heat through.\n4. Toss with the pasta.")
                .addIngredient("pasta", 200, "g").addIngredient("tuna", 1, "pcs")
                .addIngredient("onion", 1, "pcs").addIngredient("olive oil", 15, "ml"));

        DatabaseHelper.insertRecipe(db, new Recipe("Vegetable Stir-Fry",
                "1. Slice the vegetables.\n2. Heat oil in a pan or wok.\n3. Stir-fry for 5-7 minutes.\n4. Add soy sauce and toss.")
                .addIngredient("carrot", 2, "pcs").addIngredient("onion", 1, "pcs")
                .addIngredient("bell pepper", 1, "pcs").addIngredient("soy sauce", 20, "ml")
                .addIngredient("oil", 15, "ml"));

        DatabaseHelper.insertRecipe(db, new Recipe("Cheesy Rice",
                "1. Cook the rice.\n2. Fry chopped onion in butter.\n3. Stir the onion and grated cheese into the hot rice until melted.")
                .addIngredient("rice", 200, "g").addIngredient("cheese", 80, "g")
                .addIngredient("butter", 15, "g").addIngredient("onion", 1, "pcs"));

        DatabaseHelper.insertRecipe(db, new Recipe("Banana Pancakes",
                "1. Mash the bananas.\n2. Mix in eggs and flour.\n3. Cook spoonfuls in a non-stick pan until golden on both sides.")
                .addIngredient("banana", 2, "pcs").addIngredient("egg", 2, "pcs")
                .addIngredient("flour", 50, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Tomato Soup",
                "1. Fry onion and garlic in butter.\n2. Add chopped tomatoes and simmer for 20 minutes.\n3. Blend until smooth and season with salt.")
                .addIngredient("tomato", 6, "pcs").addIngredient("onion", 1, "pcs")
                .addIngredient("garlic", 2, "pcs").addIngredient("butter", 20, "g")
                .addIngredient("salt", 2, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Garlic Bread",
                "1. Mash crushed garlic into soft butter.\n2. Spread on the bread.\n3. Bake at 180C for 10 minutes.")
                .addIngredient("bread", 4, "pcs").addIngredient("garlic", 3, "pcs")
                .addIngredient("butter", 40, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Oats Porridge",
                "1. Combine oats and milk in a pot.\n2. Stir over medium heat for 5 minutes.\n3. Sweeten with sugar.")
                .addIngredient("oat", 80, "g").addIngredient("milk", 300, "ml")
                .addIngredient("sugar", 10, "g"));

        DatabaseHelper.insertRecipe(db, new Recipe("Peanut Butter Banana Toast",
                "1. Toast the bread.\n2. Spread with peanut butter.\n3. Top with sliced banana.")
                .addIngredient("bread", 2, "pcs").addIngredient("peanut butter", 30, "g")
                .addIngredient("banana", 1, "pcs"));

        DatabaseHelper.insertRecipe(db, new Recipe("Chicken and Rice",
                "1. Brown the chicken in oil.\n2. Add chopped onion and garlic.\n3. Add rice and water and simmer, covered, for 20 minutes.")
                .addIngredient("chicken", 300, "g").addIngredient("rice", 200, "g")
                .addIngredient("onion", 1, "pcs").addIngredient("garlic", 2, "pcs")
                .addIngredient("oil", 15, "ml"));

        DatabaseHelper.insertRecipe(db, new Recipe("Macaroni Cheese",
                "1. Boil the macaroni.\n2. Make a sauce from butter, flour and milk.\n3. Stir in the cheese until melted.\n4. Mix with the macaroni.")
                .addIngredient("macaroni", 200, "g").addIngredient("cheese", 150, "g")
                .addIngredient("milk", 200, "ml").addIngredient("butter", 30, "g")
                .addIngredient("flour", 20, "g"));
    }
}