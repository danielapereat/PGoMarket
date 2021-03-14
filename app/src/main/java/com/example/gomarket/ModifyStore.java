package com.example.gomarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModifyStore extends AppCompatActivity  {
    //DECLARAR VARIABLES
    Button btnModify, btnDelete;
    DBHelper MyDataBase;
    Spinner mySpiner;
    CircleImageView imgStore;
    EditText Nombre, Nit, Ubicacion, Inicio, Fin, Informacion;
    Integer Dias;
    ArrayAdapter<CharSequence> adapter;
    Bitmap imageTosave;
    Uri resultUri, FILE_URI;
    Integer id = MyStores.idUser;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    Button btnEliminar, btnCancelar;

    private static final int PERMISSION_FILE =23;
    private static final int ACCESS_FILE =43;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_store);

        //DECLRARA ELEMENTOS DEL LAYOUT
        btnModify = (Button) findViewById(R.id.btnModify);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        mySpiner = (Spinner) findViewById(R.id.spinner);
        imgStore = (CircleImageView) findViewById(R.id.profile_image);
        Nombre = (EditText) findViewById(R.id.editName);
        Nit = (EditText) findViewById(R.id.editNit);
        Ubicacion = (EditText) findViewById(R.id.editLocation);
        Inicio = (EditText) findViewById(R.id.editSchedule1);
        Fin = (EditText) findViewById(R.id.editSchedule2);
        Informacion = (EditText) findViewById(R.id.editinfo);

        adapter = ArrayAdapter.createFromResource(this, R.array.itemSpinner, android.R.layout.simple_expandable_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpiner.setAdapter(adapter);

        //INSTANCIAR BASE DE DATOS
        MyDataBase = new DBHelper( this);

        //CONSULTAR BASE DE DATOS
        List<ListElement> infoTiendas = MyDataBase.mostrarInfoTiendas(id);
        System.out.println(infoTiendas);

        //ENVIAR INFORMACION RECIVIDA A LOS ELEMENTOS DEL LAYOUT
        for (int i = 0; infoTiendas.size() > i ; ++i){
            Nombre.setText(infoTiendas.get(i).getNombre());
            Nit.setText(infoTiendas.get(i).getNit());
            Ubicacion.setText(infoTiendas.get(i).getUbicacion());
            Inicio.setText(infoTiendas.get(i).getInicio());
            Fin.setText(infoTiendas.get(i).getFin());
            Informacion.setText(infoTiendas.get(i).getDescripcion());
            mySpiner.setSelection(infoTiendas.get(i).getDias());
            imageTosave = BitmapFactory.decodeByteArray(infoTiendas.get(i).getImage(), 0, infoTiendas.get(i).getImage().length);
            imgStore.setImageBitmap(imageTosave);

        }

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
                    if (ContextCompat.checkSelfPermission(ModifyStore.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(ModifyStore.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), ACCESS_FILE);
                    }

                }
            }
        });

        //MOSTRAR POP UP PARA VEFICIAR ELIMINACIÓN
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        //MODIFICAR INFROMACION
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RECUPERAR LOS INPUTS DEL USUARIO
                String user = MainActivity.user;
                String name = Nombre.getText().toString();
                String nit = Nit.getText().toString();
                String location = Ubicacion.getText().toString();
                Integer days = Dias;
                String start = Inicio.getText().toString();
                String end = Fin.getText().toString();
                String info = Informacion.getText().toString();

                //VERIFICAR QUE LOS DATOS ESTEN COMPLETOS
                if (user.equals("") || name.equals("") || nit.equals("") || info.equals("") || location.equals("") || start.equals("") || end.equals("") || imageTosave == null) {
                    Toast.makeText(ModifyStore.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    //REALIZAR LA MODIFICACIÓN EN LA BASE DE DATOS
                    Boolean modify = MyDataBase.modificarTiendasUsuario(id, nit, name, info, user, location, days, start, end, imageTosave);
                    if (modify == true) {
                        Toast.makeText(ModifyStore.this, "Modificado exitosamente", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(getApplicationContext(), MyStores.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(ModifyStore.this, "Modicicación fallido", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    //POP UP DE CONFIRMACIÓN PARA ELIMINAR TIENDA
    public void showPopup(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_eliminartienda, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        //DECLARAR BOTONES
        btnEliminar = (Button) contactPopupView.findViewById(R.id.btnEliminar);
        btnCancelar = (Button) contactPopupView.findViewById(R.id.btnCancelar);

        //ELIMINAR TIENDA
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean delete = MyDataBase.eliminarTiendasUsuario(id);
                if (delete == true) {
                    Toast.makeText(ModifyStore.this, "Modificado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MyStores.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ModifyStore.this, "Modicicación fallido", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //SALIR DEL POP UP
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //SELECCIONAR IMAGEN Y CORTARLA
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