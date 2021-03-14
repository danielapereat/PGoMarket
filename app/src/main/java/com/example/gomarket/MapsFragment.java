package com.example.gomarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MapsFragment extends Fragment implements AdapterList.OnNoteListener  {
    // DECLARACIÓN DE VARIABLES
    private RecyclerView recyclerViewTiendas;
    private AdapterList adaptorTiendas;
    DBHelper MyDataBase;
    EditText buscar;
    String TextBuscar = "";
    public static Integer idGlobal;
    List<ListElement> tiendas = new ArrayList<>();
    View view;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {


            LatLng cali = new LatLng(3.43722, -76.5225);
            googleMap.addMarker(new MarkerOptions().position(cali).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cali, 12));


            MyDataBase = new DBHelper( view.getContext());
            tiendas = MyDataBase.mostrarTiendas();
            List<ListElement> elementos = new ArrayList<>();


            if(TextBuscar == ""){

                System.out.println("no he tocado nada");
            }

            buscar.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    TextBuscar = buscar.getText().toString();



                }

                @Override
                public void afterTextChanged(Editable s) {
                    googleMap.clear();
                    System.out.println(TextBuscar);
                    System.out.println("aqui");
                    System.out.println("esto" + TextBuscar);
                    List<ListElement> TiendasE = MyDataBase.buscarTiendas(elementos, TextBuscar);


                    for (int i = 0; TiendasE.size() > i ; ++i){

                        Random random = new Random();

                        // Convert radius from meters to degrees
                        double radiusInDegrees = 2000 / 111000f;

                        double u = random.nextDouble();
                        double v = random.nextDouble();
                        double w = radiusInDegrees * Math.sqrt(u);
                        double t = 2 * Math.PI * v;
                        double x = w * Math.cos(t);
                        double y = w * Math.sin(t);

                        // Adjust the x-coordinate for the shrinking of the east-west distances
                        double new_x = x / Math.cos(Math.toRadians(3.43722));

                        double foundLongitude = new_x + -76.5225;
                        double foundLatitude = y + 3.43722;

                        LatLng cali = new LatLng(foundLatitude, foundLongitude);
                        googleMap.addMarker(new MarkerOptions().position(cali).title("Marker"+ TiendasE.get(i).getNombre()));
                    }

                    TiendasE.clear();




                }
            });





        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_maps, container, false);
        buscar = (EditText)view.findViewById(R.id.editBuscar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }



        MyDataBase = new DBHelper( view.getContext());
        tiendas = MyDataBase.mostrarTiendas();

    }


    @Override
    public void onNoteClick(int position) {

    }
}