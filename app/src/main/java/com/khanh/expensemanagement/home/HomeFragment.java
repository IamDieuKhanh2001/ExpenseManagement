package com.khanh.expensemanagement.home;

import android.database.Cursor;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.util.FragmentUtil;
import com.khanh.expensemanagement.util.db.DatabaseHelper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private final String FRAGMENT_TITLE = "Home";

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private Button previous_month_btn;
    private Button next_month_btn;
    private int previousSelectedPosition = -1;
    private TextView transaction_history_title;
    private RecyclerView transaction_recycler_view;
    DatabaseHelper databaseHelper;

    ArrayList<Integer> totalAmountInDateArray = new ArrayList<>(Collections.nCopies(42, 0));


    private final ArrayList<TransactionHistory> transactionHistoryList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        FragmentUtil.setActionBarTitle(getActivity(), FRAGMENT_TITLE);

        initWidgets(view);
        selectedDate = LocalDate.now();
        setMonthView();
        getDataTransactionList();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        // Add border for selected cell day in recyclerView
        previousSelectedPosition = daysInMonth.indexOf(String.valueOf(selectedDate.getDayOfMonth()));

        calendarRecyclerView.post(() -> {
            // Wait for calendarRecyclerView completely create
            RecyclerView.ViewHolder viewHolder = calendarRecyclerView.findViewHolderForAdapterPosition(previousSelectedPosition);
            if (viewHolder != null) {

                View itemView = viewHolder.itemView;
                View viewById = itemView.findViewById(R.id.calendar_cell_layout);
                viewById.setBackgroundResource(R.drawable.calendar_cell_border);
            }
        });

        createTransactionHistoryTitle();
    }

    public void getDataAmount() {

        Integer totalAmountInDate = 0;

        LocalDate firstDayOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        LocalDate lastDayOfMonth = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth());

        LocalDate currentDate = firstDayOfMonth;
        while (!currentDate.isAfter(lastDayOfMonth)) {
            // Gọi hàm truy vấn với từng ngày
            Cursor cursor = databaseHelper.transactionFindByDate(currentDate);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Lấy dữ liệu từ Cursor
                    int amountColIndex = cursor.getColumnIndex("amount");
                    if (amountColIndex != -1) {

                        Integer currentAmount = cursor.getInt(amountColIndex);
                        totalAmountInDate += currentAmount;
                    }
                } while (cursor.moveToNext());
            }

            // Đóng cursor nếu không còn sử dụng
            if (cursor != null) {
                cursor.close();
            }

            totalAmountInDateArray.set(dayOfWeek + currentDate.getDayOfMonth() - 1, totalAmountInDate);
            totalAmountInDate = 0;

            // Chuyển sang ngày tiếp theo
            currentDate = currentDate.plusDays(1);
        }
    }

    private void initWidgets(View view) {

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        previous_month_btn = view.findViewById(R.id.previous_month_btn);
        next_month_btn = view.findViewById(R.id.next_month_btn);
        transaction_history_title = view.findViewById(R.id.transaction_history_title);
        transaction_recycler_view = view.findViewById(R.id.transaction_recycler_view);

        previous_month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                selectedDate = selectedDate.withDayOfMonth(1);
                setMonthView();
                onResume();
            }
        });
        next_month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                selectedDate = selectedDate.withDayOfMonth(1);
                setMonthView();
                onResume();
            }
        });
    }

    private void setMonthView() {

        totalAmountInDateArray = new ArrayList<>(Collections.nCopies(42, 0));
        getDataAmount();

        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        // Set data for recyclerView
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, totalAmountInDateArray,this);

        // Init DividerItemDecoration for the first time only (The divider between item in recyclerView)
        if (calendarRecyclerView.getItemDecorationCount() == 0) {

            calendarRecyclerView.setHasFixedSize(true);
            calendarRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
            calendarRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.HORIZONTAL));
        }

        // Set layout for recyclerView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {

        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private void createTransactionHistoryTitle() {

        // Transaction on date title
        String dateMonth = String.valueOf(selectedDate).substring(5, 7) + "/" + String.valueOf(selectedDate).substring(8);
        String formattedText = getString(R.string.transaction_history_title, dateMonth);

        // Gán chuỗi đã định dạng vào TextView
        transaction_history_title.setText(formattedText);
    }

    private void getDataTransactionList() {

        transactionHistoryList.add(new TransactionHistory(1, "Test 1", "cate 1", "999.999.999.999"));
        transactionHistoryList.add(new TransactionHistory(1, "Test 2", "cate 1", "999.999"));
        transactionHistoryList.add(new TransactionHistory(1, "Test 3", "cate 3", "999.999"));
        transactionHistoryList.add(new TransactionHistory(1, "Test 3", "cate 3", "999.999"));
        transactionHistoryList.add(new TransactionHistory(1, "Test 3", "cate 3", "999.999"));
        transactionHistoryList.add(new TransactionHistory(1, "Test 3", "cate 3", "999.999"));

        TransactionAdapter transactionAdapter = new TransactionAdapter(requireContext(), getActivity(), transactionHistoryList);
        transaction_recycler_view.setAdapter(transactionAdapter);
        transaction_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(int position, String dayText, View view) {

        ConstraintLayout cell_layout;
        if (!dayText.equals("")) {
            if (previousSelectedPosition != position) {

                RecyclerView.ViewHolder viewHolder = calendarRecyclerView.findViewHolderForAdapterPosition(previousSelectedPosition);
                if (viewHolder != null) {

                    View itemView = viewHolder.itemView;
                    View viewById = itemView.findViewById(R.id.calendar_cell_layout);
                    viewById.setBackground(null);
                }
            }

            selectedDate = selectedDate.withDayOfMonth(Integer.valueOf(dayText));
            Log.d("Selectedday", String.valueOf(selectedDate));
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();

            cell_layout = view.findViewById(R.id.calendar_cell_layout);
            cell_layout.setBackgroundResource(R.drawable.calendar_cell_border);
            // set previous position
            previousSelectedPosition = position;
            // update title
            createTransactionHistoryTitle();
        }
    }
}