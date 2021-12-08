/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juan David Osorio y Jose Luis Beltrán
 */
public class AnalizadorLR1 {

    private Gramatica gramaticaExtendida;
    private ArrayList<Estado> estados;

    public AnalizadorLR1(Gramatica gramaticaExtendida, ArrayList<Estado> estados) {
        this.gramaticaExtendida = gramaticaExtendida;
        this.estados = estados;
    }

    public void extenderGramatica(String rutaArchivo) {
        gramaticaExtendida.extenderGramatica(rutaArchivo);
    }

    public void analizarLR1() {

    }

    public void crearEstadoInicial(char identificadorEstado) {

        int i = 0;

        Estado estado = new Estado(i, new ArrayList<Produccion>());

        if (i == 0) {
            String inicialIzquierda = gramaticaExtendida.getConjuntoProduccion().get(0).getIzquierda();
            String inicialDerecha = "." + gramaticaExtendida.getConjuntoProduccion().get(0).getDerecha() + ",$";
            Produccion produccionInicial = new Produccion(inicialIzquierda, inicialDerecha);
            guardarProduccionEstado(estado, produccionInicial);
            String[] inicialDividida = inicialDerecha.split("");

            char caracterPosteriorPunto = ' ';

            for (int j = 0; j < inicialDividida.length; j++) {
                if (inicialDividida[j].equals(".")) {

                    String caracterEncontrado = inicialDividida[j + 1];
                    char[] caracterChar = caracterEncontrado.toCharArray();
                    caracterPosteriorPunto = caracterChar[0];
                    break;
                }
            }

            if (caracterPosteriorPunto == ',') {

            }

            if (Character.isUpperCase(caracterPosteriorPunto)) {

                // int vecesEnConjProduccion = buscarCaracterEnConjuntoProduccionNumVeces(caracterPosteriorPunto);
                buscarCaracterEnConjuntoProduccion(caracterPosteriorPunto, estado);

                //Produccion nuevaProduccion = buscarCaracterEnConjuntoProduccion(caracterPosteriorPunto);
                // String izquierda = nuevaProduccion.getIzquierda();
                //String derecha = "." + nuevaProduccion.getDerecha() + ",$";
            } else {

            }

        }

        i++;
    }

    public void buscarCaracterEnConjuntoProduccion(Character simbolo, Estado estado) {

        String simboloStr = simbolo.toString();
        for (Produccion produccion : gramaticaExtendida.getConjuntoProduccion()) {

            if (produccion.getIzquierda().equals(simboloStr)) {

                String izquierda = produccion.getIzquierda();
                String derecha = "." + produccion.getDerecha() + ",$";
                Produccion nuevaProduccion = new Produccion(izquierda, derecha);

                guardarProduccionEstado(estado, nuevaProduccion);
            }
        }

    }
    
    public ArrayList<Produccion> buscarCaracterEnConjuntoProduccion(Character simbolo) {
        ArrayList<Produccion> producciones=new ArrayList<>();
        String simboloStr = simbolo.toString();
        for (Produccion produccion : gramaticaExtendida.getConjuntoProduccion()) {

            if (produccion.getIzquierda().equals(simboloStr)) {
                producciones.add(produccion);
                
            }
        }
        return producciones;
    }

    public int buscarCaracterEnConjuntoProduccionNumVeces(Character simbolo) {
        String simboloStr = simbolo.toString();
        int cont = 0;
        for (Produccion produccion : gramaticaExtendida.getConjuntoProduccion()) {

            if (produccion.getIzquierda().equals(simboloStr)) {

                cont++;
            }
        }
        return cont;
    }

    private void guardarProduccionEstado(Estado estado, Produccion produccion) {
        estado.getConjuntoProduccion().add(produccion);
    }

    /**
     * Método que recorre una producción, verifica todos sus primeros y
     * finalmente los retorna
     *
     * @param produccion la producción de la cual se obtendrán sus primeros.
     * @return arreglo de String con los primeros de la producción.
     */
    public ArrayList<String> primerosProduccion(String caracterABuscar, ArrayList<String> primeros) {
        char[] caracterProduccion=caracterABuscar.toCharArray();
        ArrayList<Produccion> produccionesABuscar=buscarCaracterEnConjuntoProduccion(caracterProduccion[0]);

        for (Produccion produccion : produccionesABuscar) {
            
        
            String[] derecha = produccion.getDerecha().split("\\|");

            for (int i = 0; i < derecha.length; i++) {

                char[] letrasPalabra = derecha[i].toCharArray();

                String unirPalabra = "";

                unirPalabra = verificarPalabraPrimeros(letrasPalabra, produccion, unirPalabra, primeros);

                if (!unirPalabra.equals("") && !primeros.contains(unirPalabra)) {
                    primeros.add(unirPalabra);
                }

            }
        }

        return primeros;
    }

    /**
     * Método que recorre los símbolos de una palabra que pertenece a una
     * producción Usa la fórmula general para sacar los primeros de una
     * producción específica.
     *
     * @param letrasPalabra split de una palabra.
     * @param produccion la instancia de la producción que se está verificando
     * @param unirPalabra son los primeros de cada palabra de la producción.
     * @return después de terminar la recursión, retorna los primeros de la cada
     * palabra de la producción.
     */
    private String verificarPalabraPrimeros(char[] letrasPalabra, Produccion produccion, String unirPalabra, ArrayList<String> primeros) {
        for (int j = 0; j < letrasPalabra.length; j++) {

            if (Character.isUpperCase(letrasPalabra[j]) && j == 0) {

                Produccion nuevaProduccion = buscarCaracterEnConjuntoProduccion(letrasPalabra[j], produccion);

                if (nuevaProduccion != null) {
                    
                    primerosProduccion(nuevaProduccion.getIzquierda(), primeros);
                    break;

                } else {

                    break;
                }

            } else if (Character.isUpperCase(letrasPalabra[j])) {

                break;

            } else {

                unirPalabra += letrasPalabra[j];
                continue;
            }

        }
        return unirPalabra;
    }

    /**
     * Método que busca una producción por su símbolo a la izquierda (antes de
     * la flecha) para saber si existe o no.
     *
     * @param simbolo el símbolo a buscar en las izquierdas del conjunto
     * producción
     * @param produccionOrigen la instacia de la producción que contiene el
     * símbolo a buscar
     * @return la producción (si la encuentra) o null si no existe dentro del
     * conjunto producción.
     */
    public Produccion buscarCaracterEnConjuntoProduccion(Character simbolo, Produccion produccionOrigen) {
        String simboloStr = simbolo.toString();
        for (Produccion produccion : gramaticaExtendida.getConjuntoProduccion()) {

            if (produccion.getIzquierda().equals(simboloStr)) {

                return produccion;
            }
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AnalizadorLR1 ana = new AnalizadorLR1(new Gramatica(new ArrayList<>()), new ArrayList<Estado>());
        String rutaArchivo = "C:\\Users\\jdavi\\Desktop\\Proyecto Lenguajes\\Parcial2.json";
        ana.extenderGramatica(rutaArchivo);
        System.out.println(ana.gramaticaExtendida);

        ArrayList<String> primeros = new ArrayList<>();
        Produccion produccion = ana.gramaticaExtendida.getConjuntoProduccion().get(4);
        ana.primerosProduccion(produccion.getIzquierda(), primeros);
        System.out.println(primeros);

    }

}
