package com.example.doannam2.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

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
import com.example.doannam2.Activity.CartActivity;
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


    public cartadapter(Context contextcart, List<Cartdata> cartdata) {
        this.contextcart = contextcart;
        this.cartdata = cartdata;
    }

    @NonNull
    @Override
    public MyCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new MyCartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartHolder holder, int position) {
        Glide.with(contextcart).load(cartdata.get(position).getDataimage()).into(holder.Productimage);
        holder.ProductTitle.setText(cartdata.get(position).getDataTitle());
        holder.ProductPrice.setText(String.valueOf(cartdata.get(position).getDataPrice()));
        Cartdata cartItem = cartdata.get(position);
        holder.setData(cartItem);
        int itemPrice = cartItem.getDataPrice();
        int quantity = cartItem.getQuantity();
        int itemTotal = itemPrice * quantity;
        // Cộng tổng số tiền của từng mục vào tổng số tiền đang chạy

        // Tùy chọn, cập nhật TextView trong adapter để hiển thị tổng số tiền (nếu cần)
        // holder.totalAmountTextView.setText(String.valueOf(totalAmount));  // Ví dụ sử dụng
    }
   
    @Override
    public int getItemCount() {
        return cartdata.size();
    }
}
class MyCartHolder extends RecyclerView.ViewHolder {

    ImageView Productimage;
    TextView ProductTitle, ProductPrice,ProductQuantity;
    TextView totalAmount;
    Button btnMinus,btnPlus;
    Cartdata cartItem;
    Button deteleCart;
    String key ="";
    String imageUrl="";

    public MyCartHolder(@NonNull View itemView) {
        super(itemView);
        cartcontrol();
    }
    public void setData(Cartdata cartItem) {
        this.cartItem = cartItem;
    }

    private void cartcontrol() {
        Productimage = itemView.findViewById(R.id.productImage);
        ProductTitle = itemView.findViewById(R.id.productName);
        ProductPrice = itemView.findViewById(R.id.productPrice);
        ProductQuantity = itemView.findViewById(R.id.productQuantity);
        totalAmount = itemView.findViewById(R.id.totalprice);
        btnMinus = itemView.findViewById(R.id.btnMinus);
        btnPlus = itemView.findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentquantiy= cartItem.getQuantity();
                int newquantity = currentquantiy + 1;
                cartItem.setQuantity(newquantity);
                ProductQuantity.setText(String.valueOf(newquantity));
                int currentPrice = cartItem.getDataPrice();
                int newprice = currentPrice * newquantity;
                cartItem.setDataPrice(newprice);
                ProductPrice.setText(String.valueOf(newprice));

            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentquantiy= cartItem.getQuantity();
                int newquantity = Math.max(currentquantiy - 1,0);

                cartItem.setQuantity(newquantity);
                ProductQuantity.setText(String.valueOf(newquantity));
                int currentPrice = cartItem.getDataPrice();
                int newprice = currentPrice * newquantity;
                cartItem.setDataPrice(newprice);
                ProductPrice.setText(String.valueOf(newprice));
            }
        });

    }


}

