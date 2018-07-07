package br.com.brunocardoso.desafionw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.brunocardoso.desafionw.R;
import br.com.brunocardoso.desafionw.adapter.HeroesAdapter;
import br.com.brunocardoso.desafionw.api.HeroesService;
import br.com.brunocardoso.desafionw.model.Hero;
import br.com.brunocardoso.desafionw.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    private RecyclerView rvHeroes;
    private HeroesAdapter adapter;
    private GridLayoutManager mLayoutManager;

    private List<Hero> heroesList = new ArrayList<>();

    private Retrofit retrofit;

    public static final String TAG = "NWLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrofit = RetrofitInstance.getInstace();

        adapter = new HeroesAdapter(heroesList, new HeroesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Hero heroSelect = heroesList.get( position );

                Intent i = new Intent(MainActivity.this, CadastroActivity.class);
                i.putExtra("hero", heroSelect);
                startActivity( i );
            }
        });
        mLayoutManager = new GridLayoutManager(this, 3);

        rvHeroes = findViewById(R.id.rv_heroes_id);
        rvHeroes.setAdapter(adapter);
        rvHeroes.setLayoutManager(mLayoutManager);
        rvHeroes.setHasFixedSize(false);

        carregaBruno();

        listarHeroes();
    }

    private void carregaBruno() {

//        imageView = findViewById(R.id.img_heroe_id);
//        Glide.with(this).load("http://heroes.qanw.com.br:7266/photos/27").into(imageView);
    }

    public void listarHeroes(){

        HeroesService service = retrofit.create(HeroesService.class);
        Call<List<Hero>> heroesService = service.getHeroes();
        heroesService.enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {
                if (response.isSuccessful()){
                    for (Hero hero : response.body()){

                        heroesList.add(hero);
                        adapter.notifyDataSetChanged();
                        Log.wtf(TAG, String.format("Heroi %s adicionado com sucesso!", hero.getClassName()));

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {

            }
        });
    }

    public void openCadastroActivity(View view) {
        Intent i = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity( i );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
