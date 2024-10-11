package com.laboratorio.tp3fotoperfil.Registrar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.laboratorio.tp3fotoperfil.MainActivity;
import com.laboratorio.tp3fotoperfil.Model.Usuario;
import com.laboratorio.tp3fotoperfil.databinding.ActivityRegistrarBinding;

public class RegistrarActivity extends AppCompatActivity {
    private ActivityRegistrarBinding binding;
    private RegistrarViewModel vm;
    private Intent intent;
    private ActivityResultLauncher<Intent> arl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegistrarViewModel.class);
        binding = ActivityRegistrarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        abrirGaleria();

        vm.getMUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.etDni.setText(usuario.getDni());
                binding.etApellido.setText(usuario.getApellido());
                binding.etNombre.setText(usuario.getNombre());
                binding.etEmail.setText(usuario.getMail());
                binding.etPassword.setText(usuario.getPassword());
                String imgAvatar = usuario.getImgAvatar();
                vm.setImgAvatar(imgAvatar);
                binding.imgAvatar.setImageURI(Uri.parse(imgAvatar));
            }
        });

        Intent i = getIntent();
        boolean esLogin= i.getBooleanExtra("login", false);


        binding.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dni = binding.etDni.getText().toString();
                String apellido = binding.etApellido.getText().toString();
                String nombre = binding.etNombre.getText().toString();
                String mail = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                Usuario usuario = new Usuario(dni, apellido, nombre, mail, password);
                vm.guardar(usuario);
            }
        });

        vm.getMImgAvatar().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.imgAvatar.setImageURI(uri);
            }
        });

        vm.leerDatos(esLogin);

        //subir foto
        binding.btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(intent);
            }
        });

        vm.getMMsj().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(RegistrarActivity.this, s, Toast.LENGTH_SHORT).show();
                if (s.equals("Registro exitoso!!")) {
                    // Redirigir al MainActivity
                    Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Cierra la actividad de registro
                }
            }
        });


        binding.btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Limpiar la pila de actividades
                startActivity(intent);
                finish();
            }
        });


    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                vm.recibirFoto(result);
            }
        });
    }
}
