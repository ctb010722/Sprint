//EditProfileActivity
package com.example.mislugares2;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etContrasena;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etNombre = findViewById(R.id.et_nombre);
        etCorreo = findViewById(R.id.et_correo);
        etContrasena = findViewById(R.id.et_contrasena);
        Button btnGuardar = findViewById(R.id.btn_confirmar);


        // Obtener el usuario autenticado
        usuario = FirebaseAuth.getInstance().getCurrentUser();

        // Rellenar los campos con la información actual del usuario
        if (usuario != null) {
            etNombre.setText(usuario.getDisplayName());
            etCorreo.setText(usuario.getEmail());
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPerfil();
            }
        });
    }

    private void actualizarPerfil() {
        String nuevoNombre = etNombre.getText().toString();
        String nuevoCorreo = etCorreo.getText().toString();
        String nuevaContrasena = etContrasena.getText().toString();

        // Actualizar el nombre de usuario
        UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                .setDisplayName(nuevoNombre)
                .build();

        usuario.updateProfile(perfil)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Nombre actualizado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Actualizar el correo electrónico
        if (!nuevoCorreo.isEmpty()) {
            usuario.updateEmail(nuevoCorreo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Correo actualizado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Error al actualizar correo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        // Actualizar la contraseña
        if (!nuevaContrasena.isEmpty()) {
            usuario.updatePassword(nuevaContrasena)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
