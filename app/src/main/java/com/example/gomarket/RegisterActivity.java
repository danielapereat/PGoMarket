package com.example.gomarket;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    //DECLARAR VARIABLES
    EditText username2, password2, nombre, apellido,password2Valid, mail;
    ImageButton back;
    Button signup2;
    DBHelper MyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //DECLARAR ELEMENTOS DEL LAYOUT
        username2 = (EditText) findViewById(R.id.editUsername);
        password2 = (EditText) findViewById(R.id.editPassword);
        mail = (EditText) findViewById(R.id.editMail);
        password2Valid = (EditText) findViewById(R.id.editContraseñaValid);
        nombre = (EditText) findViewById(R.id.editName);
        apellido = (EditText) findViewById(R.id.editLastname);
        signup2 = (Button) findViewById(R.id.btnsignup2);
        back = (ImageButton) findViewById(R.id.btnBack);



        //DECLARAR BASE DE DATOS
        MyDB = new DBHelper( this);

        //MOVERSE A LA SIGUIENTE ACTIVIDAD
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //MOVERSE ATRAS
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //REALIZAR REGISTRO
        signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TRAER INPUT DEL USUARIO
                String user = username2.getText().toString();
                String email = mail.getText().toString();
                String pass = password2.getText().toString();
                String valid = password2Valid.getText().toString();
                String name = nombre.getText().toString();
                String lastN = apellido.getText().toString();


                if(user.equals("")||pass.equals("")||name.equals("")||lastN.equals("")||email.equals("")||valid.equals("")){
                    Toast.makeText(RegisterActivity.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    //VERIFICAR QUE EL USUARIO NO EXISTA
                    Boolean checkuser = MyDB.checkusername(user);

                    System.out.println(pass);
                    System.out.println(valid);
                    if(checkuser==false){
                        if(pass.equals(valid)){
                            //INSERTAS DATOS EN LA BASE DE DATOS
                            Boolean insert = MyDB.insertDataUser(user, pass, name, lastN, email);
                            if(insert==true){
                                Toast.makeText(RegisterActivity.this, "Registrado exitosamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Este usuario ya existe! Por favor inicia sesión", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}