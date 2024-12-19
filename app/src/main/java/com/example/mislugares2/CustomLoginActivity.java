package com.example.mislugares2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.GoogleAuthProvider;

public class CustomLoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String correo = "";
    private String contraseña = "";
    private ViewGroup contenedor;
    private EditText etCorreo, etContraseña;
    private TextInputLayout tilCorreo, tilContraseña;
    private ProgressDialog dialogo;
    private static final int RC_GOOGLE_SIGN_IN = 123;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private LoginButton btnFacebook;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);

        // 初始化 ProgressDialog
        dialogo = new ProgressDialog(this);
        dialogo.setTitle("Verificando usuario");
        dialogo.setMessage("Por favor espere...");
        dialogo.setCancelable(false);  // 防止用户在加载期间取消操作

        // 初始化 FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // 绑定 UI 元素
        etCorreo = findViewById(R.id.correo);
        etContraseña = findViewById(R.id.contraseña);
        tilCorreo = findViewById(R.id.til_correo);
        tilContraseña = findViewById(R.id.til_contraseña);
        //contenedor = findViewById(R.id.contenedor);

        // Facebook 登录
        setupFacebookLogin();

        // Google 登录
        setupGoogleSignIn();

        // 打开“关于”页面


        // 检查用户是否已登录
        /*verificaSiUsuarioValidado();*/
    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        btnFacebook = findViewById(R.id.facebook);
        btnFacebook.setReadPermissions("email", "public_profile");
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookAuth(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                mensaje("Cancelada autentificación con Facebook");
            }

            @Override
            public void onError(FacebookException error) {
                mensaje(error.getLocalizedMessage());
            }
        });
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btn_google_sign_in).setOnClickListener(v -> autentificarGoogle());
    }

    private void autentificarGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }



    public void inicioSesiónCorreo(View v) {
        if (verificaCampos()) {
            dialogo.show();
            auth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this, task -> {
                        dialogo.dismiss();
                        if (task.isSuccessful()) {
                            verificaSiUsuarioValidado();
                        } else {
                            mensaje(task.getException() != null ? task.getException().getLocalizedMessage() : "Error desconocido");
                        }
                    });
        }
    }
    private void verificaSiUsuarioValidado() {
        if (auth.getCurrentUser() != null) {
            Log.d("LoginStatus", "User is logged in.");
            Intent i = new Intent(this, PaginaActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();  // 避免用户按返回键再次回到登录页面
        } else {
            Log.d("LoginStatus", "User is NOT logged in.");
        }
    }
    /*public void registroCorreo(View v) {
        Intent intent = new Intent(CustomLoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }*/

    private void mensaje(String mensaje) {
        Snackbar.make(contenedor, mensaje, Snackbar.LENGTH_LONG).show();
    }

    private boolean verificaCampos() {
        correo = etCorreo.getText().toString().trim();
        contraseña = etContraseña.getText().toString().trim();
        tilCorreo.setError(null);
        tilContraseña.setError(null);

        if (correo.isEmpty()) {
            tilCorreo.setError("Introduce un correo");
        } else if (!correo.matches(".+@.+[.].+")) {
            tilCorreo.setError("Correo no válido");
        } else if (contraseña.isEmpty()) {
            tilContraseña.setError("Introduce una contraseña");
        } else if (contraseña.length() < 6) {
            tilContraseña.setError("Ha de contener al menos 6 caracteres");
        } else if (!contraseña.matches(".*[0-9].*")) {
            tilContraseña.setError("Ha de contener un número");
        } else {
            return true;
        }
        return false;
    }

    public void firebaseUI(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    googleAuth(account.getIdToken());
                } else {
                    mensaje("Error de autentificación con Google");
                }
            } catch (ApiException e) {
                mensaje("Error de autentificación con Google");
            }
        }
    }

    private void googleAuth(String idToken) {
        if (idToken == null) {
            mensaje("Token inválido para autentificación con Google");
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        verificaSiUsuarioValidado();
                    } else {
                        mensaje(task.getException().getLocalizedMessage());
                    }
                });
    }

    private void facebookAuth(String token) {
        if (token == null) {
            mensaje("Token inválido para autentificación con Facebook");
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        verificaSiUsuarioValidado();
                    } else {
                        mensaje(task.getException().getLocalizedMessage());
                    }
                });
    }

    private void openAboutPage() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void openDesignPage() {
        Intent intent = new Intent(this, DesignActivity.class);
        startActivity(intent);
    }

    public void cerrarSesion(View view) {
        auth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            mensaje("Sesión de Google cerrada.");
            verificaSiUsuarioValidado();
        });

        LoginManager.getInstance().logOut();
        mensaje("Sesión de Facebook cerrada.");
        verificaSiUsuarioValidado();
    }
}
