package com.khanh.expensemanagement.naiji;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.m_name.kbn.SourcePaymentClass;
import com.khanh.expensemanagement.m_name.view.MNameAdapter;

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
        m_name_source = view.findViewById(R.id.m_name_source);
        m_name_source.setFocusable(false);
        m_name_source.setClickable(true);
        m_name_source.setOnClickListener(view_m_name -> {

            showBottomDialog(SourcePaymentClass.NAME_IDENT_CD, true);
        });

        m_name_category = view.findViewById(R.id.m_name_category);
        m_name_category.setFocusable(false);
        m_name_category.setClickable(true);
        m_name_category.setOnClickListener(view_m_name -> {

            showBottomCategoryDialog(CategoryClass.NAME_IDENT_CD, false);
        });
    }

    private void showBottomCategoryDialog(String nameIdentCd, Boolean enableCellIcon) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.m_name_bottom);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // add data dialog
        m_name_recycler_view = dialog.findViewById(R.id.m_name_recycler_view);
        m_name_title = dialog.findViewById(R.id.m_name_title);
        m_name_title.setText("カテゴリー");

        MNameAdapter mNameAdapter = new MNameAdapter(getContext(), getActivity(), nameIdentCd, enableCellIcon, (position, mName, view) -> {
            Toast.makeText(getContext(), "Clicked: " + mName.getNameCd(), Toast.LENGTH_SHORT).show();
            m_name_category.setText(mName.getNameSs());

            dialog.dismiss(); // close dialog
        });

        m_name_recycler_view.setAdapter(mNameAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        m_name_recycler_view.setLayoutManager(layoutManager);

        // display dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showBottomDialog(String nameIdentCd, Boolean enableCellIcon) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.m_name_bottom);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // add data dialog
        m_name_recycler_view = dialog.findViewById(R.id.m_name_recycler_view);
        m_name_title = dialog.findViewById(R.id.m_name_title);
        m_name_title.setText("支払い方");

        MNameAdapter mNameAdapter = new MNameAdapter(getContext(), getActivity(), nameIdentCd, enableCellIcon, (position, mName, view) -> {
            Toast.makeText(getContext(), "Clicked: " + mName.getNameCd(), Toast.LENGTH_SHORT).show();
            m_name_source.setText(mName.getNameSs());

            dialog.dismiss(); // close dialog
        });

        m_name_recycler_view.setAdapter(mNameAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        m_name_recycler_view.setLayoutManager(layoutManager);

        // display dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}