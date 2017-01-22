package com.bestnewsapp.sendnewsdata;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleSiteAllData extends AppCompatActivity {

    TextView allSiteData;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_site_all_data);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("From the Main Activity");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        allSiteData = (TextView) findViewById(R.id.setText);

        final LoadData loadData = new LoadData(this);
        loadData.execute();


    }



    class LoadData extends AsyncTask<Void, Void, String > {
        String data = "";
        Context context;

        public LoadData(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Into The OnPreExecute");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(Void... voids) {

            Document doc = null;


            String url = "https://www.washingtonpost.com/opinions/";

            Elements el, views = null;
            // String datato = "";
            String title = "";
            String image = "";
            String summary = "";
            String author = "";
            String timeStamp = "";
            try {
                doc = Jsoup.connect(url).get();
                el = doc.select("div.story-headline");
                title = el.toString();
                Log.i("Data From the Site",el.toString() );
                // int size = el.size();
//                for (Element element : el) {
//
//                    title = element.select("h2.headline").text();
//                    summary = element.select("p.summary").text();
//                    image = element.select("img[src]").attr("src");
//                    author = element.select("p.byline").text().replaceAll("By ", "");
//                    timeStamp = element.select("time.dateline").text();
//                    Log.i("Data for the ImageURL",image );
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            allSiteData.setText(s);
            progressDialog.dismiss();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
