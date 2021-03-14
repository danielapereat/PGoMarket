package com.example.gomarket;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    //DECLARAR VARIABLES
    CardView cardStore, cardProfile;
    View vista;
    String user = MainActivity.user;
    TextView Nombre, apellido;
    ArrayAdapter<CharSequence> adapter;
    DBHelper MyDataBase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //INFLATE
        vista = inflater.inflate(R.layout.fragment_profile, container, false);

        //INSTANCIAR BASE DE DATOS
        MyDataBase = new DBHelper(vista.getContext());
        
        //DECLARAR ELEMENTOS DEL LAYOUT
        cardStore = vista.findViewById(R.id.cardStores);
        cardProfile = vista.findViewById(R.id.cardsProfile);
        Nombre = (TextView)vista.findViewById(R.id.textName1);
        apellido = (TextView) vista.findViewById(R.id.textLastname1);


        //CONCULTAR INFORMACION DE LOS USUSARIOS
        List<ListElement> infoUser = MyDataBase.mostrarInfoUsuarios(user);


        //GUARDAR DATOS RETORNADOS POR LA BASE DE DATOS
        for (int i = 0; infoUser.size() > i ; ++i){
            Nombre.setText(infoUser.get(i).getNombrepersona());
            apellido.setText(infoUser.get(i).getApellido());

        }

        //MOVERSE HACIA LA ACTIVIDAD "MYSTORE"
        cardStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyStores.class);
                startActivity(intent);
            }
        });
        //MOVERSE HACIA LA ACTIVIDAD "MYDATA"
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyData.class);
                startActivity(intent);
            }
        });
        return vista;
    }
}