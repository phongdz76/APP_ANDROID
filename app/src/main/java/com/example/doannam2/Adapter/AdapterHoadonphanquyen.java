package com.example.doannam2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doannam2.R;
import com.example.doannam2.model.Cartdata;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class AdapterHoadonphanquyen extends RecyclerView.Adapter<MyViewHolderhoadonphanquyen>{
    Context contextcartphanquyen;
    List<Cartdata> cartdataphanquyen;
    private DatabaseReference databaseReference;
    public AdapterHoadonphanquyen(Context contextcartphanquyen, List<Cartdata> cartdataphanquyen,DatabaseReference databaseReference) {
        this.contextcartphanquyen = contextcartphanquyen;
        this.cartdataphanquyen = cartdataphanquyen;
        this.databaseReference = databaseReference;
    }
    @NonNull
    @Override
    public MyViewHolderhoadonphanquyen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layouthoadon, parent, false);
        return new MyViewHolderhoadonphanquyen(view,databaseReference);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderhoadonphanquyen holder, int position) {
        Glide.with(contextcartphanquyen).load(cartdataphanquyen.get(position).getDataimage()).into(holder.Productimage);
        holder.ProductTitle.setText(cartdataphanquyen.get(position).getDataTitle());
        holder.ProductPrice.setText(String.valueOf(cartdataphanquyen.get(position).getDataPrice()));
        holder.ProductQuantity.setText(String.valueOf(cartdataphanquyen.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartdataphanquyen.size();
    }
}
class MyViewHolderhoadonphanquyen extends RecyclerView.ViewHolder {
    DatabaseReference databaseReference;
    ImageView Productimage;
    TextView ProductTitle, ProductPrice, ProductQuantity;

    public MyViewHolderhoadonphanquyen(@NonNull View itemView, DatabaseReference databaseReference) {
        super(itemView);

        this.databaseReference = databaseReference;
        cartcontrol();
    }

    private void cartcontrol() {
        Productimage = itemView.findViewById(R.id.productImage);
        ProductTitle = itemView.findViewById(R.id.productName);
        ProductPrice = itemView.findViewById(R.id.productPrice);
        ProductQuantity = itemView.findViewById(R.id.productQuantity);
    }
}
