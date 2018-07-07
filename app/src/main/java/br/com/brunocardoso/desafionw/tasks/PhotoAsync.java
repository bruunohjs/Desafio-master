package br.com.brunocardoso.desafionw.tasks;


import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.brunocardoso.desafionw.api.PhotosService;
import br.com.brunocardoso.desafionw.network.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoAsync extends AsyncTask<List<Uri>, String, String> {
    private Activity activity;
    private SaveHeroInterface saveHeroInterface;
    public static final String TAG = "NWLOG";

    public PhotoAsync(Activity activity) {
        if (activity instanceof SaveHeroInterface) {
            saveHeroInterface = (SaveHeroInterface) activity;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // inicia progressbar
    }

    @Override
    protected String doInBackground(List<Uri>... uris) {
        String fotoId = "";

        Retrofit retrofit = RetrofitInstance.getInstace();
        PhotosService serviceApi = retrofit.create(PhotosService.class);

        try{
            for (Uri uri: uris[0]) {
                File file = new File(uri.getPath());
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                Call<ResponseBody> response = serviceApi.uploadFiles(body);
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Log.wtf(TAG, "dEU BOM");
                        }else{
                            Log.wtf(TAG, " ñ dEU BOM");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

        }catch (Exception e){
        }

        return fotoId;
    }

    @Override
    protected void onPostExecute(String id) {
        super.onPostExecute(id);

//        saveHeroInterface.saveHero(strings);
    }

    public interface SaveHeroInterface {
        public void saveHero(List<String> photos);
    }
}