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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        List<SinglePostData> allPosts = new ArrayList<>();


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


            String NytTimes = "https://www.nytimes.com/section/us";
            String singleArticleTOI = "http://timesofindia.indiatimes.com/sports/ipl/news/ipl-10-to-start-on-april-5-existing-process-to-continue-for-vendor-deals/articleshow/56919653.cms";
            String test2 = "http://timesofindia.indiatimes.com/india/next-month-volunteers-may-knock-on-your-door-for-cancer-diabetes-tests/articleshow/56696903.cms";
            Elements el, views, toi = null;
            // String datato = "";
            String urlPost= "";
            String img_url= "";
            String senderName= "";
            String content= "";
            String title = "";
            String image = "";
            String summary = "";
            String author = "";
            String timeStamp = "";
            try {
                doc = Jsoup.connect(NytTimes).get();
                el = doc.select("article");


                for (Element element : el) {
                    title = element.select("h2.headline").text();
                    image = element.select("img[src]").attr("src");
                    timeStamp = element.select("time.dateline").text();
                    urlPost = element.select("a[href]").attr("href");
                    if (!title.isEmpty() && !image.isEmpty() && !timeStamp.isEmpty() && !urlPost.isEmpty()) {
                        SinglePostData newsItem = new SinglePostData(title, urlPost, image, timeStamp);
                        allPosts.add(newsItem);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for(int i = 0; i < allPosts.size(); i ++)
            {
                SinglePostData oneEntry = allPosts.get(i);
                final DatabaseReference databaseReference = FirebaseUtil.getBaseRef();
                DatabaseReference AllPostsRef = FirebaseUtil.getAllPostsRef();
                final String postsKey = AllPostsRef.push().getKey();

                Map<String, Object> postValues = oneEntry.toMap();
                Map<String, Object> updatedUserData = new HashMap<>();
                updatedUserData.put(FirebaseUtil.getAllPostsPaths()+postsKey, postValues);
                updatedUserData.put(FirebaseUtil.getNYTPaths()+postsKey, postValues);
                databaseReference.updateChildren(updatedUserData);

//                Map<String, Object> updatedUserData = new HashMap<>();
//                updatedUserData.put(FirebaseUtil.getAllPostsPaths()+postsKey, new ObjectMapper().convertValue(oneEntry, Map.class));
//                updatedUserData.put(FirebaseUtil.getNYTPaths()+postsKey, new ObjectMapper().convertValue(oneEntry, Map.class));
//                databaseReference.updateChildren(updatedUserData);

            }

//            Spanned result;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                result = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY);
//            }
//            else {
//                result = Html.fromHtml(s);
//            }
//            allSiteData.setText(result);
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
