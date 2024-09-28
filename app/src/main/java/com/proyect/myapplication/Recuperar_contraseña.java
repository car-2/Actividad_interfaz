package com.proyect.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Recuperar_contraseña extends AppCompatActivity {

    // Declaración de variables
    Button recuperarBoton;
    EditText emailforRecuperation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);


        // Inicialización y búsqueda de elementos
        TextView retrocederlogin = findViewById(R.id.recedeLogin);
        recuperarBoton = findViewById(R.id.recuperarbtn);
        emailforRecuperation = findViewById(R.id.recoverEmail);


        // Navega desde la actividad de recuperación de contraseña a la actividad de inicio de sesión al hacer clic
        retrocederlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Recuperar_contraseña.this, InicioSesion.class);
                startActivity(intent);
            }
        });

        // Configura el botón de recuperación.
        recuperarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        recuperarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailforRecuperation.getText().toString().trim();
                if (email.isEmpty()) {
                    final Toast enterEmailToast = Toast.makeText(Recuperar_contraseña.this, "\uD83D\uDEA8 Ingresa tu Correo \uD83D\uDEA8", Toast.LENGTH_SHORT);
                    enterEmailToast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enterEmailToast.cancel();
                        }
                    }, 2000);

                } else {
                    sendEmail(email);
                }
            }
        });
    }

    // Valida el formato del correo electrónico ingresado y envía un correo de recuperación si es válido.
    public void validate() {
        String email = emailforRecuperation.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailforRecuperation.setError("\uD83D\uDEA8 Formato de Correo Incorrecto \uD83D\uDEA8");
            return;
        }
        sendEmail(email);
    }

    //Maneja la acción cuando se presiona el botón de retroceso. Navega a la actividad de inicio de sesión.
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Recuperar_contraseña.this, InicioSesion.class);
        startActivity(intent);
        finish();
    }

    // Envía un correo de recuperación de contraseña a la dirección de correo especificada.
    public void sendEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            final Toast emailSentToast = Toast.makeText(Recuperar_contraseña.this, "\uD83D\uDCE8 Correo Enviado ✅", Toast.LENGTH_SHORT);
                            emailSentToast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    emailSentToast.cancel();
                                }
                            }, 3000);
                            Intent intent = new Intent(Recuperar_contraseña.this, InicioSesion.class);
                            startActivity(intent);
                            finish();

                        } else {
                            final Toast incorrectEmailToast = Toast.makeText(Recuperar_contraseña.this, "\uD83D\uDEA8 Correo Incorrecto \uD83D\uDEA8", Toast.LENGTH_SHORT);
                            incorrectEmailToast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    incorrectEmailToast.cancel();
                                }
                            }, 2000);
                        }
                    }
                });
    }
}
