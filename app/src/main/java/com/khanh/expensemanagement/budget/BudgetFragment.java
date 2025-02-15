package com.khanh.expensemanagement.budget;

import android.database.Cursor;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.util.FormUtil;
import com.khanh.expensemanagement.util.FragmentUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.util.grap.SemiCircularProgressBar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.YearMonth;

public class BudgetFragment extends Fragment {

    private final String FRAGMENT_TITLE = "Budget";

    SemiCircularProgressBar progressBar;
    TextView remaining_amount;
    TextView remaining_title;
    TextView over_spent_icon;
    TextView spent;
    TextView budget;
    DatabaseHelper databaseHelper;
    CardView card_total_budget;

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

        remaining_amount = view.findViewById(R.id.remaining_amount);
        remaining_title = view.findViewById(R.id.remaining_title);
        over_spent_icon = view.findViewById(R.id.over_spent_icon);
        progressBar = view.findViewById(R.id.semiCircularProgressBar);
        card_total_budget = view.findViewById(R.id.card_total_budget);
        spent = view.findViewById(R.id.spent);
        budget = view.findViewById(R.id.budget);
    }

    private void getDataDisplay() {

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
            if (limitAmount.compareTo(totalSpent) < 0) {

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
}