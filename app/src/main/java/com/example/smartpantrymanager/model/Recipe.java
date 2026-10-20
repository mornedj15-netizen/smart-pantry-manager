package com.example.smartpantrymanager.model;

import java.util.ArrayList;
import java.util.List;

/** A recipe with its method and the full list of ingredients it requires. */
public class Recipe {
    private int id;
    private String name;
    private String steps;
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe() { }

    public Recipe(String name, String steps) {
        this.name = name;
        this.steps = steps;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }

    /** Convenience method used when building recipes in code. */
    public Recipe addIngredient(String name, double quantity, String unit) {
        ingredients.add(new RecipeIngredient(name, quantity, unit));
        return this;
    }
}