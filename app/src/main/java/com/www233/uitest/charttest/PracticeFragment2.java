package com.www233.uitest.charttest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.www233.uitest.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PracticeFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PracticeFragment2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PracticeFragment2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private IFragmentCallback fragmentcallback;
    View v;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PracticeFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static PracticeFragment2 newInstance(String param1, String param2) {
        PracticeFragment2 fragment = new PracticeFragment2();
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
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_practice2, container, false);
        }

        TextView tv = v.findViewById(R.id.tv);
        Button bt = v.findViewById(R.id.button2);
        bt.setOnClickListener(v -> fragmentcallback.sendToActivity("fragment的消息！！！"));
        String string = fragmentcallback.getFromActivity(null);
        Log.e(TAG, "onCreateView: " + string);
        tv.setText(string);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    public IFragmentCallback getFragmentcallback() {
        return fragmentcallback;
    }

    public void setFragmentcallback(IFragmentCallback fragmentcallback) {
        this.fragmentcallback = fragmentcallback;
    }

}