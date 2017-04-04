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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.in.praneethambati.fsataskandroid.Adapter.JsonFunction;
import com.in.praneethambati.fsataskandroid.Adapter.ProductsAdapter;
import com.in.praneethambati.fsataskandroid.Model.ProductsModel;
import com.in.praneethambati.fsataskandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayProductsActivity extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String userKey2 = "userKey2";
    private ListView pLVProducts;
    private ProductsAdapter mAdapter;
    private boolean isUserScroll;
    private boolean isDataLoading;
    SearchView search;

    String url = "http://fsataskambati.atwebpages.com/FSATask/getProducts.php";
    JSONObject jsonoBject=null;
    String respone="";
    String username="";
    String username1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);




        pLVProducts = (ListView) findViewById(R.id.productsLV);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setLogo(R.mipmap.ic_launcher);
        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();

        Intent i = getIntent();
        username = i.getStringExtra("username");
        getSharedPref();

        //Make call to AsyncTask
        new AsyncFetch().execute();

       pLVProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 1) {
                    isUserScroll = true;
                } else {
                    isUserScroll = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (isUserScroll) {

                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if (!isDataLoading && (lastInScreen == totalItemCount)) {
                        //load data

                        isDataLoading = true;
                    }
                }
            }
        });

        //On item click, Collecting information of that particular cell and passing the next Activity
        pLVProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DisplayProductsActivity.this,ProductDetailsActivity.class);
                i.putExtra("pId",mAdapter.data.get(position).getpId());
                i.putExtra("pName",mAdapter.data.get(position).getpName());
                i.putExtra("pDesc",mAdapter.data.get(position).getpDesc());
                i.putExtra("pCategory",mAdapter.data.get(position).getpCategory());
                i.putExtra("pImgUrl",mAdapter.data.get(position).getpImgUrl());
                i.putExtra("pPrice",mAdapter.data.get(position).getpPrice().toString());
                i.putExtra("username",username);
                System.out.println(username);
                startActivity(i);
            }
        });

        search = (SearchView) findViewById(R.id.searchView1);
        //*** setOnQueryTextListener **
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                return false;
            }
        });

    }

    private void getSharedPref() {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        username = preferences.getString("userKey","");
    }



    @Override
    protected void onResume() {
        super.onResume();
        getSharedPref();

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(DisplayProductsActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading Products...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            jsonoBject = JsonFunction.getJsonFromUrl(url);
            Log.i("json", "" + jsonoBject);
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
                    JSONArray jArray =  jsonoBject.getJSONArray("message");

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);

                            ProductsModel productsData = new ProductsModel();
                            productsData.pId = json_data.getInt("pID");
                            productsData.pName = json_data.getString("pName");
                            productsData.pQuantity = json_data.getInt("pQuantity");
                            productsData.pPrice = json_data.getDouble("pPrice");
                            productsData.pCategory = json_data.getString("pCategory");
                            productsData.pImgUrl = json_data.getString("pImgUrl");
                            productsData.pDesc = json_data.getString("pDesc");
                            data.add(productsData);
                        System.out.println(productsData.toString());

                    }

                    // Setup and Handover data to recyclerview
                    pLVProducts = (ListView) findViewById(R.id.productsLV);
                    mAdapter = new ProductsAdapter(DisplayProductsActivity.this, data);
                    pLVProducts.setAdapter(mAdapter);
                    //pLVProducts.setLayoutManager(new LinearLayoutManager(DisplayProductsActivity.this));
                }
            } catch (JSONException e) {
                Toast.makeText(DisplayProductsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        }



    public void onViewCartPressed(View v)
    {
        Intent intent = new Intent(DisplayProductsActivity.this, DisplayCartActivity.class);
        intent.putExtra("username",username);
        System.out.println("usernamein ViewCart:"+username);
        startActivity(intent);
    }



    public void onLogoutPressed(View v)
    {
        Intent intent = new Intent(DisplayProductsActivity.this, LoginActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);

    }


}
