package com.in.praneethambati.fsataskandroid.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.in.praneethambati.fsataskandroid.Model.ProductsModel;
import com.in.praneethambati.fsataskandroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Praneeth Ambati on 3/18/2017.
 */

public class CartAdapter  extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    public List<ProductsModel> data;
    private ArrayList<ProductsModel> arraylist;
    double totalCalcPriceCount=0;
    ProductsModel current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public CartAdapter(Context context, List<ProductsModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;

        this.arraylist = new ArrayList<ProductsModel>();
        this.arraylist.addAll(data);
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.container_cart, null);
            holder = new ViewHolder();
            holder.icon = (ImageView)convertView.findViewById(R.id.lv_imageCart);
            holder.pName = (TextView)convertView.findViewById(R.id.lv_pnameCart);
            holder.pQuantity = (TextView)convertView.findViewById(R.id.lv_quantityCart);
            holder.pUnitPrice = (TextView)convertView.findViewById(R.id.lv_priceCart);
            holder.totalPrice = (TextView)convertView.findViewById(R.id.lv_totalPriceCart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //
        holder.pName.setText(data.get(position).getpName());
        holder.pQuantity.setText(String.valueOf(data.get(position).getpQuantity()));
        holder.pUnitPrice.setText("$"+data.get(position).getpPrice().toString());
        double totalPriceQuanity = Integer.parseInt(holder.pQuantity.getText().toString()) * data.get(position).getpPrice();
        holder.totalPrice.setText("$"+String.format("%.2f",totalPriceQuanity));

        if (holder.icon != null) {
            Glide.with(context).load(data.get(position).getpImgUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_img_error)
                    .into(holder.icon);
        }

        return convertView;
    }


    class ViewHolder {
        ImageView icon;
        TextView pName,pQuantity,pUnitPrice,totalPrice;

    }


}
