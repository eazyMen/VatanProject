package com.kaspsoft.radinvatan.data.global.network;

import com.kaspsoft.radinvatan.models.DetailList;
import com.kaspsoft.radinvatan.models.ItemMenu;
import com.kaspsoft.radinvatan.models.ListSchedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ListApi {

    @GET("api.php?do=getMenu&key=156dfdd335hjkodS")
    Call<List<ItemMenu>> getMenu();

    @GET("api.php?do=getPostList&CatID=?&key=156dfdd335hjkodS")
    Call<List<ListSchedule>> getList(@Query("CatID") int id);

    @GET("api.php?do=getPostById&PostID=?&key=156dfdd335hjkodS")
    Call<DetailList> getDetail(@Query("PostID") int postId);
}
