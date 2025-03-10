package com.khanh.expensemanagement.budget_mainte;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

import java.util.ArrayList;
import java.util.List;

class BudgetCategorySelectAdapter extends RecyclerView.Adapter<BudgetCategorySelectViewHolder> {

    private int selectedPosition = -1;
    private Context context;
    private Activity activity;
    private List<Category> categoryList;
    private final OnCategorySelectedListener onCategorySelectedListener;
    private List<Integer> disablePositionList = new ArrayList<>();

    public BudgetCategorySelectAdapter(Context context, Activity activity, List<Category> categoryList, OnCategorySelectedListener onCategorySelectedListener) {
        this.context = context;
        this.activity = activity;
        this.categoryList = categoryList;
        this.onCategorySelectedListener = onCategorySelectedListener;
    }

    @NonNull
    @Override
    public BudgetCategorySelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.radio_budget_category_select_row, parent, false);
        return new BudgetCategorySelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetCategorySelectViewHolder holder, int position) {

        holder.category_name.setText(categoryList.get(position).getCategoryName());
        if (categoryList.get(position).getDrawableIconUrl() != null) {
            int imageResId = context.getResources().getIdentifier(
                    categoryList.get(position).getDrawableIconUrl(), "drawable", context.getPackageName()
            );
            if (imageResId != 0) {
                holder.category_icon.setImageResource(imageResId);
            }
        }
        holder.category_rb.setChecked(position == selectedPosition);

        // Handle RadioButton click listener
        holder.category_rb.setOnClickListener(v -> {
            if (!disablePositionList.contains(position)) {  // Prevent click action if item is disabled
                selectedPosition = position;
                onCategorySelectedListener.onCategorySelected(position, categoryList.get(position).getCategoryId());
                notifyDataSetChanged();
            }
        });

        // Handle item click listener
        holder.itemView.setOnClickListener(v -> {
            if (!disablePositionList.contains(position)) {  // Prevent click action if item is disabled
                selectedPosition = position;
                onCategorySelectedListener.onCategorySelected(position, categoryList.get(position).getCategoryId());
                notifyDataSetChanged();
            }
        });

        // Apply disabled state if the position is in disablePositionList
        if (disablePositionList.contains(position)) {
            // Show 'created' text, hide radio button
            holder.created_category_tv.setVisibility(View.VISIBLE);
            holder.category_rb.setVisibility(View.GONE);

            // Disable click on layout
            holder.itemView.setOnClickListener(null);

            // Blur icon and title of selection
            holder.category_icon.setAlpha(0.5f);
            holder.category_name.setAlpha(0.5f);

            // Disable the radio button's click behavior
            holder.category_rb.setEnabled(false); // Disable radio button interaction
        } else {
            // reset any previously applied disabled states
            holder.created_category_tv.setVisibility(View.GONE);
            holder.category_rb.setVisibility(View.VISIBLE);

            // Re-enable the radio button if it was disabled
            holder.category_rb.setEnabled(true);
            // Restore full opacity
            holder.category_icon.setAlpha(1f);
            holder.category_name.setAlpha(1f);
        }
    }


    public void clearSelection() {

        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    public void disableSelectionAtPositions(List<Integer> positionList) {

        this.disablePositionList = positionList;
        if (positionList.size() > 0) {

            notifyDataSetChanged();
        }
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(int position, Integer categoryIdClick);
    }
}
