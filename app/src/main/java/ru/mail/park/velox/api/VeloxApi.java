package ru.mail.park.velox.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.mail.park.velox.model.Message;
import ru.mail.park.velox.model.Page;
import ru.mail.park.velox.model.Token;

public interface VeloxApi {

    String BASE_URL = "https://velox-server-usa.herokuapp.com";

    @GET("/qr/{id}")
    Call<Page> getPage(@Path("id") String uuid);

    @DELETE("/qr/{id}/delete")
    Call<Message> deletePage(@Path("id") String uuid);

    @GET("/")
    Call<List<Page>> getPages(@Query("sort") String sort, @Query("own") String own);

    @GET("/getuser")
    Call<Message> getUser();

    @POST("/login/yandex")
    Call<Message> loginYandex(@Body Token token);

    @GET("/logout")
    Call<Message> logout();

}
