package com.example.talk.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.talk.R;
import com.example.talk.Reading_category;

public class CategoryFragment extends Fragment {

    TextView ct1;
    TextView ct2;
    TextView ct3;
    TextView ct4;
    TextView ct5;
    TextView ct6;
    TextView ct7;
    TextView ct8;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ct1 = (TextView) view.findViewById(R.id.ct1);
        ct2 = (TextView) view.findViewById(R.id.ct1);
        ct3 = (TextView) view.findViewById(R.id.ct1);
        ct4 = (TextView) view.findViewById(R.id.ct1);
        ct5 = (TextView) view.findViewById(R.id.ct1);
        ct6 = (TextView) view.findViewById(R.id.ct1);
        ct7 = (TextView) view.findViewById(R.id.ct1);
        ct8 = (TextView) view.findViewById(R.id.ct1);

        ct1.setOnClickListener(ocl);
        ct2.setOnClickListener(ocl);
        ct3.setOnClickListener(ocl);
        ct4.setOnClickListener(ocl);
        ct5.setOnClickListener(ocl);
        ct6.setOnClickListener(ocl);
        ct7.setOnClickListener(ocl);
        ct8.setOnClickListener(ocl);

        ct1.setOnClickListener(new View.OnClickListener());
        return view;

    }
    TextView.OnClickListener ocl = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            intent = new Intent(getActivity(),Reading_category.class);
            switch(v.getId()){
                case R.id.ct1:
                    intent.putExtra("category",ct1.getText().toString());
                    break;
                case R.id.ct2:
                    intent.putExtra("category",ct2.getText().toString());
                    break;
                case R.id.ct3:
                    intent.putExtra("category",ct3.getText().toString());
                    break;
                case R.id.ct4:
                    intent.putExtra("category",ct4.getText().toString());
                    break;
                case R.id.ct5:
                    intent.putExtra("category",ct5.getText().toString());
                    break;
                case R.id.ct6:
                    intent.putExtra("category",ct6.getText().toString());
                    break;
                case R.id.ct7:
                    intent.putExtra("category",ct7.getText().toString());
                    break;
                case R.id.ct8:
                    intent.putExtra("category",ct8.getText().toString());
                    break;
            }
            startActivity(intent);
        }
    };
}