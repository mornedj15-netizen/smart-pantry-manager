package com.example.smartpantrymanager.util;

import android.app.Activity;
import android.content.Intent;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.ui.PantryListActivity;
import com.example.smartpantrymanager.ui.SettingsActivity;
import com.example.smartpantrymanager.ui.SuggestedRecipesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Wires up the bottom navigation bar so every main screen behaves the same way. */
public class NavigationHelper {

    public static void setup(Activity activity, BottomNavigationView nav, int selectedId) {
        nav.setSelectedItemId(selectedId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) return true; // already on this screen

            Class<?> target;
            if (id == R.id.nav_pantry) {
                target = PantryListActivity.class;
            } else if (id == R.id.nav_suggested) {
                target = SuggestedRecipesActivity.class;
            } else {
                target = SettingsActivity.class;
            }

            // Explicit Intent to the chosen screen; CLEAR_TOP reuses the pantry screen
            // instead of stacking copies of it.
            Intent intent = new Intent(activity, target);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intent);

            // The pantry screen is the "home" screen, so it stays; the others close
            if (!(activity instanceof PantryListActivity)) {
                activity.finish();
            }
            return true;
        });
    }
}