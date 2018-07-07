package br.com.brunocardoso.desafionw.api;

import android.net.Uri;

import java.io.File;
import java.util.List;
import java.util.Map;

import br.com.brunocardoso.desafionw.model.Photo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface PhotosService {

    @GET("photos/{id}")
    Call<ResponseBody> getPhoto(@Path("id") int id);

    @Multipart
    @POST("photos")
    Call<ResponseBody> uploadFiles(@Part MultipartBody.Part file);

    @Multipart
    @POST("photos")
    Call<Photo> uploadFile(@Path("file") File file);

}
