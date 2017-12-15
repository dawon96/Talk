package com.example.talk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.talk.R;

public class CategoryFragment extends Fragment {

    TextView ct1;
    TextView ct2;
    TextView ct3;
    TextView ct4;
    TextView ct5;
    TextView ct6;
    TextView ct7;
    TextView ct8;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ct1 = (TextView) view.findViewById(R.id.ct1);
        ct2 = (TextView) view.findViewById(R.id.ct2);
        ct3 = (TextView) view.findViewById(R.id.ct3);
        ct4 = (TextView) view.findViewById(R.id.ct4);
        ct5 = (TextView) view.findViewById(R.id.ct5);
        ct6 = (TextView) view.findViewById(R.id.ct6);
        ct7 = (TextView) view.findViewById(R.id.ct7);
        ct8 = (TextView) view.findViewById(R.id.ct8);

        ct1.setOnClickListener(ocl);
        ct2.setOnClickListener(ocl);
        ct3.setOnClickListener(ocl);
        ct4.setOnClickListener(ocl);
        ct5.setOnClickListener(ocl);
        ct6.setOnClickListener(ocl);
        ct7.setOnClickListener(ocl);
        ct8.setOnClickListener(ocl);

        return view;

    }

    TextView.OnClickListener ocl = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.ct1:
                    putPreferences(getActivity(), "category", ct1.getText().toString());
                    break;
                case R.id.ct2:
                    putPreferences(getActivity(), "category", ct2.getText().toString());
                    break;
                case R.id.ct3:
                    putPreferences(getActivity(), "category", ct3.getText().toString());
                    break;
                case R.id.ct4:
                    putPreferences(getActivity(), "category", ct4.getText().toString());
                    break;
                case R.id.ct5:
                    putPreferences(getActivity(), "category", ct5.getText().toString());
                    break;
                case R.id.ct6:
                    putPreferences(getActivity(), "category", ct6.getText().toString());
                    break;
                case R.id.ct7:
                    putPreferences(getActivity(), "category", ct7.getText().toString());
                    break;
                case R.id.ct8:
                    putPreferences(getActivity(), "category", ct8.getText().toString());
                    break;
            }

            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainactivity_framelayout,new Reading_category()).commit();

        }
    };

    private void putPreferences(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("category", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

}