package com.dashuai.oclick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

import  com.dashuai.oclick.Needle;

import com.dashuai.oclick.Digit;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link needleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class needleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Needle needle;
    private Digit digit;

    public needleFragment() {
        // Required empty public constructor
    }
    public needleFragment(String arg){
        mParam1=arg;
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment needleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static needleFragment newInstance(String param1) {
        needleFragment fragment = new needleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // needlre=container.findViewById(R.id.clock);
        View view=inflater.inflate(R.layout.fragment_needle, container, false);
        needle=view.findViewById(R.id.clock);
        return view;
    }
}