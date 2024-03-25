package com.example.doannam2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doannam2.R;
import com.example.doannam2.model.Cartdata;
import com.example.doannam2.model.dataclass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterHoadon extends RecyclerView.Adapter<MyViewHolderhoadon>{
    private Context contextcart;
    private List<Cartdata> cartdata;
    private DatabaseReference databaseReference;
    public AdapterHoadon(Context contextcart, List<Cartdata> cartdata,DatabaseReference databaseReference) {
        this.contextcart = contextcart;
        this.cartdata = cartdata;
        this.databaseReference = databaseReference;
    }
    @NonNull
    @Override
    public MyViewHolderhoadon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layouthoadon, parent, false);
        return new MyViewHolderhoadon(view,databaseReference);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderhoadon holder, int position) {
        Glide.with(contextcart).load(cartdata.get(position).getDataimage()).into(holder.Productimage);
        holder.ProductTitle.setText(cartdata.get(position).getDataTitle());
        holder.ProductPrice.setText(String.valueOf(cartdata.get(position).getDataPrice()));
        holder.ProductQuantity.setText(String.valueOf(cartdata.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartdata.size();
    }
}
class MyViewHolderhoadon extends RecyclerView.ViewHolder {
    DatabaseReference databaseReference;
    ImageView Productimage;
    TextView ProductTitle, ProductPrice,ProductQuantity;

    public MyViewHolderhoadon(@NonNull View itemView, DatabaseReference databaseReference) {
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
