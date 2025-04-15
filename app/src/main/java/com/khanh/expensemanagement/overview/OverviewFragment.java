package com.khanh.expensemanagement.overview;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.util.SqliteUtil;

import java.math.BigInteger;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {

    DatabaseHelper databaseHelper;
    PieChart pieChart;
    RecyclerView category_recycler_view;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
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

        pieChart = view.findViewById(R.id.pieChart);

        ArrayList<CategoryExpense> categoryExpenses = new ArrayList<>();
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Entertainment", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Beauty", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));

        category_recycler_view = view.findViewById(R.id.category_recycler_view);
        CategoryExpenseAdapter categoryExpenseAdapter = new CategoryExpenseAdapter(requireContext(), getActivity(), categoryExpenses);
        category_recycler_view.setAdapter(categoryExpenseAdapter);
        category_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getDataDisplay() {

        BigInteger categoryTotalSpent;

        ArrayList<CategoryExpense> categoryExpenseList = new ArrayList<>();
        Cursor cursor = databaseHelper.mNameFindAll(CategoryClass.NAME_IDENT_CD);

        // Get category list
        if (cursor != null && cursor.moveToFirst()) {

            do {
                categoryExpenseList.add(new CategoryExpense(cursor.getInt(1),cursor.getString(3), BigInteger.ZERO));
            } while (cursor.moveToNext());
        }

        // Get total spent on category
        for (CategoryExpense item : categoryExpenseList) {

            categoryTotalSpent = databaseHelper.transactionTotalSpentOnMonth(YearMonth.now(), item.getId());
            item.setTotalSpent(categoryTotalSpent);
        }

        // Setup pie data chart
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (CategoryExpense item : categoryExpenseList) {

            // Hide category with spent amount on month is 0
            if (!item.getTotalSpent().equals(BigInteger.ZERO)) {

                entries.add(new PieEntry(item.getTotalSpent().floatValue(), item.getCategoryName()));
            }
        }
        setExpenditurePieChart(entries);

        // Release cursor used
        SqliteUtil.releaseCursor(cursor);
    }

    private void setExpenditurePieChart(ArrayList<PieEntry> dataEntries) {

        PieDataSet dataSet = new PieDataSet(dataEntries, "");
        List<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorOrange));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorRed));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorPink));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorBlue));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorGreen));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorYellow));
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setExtraOffsets(10, 10, 10, 10);
        pieChart.getDescription().setEnabled(false); // Ẩn description
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.animateY(1000); // Animation
        pieChart.invalidate(); // Refresh chart
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(51f);
        pieChart.setRotationEnabled(false);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);


//        pieChart.setDrawEntryLabels(true);
//        // Màu chữ của nhãn (ví dụ: màu đen)
//        pieChart.setEntryLabelColor(Color.BLACK);
//
//        // Kích thước chữ của nhãn (đơn vị: sp)
//        pieChart.setEntryLabelTextSize(12f);
//
//        // Kiểu chữ (tuỳ chọn)
//        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);

        // Hiển thị label (tiêu đề) bên ngoài
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // Tạo đường dẫn từ label đến biểu đồ (dạng mũi tên)
        dataSet.setValueLinePart1Length(0.5f); // Độ dài đoạn đầu
        dataSet.setValueLinePart2Length(0.4f); // Độ dài đoạn sau
        dataSet.setValueLineWidth(2f); // Độ dày đường
        dataSet.setValueLineColor(Color.BLACK); // Màu của đường dẫn
    }

}