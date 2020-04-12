package com.example.gymfitnessclub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    ////////////-------------------------------------VARIABLES----------------------------------------------------------//
    TextInputLayout tilUsuario, tilContraseña;
    Button btnIngresar;
    TextView tvRegistrar;
    String idUser,nombre,estatura,usuario,contraseña;
    ////////////-------------------------------------FIN VARIABLES----------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        ////////////-------------------------------------REFERENCIAS ----------------------------------------------------------//
        tilUsuario = findViewById(R.id.tilUsuario);
        tilContraseña = findViewById(R.id.tilContraseña);
        btnIngresar = findViewById(R.id.btnIngresar);
        tvRegistrar = findViewById(R.id.tvRegistrar);
        ////////////-------------------------------------REFERENCIAS ----------------------------------------------------------//

        ////////////-------------------------------------CONFIG BOTON REGISTRO USUARIO CON VALIDACIONES ----------------------------------------------------------//
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario =  tilUsuario.getEditText().getText().toString();
                contraseña = tilContraseña.getEditText().getText().toString();

                if(usuario.length()>0 && contraseña.length()>0){
                    login(usuario,contraseña);
                }
                if (usuario.length()==0){
                    tilUsuario.setError("Ingrese usuario");
                }
                if (contraseña.length()==0){
                    tilContraseña.setError("Ingrese contraseña");
                }


            }
        });
        ////////////-------------------------------------FIN CONFIG BOTON REGISTRO USUARIO CON VALIDACIONES ----------------------------------------------------------//


        ////////////-------------------------------------TEXTVIEW REDIRECT A REGISTRO USUARIO ----------------------------------------------------------//
        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), registraUsuario.class);
                startActivity(intent);
            }
        });

        ////////////-------------------------------------TEXTVIEW REDIRECT A REGISTRO USUARIO ----------------------------------------------------------//



    } //FIN ONCREATE



    ////////////-------------------------------------VALIDA QUE USUARIO Y CONTRASEÑA EXISTAN EN LA BD----------------------------------------------------------//
    public void login(String usuario, String contraseña){
        DBhelper dBhelper = new DBhelper(this,"GYMFITNESS_PRODUCTIVO",null,1);
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        if(db != null) {
            Cursor cur = db.rawQuery("SELECT * FROM  tbl_usuarios where nombreUsuario = '"+usuario+"' and clave = '"+contraseña+"' ",null);
            int cantidad = cur.getCount();
            if(cantidad == 1){
                if(cur.moveToFirst()){
                    do {
                        idUser = cur.getString(0);
                        nombre = cur.getString(2);
                        estatura = cur.getString(5);
                    }while (cur.moveToNext());
                }
                Toast.makeText(this, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(this, registraActividad.class);
                intent.putExtra("idUser",idUser);
                intent.putExtra("estatura",estatura);
                startActivity(intent);
            }else{
                Toast.makeText(this,"Usuario inválido", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////-------------------------------------FIN VALIDA QUE USUARIO Y CONTRASEÑA EXISTAN EN LA BD----------------------------------------------------------//





}
