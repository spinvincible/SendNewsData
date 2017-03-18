package com.bestnewsapp.sendnewsdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bestnewsapp.sendnewsdata.util.ArticleTextExtractor;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class SingleSiteAllData extends AppCompatActivity {

    TextView allSiteData;
    ProgressDialog progressDialog;
    EditText enterUrl;
    TextView urlOutput;
    Button getDataButton;

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
        Activity thisActivity = this;

        enterUrl = (EditText) findViewById(R.id.UrlData);
        urlOutput = (TextView) findViewById(R.id.txtViewDataFromtheUrl);
        getDataButton = (Button) findViewById(R.id.btnGetDataFromUrl);

        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String headerData = "https://mercury.postlight.com/parser?url=";
                String URL = headerData + enterUrl.getText().toString();
                getVolleyData(URL);
            }
        });


        final LoadData loadData = new LoadData(this, thisActivity);
        loadData.execute();

    }

    public void GetDataFromUrl(View view) {
    }


    public class LoadData extends AsyncTask<Void, Void, String> {
        String data = "";
        Context context;
        Activity activity;
        List<SinglePostData> allPosts = new ArrayList<>();
        List<SinglePostData> noRepeatAllPosts = new ArrayList<>();

        public LoadData(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;

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
                //  Log.d("HTML" , "\n \n \n"+doc.toString());

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

                String mobilizedHtml = ArticleTextExtractor.extractContent(NYTimesSinglePost, "  ");

                Log.v("HTML", mobilizedHtml + "\n \n \n" + el.toString());
//                int sizeOfData = allPosts.size();
//
//                for (SinglePostData event : allPosts) {
//                    boolean isFound = false;
//                    // check if the event name exists in noRepeat
//                    for (SinglePostData e : noRepeatAllPosts) {
//                        if (e.getTitle().equals(event.getTitle()))
//                            isFound = true;
//                        break;
//                    }
//
//                    if (!isFound) noRepeatAllPosts.add(event);
//                }

                for (int i = 0; i < allPosts.size(); i++) {
                    boolean isFound = false;
                    for (int j = i + 1; j < allPosts.size(); j++) {
                        if (allPosts.get(i).getImg_url().equals(allPosts.get(j).getImg_url())) {
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound) noRepeatAllPosts.add(allPosts.get(i));
                }

//                addDataToSharedPreferences(allPosts, context);

                compareTheLists(noRepeatAllPosts, context);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
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


        public void sendDataToFirebase(final List<SinglePostData> allPosts) {


            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Total number of articles to be updated " + String.valueOf(allPosts.size()), Toast.LENGTH_LONG).show();
                }
            });


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

        public List<SinglePostData> getAllNewPosts() {
            return allPosts;
        }


        void compareTheLists(List<SinglePostData> noRepeatAllPosts, final Context context) {

            List<SinglePostData> postsToSendToFirebase = new ArrayList<>();
            List<SinglePostData> alreadySentData = new ArrayList<>();
            SharedPreferences getSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String sentData = getSharedPrefs.getString("SendNYTTimesList", "");
            Type type = new TypeToken<List<SinglePostData>>() {
            }.getType();
            alreadySentData = gson.fromJson(sentData, type);
            if (alreadySentData != null) {
                for (int i = 0; i < noRepeatAllPosts.size(); i++) {
                    boolean isFound = false;
                    for (int j = 0; j < alreadySentData.size(); j++) {
                        if (noRepeatAllPosts.get(i).getImg_url().equals(alreadySentData.get(j).getImg_url())) {
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound) postsToSendToFirebase.add(noRepeatAllPosts.get(i));
                }
            }

            if (postsToSendToFirebase.size() > 0)
                sendDataToFirebase(postsToSendToFirebase);
            else
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "No new articles found on the server", Toast.LENGTH_LONG).show();
                    }
                });

            SharedPreferences.Editor prefsEditor = getSharedPrefs.edit();
            String json = gson.toJson(noRepeatAllPosts);
            prefsEditor.putString("SendNYTTimesList", json);
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


    public String getVolleyData(String url) {


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                urlOutput.setText(Html.fromHtml(response));
                Log.i("VOLLEYData", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("x-api-key", "ZW7P0pMC3mGGdB6m0qSojq44W8TEGAfdzpMcvpWN");
                return params;
            }

        };

        requestQueue.add(stringRequest);


        return "";
    }
}
