/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 *@author Juan David Osorio y Jose Luis Beltrán
 */
public class Produccion {

    private String izquierda;
    private String derecha;
    

    public Produccion(String izquierda, String derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
        
    }

    public Produccion() {

    }
    
     
    public String getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(String izquierda) {
        this.izquierda = izquierda;
    }

    public String getDerecha() {
        return derecha;
    }

    public void setDerecha(String derecha) {
        this.derecha = derecha;
    }

    @Override
    public String toString() {
        return izquierda +" "+ "-->" +" "+ derecha ;
    }
    
    
    public void analizarLR1(){
        
    }

}
