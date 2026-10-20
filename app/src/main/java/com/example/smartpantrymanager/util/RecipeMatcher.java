package com.example.smartpantrymanager.util;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Core business logic: decides which recipes can be made from the pantry.
 * A recipe is only suggested if EVERY ingredient is present in at least
 * the required quantity (the strict-matching rule).
 */
public class RecipeMatcher {

    /** Returns only the recipes the user can make right now. */
    public static List<Recipe> getStrictMatches(List<Recipe> recipes, List<PantryItem> pantry) {
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (canMake(recipe, pantry)) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    /** True only if every required ingredient is covered by the pantry. */
    public static boolean canMake(Recipe recipe, List<PantryItem> pantry) {
        if (recipe.getIngredients().isEmpty()) {
            return false;
        }
        for (RecipeIngredient needed : recipe.getIngredients()) {
            if (!hasIngredient(needed, pantry)) {
                return false; // one missing ingredient rules the recipe out
            }
        }
        return true;
    }

    private static boolean hasIngredient(RecipeIngredient needed, List<PantryItem> pantry) {
        String neededName = normalise(needed.getIngredientName());
        double neededBase = toBaseUnits(needed.getQuantity(), needed.getUnit());
        String neededType = unitType(needed.getUnit());

        // Add up all pantry entries with the same name (e.g. two separate "egg" entries)
        double available = 0;
        for (PantryItem item : pantry) {
            if (normalise(item.getName()).equals(neededName)
                    && unitType(item.getUnit()).equals(neededType)) {
                available += toBaseUnits(item.getQuantity(), item.getUnit());
            }
        }
        return available >= neededBase;
    }

    /** Lowercases, trims, and strips simple plurals so "Tomatoes" matches "tomato". */
    public static String normalise(String name) {
        if (name == null) return "";
        String n = name.trim().toLowerCase(Locale.ROOT);
        if (n.endsWith("ies") && n.length() > 3) {
            return n.substring(0, n.length() - 3) + "y";   // berries -> berry
        }
        if (n.endsWith("oes") && n.length() > 3) {
            return n.substring(0, n.length() - 2);          // tomatoes -> tomato
        }
        if (n.endsWith("ches") || n.endsWith("shes")) {
            return n.substring(0, n.length() - 2);          // peaches -> peach
        }
        if (n.endsWith("s") && !n.endsWith("ss") && n.length() > 2) {
            return n.substring(0, n.length() - 1);          // eggs -> egg
        }
        return n;
    }

    /** Converts to g, ml or pcs so different units can be compared. */
    private static double toBaseUnits(double quantity, String unit) {
        switch (unit.trim().toLowerCase(Locale.ROOT)) {
            case "kg": return quantity * 1000;
            case "l":  return quantity * 1000;
            default:   return quantity; // g, ml, pcs are already base units
        }
    }

    /** Groups units: weight, volume or count. Weight can't satisfy a count requirement. */
    private static String unitType(String unit) {
        switch (unit.trim().toLowerCase(Locale.ROOT)) {
            case "g":
            case "kg":  return "weight";
            case "ml":
            case "l":   return "volume";
            default:    return "count";
        }
    }
}
