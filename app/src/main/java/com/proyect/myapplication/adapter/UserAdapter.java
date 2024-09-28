package com.proyect.myapplication.adapter;



import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyect.myapplication.CrearUsuario;

import com.proyect.myapplication.Ingreso;
import com.proyect.myapplication.R;
import com.proyect.myapplication.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.ViewHolder> {

    // Instancia de FirebaseFirestore utilizada para realizar operaciones en Firestore.
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();


    // Constructor de UserAdapter que toma opciones de FirestoreRecyclerOptions y una referencia a la actividad actual.
    Activity activity;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options, Activity activity) {
        super(options);
        this.activity = activity;

    }



    // Asigna los valores de los datos del usuario a las vistas dentro del ViewHolder.
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull User User) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();
        viewHolder.NameUser.setText(User.getNameUser());
        viewHolder.AgeUser.setText(User.getAgeUser());
        viewHolder.CellUser.setText(User.getCellUser());
        viewHolder.Location.setText(User.getLocation());
        viewHolder.favoriteStation.setText(User.getFavoriteStation());
        viewHolder.ActivityFavorite.setText(User.getActivityFavorite());

        // Configura un listener para el botón de elimination en un ViewHolder que llama al método deleteUser con el ID del usuario.
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(id);
            }
        });

        // Al hacer clic en el botón de edición, inicia la actividad CrearUsuario con el ID del usuario como dato extra.
        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, CrearUsuario.class);
                i.putExtra("id_user", id);
                activity.startActivity(i);
            }
        });
    }

    // Elimina un usuario de Firestore utilizando el ID proporcionado y muestra un mensaje de éxito o error.
    private void deleteUser(String id) {
        mFirestore.collection("CreateUsers").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast deleteToast = Toast.makeText(activity, "Eliminado ✅\uD83D\uDDD1", Toast.LENGTH_SHORT);
                deleteToast.show();

                activity.getWindow().getDecorView().postDelayed(deleteToast::cancel, 5000);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "\uD83D\uDEA8 Error al Eliminar \uD83D\uDEA8", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crea un nuevo ViewHolder inflando una vista desde un diseño XML y lo devuelve.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ver_usuario, parent, false);
        return new ViewHolder(v);
    }

    // Clase que representa un ViewHolder para mantener las vistas de un elemento de la lista.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView NameUser, AgeUser, CellUser, Location, favoriteStation, ActivityFavorite;
        ImageView btn_delete, btn_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NameUser = itemView.findViewById(R.id.postName);
            AgeUser = itemView.findViewById(R.id.postAge);
            CellUser = itemView.findViewById(R.id.postCellPhone);
            Location = itemView.findViewById(R.id.postLocation);
            favoriteStation = itemView.findViewById(R.id.postFavoriteStation);
            ActivityFavorite = itemView.findViewById(R.id.postfavoriteNature);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);
            btn_edit = itemView.findViewById(R.id.btn_editar);
        }
    }
}
