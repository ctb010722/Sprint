package com.example.mislugares2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario_dato);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener una instancia del usuario autenticado
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();


        TextView nombre = findViewById(R.id.nombre_usuario);
        nombre.setText(usuario.getDisplayName());

        TextView correo = findViewById(R.id.correo);
        correo.setText(usuario.getEmail());


        String proveedores = "";
        for (int n=0; n<usuario.getProviderData().size(); n++){
            proveedores += usuario.getProviderData().get(n).getProviderId()+", ";
        }
        TextView proveedor = findViewById(R.id.provedor);
        proveedor.setText(proveedores);

        TextView telefono = findViewById(R.id.telefono);
        telefono.setText(usuario.getPhoneNumber());

        TextView uid = findViewById(R.id.uid);
        uid.setText(usuario.getUid());
    }

    public void cerrarSesion(View view) {
        AuthUI.getInstance().signOut(getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(
                                getApplicationContext (),LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                });
    }




}
