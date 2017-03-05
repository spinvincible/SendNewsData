package com.bestnewsapp.sendnewsdata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleSiteAllData extends AppCompatActivity {

    TextView allSiteData;
    ProgressDialog progressDialog;

    List<SinglePostData> alreadySentData = new ArrayList<>();
    List<SinglePostData> allPosts = new ArrayList<>();
    List<SinglePostData> toBeSentToFirebase = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_site_all_data);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("From the Main Activity");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


//        allSiteData = (TextView) findViewById(R.id.setText);

        final LoadData loadData = new LoadData(this);
        loadData.execute();

        allPosts = loadData.getAllNewPosts();

//        compareTheLists(allPosts, alreadySentData, toBeSentToFirebase);
    }


    public class LoadData extends AsyncTask<Void, Void, String> {
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
            String NYTimesSinglePost = "https://www.nytimes.com/2017/02/28/world/africa/nigeria-civilian-massacre.html";
            String singleArticleTOI = "http://timesofindia.indiatimes.com/sports/ipl/news/ipl-10-to-start-on-april-5-existing-process-to-continue-for-vendor-deals/articleshow/56919653.cms";
            String test2 = "http://timesofindia.indiatimes.com/india/next-month-volunteers-may-knock-on-your-door-for-cancer-diabetes-tests/articleshow/56696903.cms";
            Elements el, views, toi = null;
            Element singleAttr;
            // String datato = "";
            String urlPost = "";
            String img_url = "";
            String senderName = "";
            String content = "";
            String title = "";
            try {
//                doc = Jsoup.connect(NYTimesSinglePost).get();
                doc = Jsoup.connect(NytTimes).get();
                el = doc.select("article");
//                singleAttr = doc.select("article").first();
//
//                Log.d("SingleSiteData", singleAttr.toString());
//                Log.d("el", el.toString());
//
//                title = singleAttr.select("h1.headline").text();
//                img_url = singleAttr.select("img").first().toString();
//                content = singleAttr.select("p.story-body-text.story-content").toString();
//
//                Log.d("SingleSiteData", title + "\n" + img_url + "\n" + content + "\n");

                parseNYTimesData(el, allPosts);

                addDataToSharedPreferences(allPosts, context);

                compareTheLists(allPosts, context);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            sendDataToFirebase(allPosts);


            progressDialog.dismiss();

        }


        public void sendDataToFirebase(List<SinglePostData> allPosts) {
            for (int i = 0; i < allPosts.size(); i++) {
                SinglePostData oneEntry = allPosts.get(i);
                final DatabaseReference databaseReference = FirebaseUtil.getBaseRef();
                DatabaseReference AllPostsRef = FirebaseUtil.getAllPostsRef();
                final String postsKey = AllPostsRef.push().getKey();

                Map<String, Object> postValues = oneEntry.toMap();
                Map<String, Object> updatedUserData = new HashMap<>();
                updatedUserData.put(FirebaseUtil.getAllPostsPaths() + postsKey, postValues);
                updatedUserData.put(FirebaseUtil.getNYTPaths() + postsKey, postValues);
                databaseReference.updateChildren(updatedUserData);

//                Map<String, Object> updatedUserData = new HashMap<>();
//                updatedUserData.put(FirebaseUtil.getAllPostsPaths()+postsKey, new ObjectMapper().convertValue(oneEntry, Map.class));
//                updatedUserData.put(FirebaseUtil.getNYTPaths()+postsKey, new ObjectMapper().convertValue(oneEntry, Map.class));
//                databaseReference.updateChildren(updatedUserData);

            }
        }

        public void parseNYTimesData(Elements el, List<SinglePostData> oAllPosts) {

            for (Element element : el) {
                String title = element.select("h2.headline").text();
                String image = element.select("img[src]").attr("src");
                String timeStamp = element.select("time.dateline").text();
                String urlPost = element.select("a[href]").attr("href");
                if (!title.isEmpty() && !image.isEmpty() && !timeStamp.isEmpty() && !urlPost.isEmpty()) {
                    SinglePostData newsItem = new SinglePostData(title, urlPost, image, timeStamp);
                    oAllPosts.add(newsItem);
                }

            }
        }


//        public void compareTheLists(List<SinglePostData> allPosts, List<SinglePostData> alreadySent, List<SinglePostData> postsToSendToFirebase) {
//
//            for (int i = 0; i < allPosts.size(); i++) {
//                for (int j = 0; j < alreadySent.size(); j++) {
//                    if (allPosts.get(i) == alreadySent.get(j))
//                        postsToSendToFirebase.add(allPosts.get(i));
//                }
//            }
//            alreadySent.addAll(allPosts);
//        }

        public List<SinglePostData> getAllNewPosts() {
            return allPosts;
        }


        void addDataToSharedPreferences(List<SinglePostData> newData, Context context) {

            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(newData);
            prefsEditor.putString("SentDataList", json);
            prefsEditor.apply();

        }


        void compareTheLists(List<SinglePostData> allPosts, Context context) {

            List<SinglePostData> postsToSendToFirebase = new ArrayList<>();
            List<SinglePostData> alreadySentData = new ArrayList<>();
            SharedPreferences getSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String sentData = getSharedPrefs.getString("SentDataList", "");
            Type type = new TypeToken<List<SinglePostData>>() {
            }.getType();
            alreadySentData = gson.fromJson(sentData, type);

            for (int i = 0; i < allPosts.size(); i++) {
                for (int j = 0; j < alreadySentData.size(); j++) {
                    if (allPosts.get(i).getTitle().equals(alreadySentData.get(j).getTitle())) {
                        postsToSendToFirebase.add(allPosts.get(i));
                        break;
                    }
                }
            }


            SharedPreferences.Editor prefsEditor = getSharedPrefs.edit();
            String json = gson.toJson(alreadySentData);
            prefsEditor.putString("SentDataList", json);
            prefsEditor.apply();


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
