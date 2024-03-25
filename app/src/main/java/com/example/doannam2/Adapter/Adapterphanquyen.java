package com.example.doannam2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doannam2.Activity.DetailActivity;
import com.example.doannam2.Detailphanquyen;
import com.example.doannam2.R;
import com.example.doannam2.model.dataclass;

import java.util.ArrayList;
import java.util.List;

public class Adapterphanquyen extends RecyclerView.Adapter<MyViewHolderphanquyen>{
    private Context context;

    private List<dataclass> dataListphanquyen;

    public Adapterphanquyen(Context context, List<dataclass> dataListphanquyen) {
        this.context = context;
        this.dataListphanquyen = dataListphanquyen;
    }
    @NonNull
    @Override
    public MyViewHolderphanquyen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolderphanquyen(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderphanquyen holder, int position) {
        Glide.with(context).load(dataListphanquyen.get(position).getDataimage()).into(holder.recImage);
        holder.recTitle.setText(dataListphanquyen.get(position).getDataTitle());
        holder.recDesc.setText(dataListphanquyen.get(position).getDataDesc());
        holder.recLang.setText(String.valueOf(dataListphanquyen.get(position).getDatalang()));

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detailphanquyen.class);
                intent.putExtra("Image", dataListphanquyen.get(holder.getAdapterPosition()).getDataimage());
                intent.putExtra("Description", dataListphanquyen.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Title", dataListphanquyen.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("key",dataListphanquyen.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Language", dataListphanquyen.get(holder.getAdapterPosition()).getDatalang());
                context.startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataListphanquyen.size();
    }
    public void searchDataList(ArrayList<dataclass> searchList){
        dataListphanquyen = searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolderphanquyen extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitle, recDesc, recLang;
    CardView recCard;

    public MyViewHolderphanquyen(@NonNull View itemView) {
        super(itemView);
        ontroll();
    }

    private void ontroll() {
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recLang = itemView.findViewById(R.id.recLang);
        recTitle = itemView.findViewById(R.id.recTitle);

    }
}

