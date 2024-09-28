package com.proyect.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesion extends AppCompatActivity {

    // Declaración de variables.
    CheckBox recuerdame;
    Button btn_login;
    EditText email, password;
    FirebaseAuth mAuth;
    TextView olvidasteContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        // Inicialización y búsqueda de elementos de la interfaz y Firebase Authentication.
        recuerdame = findViewById(R.id.remenber_check);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.correoLogin);
        password = findViewById(R.id.contraseñaLogin);
        btn_login = findViewById(R.id.loginButton);
        olvidasteContraseña = findViewById(R.id.forgotPassword);

        // Recuperar preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        // Verificar si se guardaron credenciales previas
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            email.setText(savedEmail);
            password.setText(savedPassword);
            recuerdame.setChecked(true);
        }

        // Agregar un Listener al CheckBox para guardar o eliminar las credenciales según corresponda
        recuerdame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Usuario seleccionó recordar correo y contraseña, guardar las credenciales
                    String emailrecuerdamelo = email.getText().toString();
                    String passwordrecuerdamela = password.getText().toString();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", emailrecuerdamelo);
                    editor.putString("password", passwordrecuerdamela);
                    editor.apply();

                } else {
                    // Usuario deseleccionó recordar correo y contraseña, eliminar las credenciales
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("email");
                    editor.remove("password");
                    editor.apply();
                }
            }
        });

        // Redirección a la actividad de recuperación de contraseña al hacer clic en "Olvidaste tu contraseña".
        olvidasteContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioSesion.this, Recuperar_contraseña.class);
                startActivity(intent);
                finish();
            }
        });

        TextView textViewRegistrarme = findViewById(R.id.textViewRegistrarme);
        textViewRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InicioSesion.this, Registro.class);
                startActivity(intent);
            }
        });

        // Manejo del clic en el botón de inicio de sesión, incluyendo validación de datos y autenticación.
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = email.getText().toString().trim();
                String passUser = password.getText().toString().trim();

                if(emailUser.isEmpty() && passUser.isEmpty()) {
                    final Toast toast = Toast.makeText(InicioSesion.this, "\uD83D\uDEA8 Ingresa tus Datos \uD83D\uDEA8", Toast.LENGTH_SHORT);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 2000);

                } else {
                    loginUser(emailUser, passUser);
                }
            }
        });
    }

    // Autenticación del usuario con las credenciales proporcionadas y manejo de resultados.
    private void loginUser(String emailUser, String passUser) {
        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(InicioSesion.this, Ingreso.class));
                    final Toast welcomeToast = Toast.makeText(InicioSesion.this, "\uD83C\uDF89 \uD83C\uDF0D¡BIENVENIDO!\uD83C\uDF0E \uD83C\uDF89", Toast.LENGTH_SHORT);
                    welcomeToast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            welcomeToast.cancel();
                        }
                    }, 4000);

                } else {
                    final Toast incorrectToast = Toast.makeText(InicioSesion.this, "Correo o Contraseña Incorrectos \uD83D\uDE27", Toast.LENGTH_SHORT);
                    incorrectToast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            incorrectToast.cancel();
                        }
                    }, 3000);
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                final Toast errorToast = Toast.makeText(InicioSesion.this, "Error al Iniciar Sesión \uD83D\uDD12", Toast.LENGTH_SHORT);
                errorToast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        errorToast.cancel();
                    }
                }, 5000);
            }
        });
    }

    // Comprobación de la autenticación al inicio de la actividad.
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(InicioSesion.this, Ingreso.class));
            finish();
        }
    }
}


