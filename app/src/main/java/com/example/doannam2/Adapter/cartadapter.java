package com.example.doannam2.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.doannam2.Activity.DetailActivity;
import com.example.doannam2.Activity.manhinhchinh;
import com.example.doannam2.R;
import com.example.doannam2.model.Cartdata;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class cartadapter extends RecyclerView.Adapter<MyCartHolder> {
    private Context contextcart;
    private List<Cartdata> cartdata;
    private DatabaseReference databaseReference;



    public cartadapter(Context contextcart, List<Cartdata> cartdata,DatabaseReference databaseReference) {
        this.contextcart = contextcart;
        this.cartdata = cartdata;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public MyCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new MyCartHolder(view,databaseReference);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartHolder holder, int position) {
        Glide.with(contextcart).load(cartdata.get(position).getDataimage()).into(holder.Productimage);
        holder.ProductTitle.setText(cartdata.get(position).getDataTitle());
        holder.ProductPrice.setText(String.valueOf(cartdata.get(position).getDataPrice()));
        Cartdata cartItem = cartdata.get(position);
        holder.setData(cartItem);
        holder.setData(cartdata.get(position));
    }
   
    @Override
    public int getItemCount() {
        return cartdata.size();
    }

}
class MyCartHolder extends RecyclerView.ViewHolder {
    DatabaseReference databaseReference;
    ImageView Productimage;
    TextView ProductTitle, ProductPrice,ProductQuantity;
    TextView totalAmount;
    cartadapter cartAdapter;
    Button btnMinus,btnPlus;
    Cartdata cartItem;
    TextView deteleCart;
    String key = "";
    public MyCartHolder(@NonNull View itemView,DatabaseReference databaseReference) {
        super(itemView);

        this.databaseReference = databaseReference;
        cartcontrol();
    }
    public void setData(Cartdata cartItem) {
        this.cartItem = cartItem;
        Glide.with(itemView.getContext()).load(cartItem.getDataimage()).into(Productimage);
        ProductTitle.setText(cartItem.getDataTitle());
        ProductPrice.setText(String.valueOf(cartItem.getDataPrice()));
        ProductQuantity.setText(String.valueOf(cartItem.getQuantity()));
    }


    private void cartcontrol() {
        Productimage = itemView.findViewById(R.id.productImage);
        ProductTitle = itemView.findViewById(R.id.productName);
        ProductPrice = itemView.findViewById(R.id.productPrice);
        ProductQuantity = itemView.findViewById(R.id.productQuantity);
        deteleCart=itemView.findViewById(R.id.deteleCart);
        btnMinus = itemView.findViewById(R.id.btnMinus);
        btnPlus = itemView.findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("cart");
                int currentQuantity = cartItem.getQuantity();
                int newQuantity = currentQuantity + 1;
                cartItem.setQuantity(newQuantity);


                databaseReference.child(cartItem.getKey()).child("quantity").setValue(newQuantity)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Cập nhật số lượng thành công, giờ cập nhật giá
                                int currentPrice = cartItem.getDataPrice();
                                int newPrice = currentPrice * newQuantity;
                                cartItem.setDataPrice(newPrice);
                                ProductQuantity.setText(String.valueOf(newQuantity));
                                databaseReference.child(cartItem.getKey()).child("totalPrice").setValue(newPrice);

                            }
                        });
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("cart");
                int currentQuantity = cartItem.getQuantity();
                int newQuantity = Math.max(currentQuantity - 1, 0);
                cartItem.setQuantity(newQuantity);

                // Cập nhật số lượng trong Firebase

                databaseReference.child(cartItem.getKey()).child("quantity").setValue(newQuantity)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Cập nhật số lượng thành công, giờ cập nhật giá
                                int currentPrice = cartItem.getDataPrice();
                                int newPrice = currentPrice * newQuantity;
                                cartItem.setDataPrice(newPrice);
                                databaseReference.child(cartItem.getKey()).child("totalPrice").setValue(newPrice);
                                ProductQuantity.setText(String.valueOf(newQuantity));
                            }
                        });
            }
        });
        deteleCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cart");

                String cartItemKey = cartItem.getKey();

                reference.child(cartItemKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(itemView.getContext(), "Mục đã được xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


}

