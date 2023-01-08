package com.urjc.nasaapod;

public class APIResponse {

    // Class to serialize JSON data coming from the Nasa API

    protected String url;
    protected String explanation;
    protected String title;

    public APIResponse() {}

    public APIResponse(String url, String explanation, String title) {
        this.url = url;
        this.explanation = explanation;
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
