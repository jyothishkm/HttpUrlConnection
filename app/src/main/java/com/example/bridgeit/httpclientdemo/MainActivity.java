package com.example.bridgeit.httpclientdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView mWeatherJson;
    Button mFetchWeather;
    private String mURL = "https://raw.githubusercontent.com/ianbar20/JSON-Volley-Tutorial/master/Example-JSON-Files/Example-Object.JSON";
    private String mWeb = "", mData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeatherJson = (TextView) findViewById(R.id.tv_weather_json);


    }

    public void onClick(View view) {
        //check the internet connection
        ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connection.getActiveNetworkInfo();
        if (info !=null && info.isConnected()) {

            new FetchWeatherData().execute();

        } else {
            Toast.makeText(MainActivity.this, "Check the internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
    private class FetchWeatherData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "onPreExecute",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastjsonstr = "";

            try {
                URL url = new URL(mURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                if (is == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(is));
                while ((mData = reader.readLine()) !=null) {
                    forecastjsonstr += mData + "\n";
                }

                return forecastjsonstr;

            } catch (IOException e) {
                Log.e("MainActivity", "error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, "onPostExecute", Toast.LENGTH_SHORT).show();
            mWeatherJson.setText(s);
        }


    }
}
