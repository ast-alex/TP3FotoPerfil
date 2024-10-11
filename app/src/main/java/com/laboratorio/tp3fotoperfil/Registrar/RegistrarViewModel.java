package com.laboratorio.tp3fotoperfil.Registrar;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.laboratorio.tp3fotoperfil.Model.Usuario;
import com.laboratorio.tp3fotoperfil.Request.ApiClient;

public class RegistrarViewModel extends AndroidViewModel {
    private MutableLiveData<Usuario> mUsuario;
    private MutableLiveData<Uri> mImgAvatar;
    private MutableLiveData<String> mMsj;
    private String imgAvatar;
    public RegistrarViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Usuario> getMUsuario(){
        if(mUsuario==null){
            mUsuario=new MutableLiveData<>();
        }
        return mUsuario;
    }

    public LiveData<Uri> getMImgAvatar(){
        if(mImgAvatar==null){
            mImgAvatar=new MutableLiveData<>();
        }
        return mImgAvatar;
    }

    public LiveData<String> getMMsj(){
        if(mMsj==null){
            mMsj=new MutableLiveData<>();
        }
        return mMsj;
    }

    public void guardar(Usuario usuario){
        if(usuario.getDni().isEmpty() ||usuario.getNombre().isEmpty() || usuario.getApellido().isEmpty() || usuario.getMail().isEmpty() || usuario.getPassword().isEmpty()){
            Toast.makeText(getApplication().getApplicationContext(), "Todos los campos deben estar completos!!", Toast.LENGTH_SHORT).show();
        }else{
            usuario.setImg(imgAvatar);
            if(ApiClient.Guardar(getApplication().getApplicationContext(), usuario)){
                mUsuario.setValue(usuario);
                Toast.makeText(getApplication().getApplicationContext(), "Registro exitoso!!", Toast.LENGTH_SHORT).show();
            }else {
                mMsj.postValue("Error al registrar!!");
            }
        }
    }

    public void leerDatos(boolean bolean){
        if(bolean){
            Usuario usuario = ApiClient.leer(getApplication().getApplicationContext());
            imgAvatar=usuario.getImgAvatar();
            mUsuario.setValue(usuario);
        }
    }

    public void recibirFoto(ActivityResult result){
        if(result.getResultCode()==RESULT_OK){
            Intent data = result.getData();
            Uri uri = data.getData();
            imgAvatar=uri.toString();
            getApplication().getApplicationContext().getContentResolver()
                    .takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mImgAvatar.setValue(uri);
        }
    }

    public void setImgAvatar(String imgAvatar) {
        this.imgAvatar = imgAvatar;
    }
}
