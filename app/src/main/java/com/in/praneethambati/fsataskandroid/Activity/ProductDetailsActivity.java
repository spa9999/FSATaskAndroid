package com.in.praneethambati.fsataskandroid.Activity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.in.praneethambati.fsataskandroid.Adapter.JsonFunction;
import com.in.praneethambati.fsataskandroid.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.InjectView;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView pdName, pdDesc, pdPrice,pdCategory;
    ImageView pdImgUrl;
    Spinner quantitySP;
    String username="";
    String username1="";
    String pNameStr,pDescStr,pPriceStr,pCategoryStr,pImgUrlStr,pID;
    Button addtoCartBtn;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String userKey2 = "userKey2";

    JSONObject jsonoBject;
    String url = "http://fsataskambati.atwebpages.com/FSATask/addToCart.php";
    String respone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        setCustomeToolbar();

        initializeTheLayoutFields();

        getIntentValues();

        setProductDetailsToFields();

        getSharedPref();

        addtoCartBtn.setOnClickListener(this);


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

    private void setCustomeToolbar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(tb);
        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        // ab.setHomeAsUpIndicator(R.mipmap.ic_launcher); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeTheLayoutFields() {
        //Initializing Layout variables
        pdName = (TextView) findViewById(R.id.pdName);
        pdDesc = (TextView) findViewById(R.id.pdDesc);
        pdCategory = (TextView) findViewById(R.id.pdCategory);
        pdPrice = (TextView) findViewById(R.id.pdPrice);
        pdImgUrl = (ImageView) findViewById(R.id.pdImgView);
        quantitySP = (Spinner) findViewById(R.id.quantitySP);
        addtoCartBtn = (Button) findViewById(R.id.addtoCartBtn);
        addItemsOnSpinner();
    }

    private void getIntentValues() {
        //Getting data from Intents
        Intent i = getIntent();
        pID = i.getStringExtra("pId");
        pNameStr = i.getStringExtra("pName");
        pDescStr = i.getStringExtra("pDesc");
        pPriceStr = i.getStringExtra("pPrice");
        pCategoryStr = i.getStringExtra("pCategory");
        pImgUrlStr = i.getStringExtra("pImgUrl");
        username = i.getStringExtra("username");

    }


    private void setProductDetailsToFields() {
        //Setting up values or text to all fields on layout
        pdName.setText(pNameStr);
        pdDesc.setText(pDescStr);
        pdCategory.setText(pCategoryStr);
        pdPrice.setText("$"+pPriceStr);

        //Picasso which helps in parsing the From URL To ImageView.
        Context context = pdImgUrl.getContext();
        Glide.with(context).load(pImgUrlStr)
                .placeholder(R.mipmap.ic_img_error)
                .error(R.mipmap.ic_img_error)
                .into(pdImgUrl);
    }

    public void addItemsOnSpinner() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.quantity));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySP.setAdapter(dataAdapter);
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
        Intent intent = new Intent(ProductDetailsActivity.this, DisplayProductsActivity.class);
        startActivity(intent);
    }

    public void onViewCartPressed(View v)
    {
        Intent intent = new Intent(ProductDetailsActivity.this, DisplayCartActivity.class);
        intent.putExtra("username",username);
        System.out.println("usernamein ViewCart PD:"+username);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addtoCartBtn:
                JSONObject jsonObjectAddToCart = new JSONObject();
                try {

                    jsonObjectAddToCart.put("username",username);
                    jsonObjectAddToCart.put("pName",pNameStr);
                    jsonObjectAddToCart.put("pImgUrl",pImgUrlStr);
                    jsonObjectAddToCart.put("pCategory",pCategoryStr);
                    jsonObjectAddToCart.put("pQuantity",quantitySP.getSelectedItem());
                    jsonObjectAddToCart.put("pPrice",pPriceStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Reg reg = new Reg();
                reg.execute(jsonObjectAddToCart.toString());
                Log.e("JSON Values2:",jsonObjectAddToCart.toString());

        }
}

    private class Reg extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            jsonoBject = JsonFunction.getJsonFromUrlparam(url, params[0]);
            Log.i("json", "" + jsonoBject);
            return String.valueOf(jsonoBject);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(jsonoBject.toString());
                respone = jsonObject.getString("status");

                if (respone.trim().equals("1")) {

                    String message = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();


                } else {
                    String message = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    }
