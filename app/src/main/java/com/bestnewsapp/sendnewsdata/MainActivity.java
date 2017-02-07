package com.bestnewsapp.sendnewsdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<NewsSitesList> addData = new ArrayList<>();
    private NewsSiteListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView newsRecyclerView = (RecyclerView) findViewById(R.id.newList);

        addData.add(new NewsSitesList("The New York Times", "https://static01.nyt.com/images/icons/t_logo_291_black.png"));
        addData.add(new NewsSitesList("Washington Post", "http://www.washingtonpost.com/grid/local/wp-content/themes/wapo-grid-theme-v3/img/fb.png"));
        addData.add(new NewsSitesList("USA Today", "http://www.celebritywealthexpo.com/wp-content/uploads/2016/01/usatoday.png"));

        adapter = new NewsSiteListAdapter(getApplicationContext(), addData);
        adapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setAdapter(adapter);

    }
}


// String timesOfIndia = "http://timesofindia.indiatimes.com/";

//      doc = Jsoup.connect(timesOfIndia).get();
// //            views = doc.select("ul[data-vr-zone]");
// //            el = doc.select("[href]");
// //            el = doc.select("img[src]");
// //            System.out.println(el);
// //                To get Text and then Extract the full source to it.
// //                String extendFullLink;
// //                String fullLink = "";
// //                String imageLink = element.select("img[src]").attr("abs:src");
// //                if (!imageLink.isEmpty()) {
// //                    extendFullLink = element.select("img[src]").attr("data-src");
// //                    if (!extendFullLink.isEmpty()) {
// //                        fullLink = imageLink + extendFullLink;
// //                        System.out.println(fullLink);
// //
// //                    }
// //                }

//             searchLinks = doc.select("a[pg*=city],[pg*=new_latest],[pg*=new_tops]" +
//                     ",[pg*=special],[pg*=cricket],[pg*=entertainment_Movie],[pg*=education],[pg*=world],[pg*=mostPopular]")
//                     .not("a[pg*=nav]").not("a[pg*=speak]").not("a[style]").not("span").not("a[class]").not("a[rel]").not("img");
// //            System.out.println(searchLinks.toString());

// //            searchContent = doc.select("span.time_cptn");
// //            System.out.println(searchLinks.text());
//             int size = searchLinks.size();
//             System.out.println(size);
//             for (Element element : searchLinks) {
//                 ArticleData articleData;
//                 urlPost = element.absUrl("href");
//                 title = element.attr("title");
// //                internalData = Jsoup.connect(urlPost).get();
// //                img_url= internalData.select("img[data-imgid]").attr("abs:src");
// //                content = internalData.select("div.Normal").toString();
// //                System.out.println(searchContent);

//                 if(!title.isEmpty()&&!urlPost.isEmpty()){
//                     articleData = new ArticleData(title, urlPost);
//                     fullValidList.add(articleData);
//                 }


//             }

//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//         Set<ArticleData> uniqueData = new LinkedHashSet<>();
//         uniqueData.addAll(fullValidList);
//         fullValidList.clear();
//         fullValidList.addAll(uniqueData);

//         for (ArticleData aFullValidList : fullValidList) {
//             System.out.println(aFullValidList.getTitle());
//             System.out.println(aFullValidList.getUrl());
//         }

//     }
