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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment{

    private RecyclerView recyclerView;

    private List<adapter> adapters = new ArrayList<>();

    private List<String> uidLists = new ArrayList<>();
    private FirebaseDatabase database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        database = FirebaseDatabase.getInstance();

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.homefragment_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();
        recyclerView.setAdapter(boardRecyclerViewAdapter);

        database.getReference().child("writings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adapters.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    adapter adapter = snapshot.getValue(adapter.class);
                    adapters.add(adapter);
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

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((CustomViewHolder)holder).title.setText(adapters.get(position).ad_title);
            ((CustomViewHolder)holder).money.setText(adapters.get(position).ad_money);
            ((CustomViewHolder)holder).content.setText(adapters.get(position).ad_content);

            Glide.with(getActivity()).load(adapters.get(position).imageUrl).into(((CustomViewHolder) holder).imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   putPreferences(getActivity(), "title", adapters.get(position).ad_title);
                   putPreferences(getActivity(), "money", adapters.get(position).ad_money);
                   putPreferences(getActivity(), "content", adapters.get(position).ad_content);
                   putPreferences(getActivity(), "imageUrl", adapters.get(position).imageUrl);
                   putPreferences(getActivity(), "useruid", adapters.get(position).ad_useruid);


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

}