package com.in.praneethambati.fsataskandroid.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.in.praneethambati.fsataskandroid.Adapter.CartAdapter;
import com.in.praneethambati.fsataskandroid.Adapter.JsonFunction;
import com.in.praneethambati.fsataskandroid.Adapter.ProductsAdapter;
import com.in.praneethambati.fsataskandroid.Model.ProductsModel;
import com.in.praneethambati.fsataskandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayCartActivity extends AppCompatActivity {

    ListView cartLV;
    Button checkoutBtn;
    CartAdapter cartAdapter;
    TextView totalCalPriceCart;
    String username="";

    String url = "http://fsataskambati.atwebpages.com/FSATask/getCart.php";
    JSONObject jsonoBject=null;
    String respone="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cart);

        cartLV= (ListView) findViewById(R.id.cartLV);
        checkoutBtn = (Button) findViewById(R.id.checkoutBtn);

        Intent i = getIntent();
        username = i.getStringExtra("username");

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        tb.setLogo(R.mipmap.ic_launcher);
        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.mipmap.ic_launcher); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);

        JSONObject jsonObjectCart = new JSONObject();

        try {
            jsonObjectCart.put("username",username);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Reg reg = new Reg();
        reg.execute(jsonObjectCart.toString());
        Log.e("JSON Values cart:",jsonObjectCart.toString());

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ComingSoonActivity.class);
                startActivity(intent);
            }
        });


    }

    private class Reg extends AsyncTask<String,String,String> {
        ProgressDialog pdLoading = new ProgressDialog(DisplayCartActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading Cart...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            jsonoBject = JsonFunction.getJsonFromUrlparam(url,params[0]);
            Log.i("json Cart Request", "" + jsonoBject);
            return String.valueOf(jsonoBject);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //this method will be running on UI thread

            pdLoading.dismiss();
            List<ProductsModel> data = new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONObject jsonObject = new JSONObject(jsonoBject.toString());
                respone = jsonObject.getString("status");

                if (respone.trim().equals("1")) {
                    JSONArray jArray =  jsonoBject.getJSONArray("cart");

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);

                        ProductsModel productsData = new ProductsModel();

                        productsData.pName = json_data.getString("pName");
                        productsData.pQuantity = json_data.getInt("pQuantity");
                        productsData.pPrice = json_data.getDouble("pPrice");
                        productsData.pImgUrl = json_data.getString("pImgUrl");
                        data.add(productsData);
                        System.out.println("Cart details:" + productsData.toString());

                    }

                    // Setup and Handover data to recyclerview
                    cartLV = (ListView) findViewById(R.id.cartLV);
                    cartAdapter = new CartAdapter(DisplayCartActivity.this, data);
                    cartLV.setAdapter(cartAdapter);
                    //pLVProducts.setLayoutManager(new LinearLayoutManager(DisplayProductsActivity.this));
                }
                else{
                   respone = jsonObject.getString("cart");
                    Toast.makeText(DisplayCartActivity.this,respone.trim().toString(),Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(DisplayCartActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(DisplayCartActivity.this, DisplayProductsActivity.class);
        startActivity(intent);
    }
}
