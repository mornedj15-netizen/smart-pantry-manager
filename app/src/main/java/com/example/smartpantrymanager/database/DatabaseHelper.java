package com.example.smartpantrymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles creating the SQLite database and all CRUD operations
 * for pantry items and recipes.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    // Table and column names
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_QUANTITY = "quantity";
    private static final String COL_UNIT = "unit";
    private static final String COL_EXPIRY = "expiry_date";
    private static final String COL_STEPS = "steps";
    private static final String COL_RECIPE_ID = "recipe_id";
    private static final String COL_INGREDIENT_NAME = "ingredient_name";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Needed so deleting a recipe also deletes its ingredient rows
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_QUANTITY + " REAL NOT NULL, "
                + COL_UNIT + " TEXT NOT NULL, "
                + COL_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_STEPS + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RECIPE_ID + " INTEGER NOT NULL, "
                + COL_INGREDIENT_NAME + " TEXT NOT NULL, "
                + COL_QUANTITY + " REAL NOT NULL, "
                + COL_UNIT + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COL_RECIPE_ID + ") REFERENCES "
                + TABLE_RECIPES + "(" + COL_ID + ") ON DELETE CASCADE)");

        RecipeSeeder.seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    // ===================== PANTRY CRUD =====================

    /** CREATE: inserts a pantry item and returns its new row id (-1 on failure). */
    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_PANTRY, null, toValues(item));
    }

    /** READ: returns every pantry item, sorted alphabetically. */
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(TABLE_PANTRY, null, null, null, null, null,
                COL_NAME + " COLLATE NOCASE ASC")) {
            while (c.moveToNext()) {
                items.add(cursorToPantryItem(c));
            }
        }
        return items;
    }

    /** READ: returns one pantry item by id, or null if not found. */
    public PantryItem getPantryItem(int id) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(TABLE_PANTRY, null, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            return c.moveToFirst() ? cursorToPantryItem(c) : null;
        }
    }

    /** UPDATE: saves changes to an existing item; returns rows affected. */
    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_PANTRY, toValues(item), COL_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    /** DELETE: removes an item by id; returns rows affected. */
    public int deletePantryItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PANTRY, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // ===================== RECIPES =====================

    /**
     * Inserts a recipe and its ingredients. Takes the db as a parameter so it can
     * be called from onCreate() during seeding, where getWritableDatabase() is unsafe.
     */
    public static void insertRecipe(SQLiteDatabase db, Recipe recipe) {
        ContentValues rv = new ContentValues();
        rv.put(COL_NAME, recipe.getName());
        rv.put(COL_STEPS, recipe.getSteps());
        long recipeId = db.insert(TABLE_RECIPES, null, rv);

        for (RecipeIngredient ing : recipe.getIngredients()) {
            ContentValues iv = new ContentValues();
            iv.put(COL_RECIPE_ID, recipeId);
            iv.put(COL_INGREDIENT_NAME, ing.getIngredientName());
            iv.put(COL_QUANTITY, ing.getQuantity());
            iv.put(COL_UNIT, ing.getUnit());
            db.insert(TABLE_RECIPE_INGREDIENTS, null, iv);
        }
    }

    /** READ: returns all recipes, each with its ingredient list filled in. */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(TABLE_RECIPES, null, null, null, null, null,
                COL_NAME + " COLLATE NOCASE ASC")) {
            while (c.moveToNext()) {
                recipes.add(cursorToRecipe(db, c));
            }
        }
        return recipes;
    }

    /** READ: returns one recipe by id, or null if not found. */
    public Recipe getRecipe(int id) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(TABLE_RECIPES, null, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            return c.moveToFirst() ? cursorToRecipe(db, c) : null;
        }
    }

    // ===================== HELPERS =====================

    private ContentValues toValues(PantryItem item) {
        ContentValues v = new ContentValues();
        v.put(COL_NAME, item.getName());
        v.put(COL_QUANTITY, item.getQuantity());
        v.put(COL_UNIT, item.getUnit());
        v.put(COL_EXPIRY, item.getExpiryDate());
        return v;
    }

    private PantryItem cursorToPantryItem(Cursor c) {
        return new PantryItem(
                c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                c.getDouble(c.getColumnIndexOrThrow(COL_QUANTITY)),
                c.getString(c.getColumnIndexOrThrow(COL_UNIT)),
                c.getString(c.getColumnIndexOrThrow(COL_EXPIRY)));
    }

    private Recipe cursorToRecipe(SQLiteDatabase db, Cursor c) {
        Recipe r = new Recipe();
        r.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
        r.setName(c.getString(c.getColumnIndexOrThrow(COL_NAME)));
        r.setSteps(c.getString(c.getColumnIndexOrThrow(COL_STEPS)));

        try (Cursor ic = db.query(TABLE_RECIPE_INGREDIENTS, null, COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(r.getId())}, null, null, null)) {
            while (ic.moveToNext()) {
                RecipeIngredient ing = new RecipeIngredient(
                        ic.getString(ic.getColumnIndexOrThrow(COL_INGREDIENT_NAME)),
                        ic.getDouble(ic.getColumnIndexOrThrow(COL_QUANTITY)),
                        ic.getString(ic.getColumnIndexOrThrow(COL_UNIT)));
                ing.setId(ic.getInt(ic.getColumnIndexOrThrow(COL_ID)));
                ing.setRecipeId(r.getId());
                r.getIngredients().add(ing);
            }
        }
        return r;
    }
}