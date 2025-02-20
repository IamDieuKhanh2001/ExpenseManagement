package com.khanh.expensemanagement.component;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.m_name.view.MName;
import com.khanh.expensemanagement.m_name.view.MNameAdapter;
import com.khanh.expensemanagement.trans_mainte.TransRegisterActivity;

import java.util.ArrayList;

public class CustomEditText extends AppCompatEditText {

    private String nameIdentCd;

    private Boolean enableCellIcon = true;

    private DatabaseHelper databaseHelper;

    public CustomEditText(Context context) {
        super(context);
        init(null, context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {

        ArrayList<MName> mNameDataList = new ArrayList<>();

        if (attrs != null) {

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditText);
            nameIdentCd = a.getString(R.styleable.CustomEditText_nameIdentCd);

            databaseHelper = new DatabaseHelper(context);
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

            a.recycle();
        }

        setOnClickListener(view -> {

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
        m_name_title.setText("支払い方");

        // Kiểm soát việc hiển thị để tránh mở nhiều lần
        dialog.setOnShowListener(dialogInterface -> {
            if (dialog.isShowing()) {
                return; // Không làm gì nếu dialog đã hiển thị
            }
        });

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
    }
}
