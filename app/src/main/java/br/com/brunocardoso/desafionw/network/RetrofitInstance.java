package br.com.brunocardoso.desafionw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.brunocardoso.desafionw.model.Hero;
import br.com.brunocardoso.desafionw.util.HeroeDesirializer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://heroes.qanw.com.br:7266/";

    public static Retrofit getInstace(){
        if (retrofit == null){

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Gson heroeConverter = new GsonBuilder().registerTypeAdapter(Hero.class, new HeroeDesirializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl( BASE_URL )
                    .addConverterFactory(GsonConverterFactory.create() )
                    .client( client )
                    .build();
        }

        return retrofit;
    }
}
