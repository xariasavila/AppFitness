package com.example.gymfitnessclub;



public class CalculoIMC {


    public Double calcularIMC(String estatura, String peso){

        Double estatura_cm = Double.valueOf(estatura);
        Double peso_kg = Double.valueOf(peso);
        Double imc = null;
        imc = peso_kg/(estatura_cm*estatura_cm);
        return imc;

    }

}
