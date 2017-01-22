package com.bestnewsapp.sendnewsdata;

/**
 * Created by Nixxmare on 1/21/2017.
 */

public class NewsSitesList {

    private String newsSiteName = "";

    private String newsLogoUrl = "";

    public NewsSitesList(String newsSiteName, String newsLogoUrl) {
        this.newsSiteName = newsSiteName;
        this.newsLogoUrl = newsLogoUrl;
    }


    public String getNewsSiteName() {
        return newsSiteName;
    }

    public String getNewsLogoUrl() {
        return newsLogoUrl;
    }

}
