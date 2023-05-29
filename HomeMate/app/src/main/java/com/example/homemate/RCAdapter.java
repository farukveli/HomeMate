package com.example.homemate;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemate.R;
import com.example.homemate.RCModel;
import com.example.homemate.RecyclerViewInterface;

import java.util.ArrayList;

public class RCAdapter extends RecyclerView.Adapter<RCAdapter.RCViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<RCModel> modelArrayList;
    private int selected_item =-1;
    public RCAdapter(Context context, ArrayList<RCModel> modelArrayList,RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rc_item,parent,false);
        return new RCViewHolder(v,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        RCModel rcModel = modelArrayList.get(position);
        holder.rc_title.setText(rcModel.getTitle());
        if(rcModel.getImage() != null){
            Glide.with(context).load(rcModel.getImage().toString()).into(holder.rc_image);
        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class RCViewHolder extends RecyclerView.ViewHolder {
        ImageView rc_image;
        TextView rc_title;
        public RCViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            rc_image = itemView.findViewById(R.id.rc_image);
            rc_title = itemView.findViewById(R.id.rc_title);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }

}