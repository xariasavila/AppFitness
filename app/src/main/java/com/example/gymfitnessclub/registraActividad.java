package com.example.gymfitnessclub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;


import java.util.Calendar;


public class registraActividad extends AppCompatActivity {

    ////////////-------------------------------------VARIABLES----------------------------------------------------------//

    TextInputLayout tilFecha, tilPeso;
    TextView tvImc, tvCerrarSesion;
    Button btnRegistroImc,btnBuscarFecha;
    ListView lvRegistros;
    String fecha,peso,imc,idUser,estatura;
    String [] datos;
    ////////////-------------------------------------FIN VARIABLES----------------------------------------------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registra_actividad);

////////////-------------------------------------CAPTURA DE VALORES----------------------------------------------------------//
        tilFecha = findViewById(R.id.tilFechaActividad);
        tilPeso = findViewById(R.id.tilPesoActividad);
        tvImc = findViewById(R.id.txtImc);
        btnRegistroImc = findViewById(R.id.btnRegistroImc);
        btnBuscarFecha = findViewById(R.id.btnBuscarFecha);
        lvRegistros = findViewById(R.id.lvRegistros);
        ////////////-------------------------------------FIN CAPTURA DE VALORES----------------------------------------------------------//


        idUser = getIntent().getStringExtra("idUser");
        estatura = getIntent().getStringExtra("estatura");

        datos =  listar();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datos);
        lvRegistros.setAdapter(adapter);

////////////-------------------------------------DATEPICKER----------------------------------------------------------//

        tilFecha.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(registraActividad.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                                @SuppressLint("DefaultLocale") String fecha = String.format("%02d/%02d/%d", mday,(mmonth +1), myear);
                                tilFecha.getEditText().setText(fecha);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        ////////////------------------------------------FIN-DATEPICKER----------------------------------------------------------//

////////////-------------------------------------CALCULA IMC AUTOMATICO ---------------------------------------------------------//

        tilPeso.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                peso = tilPeso.getEditText().getText().toString();
                if (peso.length()>0){
                    CalculoIMC calculo = new CalculoIMC();
                    Double Imc = calculo.calcularIMC(estatura ,peso);
                    tvImc.setText(String.format("%.2f", Imc));
                }
            }
        });

////////////-------------------------------------FIN CALCULA IMC Y MOSTRAR ----------------------------------------------------------//

////////////-------------------------------------CONFIGURACION BOTON REGISTRA ACTIVIDAD CON VALIDACION DE CAMPOS---------------------------------------------------------//

        btnRegistroImc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fecha = tilFecha.getEditText().getText().toString();
                peso = tilPeso.getEditText().getText().toString();
                imc = tvImc.getText().toString();

                if (fecha.length()>0 && peso.length()>0 && (imc.equals("Calculando...") == false)){
                    insertarRegistro(fecha,peso,imc,idUser);
                    datos =  listar();
                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,datos);
                    lvRegistros.setAdapter(adapter);
                    tilFecha.getEditText().setText("");
                    tilPeso.getEditText().setText("");
                    tvImc.setText("Calculando...");
                }

                if (fecha.length()==0){
                    tilFecha.setError("Ingrese fecha");
                }
                if (peso.length()==0){
                    tilPeso.setError("Ingrese peso");
                }


            }
        });
////////////-----------------------------------FIN--CONFIGURACION BOTON REGISTRA ACTIVIDAD CON VALIDACION DE CAMPOS---------------------------------------------------------//

////////////-------------------------------------CONFIGURACION BOTON BUSCAR POR USUARIO Y ESTATURA Y REDIRECCIONA A BUSCAR POR FECHA---------------------------------------------------------//

        btnBuscarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), buscaRango.class);
                intent.putExtra("idUser",idUser);
                intent.putExtra("estatura",estatura);
                startActivity(intent);
            }
        });
        ////////////--------------------------------FIN-----CONFIGURACION BOTON BUSCAR POR USUARIO Y ESTATURA Y REDIRECCIONA A BUSCAR POR FECHA---------------------------------------------------------//



        lvRegistros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), deleteActividad.class);
                intent.putExtra("idUser",idUser);
                intent.putExtra("estatura",estatura);
                intent.putExtra("id",datos[position].split("-")[0]);
                intent.putExtra("texto",datos[position].split("-")[1]);
                startActivity(intent);

            }
        });


    }  //FIN ONCREATE

////////////-------------------------------------INSERTA REGISTROS EN BASE DE DATOS---------------------------------------------------------//

    public void insertarRegistro(String fecha, String peso, String imc, String id_usuario){
        DBhelper dBhelper = new DBhelper(this,"GYMFITNESS_PRODUCTIVO",null,1);
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        if(db != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("fecha",fecha);
            contentValues.put("peso",peso);
            contentValues.put("imc",imc);
            contentValues.put("id_usuario",id_usuario);
            long nFila = db.insert("tbl_evaluaciones",null,contentValues);
            if(nFila>0){
                Toast.makeText(this, "Actividad registrada", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Problema al guardar", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////--------------------------------FIN-----INSERTA REGISTROS EN BASE DE DATOS---------------------------------------------------------//


////////////-------------------------------------MOSTRAR DATOS EN LISTVIEW--------------------------------------------------------//

    public String[]  listar(){
        String[] datos = new String[0];
        DBhelper dBhelper = new DBhelper(this,"GYMFITNESS_PRODUCTIVO",null,1);
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        if(db != null) {
            Cursor cur = db.rawQuery("SELECT * FROM  tbl_evaluaciones where id_usuario = '"+idUser+"'",null);
            int cantidad = cur.getCount();
            int i = 0;
            datos = new String[cantidad];
            if(cur.moveToFirst()){
                do {
                    String fila = cur.getString(0)+"- Fecha: "+cur.getString(1)+" Peso: "+cur.getString(2)+" IMC: "+cur.getString(3);
                    datos[i] = fila;
                    i++;
                }while (cur.moveToNext());
            }
        }
        return datos;
    }
    ////////////-------------------------------------MOSTRAR DATOS EN LISTVIEW--------------------------------------------------------//


}
