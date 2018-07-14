package com.example.globalnews.utils;

import android.util.Log;

import com.example.globalnews.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();


    public static ArrayList<News> createNewsArrayList(String response) {
        ArrayList<News> newsArrayList = new ArrayList<>();
        JSONObject jsonObject = makeJSONObject(response);

        if (jsonObject != null) {
            for (int i = 0; i < getResultsCount(jsonObject); i++) {
                News news = new News();
                news.setTitle(getArticleTitle(jsonObject, i));
                news.setUrl(getArticleUrlAsString(jsonObject, i));
                news.setUrlToImage(getImageUri(jsonObject, i));
                newsArrayList.add(news);
            }
        }
        return newsArrayList;
    }

    private static JSONObject makeJSONObject(String jsonResponse) {
        try {
            return new JSONObject(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception caught at makeJSONObject(): " + e);
            return null;
        }
    }

    private static String getImageUri(JSONObject jsonObject, int index) {
        try {
            JSONArray articlesArray = jsonObject.getJSONArray("articles");
            JSONObject article = articlesArray.getJSONObject(index);
            return article.getString("urlToImage");
        } catch (JSONException e) {
            Log.e(TAG, "Exception caught at getImageUri(): " + e);
            return null;
        }
    }

    private static String getArticleTitle(JSONObject jsonObject, int index) {
        try {
            JSONArray articlesArray = jsonObject.getJSONArray("articles");
            JSONObject article = articlesArray.getJSONObject(index);
            return article.getString("title");

        } catch (JSONException e) {
            Log.e(TAG, "Exception caught at getArticleTitle(): " + e);
            return null;
        }
    }

    private static String getArticleUrlAsString(JSONObject jsonObject, int index) {
        try {
            JSONArray articlesArray = jsonObject.getJSONArray("articles");
            JSONObject article = articlesArray.getJSONObject(index);
            return article.getString("url");

        } catch (JSONException e) {
            Log.e(TAG, "Exception caught at getArticleUrlAsString(): " + e);
            return null;
        }
    }

    private static int getResultsCount(JSONObject jsonObject) {
        try {
            return jsonObject.getInt("totalResults");
        } catch (JSONException e) {
            Log.e(TAG, "Exception caught at getResultsCount(): " + e);
            return -1;
        }
    }
}
