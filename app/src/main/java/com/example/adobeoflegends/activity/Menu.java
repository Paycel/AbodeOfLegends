package com.example.adobeoflegends.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.adobeoflegends.R;
import com.example.adobeoflegends.database.DBHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.Locale;

public class Menu extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button buttonRandomGame;
    private Button buttonLoad;
    private Button buttonShop;
    private Button buttonChooseGame;
    private Button buttonSignOut;
    private Button signInButton;
    private ImageView profileImage;
    private TextView textPoints;
    private GoogleApiClient googleApiClient;
    private DBHelper dbHelper;
    private SQLiteDatabase dataBase;
    private int points;
    private String currentUser;
    private static final int SIGN_IN_CODE = 100;
    private static final String LOG_TAG = "MY_LOGS";
    private SharedPreferences sharedPreferences;
    private static final String locale = "Locale";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);
        dbHelper = new DBHelper(this);
        dataBase = dbHelper.getWritableDatabase();
        sharedPreferences = getPreferences(MODE_PRIVATE);
        textPoints = (TextView) findViewById(R.id.points);
        /*
        TODO Ловить интент и записывать в preferences данные, которые передаются в Locale в БД
        TODO Если есть учётная запись, то кидать данные из Locale в эту учётку

        TODO Запуск приложения - создаётся Locale, где есть поинты
        TODO Привязываешь аккаунт - поинты из Locale передаются аккаунту
        TODO Если появляется 3 и т.д. строка в БД, присваивать тогда ей 0 поинтов
        TODO Если currentUser != Locale, тогда присваивать поинты нужному аккаунту в БД
         */

        points = getIntent().getIntExtra("points", 0);
        currentUser = getIntent().getStringExtra("currentUser");
        if (currentUser == null) currentUser = locale;
        if (dbHelper.getPoints(dataBase, locale) == -1) dbHelper.addUser(dataBase, locale, 0);
        dbHelper.setPoints(dataBase, currentUser, dbHelper.getPoints(dataBase, currentUser) + points);
        showPoints(currentUser);
        dbHelper.showInfo(dataBase);
        Log.d(LOG_TAG, "Loaded points = " + dbHelper.getPoints(dataBase, currentUser));
        buttonRandomGame = (Button) findViewById(R.id.btn_RandomGame);
        buttonLoad = (Button) findViewById(R.id.btn_Load);
        buttonShop = (Button) findViewById(R.id.btn_Shop);
        buttonChooseGame = (Button) findViewById(R.id.btn_ChooseLevel);
        Button buttonRU = (Button) findViewById(R.id.btn_RU);
        signInButton = (Button) findViewById(R.id.btn_SIGNIN);
        buttonSignOut = (Button) findViewById(R.id.btn_EXIT);
        buttonSignOut.setVisibility(View.INVISIBLE);
        Button buttonEN = (Button) findViewById(R.id.btn_ENG);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_RandomGame:
                        Intent newGame = new Intent(Menu.this, BattleActivity.class);
                        newGame.putExtra("difficulty", 0);
                        newGame.putExtra("currentUser", currentUser);
                        startActivity(newGame);
                        break;
                    case R.id.btn_Load:
                        Intent load = new Intent(Menu.this, Load.class);
                        load.putExtra("currentUser", currentUser);
                        startActivity(load);
                        break;
                    case R.id.btn_Shop:
                        //TODO Магазин
                        break;
                    case R.id.btn_ChooseLevel:
                        Intent i = new Intent(Menu.this, Choose_Level.class);
                        i.putExtra("currentUser", currentUser);
                        startActivity(i);
                        break;
                    case R.id.btn_ENG:
                        Locale locale = new Locale("en");
                        changeLocale(locale);
                        break;
                    case R.id.btn_RU:
                        locale = new Locale("ru");
                        changeLocale(locale);
                        break;
                }
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            signInButton.setVisibility(View.VISIBLE);
                            buttonSignOut.setVisibility(View.INVISIBLE);
                            Log.d(LOG_TAG, "Successful sign out of account!");
                            currentUser = locale;
                            showPoints(currentUser);
                        } else {
                            Log.d(LOG_TAG, "Sign out failed!");
                        }
                    }
                });
            }
        });
        buttonLoad.setOnClickListener(listener);
        buttonRandomGame.setOnClickListener(listener);
        buttonShop.setOnClickListener(listener);
        buttonChooseGame.setOnClickListener(listener);
        buttonEN.setOnClickListener(listener);
        buttonRU.setOnClickListener(listener);
    }

    void showPoints(String email){
        Log.d(LOG_TAG, "Current user = " + currentUser);
        textPoints.setText(getResources().getText(R.string.points).toString() + dbHelper.getPoints(dataBase, email));
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private void changeLocale(Locale locale){
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources()
                .updateConfiguration(configuration,
                        getBaseContext()
                                .getResources()
                                .getDisplayMetrics());
        buttonLoad.setText(getResources().getText(R.string.load_results).toString());
        buttonRandomGame.setText(getResources().getText(R.string.randomly).toString());
        buttonShop.setText(getResources().getText(R.string.shop).toString());
        buttonChooseGame.setText(getResources().getText(R.string.choose_level).toString());
        buttonSignOut.setText(getResources().getText(R.string.exit).toString());
        signInButton.setText(getResources().getText(R.string.sign_in).toString());
        showPoints(currentUser);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                Log.d(LOG_TAG, "Login success!");
                GoogleSignInAccount account = result.getSignInAccount();
                String email = account.getEmail();
                String name = account.getDisplayName();
                Log.d(LOG_TAG, "EMAIL = " + email + "\nNAME = " + name);
                if (dbHelper.getRowsCount(dataBase) == 1) dbHelper.addUser(dataBase, email, dbHelper.getPoints(dataBase, locale));
                else if (dbHelper.getPoints(dataBase, email) == -1) dbHelper.addUser(dataBase, email, 0);
                currentUser = email;
                showPoints(currentUser);
                points = dbHelper.getPoints(dataBase, email);
                Log.d(LOG_TAG, "Points = " + points);
                signInButton.setVisibility(View.INVISIBLE);
                buttonSignOut.setVisibility(View.VISIBLE);
            } else {
                Log.d(LOG_TAG, "Login failed!");
                buttonSignOut.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String email = account.getEmail();
            currentUser = email;
            showPoints(currentUser);
            String name = account.getDisplayName();
            Log.d(LOG_TAG, "EMAIL = " + email + "\nNAME = " + name);

            points = dbHelper.getPoints(dataBase, email);

            Log.d(LOG_TAG, "Points = " + points);
            try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
            }catch (NullPointerException e){
                Log.d(LOG_TAG, "Image not found");
            }
            signInButton.setVisibility(View.INVISIBLE);
            buttonSignOut.setVisibility(View.VISIBLE);
        } else {
            currentUser = locale;
            showPoints(currentUser);
            signInButton.setVisibility(View.VISIBLE);
            buttonSignOut.setVisibility(View.INVISIBLE);
            Log.d(LOG_TAG, "Start login failed");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
}
