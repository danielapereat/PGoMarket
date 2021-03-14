package com.example.gomarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyData extends AppCompatActivity {
    //DECLRAR VARIABLES
    Button btnModify, btnDelete;
    DBHelper MyDataBase;

    CircleImageView imgStore;
    EditText Nombre, apellido, usuario, contraseña, oldPasword, newPasword, validPassword, email;

    String user = MainActivity.user;
    String pass = MainActivity.pass;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    Button btnCambiar, btnCancelar;

    ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydata);

        //DECLARAR ELEMENTOS DEL LAYOUT
        imgStore = (CircleImageView) findViewById(R.id.profile_image);
        btnModify = (Button) findViewById(R.id.btnModify);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        Nombre = (EditText) findViewById(R.id.editName);
        apellido = (EditText) findViewById(R.id.editLastname);
        usuario = (EditText) findViewById(R.id.editUsername);
        email = (EditText) findViewById(R.id.editMail1);
        back = (ImageButton) findViewById(R.id.btnBack);

        //INSTANCIAR BASE DE DATOS
        MyDataBase = new DBHelper( this);

        //REALIZAR UNA CONSULTA A LA BASE DE DATOS

        List<ListElement> infoPerfil = MyDataBase.mostrarInfoUsuarios(user);

        //ENVIAR INFORMACION RECIVIDA A LOS ELEMENTOS DEL LAYOUT
        for (int i = 0; infoPerfil.size() > i ; ++i){
            Nombre.setText(infoPerfil.get(i).getNombrepersona());
            apellido.setText(infoPerfil.get(i).getApellido());
            usuario.setText(infoPerfil.get(i).getUsuario());
            email.setText(infoPerfil.get(i).getMail());

            System.out.println(infoPerfil.get(i).getMail());
        }

        //MOVERSE ATRAS
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //BOTON PARA MODIFICAR LA INFORMACIÓN DEL USUARIO
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RECUPERAR LOS INPUTS DEL USUARIO
                String name = Nombre.getText().toString();
                String lastName = apellido.getText().toString();
                String username = usuario.getText().toString();
                String mail = usuario.getText().toString();

                //VERIFICAR QUE LOS DATOS ESTEN COMPLETOS
                if (user.equals("") || name.equals("") || lastName.equals("") || username.equals("")|| mail.equals("") ) {
                    Toast.makeText(MyData.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    //REALIZAR LA MODIFICACIÓN EN LA BASE DE DATOS
                    Boolean modify = MyDataBase.modificarInfoUsuarios(name, lastName,  username,user,mail);
                    if (modify == true) {
                        Toast.makeText(MyData.this, "Modificado exitosamente", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(MyData.this, "Modicicación fallido", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        //DESPLEGAR POP UP DE CONFIRMACIÓN PARA MODIFICAR CONTRASEÑA
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();

            }
        });
    }

    //POP UP DE CONFIRMACIÓN PARA MODIFICAR CONTRASEÑA
    public void showPopup(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_password, null);

        // DECLRAR ELEMENTOS DEL LAYOUT
        btnCambiar = (Button) contactPopupView.findViewById(R.id.btnCambiar);
        btnCancelar = (Button) contactPopupView.findViewById(R.id.btnCancelar1);
        oldPasword = (EditText) contactPopupView.findViewById(R.id.editOldPass);
        newPasword = (EditText) contactPopupView.findViewById(R.id.editNewPass);
        validPassword = (EditText) contactPopupView.findViewById(R.id.editValidPass);


        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        //CAMBIAR CONTRASEÑA
        btnCambiar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //RECUPERARA INPUTS DEL USUARIO
                String oldP = oldPasword.getText().toString();
                String newP = newPasword.getText().toString();
                String validP = validPassword.getText().toString();

               //VALIDAR SI LA CONTRASEÑA ESTA BIEN ESCRITA
                if (newP.equals(validP)){
                    //VALIDAR SI LA CONTRASEÑA ACTUAL ES LA CORRRECTA
                    if(oldP.equals(pass)){
                        Boolean delete = MyDataBase.CambiarContraseña(user, newP);
                        if (delete == true) {
                            Toast.makeText(MyData.this, "Modificado exitosamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MyData.this, "Modicicación fallido", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MyData.this, "Contraseña invalida", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MyData.this, "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }



            }
        });
        //CERRAR POP UP
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    }
