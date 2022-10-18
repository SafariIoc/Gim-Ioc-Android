package com.DAM.gim_ioc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import com.DAM.gim_ioc.ModelsRealm.LoginUsuari;

public class IniciActivity extends AppCompatActivity {

    Realm realm;
    EditText Usuari, Password;
    Button LoginButton, RecordarPass;
    String UsuariHolder, PasswordHolder;
    ProgressBar progressBarLogin;
    Boolean CheckEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inici);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.hide();

        //Carraguem configuració Realm
        Realm.init(getApplicationContext());
        setUpRealmConfig();

        realm = Realm.getDefaultInstance();

        progressBarLogin = findViewById(R.id.progressBarNouUsuari);

        realm = Realm.getDefaultInstance();

        Usuari = findViewById(R.id.editText_NouUsuari);
        Password = findViewById(R.id.editText_Password);
        LoginButton = findViewById(R.id.button_login);
        RecordarPass = findViewById(R.id.btn_recordar);

        LoginButton.setOnClickListener(view -> {

            CheckEditTextIsEmptyOrNot();

            if (CheckEditText) {
                UserLogin();
            } else {
                Toast.makeText(getApplicationContext(), "Emplena tots els camps.", Toast.LENGTH_LONG).show();
            }
        });

        RecordarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "S'ha enviat un correu amb instruccions", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void UserLogin() {

        progressBarLogin.setVisibility(View.VISIBLE);
        progressBarLogin.setEnabled(true);

        RealmResults<LoginUsuari> results = realm.where(LoginUsuari.class)
                .findAll();
        if (results.size() != 0) {
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        }
        realm.beginTransaction();
        LoginUsuari loginUsuari = realm.createObject(LoginUsuari.class, String.valueOf(0));
        loginUsuari.setIdBBDD(1);
        loginUsuari.setUserName("ferran");
        loginUsuari.setToken("token");
        loginUsuari.setEmail("email");
        loginUsuari.setSubscripcio("subscripcio");
        realm.commitTransaction();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void CheckEditTextIsEmptyOrNot() {

        UsuariHolder = Usuari.getText().toString().trim();
        PasswordHolder = Password.getText().toString().trim();
        CheckEditText = !TextUtils.isEmpty(UsuariHolder) && !TextUtils.isEmpty(PasswordHolder);
    }


    private void setUpRealmConfig() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}