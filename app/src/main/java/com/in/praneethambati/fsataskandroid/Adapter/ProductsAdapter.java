package com.in.praneethambati.fsataskandroid.Adapter;

import android.app.Activity;
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

public class ProductsAdapter  extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    public List<ProductsModel> data;
    private ArrayList<ProductsModel> arraylist;

    ProductsModel current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public ProductsAdapter(Context context, List<ProductsModel> data){
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
            convertView = inflater.inflate(R.layout.container_product, null);
            holder = new ViewHolder();
            holder.icon = (ImageView)convertView.findViewById(R.id.lv_image);
            holder.pName = (TextView)convertView.findViewById(R.id.lv_pname);
            holder.pCategory = (TextView)convertView.findViewById(R.id.lv_category);
            holder.pPrice = (TextView)convertView.findViewById(R.id.lv_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //
        holder.pName.setText(data.get(position).getpName());
        holder.pCategory.setText(data.get(position).getpCategory());
        holder.pPrice.setText("$"+data.get(position).getpPrice().toString());

        if (holder.icon != null) {
            Glide.with(context).load(data.get(position).getpImgUrl())
                    .placeholder(R.mipmap.ic_img_error)
                    .error(R.mipmap.ic_img_error)
                    .into(holder.icon);
        }




        return convertView;
    }

    // Filter Class .. Can search with Firstname, Lastname, Title
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(arraylist);
        } else {
            for (ProductsModel wp : arraylist) {
                if (wp.getpName().toLowerCase(Locale.getDefault())
                        .contains(charText) || wp.getpCategory().toLowerCase(Locale.getDefault()).contains(charText)
                        ) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }





    class ViewHolder {
        ImageView icon;
        TextView pName,pPrice,pCategory;

    }


}
