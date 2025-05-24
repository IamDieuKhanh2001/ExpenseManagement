package com.khanh.expensemanagement.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.util.FragmentUtil;
import com.khanh.expensemanagement.util.LangUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private RadioGroup language_radio_group;
    private Button btn_save;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String FRAGMENT_TITLE = getString(R.string.main_tab_4);
        FragmentUtil.setActionBarTitle(getActivity(), FRAGMENT_TITLE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initWidgets(view);
        getDataDisplay();
        return view;
    }

    private void initWidgets(View view) {

        language_radio_group = view.findViewById(R.id.language_radio_group);
        btn_save = view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(viewBtn -> {
            int selectedId = language_radio_group.getCheckedRadioButtonId();
            String languageCode = "en"; // Default

            if (selectedId == R.id.radio_en) {
                languageCode = "en";
            } else if (selectedId == R.id.radio_ja) {
                languageCode = "ja";
            } else if (selectedId == R.id.radio_vi) {
                languageCode = "vi";
            }

            // Lưu ngôn ngữ vào SharedPreferences
            Boolean saveSuccess = LangUtil.saveLanguage(getActivity(), languageCode);
            if (saveSuccess) {

                // Restart ứng dụng để áp dụng thay đổi
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void getDataDisplay() {

        String savedLanguage = LangUtil.getSavedLanguage(getContext());
        if (savedLanguage.equals("en")) {
            language_radio_group.check(R.id.radio_en);
        } else if (savedLanguage.equals("ja")) {
            language_radio_group.check(R.id.radio_ja);
        } else {
            language_radio_group.check(R.id.radio_vi);
        }
    }
}