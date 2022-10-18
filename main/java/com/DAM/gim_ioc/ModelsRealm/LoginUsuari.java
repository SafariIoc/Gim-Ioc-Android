package com.DAM.gim_ioc.ModelsRealm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LoginUsuari extends RealmObject {

    @PrimaryKey
    private int id;
    private int idBBDD;
    private String userName;
    private String token;
    private String email;
    private String subscripcio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBBDD() {
        return idBBDD;
    }

    public void setIdBBDD(int idBBDD) {
        this.idBBDD = idBBDD;
    }

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getSubscripcio() {return subscripcio;}

    public void setSubscripcio(String subscripcio) {this.subscripcio = subscripcio;}

}
