package com.proyect.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CrearUsuario extends AppCompatActivity {

    // Declaración de variables.
    Button btn_crear;
    TextView Name, Age, Cell, Location, favoriteStation, activityFavorite, textViewVolverPrincipal;
    FirebaseFirestore mfFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_usuario);

        // Inicialización de elementos.
        String id = getIntent().getStringExtra("id_user");
        mfFirestore = FirebaseFirestore.getInstance();

        Name = findViewById(R.id.postName);
        Age = findViewById(R.id.postAge);
        Cell = findViewById(R.id.postCellPhone);
        Location = findViewById(R.id.postLocation);
        favoriteStation = findViewById(R.id.postFavoriteStation);
        activityFavorite = findViewById(R.id.postfavoriteNature);
        textViewVolverPrincipal = findViewById(R.id.textViewVolverPrincipal);
        btn_crear = findViewById(R.id.btn_post);


        // Navegación desde la pantalla de Crear Usuario a la pantalla Principal.
        textViewVolverPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CrearUsuario.this, Ingreso.class);
                startActivity(intent);
            }
        });

        // Verifica el ID, configura un listener para el botón de creación y valida campos de entrada.
        // Muestra un mensaje si todos los campos están vacíos; de lo contrario, realiza una acción de creación de usuario.
        if (id == null || id == "") {
            // Manejo del clic en el botón de creación y validación de campos antes de la creación del usuario.
            btn_crear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameUser = Name.getText().toString().trim();
                    String ageUser = Age.getText().toString().trim();
                    String cellUser = Cell.getText().toString().trim();
                    String locationUser = Location.getText().toString().trim();
                    String stationYear = favoriteStation.getText().toString().trim();
                    String favoriteNature = activityFavorite.getText().toString().trim();

                    if (nameUser.isEmpty() && ageUser.isEmpty() && cellUser.isEmpty() && locationUser.isEmpty() && stationYear.isEmpty() && favoriteNature.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "\uD83D\uDEA8 Ingresa tus Datos \uD83D\uDEA8", Toast.LENGTH_SHORT).show();

                    } else {
                        postUser(nameUser, ageUser, cellUser, locationUser, stationYear, favoriteNature);
                    }
                }
            });

            // Si el ID no es nulo, el botón se configura para actualizar, obtiene los datos del usuario y realiza la acción correspondiente.
        } else {
            btn_crear.setText("Actualizar");
            getUser(id);
            btn_crear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameUser = Name.getText().toString().trim();
                    String ageUser = Age.getText().toString().trim();
                    String cellUser = Cell.getText().toString().trim();
                    String locationUser = Location.getText().toString().trim();
                    String stationYear = favoriteStation.getText().toString().trim();
                    String favoriteNature = activityFavorite.getText().toString().trim();

                    if (nameUser.isEmpty() && ageUser.isEmpty() && cellUser.isEmpty() && locationUser.isEmpty() && stationYear.isEmpty() && favoriteNature.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "\uD83D\uDEA8 Ingresa tus Datos \uD83D\uDEA8", Toast.LENGTH_SHORT).show();

                    } else {
                        updateUser(nameUser, ageUser, cellUser, locationUser, stationYear, favoriteNature, id);
                    }
                }
            });
        }
    }


    // Actualiza los datos del usuario en Firestore con la información proporcionada, muestra un mensaje de éxito o error y finaliza la actividad.
    private void updateUser(String nameUser, String ageUser, String cellUser, String locationUser, String stationYear, String favoriteNature, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("NameUser", nameUser);
        map.put("AgeUser", ageUser);
        map.put("CellUser", cellUser);
        map.put("Location", locationUser);
        map.put("favoriteStation", stationYear);
        map.put("ActivityFavorite", favoriteNature);

        mfFirestore.collection("CreateUsers").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "\uD83D\uDC9A Actualización Exitosa ✅", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "\uD83D\uDEA8 Error al Actualizar \uD83D\uDEA8", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crea un mapa de datos con la información del usuario y lo guarda en la base de datos.
    private void postUser(String nameUser, String ageUser, String cellUser, String locationUser, String stationYear, String favoriteNature) {
        Map<String, Object> map = new HashMap<>();
        map.put("NameUser", nameUser);
        map.put("AgeUser", ageUser);
        map.put("CellUser", cellUser);
        map.put("Location", locationUser);
        map.put("favoriteStation", stationYear);
        map.put("ActivityFavorite", favoriteNature);


        // Guarda los datos del usuario en Firestore y maneja las respuestas.
        mfFirestore.collection("CreateUsers").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(getApplicationContext(), "\uD83D\uDC9A Creación Exitosa ✅", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "\uD83D\uDEA8 Error al Crear \uD83D\uDEA8", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Obtiene los datos del usuario desde Firestore utilizando el ID proporcionado y actualiza los campos de entrada con la información recuperada.
    private void getUser(String id) {
        mfFirestore.collection("CreateUsers").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nameUser = documentSnapshot.getString("NameUser");
                String ageUser = documentSnapshot.getString("AgeUser");
                String cellUser = documentSnapshot.getString("CellUser");
                String locationUser = documentSnapshot.getString("Location");
                String stationYear = documentSnapshot.getString("favoriteStation");
                String favoriteNature = documentSnapshot.getString("ActivityFavorite");


                Name.setText(nameUser);
                Age.setText(ageUser);
                Cell.setText(cellUser);
                Location.setText(locationUser);
                favoriteStation.setText(stationYear);
                activityFavorite.setText(favoriteNature);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "\uD83D\uDEA8 Error al Traer los Datos \uD83D\uDEA8", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp () {
        onBackPressed();
        return false;
    }
}

