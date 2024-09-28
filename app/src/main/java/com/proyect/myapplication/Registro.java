package com.proyect.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro  extends AppCompatActivity {

    Button btn_register;
    EditText name, email, password, confirm_password, cellPhone;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicialización de elementos y Firebase.
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.Nombre);
        email = findViewById(R.id.Correo);
        password = findViewById(R.id.Contraseña);
        confirm_password = findViewById(R.id.Confirmar_Contraseña);
        cellPhone = findViewById(R.id.Celular);
        btn_register = findViewById(R.id.btn_registration);
        TextView textViewIniciarSeción = findViewById(R.id.textViewIniciarSeción);

        textViewIniciarSeción = findViewById(R.id.textViewIniciarSeción);
        textViewIniciarSeción.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this, InicioSesion.class);
                startActivity(intent);
            }
        });

        // Acción al hacer clic en el botón de registro: valida los campos y registra al usuario si los datos son válidos.
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUser = name.getText().toString().trim();
                String emailUser = email.getText().toString().trim();
                String passUser = password.getText().toString().trim();
                String confPass = confirm_password.getText().toString().trim();
                String cell = cellPhone.getText().toString().trim();

                if (nameUser.isEmpty() && emailUser.isEmpty() && passUser.isEmpty() && confPass.isEmpty() && cell.isEmpty()) {
                    final Toast completeDataToast = Toast.makeText(Registro.this, "\uD83D\uDEA8 Complete los Datos \uD83D\uDEA8", Toast.LENGTH_SHORT);
                    completeDataToast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            completeDataToast.cancel();
                        }
                    }, 2000);

                } else {
                    registerUser(nameUser, emailUser, passUser, confPass, cell);
                }
            }
        });
    }

    // Registro de usuario y almacenamiento de datos en Firebase Auth y Firestore.
    private void registerUser(String nameUser, String emailUser, String passUser, String confPass, String cell) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", nameUser);
                map.put("email", emailUser);
                map.put("password", passUser);
                map.put("confirm_password", confPass);
                map.put("cellPhone", cell);

                mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(Registro.this, InicioSesion.class));
                        final Toast successToast = Toast.makeText(Registro.this, "Registro Exitoso ✅\uD83C\uDF89", Toast.LENGTH_SHORT);
                        successToast.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                successToast.cancel();
                            }
                        }, 4000);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro.this, "\uD83D\uDEA8 Error al Guardar \uD83D\uDEA8" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro.this, "\uD83D\uDEA8 Error al Registrar \uD83D\uDEA8" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

