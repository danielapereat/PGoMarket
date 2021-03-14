package com.example.gomarket;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MyStores extends AppCompatActivity implements AdapterList.OnNoteListener {

    //DECLARAR VARIABLES
    private RecyclerView recyclerViewTiendas;
    private AdapterList adaptorTiendas;
    public static Integer idUser, positionGlobal;
    List<ListElement> tiendas = new ArrayList<>();
    DBHelper MyDataBase;
    String user = MainActivity.user;
    ImageButton back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        //INICIAR RECYCLEVIEW
        recyclerViewTiendas = (RecyclerView) findViewById(R.id.RecyclerViewUser);
        recyclerViewTiendas.setLayoutManager(new LinearLayoutManager(this));

        //INSTANCIAR BASE DE DATOS
        MyDataBase = new DBHelper(getApplicationContext());

        //CONSULTAR BASE DE DATOS
        tiendas = MyDataBase.mostrarTiendasUsuario(user);
        adaptorTiendas = new AdapterList(tiendas, this);
        recyclerViewTiendas.setAdapter(adaptorTiendas);
        back = (ImageButton) findViewById(R.id.btnBack);

        //ATRAS
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //MOVERSE A LA ACTIVIDAD "ADDSTORE"
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), AddStore.class);
                startActivity(intent);
            }
        });
    }

    //METODO CARD CLICKEABLE
    @Override
    public void onNoteClick(int position) {
        idUser  = tiendas.get(position).getId();
        System.out.println(idUser);
        positionGlobal = position;
        Intent intent = new Intent(getApplicationContext(), ModifyStore.class);
        startActivity(intent);
    }
}