package com.khanh.expensemanagement.naiji;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.m_name.kbn.SourcePaymentClass;
import com.khanh.expensemanagement.m_name.view.MNameAdapter;
import com.khanh.expensemanagement.util.LangUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NaijiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NaijiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText m_name_source;
    private EditText m_name_category;
    private RecyclerView m_name_recycler_view;
    private TextView m_name_title;

    private PieChart pieChart;
    private PieChart budget_category_chart;
    private RecyclerView category_recycler_view;

    private RadioGroup language_radio_group;
    private Button btn_save;

    public NaijiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NaijiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NaijiFragment newInstance(String param1, String param2) {
        NaijiFragment fragment = new NaijiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_naiji, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view) {

        ArrayList<CategoryExpense> categoryExpenses = new ArrayList<>();
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Entertainment", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Beauty", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));
        categoryExpenses.add(new CategoryExpense(1, "Food", BigInteger.valueOf(999999999)));

        category_recycler_view = view.findViewById(R.id.category_recycler_view);
        CategoryExpenseAdapter categoryExpenseAdapter = new CategoryExpenseAdapter(requireContext(), getActivity(), categoryExpenses);
        category_recycler_view.setAdapter(categoryExpenseAdapter);
        category_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        pieChart = view.findViewById(R.id.pieChart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(53f, "Food"));
        entries.add(new PieEntry(36f, "Entertainment"));
        entries.add(new PieEntry(9f, "Beauty"));
        entries.add(new PieEntry(20f, "Bills"));
        entries.add(new PieEntry(1f, "Other"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        List<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorOrange));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorRed));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorPink));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorGreen));
        colors.add(ContextCompat.getColor(getContext(), R.color.pieChartColorBlue));
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