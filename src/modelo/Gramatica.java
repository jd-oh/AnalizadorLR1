/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import control.ControlJSON;
import java.util.ArrayList;

/**
 *
 *@author Juan David Osorio y Jose Luis Beltrán
 */
public class Gramatica {

    private ArrayList<Produccion> conjuntoProduccion;

    public Gramatica(ArrayList<Produccion> conjuntoProduccion) {
        this.conjuntoProduccion = conjuntoProduccion;
    }

    public ArrayList<Produccion> getConjuntoProduccion() {
        return conjuntoProduccion;
    }

    public void setConjuntoProduccion(ArrayList<Produccion> conjuntoProduccion) {
        this.conjuntoProduccion = conjuntoProduccion;
    }
    
    
    
    /**
     * Método que asigna el conjunto producción del archivo leído al del objeto
     * @param rutaArchivo la ruta del archivo que se leerá
     */
    public void cargarConjuntoProduccionJSON(String rutaArchivo) {

        ControlJSON controlJSON = new ControlJSON();
        Gramatica gra = controlJSON.leerGramatica(rutaArchivo);
        this.conjuntoProduccion=gra.getConjuntoProduccion();
 
    }
    
    public void analizar(String rutaArchivo){
        cargarConjuntoProduccionJSON(rutaArchivo);
        
        
    }

    

    @Override
    public String toString() {
        return "Gramatica{" + "conjuntoProduccion=" + conjuntoProduccion + '}';
    }
    
    

   

}
