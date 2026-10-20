package com.example.smartpantrymanager.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/** Form used for both adding a new ingredient and editing an existing one. */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";
    private static final List<String> UNITS = Arrays.asList("g", "kg", "ml", "l", "pcs");

    private EditText etName, etQuantity, etExpiry;
    private Spinner spUnit;
    private DatabaseHelper db;
    private int editingId = -1; // -1 means "adding a new item"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        Button btnSave = findViewById(R.id.btnSave);

        spUnit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, UNITS));

        // If an item id was passed in the Intent, we are editing that item
        editingId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (editingId != -1) {
            ((TextView) findViewById(R.id.tvFormTitle)).setText("Edit Ingredient");
            loadItemIntoForm();
        }

        etExpiry.setOnClickListener(v -> showDatePicker());
        etExpiry.setOnLongClickListener(v -> {
            etExpiry.setText("");
            return true;
        });
        btnSave.setOnClickListener(v -> saveItem());
    }

    private void loadItemIntoForm() {
        PantryItem item = db.getPantryItem(editingId);
        if (item == null) {
            finish();
            return;
        }
        etName.setText(item.getName());
        etQuantity.setText(String.valueOf(item.getQuantity() % 1 == 0
                ? String.valueOf((long) item.getQuantity())
                : String.valueOf(item.getQuantity())));
        int pos = UNITS.indexOf(item.getUnit());
        spUnit.setSelection(pos >= 0 ? pos : 0);
        etExpiry.setText(item.getExpiryDate());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                etExpiry.setText(String.format("%04d-%02d-%02d", year, month + 1, day)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    /** Validates the form, then creates or updates the record. */
    private void saveItem() {
        String name = etName.getText().toString().trim();
        String qtyText = etQuantity.getText().toString().trim();

        // --- Input validation ---
        if (name.isEmpty()) {
            etName.setError("Please enter an ingredient name");
            return;
        }
        if (!name.matches("[\\p{L} '\\-]+")) {
            etName.setError("Use letters only (no numbers or symbols)");
            return;
        }
        if (qtyText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            return;
        }
        if (quantity <= 0) {
            etQuantity.setError("Quantity must be greater than 0");
            return;
        }

        String unit = spUnit.getSelectedItem().toString();
        String expiry = etExpiry.getText().toString().trim();
        if (expiry.isEmpty()) expiry = null;

        PantryItem item = new PantryItem(0, name, quantity, unit, expiry);

        if (editingId == -1) {
            db.addPantryItem(item);                 // CREATE
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            item.setId(editingId);
            db.updatePantryItem(item);              // UPDATE
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish(); // go back to the list
    }
}