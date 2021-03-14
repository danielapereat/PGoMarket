package com.example.gomarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoStore extends AppCompatActivity {
    //DECLARAR VARIABLES
    DBHelper MyDataBase;
    CircleImageView imgStore;
    TextView Nombre, Nit, Ubicacion, Inicio, Fin, Informacion, Horario;
    ArrayAdapter<CharSequence> adapter;
    Bitmap imageTosave;
    Integer id = home.idGlobal;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_store);

        //INSTANCIAR BASE DE DATOS
        MyDataBase = new DBHelper( this);

        //DECLARAR ELEMENTOS DEL LAYOUT
        imgStore = (CircleImageView) findViewById(R.id.profile_image);
        Nombre = (TextView) findViewById(R.id.editName);
        Nit = (TextView) findViewById(R.id.editNit);
        Ubicacion = (TextView) findViewById(R.id.editLocation);
        Inicio = (TextView) findViewById(R.id.editSchedule1);
        Fin = (TextView) findViewById(R.id.editSchedule2);
        Informacion = (TextView) findViewById(R.id.editinfo);
        Horario = (TextView) findViewById(R.id.editHorario1);
        back = (ImageButton) findViewById(R.id.btnBack);


        //MOVERSE ATRAS
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //CONSULTAR BASE DE DATOS
        List<ListElement> infoTiendas = MyDataBase.mostrarInfoTiendas(id);
        System.out.println(infoTiendas);

        //DARLE VALOR A "DIAS"
        for (int i = 0; infoTiendas.size() > i ; ++i){
            Integer temp = infoTiendas.get(i).getDias();
            switch (temp){
                case 0: Horario.setText("Lunes-Viernes");
                    break;
                case 1: Horario.setText("Lunes-Sabado");
                    break;
                case 2: Horario.setText("Lunes-Domingo");
                    break;
            }

            //MOSTRAR DATOS
            Nombre.setText(infoTiendas.get(i).getNombre());
            Nit.setText(infoTiendas.get(i).getNit());
            Ubicacion.setText(infoTiendas.get(i).getUbicacion());
            Inicio.setText(infoTiendas.get(i).getInicio());
            Fin.setText(infoTiendas.get(i).getFin());
            Informacion.setText(infoTiendas.get(i).getDescripcion());
            imageTosave = BitmapFactory.decodeByteArray(infoTiendas.get(i).getImage(), 0, infoTiendas.get(i).getImage().length);
            imgStore.setImageBitmap(imageTosave);

        }
    }
}