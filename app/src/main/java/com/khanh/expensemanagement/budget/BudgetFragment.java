package com.khanh.expensemanagement.budget;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.budget_mainte.BudgetCategorySelectActivity;
import com.khanh.expensemanagement.budget_mainte.BudgetRegisterActivity;
import com.khanh.expensemanagement.budget_mainte.BudgetUpdateActivity;
import com.khanh.expensemanagement.util.FormUtil;
import com.khanh.expensemanagement.util.FragmentUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.component.SemiCircularProgressBar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    private final String FRAGMENT_TITLE = "Budget";

    Button add_budget_btn;
    Button more_button;
    SemiCircularProgressBar progressBar;
    TextView remaining_amount;
    TextView remaining_title;
    TextView over_spent_icon;
    TextView spent;
    TextView budget;
    DatabaseHelper databaseHelper;
    CardView card_total_budget;
    RecyclerView budget_category_recycler_view;

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentUtil.setActionBarTitle(getActivity(), FRAGMENT_TITLE);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        initWidgets(view);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        getDataDisplay();
    }

    private void initWidgets(View view) {

        add_budget_btn = view.findViewById(R.id.add_budget_btn);
        add_budget_btn.setOnClickListener(buttonView -> {

//            Intent budgetRegisterIntent = new Intent(buttonView.getContext(), BudgetRegisterActivity.class);
//            startActivity(budgetRegisterIntent);
            Intent budgetCategorySelectIntent = new Intent(buttonView.getContext(), BudgetCategorySelectActivity.class);
            startActivity(budgetCategorySelectIntent);
        });
        remaining_amount = view.findViewById(R.id.remaining_amount);
        remaining_title = view.findViewById(R.id.remaining_title);
        over_spent_icon = view.findViewById(R.id.over_spent_icon);
        progressBar = view.findViewById(R.id.semiCircularProgressBar);
        card_total_budget = view.findViewById(R.id.card_total_budget);
        spent = view.findViewById(R.id.spent);
        budget = view.findViewById(R.id.budget);
        more_button = view.findViewById(R.id.more_button);
        more_button.setOnClickListener(buttonView -> {

            showMoreBottomDialog();
        });
        budget_category_recycler_view = view.findViewById(R.id.budget_category_recycler_view);
    }

    private void getDataDisplay() {

        getBudgetSpendingMonth();
        getBudgetByCategory();
    }

    private void getBudgetSpendingMonth() {

        BigInteger limitAmount;
        String limitAmountText;
        BigInteger totalSpent;
        String totalSpentText;
        BigInteger remainingAmount;
        String remainingAmountText;
        BigDecimal remainingPercentage;

        Cursor cursor = databaseHelper.budgetTotalSpendingMonth();

        if (cursor != null && cursor.moveToFirst()) {

            card_total_budget.setVisibility(View.VISIBLE);

            // set budget
            limitAmount = BigInteger.valueOf(cursor.getInt(0));
            limitAmountText = limitAmount.toString();
            limitAmountText = FormUtil.fncDecFormat(limitAmountText);
            budget.setText(getString(R.string.transaction_amount_currency, limitAmountText));

            // set spent
            totalSpent = databaseHelper.transactionTotalSpentOnMonth(YearMonth.now());
            totalSpentText = totalSpent.toString();
            totalSpentText = FormUtil.fncDecFormat(totalSpentText);
            spent.setText(getString(R.string.transaction_amount_currency, totalSpentText));

            // set remaining
            remainingAmount =  limitAmount.subtract(totalSpent);
            if (remainingAmount.compareTo(BigInteger.ZERO) < 0) {

                over_spent_icon.setVisibility(View.VISIBLE);
                remaining_title.setText(R.string.over_spent);
                remaining_amount.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                remainingAmount = remainingAmount.abs();
            } else {

                over_spent_icon.setVisibility(View.GONE);
                remaining_title.setText(R.string.remaining);
                remaining_amount.setTextColor(ContextCompat.getColor(requireContext(), R.color.pink));
            }
            remainingAmountText = remainingAmount.toString();
            remainingAmountText = FormUtil.fncDecFormat(remainingAmountText);
            remaining_amount.setText(getString(R.string.transaction_amount_currency, remainingAmountText));

            // set circular progress bar
            if (limitAmount.compareTo(totalSpent) < 0 || limitAmount.signum() != 1) {

                progressBar.setProgress(0);
            } else {

                remainingPercentage = new BigDecimal(limitAmount.subtract(totalSpent))
                        .multiply(BigDecimal.valueOf(100))
                        .divide(new BigDecimal(limitAmount), 2, RoundingMode.HALF_UP);
                progressBar.setProgress(remainingPercentage.floatValue());
            }

        } else {

            card_total_budget.setVisibility(View.GONE);
        }
    }

    private void getBudgetByCategory() {

        List<BudgetCategory> budgetCategoryList = new ArrayList<>();
        budgetCategoryList.add(new BudgetCategory(1,1,"category test 1"));
        budgetCategoryList.add(new BudgetCategory(1,1,"category test 2"));
        budgetCategoryList.add(new BudgetCategory(1,1,"category test 3"));
        budgetCategoryList.add(new BudgetCategory(1,1,"category test 4"));
        BudgetCategoryAdapter budgetCategoryAdapter = new BudgetCategoryAdapter(requireContext(), getActivity(), budgetCategoryList);
        budget_category_recycler_view.setAdapter(budgetCategoryAdapter);
        budget_category_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showMoreBottomDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.budget_more_bottom);

        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> dialog.dismiss());

        TextView edit_btn = dialog.findViewById(R.id.edit_btn);
        edit_btn.setOnClickListener(view -> {

            dialog.dismiss();
            Intent intent = new Intent(view.getContext(), BudgetUpdateActivity.class);
            startActivity(intent);
        });

        TextView del_btn = dialog.findViewById(R.id.del_btn);
        del_btn.setOnClickListener(view -> {

            dialog.dismiss();
            String dialogTitle = "Delete budget";
            String dialogMessage = "Deleted budget can not be recovered";
            FormUtil.openConfirmDialog(getContext(), dialogTitle, dialogMessage, () -> {

                databaseHelper.deleteBudgetTotalSpendingMonth();
                FragmentUtil.replaceFragment(getActivity().getSupportFragmentManager(), new BudgetFragment());
            });
        });


        // display dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}