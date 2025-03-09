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

        int progress = 70; // Giá trị phần trăm (70%)


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(progress, "")); // Tiến trình
        entries.add(new PieEntry(100 - progress, "")); // Phần còn lại

        PieDataSet dataSet = new PieDataSet(entries, "");
        List<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(context, R.color.pieChartColorOrange));
        colors.add(Color.LTGRAY);
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
        holder.budget_category_chart.setRenderer(new PieChartRenderer(holder.budget_category_chart, holder.budget_category_chart.getAnimator(), holder.budget_category_chart.getViewPortHandler()) {
            @Override
            public void drawExtras(Canvas c) {
                super.drawExtras(c);

                // Load ảnh
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_category_transport);

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
        });
        holder.budget_category_chart.invalidate(); // Refresh PieChart

        holder.category_name_tv.setText(budgetCategoryList.get(position).getCategoryName().toString());
    }

    @Override
    public int getItemCount() {
        return budgetCategoryList.size();
    }
}
