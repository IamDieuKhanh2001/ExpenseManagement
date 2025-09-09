package com.khanh.expensemanagement.naiji;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
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
    private Button excelBtn;
    DatabaseHelper databaseHelper;

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
        databaseHelper = new DatabaseHelper(getActivity());
        initWidgets(view);
        return view;
    }

    public void exportExcel(List<ExcelDownloadData> list) {

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Demo excel");

            List<String> headerColList = new ArrayList<>();
            headerColList.add("Transaction Id");
            headerColList.add("Transaction date");
            headerColList.add("Amount");
            headerColList.add("Note");
            headerColList.add("Category id");
            headerColList.add("Category title");
            headerColList.add("Source id");
            headerColList.add("Source name");
            headerColList.add("Create at");
            headerColList.add("Update at");


            Row header = sheet.createRow(0);
            for (int i = 0; i < headerColList.size(); i++) {

                header.createCell(i).setCellValue(headerColList.get(i));
            }

            int rowIndex = 1;
            for (ExcelDownloadData data : list) {

                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(data.getTransactionId());
                row.createCell(1).setCellValue(data.getTransactionDt());
                row.createCell(2).setCellValue(data.getAmount());
                row.createCell(3).setCellValue(data.getNote());
                row.createCell(4).setCellValue(data.getCategoryId());
                row.createCell(5).setCellValue(data.getCategoryTitle());
                row.createCell(6).setCellValue(data.getSourceId());
                row.createCell(7).setCellValue(data.getSourceName());
                row.createCell(8).setCellValue(data.getInsDttm());
                row.createCell(9).setCellValue(data.getUpdDttm());
            }

            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "demo.xlsx"
            );

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
            workbook.close();
            Toast.makeText(getContext(), "Xuất thành công: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Xuất thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void initWidgets(View view) {


        excelBtn = view.findViewById(R.id.excelBtn);
        excelBtn.setOnClickListener(view1 -> {

            ArrayList<ExcelDownloadData> excelDownloadData = new ArrayList<>();
            Cursor cursor = databaseHelper.transactionExcelData();
            if (cursor != null && cursor.moveToFirst()) {

                do {

                    ExcelDownloadData data = new ExcelDownloadData();
                    data.setTransactionId(cursor.getInt(0));
                    data.setAmount(cursor.getInt(1));
                    data.setNote(cursor.getString(2));
                    data.setCategoryId(cursor.getInt(3));
                    data.setCategoryTitle(cursor.getString(4));
                    data.setTransactionDt(cursor.getString(5));
                    data.setSourceId(cursor.getInt(6));
                    data.setSourceName(cursor.getString(7));
                    data.setInsDttm(cursor.getString(8));
                    data.setUpdDttm(cursor.getString(9));
                    excelDownloadData.add(data);
                } while (cursor.moveToNext());
            }
            exportExcel(excelDownloadData);
        });
    }

}