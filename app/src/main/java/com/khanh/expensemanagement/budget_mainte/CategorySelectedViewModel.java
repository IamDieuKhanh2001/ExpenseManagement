package com.khanh.expensemanagement.budget_mainte;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CategorySelectedViewModel extends ViewModel {

    private MutableLiveData<Integer> categoryIdSelected = new MutableLiveData<>(-1);

    public CategorySelectedViewModel() {
    }

    public LiveData<Integer> getCategoryIdSelected() {
        return categoryIdSelected;
    }

    public void setCategoryIdSelected(int categoryId) {
        categoryIdSelected.setValue(categoryId);
    }
}
