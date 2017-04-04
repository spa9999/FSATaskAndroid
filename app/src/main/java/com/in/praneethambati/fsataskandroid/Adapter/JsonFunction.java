package com.in.praneethambati.fsataskandroid.Adapter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Praneeth Ambati on 3/18/2017.
 */

public class JsonFunction {

    public static JSONObject getJsonFromUrl(String url)
    {
        JSONObject jsonObject = null;

        try {

            URL jsonUrl=new URL(url);
            HttpURLConnection connection= (HttpURLConnection) jsonUrl.openConnection();
            InputStream is=new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            StringBuffer sb=new StringBuffer();
            String line;
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            is.close();
            Log.i("sb",sb.toString());
            jsonObject=new JSONObject(sb.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONObject getJsonFromUrlparam(String url, String para)
    {
        JSONObject jsonObject = null;

        try {

            URL jsonUrl=new URL(url);
            HttpURLConnection connection= (HttpURLConnection) jsonUrl.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(60000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setUseCaches(false);
            connection.connect();


            OutputStream mOutPut = new BufferedOutputStream(connection.getOutputStream());
            mOutPut.write(para.getBytes());
            mOutPut.flush();

            InputStream is=new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            StringBuffer sb=new StringBuffer();
            String line;
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            is.close();

            Log.i("json",sb.toString());
            jsonObject=new JSONObject(sb.toString());
            Log.i("json",""+jsonObject);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static String getJsonUrlparamString(String url, String para)
    {
        StringBuffer sb = null;

        try {

            URL jsonUrl=new URL(url);
            HttpURLConnection connection= (HttpURLConnection) jsonUrl.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(60000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setUseCaches(false);
            connection.connect();


            OutputStream mOutPut = new BufferedOutputStream(connection.getOutputStream());
            mOutPut.write(para.getBytes());
            mOutPut.flush();

            InputStream is=new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            sb=new StringBuffer();
            String line;
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            is.close();

            Log.i("json",sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

