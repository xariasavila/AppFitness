package com.example.gymfitnessclub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class registraUsuario extends AppCompatActivity {

    ////////////-------------------------------------VARIABLES----------------------------------------------------------//
    TextInputLayout tilUsuario2, tilNombre, tilApellido,tilFechNac, tilEstatura, tilContraseña2,tilRepCon;
    String nombreUsuario,nombre,apellido,fechNac,estatura, contraseña,repContraseña;
    Button btnRegistrar;
    ////////////-------------------------------------VARIABLES----------------------------------------------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registra_usuario);

////////////-------------------------------------CAPTURA DE VALORES----------------------------------------------------------//

        tilUsuario2 = findViewById(R.id.tilUser);
        tilNombre = findViewById(R.id.tilNombre);
        tilApellido = findViewById(R.id.tilApellido);
        tilFechNac = findViewById(R.id.tilFechNacimiento);
        tilEstatura = findViewById(R.id.tilEstatura);
        tilContraseña2 = findViewById(R.id.tilPassRegistro);
        tilRepCon = findViewById(R.id.tillPassRegistro2);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        ////////////-------------------------------------CAPTURA DE VALORES----------------------------------------------------------//


////////////-------------------------------------DATEPPICKER----------------------------------------------------------//
        tilFechNac.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(registraUsuario.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                                @SuppressLint("DefaultLocale") String fecha = String.format("%02d/%02d/%d", mday,(mmonth +1), myear);
                                tilFechNac.getEditText().setText(fecha);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        ////////////-------------------------------------FIN DATEPPICKER----------------------------------------------------------//



////////////-------------------------------------CONFIG BOTON DE REGISTRO USUARIO CON VALIDACIONES----------------------------------------------------------//
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombreUsuario = tilUsuario2.getEditText().getText().toString();
                nombre = tilNombre.getEditText().getText().toString();
                apellido = tilApellido.getEditText().getText().toString();
                fechNac = tilFechNac.getEditText().getText().toString();
                estatura = tilEstatura.getEditText().getText().toString();
                contraseña = tilContraseña2.getEditText().getText().toString();
                repContraseña = tilRepCon.getEditText().getText().toString();

                if (nombreUsuario.length()>0 && nombre.length()>0 && apellido.length()>0 && fechNac.length()>0  && estatura.length()>0
                        && contraseña.length()>0 && repContraseña.length()>0 && (contraseña.equals(repContraseña) == true)){
                    insertarUsuario(nombreUsuario,nombre,apellido,fechNac,estatura,contraseña);
                    Intent intent = new Intent(v.getContext(), Login.class);
                    startActivity(intent);
                }
                if (nombreUsuario.length()==0){
                    tilUsuario2.setError("Campo obligatorio");
                }
                if (nombre.length()==0){
                    tilNombre.setError ("Campo obligatorio");
                }
                if (apellido.length()==0){
                    tilApellido.setError("Campo obligatorio");
                }
                if (fechNac.length()==0){
                    tilFechNac.setError("Campo obligatorio");
                }
                if (estatura.length()==0){
                    tilEstatura.setError("Campo obligatorio");
                }
                if (contraseña.length()==0){
                    tilContraseña2.setError("Campo obligatorio");
                }
                if (repContraseña.length()==0){
                    tilRepCon.setError("Campo obligatorio");
                }
                if ((repContraseña.equals(contraseña) == false)){
                    tilRepCon.setError("Contraseña no coincide");

                }


            }
        });

////////////-------------------------------------CONFIG BOTON DE REGISTRO USUARIO CON VALIDACIONES----------------------------------------------------------//


    }  //FIN ONCREATE

    ////////////-------------------------------------INSERTA USUARIO EN BASEDATOS----------------------------------------------------------//
   public void insertarUsuario(String nombreUsuario, String nombre, String apellido, String fechNac, String estatura, String contraseña ) {
        DBhelper dBhelper = new DBhelper(this, "GYMFITNESS_PRODUCTIVO", null,1);
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        if(db != null){
            ContentValues contentValues =new ContentValues();
            contentValues.put("nombreUsuario",nombreUsuario);
            contentValues.put("nombre",nombre);
            contentValues.put("apellido",apellido);
            contentValues.put("fechNac",fechNac);
            contentValues.put("estatura",estatura);
            contentValues.put("clave",contraseña);
            long nFila = db.insert("tbl_usuarios",null,contentValues);
            if(nFila>0){
                Toast.makeText(this, "Usuario Registrado!!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Problema al registrar :c ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////-------------------------------------FIN INSERTA USUARIO EN BASEDATOS----------------------------------------------------------//



}
