package br.com.brunocardoso.desafionw.api;

import java.util.List;

import br.com.brunocardoso.desafionw.model.Specialty;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SpecialtieService {

    @GET("specialties/{id}")
    Call<Specialty> getSpecialtie(@Path("id") int id);

    @GET("specialties")
    Call<List<Specialty>> getSpecialties();

}
