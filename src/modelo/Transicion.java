/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Juan David
 */
public class Transicion {
    private Estado estadoOrigen;
    private String letra;

    public Transicion() {
    }

    public Transicion(Estado estadoOrigen, String letra) {
        this.estadoOrigen = estadoOrigen;
        this.letra = letra;
    }
    
    
    
}
