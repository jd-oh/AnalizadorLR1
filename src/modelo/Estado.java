/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Juan David Osorio y Jose Luis Beltrán
 */
public class Estado {
    private char identificador;
    private ArrayList<Produccion> conjuntoProduccion;

    public Estado(char identificador, ArrayList<Produccion> conjuntoProduccion) {
        this.identificador = identificador;
        this.conjuntoProduccion = conjuntoProduccion;
    }

    public Estado() {
    }
    
    public ArrayList<Produccion> getConjuntoProduccion() {
        return conjuntoProduccion;
    }

    public void setConjuntoProduccion(ArrayList<Produccion> conjuntoProduccion) {
        this.conjuntoProduccion = conjuntoProduccion;
    }
    
    @Override
    public String toString() {
        return "Estado "+identificador+": "+conjuntoProduccion;
    }
    
}
