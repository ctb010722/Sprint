package com.example.mislugares2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // 初始化 Firestore
    }

    public void onRegister(View view) {
        EditText nameEditText = findViewById(R.id.name);
        EditText phoneEditText = findViewById(R.id.phone);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);

        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (verificaCampos(name, phone, email, password)) { // Validar los campos ingresados
            Toast.makeText(this, "Registrando, por favor espere...", Toast.LENGTH_SHORT).show(); // Mostrar mensaje

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registro exitoso， almacenar datos en Firestore
                                saveUserData(name, phone, email);
                            } else {
                                // Registro fallido， mostrar mensaje de error
                                mensaje(task.getException() != null ? task.getException().getLocalizedMessage() : "Error desconocido");
                            }
                        }
                    });
        }
    }

    private void saveUserData(String name, String phone, String email) {
        String userId = mAuth.getCurrentUser().getUid(); // Obtener el ID del usuario

        // Crear un mapa con los datos del usuario
        User user = new User(name, phone, email);

        // Almacenar los datos en Firestore
        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Navegar a la actividad principal
                            Intent intent = new Intent(RegisterActivity.this, PaginaActivity.class);
                            startActivity(intent);
                            finish(); // Cerrar la actividad actual
                        } else {
                            mensaje("Error al guardar datos del usuario");
                        }
                    }
                });
    }

    // Clase interna para almacenar datos de usuario
    public static class User {
        private String name;
        private String phone;
        private String email;

        public User() { } // Necesario para Firestore

        public User(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        // Getters y setters (opcional)
    }

    // Método de ejemplo para validar campos de entrada
    private boolean verificaCampos(String name, String phone, String email, String password) {
        if (name.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "El teléfono no puede estar vacío", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "El correo electrónico no puede estar vacío", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true; // Todos los campos son válidos
    }

    // Este método se utiliza para mostrar un mensaje
    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
