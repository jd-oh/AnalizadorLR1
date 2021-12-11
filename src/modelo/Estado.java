/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Juan David Osorio y Jose Luis Beltrán
 */
public class Estado {
    private int identificador;
    private ArrayList<Produccion> conjuntoProduccion;
    private Transicion transiciones;

    public Estado(int identificador, ArrayList<Produccion> conjuntoProduccion, Transicion transiciones) {
        this.identificador = identificador;
        this.conjuntoProduccion = conjuntoProduccion;
        this.transiciones=transiciones;
    }

    public Estado() {
    }
    
    public ArrayList<Produccion> getConjuntoProduccion() {
        return conjuntoProduccion;
    }

    public void setConjuntoProduccion(ArrayList<Produccion> conjuntoProduccion) {
        this.conjuntoProduccion = conjuntoProduccion;
    }

    public Transicion getTransicion() {
        return transiciones;
    }
    
    
    
    @Override
    public String toString() {
        return "Estado I-"+identificador+": "+conjuntoProduccion;
    }
    
}
