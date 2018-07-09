package br.com.brunocardoso.desafionw.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
    private TextView tv_itens;

    private RecyclerView rvHeroes;
    private HeroesAdapter adapter;
    private GridLayoutManager mLayoutManager;

    private List<Hero> heroesList = new ArrayList<>();

    private Retrofit retrofit;

    MaterialSearchView searchView;
    SkeletonScreen skeletonScreen;

    public static final String TAG = "NWLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = RetrofitInstance.getInstace();

        initViews();

        listarHeroes();
    }

    private void initViews() {
        tv_itens = findViewById(R.id.tv_itens);

        adapter = createAdapter(heroesList);
        mLayoutManager = new GridLayoutManager(this, 3);

        rvHeroes = findViewById(R.id.rv_heroes_id);
        rvHeroes.setAdapter(adapter);
        rvHeroes.setLayoutManager(mLayoutManager);
        rvHeroes.setHasFixedSize(false);

        skeletonScreen = Skeleton.bind( rvHeroes )
                .adapter(adapter)
                .load(R.layout.adaptar_heroes)
                .shimmer(true)      // whether show shimmer animation.                      default is true
                .count(9)          // the recycler view item count.                        default is 10
                .color(R.color.cardview_shadow_end_color)       // the shimmer color.                                   default is #a2878787
                .angle(30)          // the shimmer angle.                                   default is 20;
                .duration(1000)     // the shimmer animation duration.                      default is 1000;
                .frozen(false)      // whether frozen recyclerView during skeleton showing  default is true;
                .show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Hero> heroesSearch = new ArrayList<>();

                for(Hero hero: heroesList){
                    String name = hero.getName().toLowerCase();

                    if(name.contains( newText.toLowerCase())){
                        heroesSearch.add( hero );
                    }
                }

                adapter = createAdapter(heroesSearch);
                rvHeroes.setAdapter( adapter );
                adapter.notifyDataSetChanged();

                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

    }

    private HeroesAdapter createAdapter(final List<Hero> heroes) {
        return new HeroesAdapter(heroes, new HeroesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Hero heroSelect = heroes.get( position );

                Intent i = new Intent(MainActivity.this, CadastroActivity.class);
                i.putExtra("hero", heroSelect);
                startActivity( i );
            }
        });
    }

    public void listarHeroes(){
        HeroesService service = retrofit.create(HeroesService.class);
        Call<List<Hero>> heroesService = service.getHeroes();
        heroesService.enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {
                if (response.isSuccessful()){

                    List<Hero> heroes = response.body();
                    String[] heroesSuggestions = new String[heroes.size()];

                    for (int i=0; i< heroes.size(); i++){
                        heroesList.add( heroes.get( i ));
                        heroesSuggestions[i] = heroes.get(i).getName();
                        adapter.notifyDataSetChanged();
                        Log.wtf(TAG, "Heroi adicionado com sucesso!");

                    }

                    searchView.setSuggestions( heroesSuggestions );
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            skeletonScreen.hide();

                            if (!(heroesList.size() > 0)){
                                tv_itens.setVisibility(View.VISIBLE);
                                rvHeroes.setVisibility(View.GONE);
                            }
                        }
                    }, 1000);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.mn_search);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        heroesList = (List<Hero>) savedInstanceState.getSerializable("heroesList");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // Recupera dados do formulario

        outState.putSerializable("heroesList", (Serializable) heroesList);
    }

}
