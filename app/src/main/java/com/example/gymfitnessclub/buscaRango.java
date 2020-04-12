package com.example.gymfitnessclub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class buscaRango extends AppCompatActivity {

////////////-------------------------------------VARIABLES----------------------------------------------------------//
    TextInputLayout tilFechaIni, tilFechaFin;
    Button btnBuscar,btnVolver2;
    ListView lvBuscar;
    String idUser, estatura;
    String[] datos;
////////////-------------------------------------VARIABLES----------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busca_rango);

        tilFechaIni = findViewById(R.id.tilFechaInicio);
        tilFechaFin = findViewById(R.id.tilFechaFin);
        btnBuscar = findViewById(R.id.btnBuscar);
        lvBuscar = findViewById(R.id.lvBuscar);
        btnVolver2 = findViewById(R.id.btnVolver2);

        idUser = getIntent().getStringExtra("idUser");
        estatura = getIntent().getStringExtra("estatura");


////////////-------------------------------------DATEPICKER----------------------------------------------------------//
        tilFechaIni.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(buscaRango.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                                @SuppressLint("DefaultLocale") String fechaIni = String.format("%02d/%02d/%d", mday,(mmonth +1), myear);
                                tilFechaIni.getEditText().setText(fechaIni);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        //SEGUNDO DATEPICKER PARA FECHAFIN
        tilFechaFin.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(buscaRango.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                                @SuppressLint("DefaultLocale") String fechaFin = String.format("%02d/%02d/%d", mday,(mmonth +1), myear);
                                tilFechaFin.getEditText().setText(fechaFin);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

////////////-------------------------------------FIN DATEPICKER----------------------------------------------------------//


////////////-------------------------------------CONFIGURACION BOTON BUSQUEDA----------------------------------------------------------//
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechaIni =tilFechaIni.getEditText().getText().toString();
                String fechaFin =tilFechaFin.getEditText().getText().toString();

                if (fechaIni.length()>0 && fechaFin.length()>0) {
                    datos = buscarActividad(fechaIni, fechaFin, idUser);
                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, datos);
                    lvBuscar.setAdapter(adapter);
                }
                if (fechaIni.length()==0){
                    tilFechaIni.setError("Campo obligatorio");
                }
                if (fechaFin.length()==0){
                    tilFechaFin.setError ("Campo obligatorio");
                }


            }
        });


////////////-------------------------------------LISTVIEW----------------------------------------------------------//
        lvBuscar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
////////////-------------------------------------FIN DATEPICKER----------------------------------------------------------//


////////////-------------------------------------CONFIGURACION BOTON VOLVER----------------------------------------------------------//
        btnVolver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), registraActividad.class);
                intent.putExtra("idUser",idUser);
                intent.putExtra("estatura",estatura);
                startActivity(intent);
            }
        });
////////////-------------------------------------FIN CONFIGURACION BOTON VOLVER----------------------------------------------------------//


    }  //FIN ONCREATE

    ////////////-------------------------------------BUSCA EVALUACIONES POR EL ID USUARIO Y FECHAS----------------------------------------------------------//
    public String[] buscarActividad (String fechaIni,String fechaFin, String idUser) {

        DBhelper dBhelper = new DBhelper(this,"GYMFITNESS_PRODUCTIVO",null,1);
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        String[] datos = new String[0];
        if(db != null) {
            Cursor cur = db.rawQuery("SELECT * FROM  tbl_evaluaciones where id_usuario = '" + idUser + "' AND fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'",null);
            int cantidad = cur.getCount();
            datos = new String[cantidad];
            int aux=0;
            Toast.makeText(this, "Actividades encontradas: "+cantidad, Toast.LENGTH_SHORT).show();
            if(cur.moveToFirst()){
                do {
                    datos[aux] = cur.getString(0)+"- FECHA: "+cur.getString(1)+" PESO: "+cur.getString(2)+" IMC: "+cur.getString(3);
                    Log.i("DATOS",datos[aux]);
                    aux = aux + 1;
                }while (cur.moveToNext());
            }

        }
        return datos;
    }
    ////////////-------------------------------------BUSCA EVALUACIONES POR EL ID USUARIO Y FECHAS----------------------------------------------------------//


}
