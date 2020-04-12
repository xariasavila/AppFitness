package com.example.gymfitnessclub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DBhelper extends SQLiteOpenHelper {

    String tbl_usuarios = "CREATE TABLE tbl_usuarios (id integer PRIMARY KEY AUTOINCREMENT,nombreUsuario string, nombre string,apellido string,fechNac date,estatura decimal,clave string);";
    String tbl_evaluaciones = "CREATE TABLE tbl_evaluaciones (id integer PRIMARY KEY AUTOINCREMENT,fecha string,peso decimal,imc decimal,id_usuario integer);";

    public DBhelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tbl_evaluaciones);
        db.execSQL(tbl_usuarios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
