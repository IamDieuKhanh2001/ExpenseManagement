package com.khanh.expensemanagement.component;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.m_name.view.MName;
import com.khanh.expensemanagement.m_name.view.MNameAdapter;

import java.util.ArrayList;

public class CustomSelectBox extends LinearLayout {

    Context context;

    // custom attrs
    private Boolean isRequired;
    private String nameIdentCd;
    private Boolean enableCellIcon;
    private String title;

    // Widgets
    private EditText m_name_text;
    private ImageView m_name_icon;

    // mName data
    private ArrayList<MName> mNameDataList;

    private Integer selectedId;

    public CustomSelectBox(Context context) {
        super(context);
        this.context = context;
        initWidgets(null, context);
        initData(context);
    }

    public CustomSelectBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initWidgets(attrs, context);
        initData(context);
    }

    public CustomSelectBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initWidgets(attrs, context);
        initData(context);
    }

    private void initWidgets(AttributeSet attrs, Context context) {

        LayoutInflater.from(context).inflate(R.layout.custom_select_box, this, true);

        m_name_text = findViewById(R.id.m_name_text);
        m_name_text.setFocusable(false);
        m_name_text.setClickable(true);
        m_name_icon = findViewById(R.id.m_name_icon);
        m_name_icon.setVisibility(View.GONE);

        if (attrs != null) {

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSelectBox);
            nameIdentCd = a.getString(R.styleable.CustomSelectBox_nameIdentCd);
            enableCellIcon = a.getBoolean(R.styleable.CustomSelectBox_enableCellIcon, false);
            title = a.getString(R.styleable.CustomSelectBox_title);
            isRequired = a.getBoolean(R.styleable.CustomSelectBox_isRequired, false);

            a.recycle();
        }

        if (isRequired) {

            m_name_text.setHintTextColor(context.getColor(R.color.red));
            m_name_text.setHint(title.concat(" [" + context.getString(R.string.required) + "]"));
        } else {

            m_name_text.setHint(title);
        }
    }

    private void initData(Context context) {

        mNameDataList = new ArrayList<>();

        if (nameIdentCd != null) {

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            Cursor cursor = databaseHelper.mNameFindAll(nameIdentCd);
            if (cursor != null && cursor.moveToFirst()) {

                do {
                    MName mName = new MName();
                    mName.setNameIdentCd(cursor.getString(0));
                    mName.setNameCd(cursor.getString(1));
                    mName.setNameIdentName(cursor.getString(2));
                    mName.setNameSs(cursor.getString(3));
                    mName.setDrawableIconUrl(cursor.getString(4));
                    mNameDataList.add(mName);
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }
        }

        m_name_text.setOnClickListener(view -> {

            showBottomDialog(context);
        });
    }

    private void showBottomDialog(Context context) {

        RecyclerView m_name_recycler_view;
        TextView m_name_title;

        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.m_name_bottom);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // add data dialog
        m_name_recycler_view = dialog.findViewById(R.id.m_name_recycler_view);
        m_name_title = dialog.findViewById(R.id.m_name_title);
        m_name_title.setText(title);

        MNameAdapter mNameAdapter = new MNameAdapter(context, mNameDataList, enableCellIcon, (position, mName, view) -> {

            selectedId = Integer.valueOf(mName.getNameCd());
            m_name_text.setText(mName.getNameSs());
            // Enable icon
            m_name_icon.setVisibility(View.VISIBLE);
            if (mName.getDrawableIconUrl() != null) {

                int imageResId = this.getResources().getIdentifier(mName.getDrawableIconUrl(), "drawable", context.getPackageName());
                if (imageResId != 0) {

                    m_name_icon.setImageResource(imageResId);
                }
            } else {

                m_name_icon.setImageResource(R.drawable.ic_no_image);
            }
            dialog.dismiss(); // close dialog
        });

        m_name_recycler_view.setAdapter(mNameAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 4);
        m_name_recycler_view.setLayoutManager(layoutManager);

        // display dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public String getNameIdentCd() {
        return nameIdentCd;
    }

    public void setNameIdentCd(String nameIdentCd) {
        this.nameIdentCd = nameIdentCd;
        initData(context);
    }

    public Boolean getEnableCellIcon() {
        return enableCellIcon;
    }

    public void setEnableCellIcon(Boolean enableCellIcon) {
        this.enableCellIcon = enableCellIcon;
    }

    public Integer getSelectedId() {

        return selectedId;
    }

    public void setSelectedId(Integer selectedId) {

        this.selectedId = selectedId;
        MName mNameSelected = mNameDataList.stream()
                .filter(mName -> selectedId.toString().equals(mName.getNameCd()))
                .findFirst()
                .orElse(null);

        if (mNameSelected != null) {

            m_name_text.setText(mNameSelected.getNameSs());
            if (mNameSelected.getDrawableIconUrl() != null) {
                int imageResId = this.getResources().getIdentifier(mNameSelected.getDrawableIconUrl(), "drawable", context.getPackageName());
                if (imageResId != 0) {

                    m_name_icon.setVisibility(View.VISIBLE);
                    m_name_icon.setImageResource(imageResId);
                } else {

                    m_name_icon.setVisibility(View.GONE);
                }
            }
        } else {

            this.selectedId = null;
        }
    }

    public void addTextChangedListener(TextWatcher textWatcher) {

        if (m_name_text != null) {
            m_name_text.addTextChangedListener(textWatcher);
        }
    }
}
