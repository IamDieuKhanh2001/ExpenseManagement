package com.khanh.expensemanagement.budget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.util.FormUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

class BudgetCategoryAdapter extends RecyclerView.Adapter<BudgetCategoryViewHolder>{

    private Context context;
    private Activity activity;
    private final List<BudgetCategory> budgetCategoryList;

    public BudgetCategoryAdapter(Context context, Activity activity, List<BudgetCategory> budgetCategoryList) {
        this.context = context;
        this.activity = activity;
        this.budgetCategoryList = budgetCategoryList;
    }

    @NonNull
    @Override
    public BudgetCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.budget_category_row, parent, false);
        return new BudgetCategoryViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetCategoryViewHolder holder, int position) {

        BudgetCategory budgetCategory;
        BigInteger remainingAmount;
        BigDecimal remainingPercentage;

        budgetCategory = budgetCategoryList.get(position);

        if (budgetCategory.getLimitAmount().compareTo(budgetCategory.getSpentAmount()) < 0 || budgetCategory.getLimitAmount().signum() != 1) {

            remainingPercentage = BigDecimal.valueOf(0);
        } else {

            remainingPercentage = new BigDecimal(budgetCategory.getLimitAmount().subtract(budgetCategory.getSpentAmount()))
                    .multiply(BigDecimal.valueOf(100))
                    .divide(new BigDecimal(budgetCategory.getLimitAmount()), 2, RoundingMode.HALF_UP);
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(100 - remainingPercentage.floatValue(), "")); // Phần còn lại
        entries.add(new PieEntry(remainingPercentage.floatValue(), "")); // Tiến trình

        PieDataSet dataSet = new PieDataSet(entries, "");
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.LTGRAY);
        colors.add(ContextCompat.getColor(context, R.color.budgetChartColor));
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(false);

        PieData pieData = new PieData(dataSet);
        holder.budget_category_chart.setData(pieData);
        holder.budget_category_chart.setExtraOffsets(1, 1, 1, 1);
        holder.budget_category_chart.setHoleRadius(70f); // Độ lớn phần trống ở giữa
        holder.budget_category_chart.setTransparentCircleRadius(0f);
        holder.budget_category_chart.setDrawEntryLabels(false);
        holder.budget_category_chart.setRotationEnabled(false);
        holder.budget_category_chart.getDescription().setEnabled(false);
        holder.budget_category_chart.getLegend().setEnabled(false); // Ẩn legend
        holder.budget_category_chart.setTouchEnabled(false);
        holder.budget_category_chart.setRenderer(new PieChartRenderer(holder.budget_category_chart, holder.budget_category_chart.getAnimator(), holder.budget_category_chart.getViewPortHandler()) {
            @Override
            public void drawExtras(Canvas c) {
                super.drawExtras(c);

                // Load ảnh
                Bitmap bitmap = getBitmapFromDrawableName(budgetCategory.getDrawableIconUrl());

                if (bitmap != null) {

                    // Lấy trung tâm của PieChart
                    MPPointF center = holder.budget_category_chart.getCenterCircleBox();

                    // Kích thước của lỗ trống
                    float holeRadius = holder.budget_category_chart.getHoleRadius() / 100f * holder.budget_category_chart.getRadius();

                    // Kích thước ảnh (tùy chỉnh nhỏ lại nếu cần)
                    int bitmapSize = (int) (holeRadius * 1.2f); // Đảm bảo ảnh vừa với lỗ trống
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, true);

                    // Tọa độ vẽ ảnh (trung tâm)
                    float x = center.x - (bitmap.getWidth() / 2f);
                    float y = center.y - (bitmap.getHeight() / 2f);

                    // Vẽ ảnh vào trung tâm
                    c.drawBitmap(bitmap, x, y, null);

                    // Giải phóng bộ nhớ
                    MPPointF.recycleInstance(center);
                }
            }
        });
        holder.budget_category_chart.invalidate(); // Refresh PieChart

        holder.category_name_tv.setText(budgetCategory.getCategoryName().toString());
        remainingAmount = budgetCategory.getLimitAmount().subtract(budgetCategory.getSpentAmount());
        if (remainingAmount.compareTo(BigInteger.ZERO) < 0) {

            remainingAmount = remainingAmount.abs();
            holder.remaining_title.setText(context.getString(R.string.over_spent));
            holder.over_spent_icon.setVisibility(View.VISIBLE);
            holder.remaining_amount.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {

            holder.remaining_title.setText(context.getString(R.string.remaining));
            holder.over_spent_icon.setVisibility(View.GONE);
            holder.remaining_amount.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        holder.remaining_amount.setText(FormUtil.currencyFormat(context, remainingAmount));
        holder.total_spent_amount.setText(FormUtil.currencyFormat(context,budgetCategory.getSpentAmount()));
        holder.budget_amount.setText(FormUtil.currencyFormat(context,budgetCategory.getLimitAmount()));
    }

    private Bitmap getBitmapFromDrawableName(String drawableName) {
        if (drawableName == null || drawableName.isEmpty()) {
            return null;
        }

        int imageResId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        if (imageResId != 0) {
            return BitmapFactory.decodeResource(context.getResources(), imageResId);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return budgetCategoryList.size();
    }
}
