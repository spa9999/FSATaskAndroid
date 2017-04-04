package com.in.praneethambati.fsataskandroid.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.in.praneethambati.fsataskandroid.Adapter.JsonFunction;
import com.in.praneethambati.fsataskandroid.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String userKey = "userKey";
    public static final String userKey2 = "userKey2";
    public static final String passwordKey = "pwdKey";
    public static final String profileKey = "profileKey";
    SharedPreferences preferences;
    String url = "http://fsataskambati.atwebpages.com/FSATask/login.php";
    JSONObject jsonoBject=null;
    String respone="";
    String uname, upass;
    Boolean rememberMeToggle = false;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.remembermeCB) CheckBox _remembermeCB;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        _remembermeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    rememberMeToggle = true;
                }
                else{
                    rememberMeToggle = false;
                }
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Start the Signup activity
            Intent intent = new Intent(getApplicationContext(), ComingSoonActivity.class);
           startActivity(intent);
        }
    });
}

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email",email);
            jsonObject.put("password",password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Reg reg = new Reg();
        reg.execute(jsonObject.toString());
        Log.e("JSON Values:",jsonObject.toString());

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
       Toast.makeText(getApplicationContext(),"Press home to exit",Toast.LENGTH_LONG).show();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        //finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private class Reg extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

                    String status = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), "" + "Welcome to FSA Mobile Store!", Toast.LENGTH_LONG).show();

                    String email = _emailText.getText().toString();
                    String password = _passwordText.getText().toString();

                    if(rememberMeToggle==true) {


                        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor store = preferences.edit();

                        store.putString(userKey2,email);
                        store.putString(passwordKey, password);
                        store.putString(profileKey, status);
                        store.commit();
                        Intent loginIntent = new Intent(LoginActivity.this, DisplayProductsActivity.class);
                        loginIntent.putExtra("username",email);
                        System.out.println("Email:"+email);
                        startActivity(loginIntent);
                    }
                    else{
                        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor store = preferences.edit();
                        store.putString(userKey, email);
                        store.commit();
                        Intent loginIntent = new Intent(LoginActivity.this, DisplayProductsActivity.class);
                        loginIntent.putExtra("username",email);
                        startActivity(loginIntent);
                    }



                } else if (respone.trim().equals("0")) {
                    //String status1 = jsonObject.getString("user");
                    Toast.makeText(getApplicationContext(), "" + "Login Failed!", Toast.LENGTH_LONG).show();



                } else if (respone.trim().equals("0")) {
                    // String status2 = jsonObject.getString("user");
                    Toast.makeText(getApplicationContext(), "" + "Error ", Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
