package com.laboratorio.tp3fotoperfil;

import static com.laboratorio.tp3fotoperfil.Request.ApiClient.leer;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.laboratorio.tp3fotoperfil.Model.Usuario;
import com.laboratorio.tp3fotoperfil.Registrar.RegistrarActivity;
import com.laboratorio.tp3fotoperfil.Request.ApiClient;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<Usuario> mUsuario;
    private MutableLiveData<String> mError;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMError(){
        if(mError==null){
            mError=new MutableLiveData<>();
        }
        return mError;
    }

    public void login(String mail, String password){
        if(mail.isEmpty()||password.isEmpty()){
            mError.setValue("Complete todos los campos");
        }else{
            Usuario u = ApiClient.login(getApplication().getApplicationContext(), mail, password);
            if(u == null){
                mError.setValue("Credenciales incorrectas");
            }else{
                Intent i = new Intent(getApplication().getApplicationContext(), RegistrarActivity.class);
                i.putExtra("login", true);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().getApplicationContext().startActivity(i);
            }
        }
    }

    public void registrar(){
        Intent i = new Intent(getApplication().getApplicationContext(), RegistrarActivity.class);
        i.putExtra("login", false);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().getApplicationContext().startActivity(i);
    }


}
