package com.example.talk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.talk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiSeong Nam on 2017-12-01.
 */

public class Reading_search extends Fragment {

    private RecyclerView recyclerView;

    private List<adapter> adapters = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();
    private FirebaseDatabase database;
    public String searchstr;
    ImageView bt_back;

    public TextView newsearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences pref = getActivity().getSharedPreferences("search",getActivity().MODE_PRIVATE);
        searchstr = pref.getString("search", "");

        removeAllPreferences(getActivity());

        String searchtemp = (" '"+searchstr+ "' 로 검색한 결과 ");

        View view = inflater.inflate(R.layout.searchlayout,container,false);

        bt_back = (ImageView)view.findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        newsearch =(TextView) view.findViewById(R.id.newsearch);
        newsearch.setText(searchtemp);

        database = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView)view.findViewById(R.id.searchfragemnt_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();
        recyclerView.setAdapter(boardRecyclerViewAdapter);

        Query query = database.getReference().child("writings").orderByChild("title");
            //search 조건 구성해야함.
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    adapters.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        adapter adapter = snapshot.getValue(adapter.class);
                        if(adapter.ad_title.contains(searchstr))
                            adapters.add(0,adapter);
                    }
                    boardRecyclerViewAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        return view;
    }

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);

            return new Reading_search.BoardRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((Reading_search.BoardRecyclerViewAdapter.CustomViewHolder)holder).title.setText(adapters.get(position).ad_title);
            ((Reading_search.BoardRecyclerViewAdapter.CustomViewHolder)holder).money.setText(adapters.get(position).ad_money);
            ((Reading_search.BoardRecyclerViewAdapter.CustomViewHolder)holder).content.setText(adapters.get(position).ad_content);
            Glide.with(getActivity()).load(adapters.get(position).imageUrl).into(((Reading_search.BoardRecyclerViewAdapter.CustomViewHolder) holder).imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    putPreferences(getActivity(), "title", adapters.get(position).ad_title);
                    putPreferences(getActivity(), "money", adapters.get(position).ad_money);
                    putPreferences(getActivity(), "content", adapters.get(position).ad_content);
                    putPreferences(getActivity(), "imageUrl", adapters.get(position).imageUrl);
                    putPreferences(getActivity(), "useruid", adapters.get(position).ad_useruid);
                    putPreferences(getActivity(), "category", adapters.get(position).ad_category);

                    getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new ReadingFragment()).commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return adapters.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView title;
            TextView money;
            TextView content;

            public CustomViewHolder(View view){
                super(view);
                imageView = (ImageView)view.findViewById(R.id.imageView);
                title = (TextView)view.findViewById(R.id.item_title);
                money = (TextView)view.findViewById(R.id.item_money);
                content =(TextView)view.findViewById(R.id.item_content);
            }
        }
    }

    private void putPreferences(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("adapter", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("search", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}