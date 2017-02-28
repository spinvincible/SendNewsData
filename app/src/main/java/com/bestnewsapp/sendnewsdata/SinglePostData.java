package com.bestnewsapp.sendnewsdata;

/**
 * Created by Nixxmare on 2/17/2017.
 */
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nixxmare on 2/17/2017.
 */


// This is the data model for the latest retrival of news from the sites and all posts with likes and comments.

public class SinglePostData {
    String title;
    String post_url;
    String img_url;
    String timeStamp;
    int likeCount = 0;
    int commentCount = 0;
    int shareCount = 0;

    public SinglePostData() {
    }

    public SinglePostData(String title, String post_url, String img_url, String timeStamp) {
        this.title = title;
        this.post_url = post_url;
        this.img_url = img_url;
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public String getPost_url() {
        return post_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTimeStamp() {
        return timeStamp;
    }



    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("post_url", post_url);
        result.put("img_url", img_url);
        result.put("timeStamp", timeStamp);
        result.put("likeCount", likeCount);
        result.put("commentCount", commentCount);
        result.put("shareCount", shareCount);

        return result;
    }
}
