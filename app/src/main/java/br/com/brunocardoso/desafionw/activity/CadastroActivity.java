package br.com.brunocardoso.desafionw.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.brunocardoso.desafionw.R;
import br.com.brunocardoso.desafionw.adapter.PhotosAdapter;
import br.com.brunocardoso.desafionw.api.ClassesService;
import br.com.brunocardoso.desafionw.api.HeroesService;
import br.com.brunocardoso.desafionw.api.PhotosService;
import br.com.brunocardoso.desafionw.api.SpecialtieService;
import br.com.brunocardoso.desafionw.model.Classe;
import br.com.brunocardoso.desafionw.model.Hero;
import br.com.brunocardoso.desafionw.model.HeroDTO;
import br.com.brunocardoso.desafionw.model.Specialty;
import br.com.brunocardoso.desafionw.network.RetrofitInstance;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CadastroActivity extends AppCompatActivity{
    private EditText edtHeroNome,
            edtHeroClasses,
            edtHeroHabilidades,
            edtHeroVida,
            edtHeroDefesa,
            edtHeroDano,
            edtHeroVeloAtaque,
            edtHeroVeloMovimento;

    private Button btnSalvarHeroi;
    private Button btnAddAvatar;

    private RecyclerView rvHeroAvatares;
    private LinearLayoutManager mLayoutManager;
    private PhotosAdapter adapter;

    private List<Classe> classeList = new ArrayList<>();
    private Classe classeSelected;

    private List<Specialty> specialtyList = new ArrayList<>();
    private List<Integer> specialtySelectedList = new ArrayList<>();
    private boolean[] specialtieCheckedList;

    List<Uri> photoList = new ArrayList<>();
    private List<String> photoSelectedList = new ArrayList<>();

    Retrofit retrofit;
    private Hero heroUpdate;
    private boolean updateForm = false;

    public static final String TAG = "NWLOG";
    private static final int REQUEST_CODE_PICK = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        retrofit = RetrofitInstance.getInstace();

        initViews();
        listarClasses();
        listarSpecialties();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey("hero")) {
                heroUpdate = (Hero) bundle.get("hero");
                updateForm = true;
                btnSalvarHeroi.setText("Atualizar");

                // Carregar campos com os dados do heroi selecionado
                carregaDadosHeroi( heroUpdate );
            }
        }
    }

    private void carregaDadosHeroi(Hero heroUpdate) {
        edtHeroNome.setText( heroUpdate.getName() );
        edtHeroClasses.setText( heroUpdate.getClassName() );
        edtHeroVida.setText( String.valueOf(heroUpdate.getHealthPoints()) );
        edtHeroDano.setText( String.valueOf(heroUpdate.getDamage()) );
        edtHeroDefesa.setText( String.valueOf(heroUpdate.getDefense()) );
        edtHeroVeloAtaque.setText( String.valueOf(heroUpdate.getAttackSpeed()) );
        edtHeroVeloMovimento.setText( String.valueOf(heroUpdate.getMovimentSpeed()) );

        // Carrega Classe
        classeSelected = new Classe( heroUpdate.getClassId(), heroUpdate.getClassName());

        // Carrega Habilidades
        StringBuilder habilidadesName = new StringBuilder();
        for(Specialty specialty: heroUpdate.getSpecialties()){
            specialtySelectedList.add( specialty.getId() );
            habilidadesName.append(specialty.getName() + ", ");
        }
        int lastIndice = habilidadesName.lastIndexOf(",");
        String habilidadesTexto = habilidadesName.replace(lastIndice, lastIndice+2, "").toString();
        edtHeroHabilidades.setText( habilidadesTexto );

        // Carrega fotos
        Uri uri = Uri.parse(
                String.format("http://heroes.qanw.com.br:7266/photos/%s", heroUpdate.getPhotos().get(0))
                );
        photoList.add( uri );
        photoSelectedList.addAll( heroUpdate.getPhotos() );
    }

    private void initViews() {

        /*Configura o recyclerview*/
        rvHeroAvatares = findViewById(R.id.rv_heroi_avatar);

        adapter = new PhotosAdapter( photoList );
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvHeroAvatares = findViewById(R.id.rv_heroi_avatar);
        rvHeroAvatares.setAdapter(adapter);
        rvHeroAvatares.setLayoutManager(mLayoutManager);
        rvHeroAvatares.setHasFixedSize(false);
        /*Configura o recyclerview*/

        /*Inicia as views*/
        edtHeroNome = findViewById(R.id.edt_heroi_nome);
        edtHeroClasses = findViewById(R.id.edt_heroi_classes);
        edtHeroHabilidades = findViewById(R.id.edt_heroi_habilidades);

        edtHeroVida = findViewById(R.id.edt_heroi_vida);
        edtHeroDefesa = findViewById(R.id.edt_heroi_defesa);
        edtHeroDano = findViewById(R.id.edt_heroi_dano);
        edtHeroVeloAtaque = findViewById(R.id.edt_heroi_velo_ataque);
        edtHeroVeloMovimento = findViewById(R.id.edt_heroi_velo_movimento);
        /*Inicia as views*/

        btnAddAvatar = findViewById(R.id.btn_addavatar_id);
        btnAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.setType("image/*");
//                startActivityForResult( i , REQUEST_CODE_PICK );

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE_PICK);
            }
        });


        btnSalvarHeroi = findViewById(R.id.btn_salvar_heroi);
        btnSalvarHeroi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new PhotoAsync(getParent()).execute(photoList);

                if (validateFieldsHero()) {
                    if (!updateForm)
                        saveHero();
                    else
                        updateHero();
                }
            }
        });
    }

    private void cadastroHeroTest() {

        HeroDTO hero = new HeroDTO();
        hero.setName( "Heroi Teste" );
        hero.setClassId( 1 );
        hero.setClassName( "Mago" );
        hero.setHealthPoints( 500.5 );
        hero.setDefense( 300.2 );
        hero.setDamage( 2550.8 );
        hero.setAttackSpeed( 2.2 );
        hero.setMovimentSpeed( 4.5 );

        hero.setPhotos( photoSelectedList );

        specialtySelectedList = new ArrayList<>();
        specialtySelectedList.add( specialtyList.get(1).getId() );
        specialtySelectedList.add( specialtyList.get(2).getId() );

        hero.setSpecialties(specialtySelectedList);

        String heroJSON = new Gson().toJson( hero );

        HeroesService service = retrofit.create(HeroesService.class);
        Call<Void> apiService = service.saveHero( hero );
        apiService.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Hero salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(CadastroActivity.this, "Não foi possivel salvar hero, tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void listarClasses(){

        ClassesService service = retrofit.create(ClassesService.class);
        Call<List<Classe>> classeService = service.getClasses();
        classeService.enqueue(new Callback<List<Classe>>() {
            @Override
            public void onResponse(Call<List<Classe>> call, Response<List<Classe>> response) {
                if (response.isSuccessful()){
                    for (Classe classe : response.body()){
                        Log.wtf(TAG, String.format("Nome da Classe: %s", classe.getName()));
                        classeList.add( classe );
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Classe>> call, Throwable t) {

            }
        });
    }

    public void listarSpecialties(){

        SpecialtieService service = retrofit.create(SpecialtieService.class);
        Call<List<Specialty>> classeSpecialtie = service.getSpecialties();
        classeSpecialtie.enqueue(new Callback<List<Specialty>>() {
            @Override
            public void onResponse(Call<List<Specialty>> call, Response<List<Specialty>> response) {
                if (response.isSuccessful()){
                    for (Specialty specialty : response.body()){
                        Log.wtf(TAG, String.format("Nome da Habilidade: %s", specialty.getName()));
                        specialtyList.add(specialty);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Specialty>> call, Throwable t) {

            }
        });
    }

    public void openDialogClasse(View view){

        String[] classesConvertList = new String[ classeList.size() ];

        for (int i =0; i<classeList.size(); i++){
            classesConvertList[ i ] = classeList.get( i ).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione uma classe");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(classesConvertList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                classeSelected = classeList.get(which);
            }
        });
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtHeroClasses.setText( classeSelected.getName() );
            }
        });

        AlertDialog dialogClasse = builder.create();
        dialogClasse.show();

    }

    public void openDialogSpecialtie(View view){
        specialtySelectedList = new ArrayList<>();
        final String[] specialtieConvertList = new String[ specialtyList.size() ];

        for (int i = 0; i< specialtyList.size(); i++){
            specialtieConvertList[ i ] = specialtyList.get( i ).getName();
        }

        specialtieCheckedList = new boolean[ specialtyList.size() ];

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Selecione as habilidades");
        builder1.setCancelable(false);

        builder1.setMultiChoiceItems(specialtieConvertList, specialtieCheckedList, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                specialtieCheckedList[which] = isChecked;
                specialtySelectedList.add(specialtyList.get( which ).getId());
            }
        });
        builder1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuilder habilidadesHero = new StringBuilder();
                for (int i = 0; i < specialtieConvertList.length; i++) {

                    boolean checked = specialtieCheckedList[i];
                    if (checked) {
                        habilidadesHero.append(specialtieConvertList[i] + ", ");
                    }
                }

                if(habilidadesHero.length() > 0){
                    int lastIndice = habilidadesHero.lastIndexOf(",");
                    String habilidadesTexto = habilidadesHero.replace(lastIndice, lastIndice + 2, "").toString();
                    edtHeroHabilidades.setText(habilidadesTexto);
                }

                edtHeroDano.setFocusable(true);
            }
        });

        AlertDialog dialogClasse = builder1.create();
        dialogClasse.show();

    }

    private void removeHero() {
        HeroesService service = retrofit.create(HeroesService.class);
        service.deleteHero( heroUpdate.getId() )
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(CadastroActivity.this, "Erro ao deletar heroi!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }

    public void saveHero() {
        HeroDTO hero = new HeroDTO();
        hero.setClassId( classeSelected.getId() );
        hero.setClassName( classeSelected.getName() );
        hero.setName( edtHeroNome.getText().toString() );
        hero.setHealthPoints( Double.parseDouble(edtHeroVida.getText().toString()) );
        hero.setDefense( Double.parseDouble(edtHeroDefesa.getText().toString()) );
        hero.setDamage( Double.parseDouble(edtHeroDano.getText().toString()) );
        hero.setAttackSpeed( Double.parseDouble(edtHeroVeloAtaque.getText().toString()) );
        hero.setMovimentSpeed( Double.parseDouble(edtHeroVeloMovimento.getText().toString()) );

        // TODO
        // Fazer upload das imagens
        String[] fakeImages = new String[]{"30", "39", "45", "49"};
        int indiceFakeImages = new Random().nextInt( fakeImages.length );
        String fakeImage = fakeImages[indiceFakeImages];

        photoSelectedList.add(fakeImage);
        hero.setPhotos( photoSelectedList );

        hero.setSpecialties(specialtySelectedList);

        HeroesService service = retrofit.create(HeroesService.class);
        Call<Void> apiService = service.saveHero( hero );
        apiService.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){

                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    Toast.makeText(CadastroActivity.this, "Heroi salvo com sucesso!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(CadastroActivity.this, "Não foi possivel salvar heroi, tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void updateHero() {
        HeroDTO hero = new HeroDTO();
        hero.setId( heroUpdate.getId() );
        hero.setClassId( classeSelected.getId() );
        hero.setClassName( classeSelected.getName() );
        hero.setName( edtHeroNome.getText().toString() );
        hero.setHealthPoints( Double.parseDouble(edtHeroVida.getText().toString()) );
        hero.setDefense( Double.parseDouble(edtHeroDefesa.getText().toString()) );
        hero.setDamage( Double.parseDouble(edtHeroDano.getText().toString()) );
        hero.setAttackSpeed( Double.parseDouble(edtHeroVeloAtaque.getText().toString()) );
        hero.setMovimentSpeed( Double.parseDouble(edtHeroVeloMovimento.getText().toString()) );

        hero.setPhotos( photoSelectedList );

        hero.setSpecialties(specialtySelectedList);

        HeroesService service = retrofit.create(HeroesService.class);
        Call<Void> apiService = service.updateHero( hero.getId(), hero );
        apiService.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){

                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    Toast.makeText(CadastroActivity.this, "Heroi alterado com sucesso!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(CadastroActivity.this, "Não foi possivel alterar heroi, tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public boolean validateFieldsHero(){
        StringBuilder dados = new StringBuilder();
        if(edtHeroNome.getText().toString().isEmpty()) {
            dados.append("Nome ,");
        }else if(edtHeroClasses.getText().toString().isEmpty()){
            dados.append("Classe ,");
        }else if(edtHeroHabilidades.getText().toString().isEmpty()){
            dados.append("Habilidades ,");
        }else if(edtHeroDano.getText().toString().isEmpty()){
            dados.append("Dano ,");
        }else if(edtHeroDefesa.getText().toString().isEmpty()){
            dados.append("Defesa ,");
        }else if(edtHeroDano.getText().toString().isEmpty()){
            dados.append("Dano ,");
        }else if(edtHeroVida.getText().toString().isEmpty()){
            dados.append("Vida ,");
        }else if(edtHeroVeloMovimento.getText().toString().isEmpty()){
            dados.append("Velodidade de Movimento ,");
        }else if(edtHeroVeloAtaque.getText().toString().isEmpty()) {
            dados.append("Velodidade de Ataque ,");
        }

        if(dados.length() > 0) {
            int lastIndice = dados.lastIndexOf(",");
            String dadosToast = dados.replace(lastIndice, lastIndice + 2, "").toString();
            Toast.makeText(this, String.format("É Necessário preencher os campos: %s", dadosToast), Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (updateForm) {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.menu_remove, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_remove:
                removeHero();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PICK && resultCode == RESULT_OK) {
            try{
                Retrofit retrofit = RetrofitInstance.getInstace();
                PhotosService serviceApi = retrofit.create(PhotosService.class);

                // Get the Image from data
                Uri selectedImage = data.getData();
                File file = new File(selectedImage.getPath());
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                Call<ResponseBody> response = serviceApi.uploadFiles(body);
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Log.wtf(TAG, "Imagem Salva no Banco de Dados");
                        }else{
                            Log.wtf(TAG, "Erro ao cadastrar imagem");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

                photoList.add( selectedImage );
                adapter.notifyDataSetChanged();

            }catch (Exception e){
                Toast.makeText(this, "Erro ao recuperar imagens selecionadas\n " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Hero hero = (Hero) savedInstanceState.getSerializable("hero");

        carregaDadosHeroi( hero );
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // Recupera dados do formulario

        Hero hero = new Hero();
        hero.setClassId( classeSelected.getId() );
        hero.setClassName( classeSelected.getName() );
        hero.setName( edtHeroNome.getText().toString() );
        hero.setHealthPoints( Double.parseDouble(edtHeroVida.getText().toString()) );
        hero.setDefense( Double.parseDouble(edtHeroDefesa.getText().toString()) );
        hero.setDamage( Double.parseDouble(edtHeroDano.getText().toString()) );
        hero.setAttackSpeed( Double.parseDouble(edtHeroVeloAtaque.getText().toString()) );
        hero.setMovimentSpeed( Double.parseDouble(edtHeroVeloMovimento.getText().toString()) );

        hero.setSpecialties( specialtyList );

        outState.putSerializable("hero", hero);
    }
}