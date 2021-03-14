package com.example.gomarket;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment implements AdapterList.OnNoteListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // DECLARACIÓN DE VARIABLES
    private RecyclerView recyclerViewTiendas;
    private AdapterList adaptorTiendas;
    DBHelper MyDataBase;
    EditText buscar;
    String TextBuscar = "";
    public static Integer idGlobal;
    List<ListElement> tiendas = new ArrayList<>();
    View view;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static home newInstance(String param1, String param2) {

        //DECLARACION DE VARIABLES
        home fragment = new home();
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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewTiendas = (RecyclerView)view.findViewById(R.id.RecyclerView);
        recyclerViewTiendas.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //INSTANCIAR BASE DE DATOS
        MyDataBase = new DBHelper( view.getContext());

        //CONSULTAR TIENDAS EN BASE DE DATOS
        tiendas = MyDataBase.mostrarTiendas();

        //DECLARAR ELEMENTOS DEL LAYOUT
        buscar = (EditText)view.findViewById(R.id.editBuscar);
        List<ListElement> elementos = new ArrayList<>();


        //DECLARAR UN VALOR POR DEFAULT
        if(TextBuscar == ""){
            adaptorTiendas = new AdapterList(MyDataBase.mostrarTiendas(),this);
            recyclerViewTiendas.setAdapter(adaptorTiendas);
        }

        //SE REALIZA UNA BUSQUEDA
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextBuscar = buscar.getText().toString();
                adaptorTiendas.removeItems();
                adaptorTiendas.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println(TextBuscar);
                System.out.println("aqui");
                System.out.println("esto" + TextBuscar);
                adaptorTiendas = new AdapterList(MyDataBase.buscarTiendas(elementos, TextBuscar),home.this::onNoteClick);
                recyclerViewTiendas.setAdapter(adaptorTiendas);
            }
        });

        return view;
    }


    //METODO PARA CARDS CLIKEABLES
    @Override
    public void onNoteClick(int position) {
        idGlobal = tiendas.get(position).getId();
        Intent intent = new Intent(getActivity().getApplicationContext(), InfoStore.class);
        startActivity(intent);
    }
}