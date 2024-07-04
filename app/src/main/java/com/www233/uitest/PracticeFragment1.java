package com.www233.uitest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeFragment1 extends Fragment {

    private static final String TAG = "Practice";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static PracticeFragment1 newInstance() {

        Bundle args = new Bundle();

        PracticeFragment1 fragment = new PracticeFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_practice1, container, false);
        Bundle bundle = this.getArguments();
        Log.e(TAG, "onCreate: Bundle??");
        if (bundle != null) {
            Log.e(TAG, "onCreate: 开始Bundle");
            String text = bundle.getString("text");

            TextView tv = v.findViewById(R.id.tv);
            tv.setText(text);
        }
        return v;
    }
}