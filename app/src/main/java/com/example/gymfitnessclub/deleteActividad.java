package com.example.gymfitnessclub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class deleteActividad extends AppCompatActivity {

    ////////////-------------------------------------VARIABLES----------------------------------------------------------//
    TextInputLayout tilFechaCrud, tilPesoCrud;
    TextView tvDatos;
    Button btnEliminar, btnActualizar, btnVolver;

    String fecha,peso,imc,idDato;
    String idUser, estatura;
////////////------------------------------------- fin VARIABLES----------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_actividad);

////////////-------------------------------------CAPTURA DE VALORES----------------------------------------------------------//
        tilFechaCrud = findViewById(R.id.tilFechaUpdate);
        tilPesoCrud  = findViewById(R.id.tilPesoUpdate);
        tvDatos = findViewById(R.id.tvDatos);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnVolver = findViewById(R.id.btnVolver);

        // rescatamos variables enviadas por intent
        idUser = getIntent().getStringExtra("idUser");
        estatura = getIntent().getStringExtra("estatura");
        String dato = getIntent().getStringExtra("texto");
        idDato = getIntent().getStringExtra("id");
 ////////////-------------------------------------FIN CAPTURA DE VARIABLES----------------------------------------------------------//


        //realizamos tratamiento de variable para obtener datos
        fecha = dato.split(" ")[2];
        peso = dato.split(" ")[4];
        imc = dato.split(" ")[6];

        //SET DE VARIABLES
        tilFechaCrud.getEditText().setText(fecha);
        tilPesoCrud.getEditText().setText(peso);
        tvDatos.setText(imc);

////////////-------------------------------------DATEPCIKER----------------------------------------------------------//
        tilFechaCrud.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(deleteActividad.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                                @SuppressLint("DefaultLocale") String fechaElm = String.format("%02d/%02d/%d", mday,(mmonth +1), myear);
                                tilFechaCrud.getEditText().setText(fechaElm);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
////////////-------------------------------------DATEPCIKER END----------------------------------------------------------//


////////////-------------------------------------REALIZA CALCLULO AUTOMATICO DEL IMC Y LO MUESTRA EN TXTVIEW----------------------------------------------------------//

        tilPesoCrud.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                peso = tilPesoCrud.getEditText().getText().toString();
                if (peso.length()>0){
                    CalculoIMC calculo = new CalculoIMC();
                    Double Imc = calculo.calcularIMC(estatura ,peso);
                    tvDatos.setText(String.format("%.2f", Imc));
                }

            }
        });
////////////-------------------------------------FIN REALIZA CALCLULO AUTOMATICO DEL IMC Y LO MUESTRA EN TXTVIEW----------------------------------------------------------//

////////////-------------------------------------METODO DELETE----------------------------------------------------------//

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarRegistro(idDato);
            }
        });
        ////////////----------------------------FIN METODO DELETE----------------------------------------------------------//


////////////-------------------------------------METODO UPDATE----------------------------------------------------------//
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pesoAct = tilPesoCrud.getEditText().getText().toString();
                String imcAct = tvDatos.getText().toString();
                String fechaAct = tilFechaCrud.getEditText().getText().toString();
                if (pesoAct.length()>0 && imcAct.length()>0 && fechaAct.length()>0)  {
                    actualizarRegistro(idDato,pesoAct,imcAct,fechaAct);
                }
                if (pesoAct.length()==0){
                    tilPesoCrud.setError("Ingresar fecha");
                }
                if (fechaAct.length()==0){
                    tilFechaCrud.setError("ingresar peso");
                }

            }
        });
////////////------------------------------------- FIN METODO UPDATE----------------------------------------------------------//


////////////-------------------------------------CONFIG BOTON VOLVER----------------------------------------------------------//
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), registraActividad.class);
                intent.putExtra("idUser",idUser);
                intent.putExtra("estatura",estatura);
                startActivity(intent);
            }
        });

////////////-------------------------------------FIN CONFIG BOTON VOLVER----------------------------------------------------------//


    } //FIN ONCREATE

    ////////////-------------------------------------METODO BORRAR DATOS POR ID ----------------------------------------------------------//
    public void borrarRegistro(String id){
        DBhelper dBhelper = new DBhelper(this,"GYMFITNESS_PRODUCTIVO",null,1);
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        if(db != null){
            int exect = db.delete("tbl_evaluaciones","id = "+id,null);
            if (exect>0){
                Intent intent = new Intent(this, registraActividad.class);
                Toast.makeText(this, "Actividad eliminada", Toast.LENGTH_SHORT).show();
                intent.putExtra("idUser",idUser);
                intent.putExtra("estatura",estatura);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Problema al eliminar ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////-------------------------------------METODO BORRAR DATOS POR ID ----------------------------------------------------------//


    ////////////-------------------------------------METODO ACTUALIZA DATOS POR ID ----------------------------------------------------------//
    public  void actualizarRegistro(String id, String peso, String imc,String fecha){
        DBhelper dBhelper = new DBhelper(this,"GYMFITNESS_PRODUCTIVO",null,1);
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        if(db != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("fecha",fecha);
            contentValues.put("peso",peso);
            contentValues.put("imc",imc);
            int exect = db.update("tbl_evaluaciones",contentValues,"id = "+id,null);
            if (exect>0){
                Intent intent = new Intent(this, registraActividad.class);
                Toast.makeText(this, "Actividad actualizada", Toast.LENGTH_SHORT).show();
                intent.putExtra("idUser",idUser);
                intent.putExtra("estatura",estatura);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Problema al actualizar ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////-------------------------------------METODO ACTUALIZA DATOS POR ID ----------------------------------------------------------//




}
