package br.com.brunocardoso.desafionw.api;

import java.util.List;

import br.com.brunocardoso.desafionw.model.Hero;
import br.com.brunocardoso.desafionw.model.HeroDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HeroesService {

    @POST("heroes")
    @Headers("Content-Type: application/json")
    Call<Void> saveHero(@Body HeroDTO hero);

    @PUT("heroes/{id}")
    @Headers("Content-Type: application/json")
    Call<Void> updateHero(@Path("id") int id, @Body HeroDTO hero);

    @DELETE("heroes/{id}")
    Call<Void> deleteHero(@Path("id") int id);

    @GET("heroes/{id}")
    Call<Hero> getHero(@Path("id") int id);

    @GET("heroes")
    Call<List<Hero>> getHeroes();
}
