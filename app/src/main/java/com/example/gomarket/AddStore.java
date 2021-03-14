package com.example.gomarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddStore extends AppCompatActivity {

    //DECLARAR CARIABLES
    Spinner mySpiner;
    CircleImageView imgStore;
    ArrayAdapter<CharSequence> adapter;
    Uri resultUri, FILE_URI;
    Button btnAddStore;
    Bitmap imageTosave;
    DBHelper MyDataBase;
    EditText Nombre, Nit, Ubicacion, Inicio, Fin, Informacion;
    Integer Dias;
    ImageButton back;

    private static final int PERMISSION_FILE =23;
    private static final int ACCESS_FILE =43;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstore);


        adapter = ArrayAdapter.createFromResource(this, R.array.itemSpinner, android.R.layout.simple_expandable_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MyDataBase = new DBHelper( this);


        //DECLARAR ELEMENTOS DEL LAYOUT
        mySpiner = (Spinner) findViewById(R.id.spinner);
        imgStore = (CircleImageView) findViewById(R.id.profile_image);
        btnAddStore = (Button) findViewById(R.id.btnaddstore);
        Nombre = (EditText) findViewById(R.id.editName);
        Nit = (EditText) findViewById(R.id.editNit);
        Ubicacion = (EditText) findViewById(R.id.editLocation);
        Inicio = (EditText) findViewById(R.id.editSchedule1);
        Fin = (EditText) findViewById(R.id.editSchedule2);
        Informacion = (EditText) findViewById(R.id.editinfo);
        back = (ImageButton) findViewById(R.id.btnBack);

        mySpiner.setAdapter(adapter);
        //ATRAS
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //RECOLECTAR LA INFORMACION DADA POR EL USUARIO
        mySpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dias = mySpiner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Dias =1;
            }
        });
        //SELECCIONAR IMAGEN
        imgStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(AddStore.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(AddStore.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), ACCESS_FILE);
                    }
                }
            }
        });
        //CREAR UNA TIENDA
        btnAddStore.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
                //ALLMACENAR LO SINPUT DE LOS USUARIOS
                String user = MainActivity.user;
                String name = Nombre.getText().toString();
                String nit = Nit.getText().toString();
                String location = Ubicacion.getText().toString();
                Integer days = Dias;
                String start = Inicio.getText().toString();
                String end = Fin.getText().toString();
                String info = Informacion.getText().toString();

                //VERIFICAR QUE TODOS LOS CAMPOS ESTEN LLENOS
                if(user.equals("")||name.equals("")||nit.equals("")||info.equals("")||location.equals("")||days.equals(null)||start.equals("")||end.equals("")||imageTosave.equals(null)){
                    Toast.makeText(AddStore.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checknit = MyDataBase.checknit(nit);
                    if(checknit==false){
                        //INSERTAR DATOS EN LA BASE DE DATOS
                        Boolean insert = MyDataBase.insertDataStores(user, name, info,nit, location,days,start,end,imageTosave);
                        if(insert==true){
                            Toast.makeText(AddStore.this, "Tienda creada exitosamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MyStores.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(AddStore.this, "Creación fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AddStore.this, "Este NIT ya existe!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //SELECCIONAR IMAGEN
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACCESS_FILE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            FILE_URI = data.getData();
            CropImage.activity(FILE_URI)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setActivityTitle("Corta la imagen")
                    .setFixAspectRatio(true)
                    .setCropMenuCropButtonTitle("Listo")
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            //CORTAR IMAGEN
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                try {
                    imageTosave = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgStore.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }




}