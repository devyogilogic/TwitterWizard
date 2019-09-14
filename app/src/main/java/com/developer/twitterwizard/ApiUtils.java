package com.developer.twitterwizard;


public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = MainActivity.server;

    public static TwitterServiceApi getTwitterServiceApi() {

        return RetrofitClient.getClient(BASE_URL).create(TwitterServiceApi.class);
    }
}
