package com.bestnewsapp.sendnewsdata;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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
            String singleArticleTOI = "http://timesofindia.indiatimes.com/sports/ipl/news/ipl-10-to-start-on-april-5-existing-process-to-continue-for-vendor-deals/articleshow/56919653.cms";
            String test2 = "http://timesofindia.indiatimes.com/india/next-month-volunteers-may-knock-on-your-door-for-cancer-diabetes-tests/articleshow/56696903.cms";
            Elements el, views, toi = null;
            // String datato = "";
            String title = "";
            String image = "";
            String summary = "";
            String author = "";
            String timeStamp = "";
            try {
                doc = Jsoup.connect(test2).get();
//                el = doc.select("div.story-headline");
                toi = doc.select("div.Normal");
                title = toi.toString();
                Log.i("Data From the Site",toi.toString() );
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
            Spanned result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY);
            }
            else {
                result = Html.fromHtml(s);
            }
            allSiteData.setText(result);
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
