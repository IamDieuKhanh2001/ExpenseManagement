package com.khanh.expensemanagement.home;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.khanh.expensemanagement.CalendarAdapter;
import com.khanh.expensemanagement.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private Button previous_month_btn;
    private Button next_month_btn;
    private int previousSelectedPosition = -1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initWidgets(view);
        selectedDate = LocalDate.now();
        setMonthView();

        previous_month_btn = view.findViewById(R.id.previous_month_btn);
        previous_month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });
        next_month_btn = view.findViewById(R.id.next_month_btn);
        next_month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        // Add border for current cell day in recyclerView
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
    }

    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        // Set data for recyclerView
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);

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
            previousSelectedPosition = position;
        }
    }
}