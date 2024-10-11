package com.laboratorio.tp3fotoperfil.Request;

import android.content.Context;
import android.widget.Toast;

import com.laboratorio.tp3fotoperfil.Model.Usuario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ApiClient {
    private static File file;

    public static File conectar(Context context){
        if(file == null){
            file = new File(context.getFilesDir(), "Usuario.obj");
        }
        return file;
    }

    public static Usuario login(Context context, String correo, String password){
        Usuario u = leer(context);
        if(u != null){
            if(u.getMail().equals(correo) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static Usuario leer(Context context){
        Usuario usuario = null;
        File file = conectar(context);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            usuario = (Usuario) ois.readObject();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error al encontrar el archivo", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error entrada/salida", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            Toast.makeText(context, "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
        }
        return usuario;
    }

    public static boolean Guardar(Context context, Usuario usuario){
        File file = conectar(context);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(usuario);
            bos.flush();
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error de archivo", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IOException e) {
            Toast.makeText(context, "Error de entrada/salida", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
