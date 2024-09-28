package com.proyect.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.proyect.myapplication.adapter.UserAdapter;
import com.proyect.myapplication.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Ingreso extends AppCompatActivity {

    // Declaración de variables.
    Button btn_exit;
    FirebaseAuth mAuth;
    Button CreateUser;
    RecyclerView mRecycler;
    UserAdapter mAdapter;
    FirebaseFirestore mFirestore;
    SearchView search_view;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso);

        // Inicialización de elementos
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        search_view = findViewById(R.id.search);
        btn_exit = findViewById(R.id.btn_close);
        CreateUser = findViewById(R.id.btn_post);

        // Navegación desde la pantalla de Bienvenida a la pantalla de inicio de Crear usuario.
        CreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ingreso.this, CrearUsuario.class);
                startActivity(intent);
            }
        });

        // Cierre de sesión y redirección a la pantalla de inicio de sesión al hacer clic en el botón de salida.
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(Ingreso.this, InicioSesion.class));
            }
        });

        setUpRecyclerView();
        search_view();
    }

    @SuppressLint("NotifyDataSetChanged")
    // Configura y muestra un RecyclerView con datos de Firestore
    private void setUpRecyclerView(){
        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        query = mFirestore.collection("CreateUsers");

        FirestoreRecyclerOptions<User> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        mAdapter = new UserAdapter(firestoreRecyclerOptions, this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }

    private void search_view() {
        // Configura el SearchView con un escutcheon para la búsqueda
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }

    private void textSearch(String s) {
        // Realiza una búsqueda de texto en Firestore y actualiza el RecyclerView
        FirestoreRecyclerOptions<User> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query.orderBy("NameUser")
                                .startAt(s).endAt(s+"~"), User.class).build();

        mAdapter = new UserAdapter(firestoreRecyclerOptions, this);
        mAdapter.startListening();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}






