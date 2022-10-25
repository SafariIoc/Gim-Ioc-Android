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
import com.DAM.gim_ioc.ModelsRealm.LoginUsuari;
import com.DAM.gim_ioc.Volley.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.servidorIOC.com",
                ServerResponse -> {

                    try {
                        JSONArray array = new JSONArray(ServerResponse);

                        JSONObject dades = array.getJSONObject(0);
                        if (dades.getString("data").equalsIgnoreCase("Data Matched")) {

                            LoginUsuari loginUsuari = realm.where(LoginUsuari.class)
                                    .findFirst();
                            realm.beginTransaction();
                            loginUsuari.setIdBBDD(dades.getInt("id"));
                            loginUsuari.setUserName(dades.getString("login"));
                            loginUsuari.setToken(dades.getString("token"));
                            loginUsuari.setEmail(dades.getString("email"));
                            loginUsuari.setSubscripcio(dades.getString("subscripcio"));
                            loginUsuari.setToken(dades.getString("token"));
                            realm.commitTransaction();

                            // Anulo el progressBar al rebre resposta del servidor
                            progressBarLogin.setVisibility(View.GONE);

                            // Tanquem l'actual Activity i obrim l'aplicació
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), dades.getString("data"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    // Anulo el progressBar al rebre resposta del servidor
                    progressBarLogin.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), R.string.toastSenseConexio, Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("User_Login", UsuariHolder);
                params.put("User_Password", PasswordHolder);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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