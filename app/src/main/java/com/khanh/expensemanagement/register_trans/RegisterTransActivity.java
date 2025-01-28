package com.khanh.expensemanagement.register_trans;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.util.DateTimeUtil;

public class RegisterTransActivity extends AppCompatActivity {

    private final String ACTIVITY_TITLE = "Add expense";

    EditText amount;
    EditText category;
    EditText date;
    EditText source;
    EditText note;
    Button add_expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_trans);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(ACTIVITY_TITLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }
        initWidgets();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back to home
            finish(); // end RegisterTransActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initWidgets() {

        amount = findViewById(R.id.amount);

        category = findViewById(R.id.category);
        category.setFocusable(false);
        category.setClickable(true);
        category.setOnClickListener(view -> {
//                showBottomDialog();
        });

        date = findViewById(R.id.date);
        date.setFocusable(false);
        date.setClickable(true);
        date.setOnClickListener(view -> {

            DateTimeUtil.showDatePicker(RegisterTransActivity.this,(EditText) view); // Open date picker dialog
        });

        source = findViewById(R.id.source);
        source.setFocusable(false);
        source.setClickable(true);
        source.setOnClickListener(view -> showSourceBottomDialog());

        note = findViewById(R.id.note);

        add_expense = findViewById(R.id.add_expense);
        add_expense.setOnClickListener(view -> {

        });
    }

    private void showSourceBottomDialog() {

        final Dialog dialog = new Dialog(RegisterTransActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.source_of_fund_bottom);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}