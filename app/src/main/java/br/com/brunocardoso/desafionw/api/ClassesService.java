package br.com.brunocardoso.desafionw.api;

import java.util.List;

import br.com.brunocardoso.desafionw.model.Classe;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ClassesService {

    @GET("classes/{id}")
    Call<Classe> getClasse(@Path("id") int id);

    @GET("classes")
    Call<List<Classe>> getClasses();

}
