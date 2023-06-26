package com.example.grocerystore;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductSeller extends RecyclerView.Adapter<AdapterProductSeller.HolderProductSeller> implements Filterable {
    private Context context;
    public ArrayList<ModelProduct> productList,filterList;
    private FilterProduct filter;

    public AdapterProductSeller(Context context,ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }

    @NonNull
    @Override
    public HolderProductSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_seller, parent, false);
        return new HolderProductSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductSeller holder, int position) {
        ModelProduct modelProduct = productList.get(position);
        String id = modelProduct.getProductId();
        String uid = modelProduct.getUid();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountNote();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String productDescription = modelProduct.getProductDescription();
        String productIcon = modelProduct.getProductIcon();
        String productQuantity = modelProduct.getProductQuantity();
        String productTitle = modelProduct.getProductTitle();
        String timestamp = modelProduct.getTimestamp();
        String originalPrice = modelProduct.getOriginalPrice();

        holder.tvTitle.setText(productTitle);
        holder.tvQuantity.setText(productQuantity);
        holder.tvDiscountedNote.setText(discountNote);
        holder.tvDiscountedPrice.setText(discountPrice);
        holder.tvOriginalPrice.setText(originalPrice);
        if (discountAvailable.equals("true")) {
            holder.tvDiscountedPrice.setVisibility(View.VISIBLE);
            holder.tvDiscountedNote.setVisibility(View.VISIBLE);
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); //add strike though on original price
        } else {
            holder.tvDiscountedPrice.setVisibility(View.GONE);
            holder.tvDiscountedNote.setVisibility(View.GONE);
        }
        try {
            Picasso.get().load(productIcon).placeholder(R.drawable.ic_add_shopping_primary).into(holder.ivIconProduct);
        }catch (Exception e){
holder.ivIconProduct.setImageResource(R.drawable.ic_add_shopping_primary);
        }
        holder.itemView.setOnClickListener(this::onItemViewClick);
    }

    private void onItemViewClick(View view) {
        //show item detail
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter= new FilterProduct(this, filterList);
        }
        return null;
    }

    class HolderProductSeller extends RecyclerView.ViewHolder {

        private ImageView ivIconProduct;

        private TextView tvDiscountedNote, tvTitle, tvQuantity, tvDiscountedPrice, tvOriginalPrice;

        private void bindingView() {
            ivIconProduct = itemView.findViewById(R.id.ivIconProduct);
            tvDiscountedNote = itemView.findViewById(R.id.tvDiscountedNote);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvDiscountedPrice = itemView.findViewById(R.id.tvDiscountedPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
        }

        public HolderProductSeller(@NonNull View itemView) {
            super(itemView);
            bindingView();
        }
    }
}
