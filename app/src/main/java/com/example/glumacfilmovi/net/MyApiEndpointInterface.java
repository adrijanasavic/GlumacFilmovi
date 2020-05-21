package com.example.glumacfilmovi.net;




import com.example.glumacfilmovi.net.model1.SearchResult;
import com.example.glumacfilmovi.net.model2.Detail;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MyApiEndpointInterface {

    @GET("/")
    Call<SearchResult> getMovieByName(@QueryMap Map<String, String> options);

    @GET("/")
    Call<Detail> getMovieData(@QueryMap Map<String, String> options);

}
