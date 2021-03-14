package com.example.gomarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Declaración de variables
    public static String user, pass;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    EditText username, password;
    Button signup, signin;
    DBHelper MyDataBase;
    TextView olvidar;
    Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DECLARAR ELEMENTOS DEL LAYOUT
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        olvidar = (TextView) findViewById(R.id.textOlvidar);
        MyDataBase = new DBHelper( this);


        //SE DEPLEGA POP UP DE "OLVIDAR CONTRASEÑA"
        olvidar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        //MOVERSE A LA SIGUIETE ACTIVIDAD
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        //REALIZAR INICIO Y VALIDACIÓN DE SESIÓN
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(MainActivity.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = MyDataBase.checkusernamepassword(user, pass);
                    System.out.println(user);
                    if(checkuserpass==true){
                        Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    //POP UP "OLVIDO CONTRASEÑA"
    public void showPopup(){

        dialogBuilder = new AlertDialog.Builder(this);

        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_fogetpassword, null);

        btnAceptar = (Button) contactPopupView.findViewById(R.id.btnAceptar);


        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}