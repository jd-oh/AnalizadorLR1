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

    public void crearEstadoInicial() {

        Estado estado = new Estado(0, new ArrayList<Produccion>(), null);

        String inicialIzquierda = gramaticaExtendida.getConjuntoProduccion().get(0).getIzquierda();
        String inicialDerecha = "." + gramaticaExtendida.getConjuntoProduccion().get(0).getDerecha() + ",$";
        Produccion produccionInicial = new Produccion(inicialIzquierda, inicialDerecha);
        guardarProduccionEstado(estado, produccionInicial);

        AnalizarCaracterPosteriorPunto(produccionInicial, estado);

        estados.add(estado);

    }

    private void AnalizarCaracterPosteriorPunto(Produccion produccionPadre, Estado estado) {

        String[] produccionPadreDividida = produccionPadre.getDerecha().split("");

        char caracterPosteriorPunto = ' ';

        for (int j = 0; j < produccionPadreDividida.length; j++) {
            if (produccionPadreDividida[j].equals(".")) {

                String caracterEncontrado = produccionPadreDividida[j + 1];
                char[] caracterChar = caracterEncontrado.toCharArray();
                caracterPosteriorPunto = caracterChar[0];
                break;
            }
        }

//        if (caracterPosteriorPunto == ',') {
//
//        }
        if (Character.isUpperCase(caracterPosteriorPunto)) {

            String ultimos = ultimosProduccion(produccionPadre);
            buscarCaracterEnConjuntoProduccion(caracterPosteriorPunto, estado, ultimos);

        }
    }

    public void analizarLR1() {
        crearEstadoInicial();
        //crearNuevoEstado();
    }

    public void crearNuevoEstado(char letra, Estado estadoOrigen, int identificador) {

        ArrayList<Produccion> conjProduccion = estadoOrigen.getConjuntoProduccion();
        ArrayList<Produccion> tempQueCumplen = new ArrayList<>();
        Estado nuevoEstado = new Estado(identificador, new ArrayList<Produccion>(), new Transicion(estadoOrigen, Character.toString(letra)));

        for (Produccion produccion : conjProduccion) {

            String izquierda = produccion.getIzquierda();
            String derecha = produccion.getDerecha();
            char[] derechaDividido = derecha.toCharArray();

            for (int i = 0; i < derechaDividido.length; i++) {

                int posicionPunto = 0;
                char derechaAlPunto = ' ';
                int posicionComa = 0;
                String caracter = Character.toString(derechaDividido[i]);

                if (caracter.equals(".") && derechaDividido[i + 1] != ',') {

                    if (derechaDividido[i + 1] == letra) {

                        derechaAlPunto = letra;
                        derechaDividido[i] = derechaAlPunto;
                        derechaDividido[i + 1] = '.';
                        String derechaUnida = unirArregloDeArray(derechaDividido);

                        Produccion produccionResultante = new Produccion(izquierda, derechaUnida);
                        tempQueCumplen.add(produccionResultante);
                        break;

                    } else {
                        continue;
                    }

                } else //if(caracter.equals(".") && derechaDividido[i+1]==',')
                {

                }
            }

        }

        for (Produccion produccion : tempQueCumplen) {
            System.out.println("A");
            guardarProduccionEstado(nuevoEstado, produccion);

        }
        estados.add(nuevoEstado);

    }

    private String unirArregloDeArray(char[] arregloChar) {
        String union = "";
        for (char c : arregloChar) {
            union += c;
        }
        return union;
    }

    /**
     * Se utiliza si después del caracter a buscar no hay nada (No hay primeros
     * para sacar). Se pondrá los mismos ultimos del padre.
     *
     * @param produccion produccion padre.
     * @return
     */
    public String ultimosProduccion(Produccion produccion) {
        String[] derechaDividida = produccion.getDerecha().split(",");
        String ultimos = derechaDividida[1];

        return ultimos;
    }

    /**
     * Busca dentro de la grámatica el caracter, que debe
     *
     * @param simbolo
     * @param estado
     * @param ultimos
     */
    public void buscarCaracterEnConjuntoProduccion(Character simbolo, Estado estado, String ultimos) {

        String simboloStr = simbolo.toString();
        for (Produccion produccion : gramaticaExtendida.getConjuntoProduccion()) {

            if (produccion.getIzquierda().equals(simboloStr)) {

                String izquierda = produccion.getIzquierda();
                String derecha = "." + produccion.getDerecha() + "," + ultimos;
                Produccion nuevaProduccion = new Produccion(izquierda, derecha);
                analizarEstado(nuevaProduccion, estado);
                guardarProduccionEstado(estado, nuevaProduccion);
            }
        }

    }

    /**
     * Analiza la produccion para saber que le sigue al punto y así traer más
     * producciones (si es mayuscula)
     *
     * @param produccion
     * @param estado
     */
    public void analizarEstado(Produccion produccion, Estado estado) {
        String derecha = produccion.getDerecha();
        String[] produccionDividida = derecha.split("");
        int posicionCaracterPosteriorPunto = 0;
        char caracterPosteriorPunto = ' ';
        String caracterEncontrado = "";
        String ultimos = "";

        for (int j = 0; j < produccionDividida.length; j++) {
            if (produccionDividida[j].equals(".")) {

                caracterEncontrado = produccionDividida[j + 1];
                posicionCaracterPosteriorPunto = j + 1;
                char[] caracterChar = caracterEncontrado.toCharArray();
                caracterPosteriorPunto = caracterChar[0];
                break;
            }
        }

        if (Character.isUpperCase(caracterPosteriorPunto)) {

            char[] caracterPosteriorCaracterChar = produccionDividida[posicionCaracterPosteriorPunto + 1].toCharArray();
            char caracterPosteriorCaracter = caracterPosteriorCaracterChar[0];

            ultimos = analizarCaracteresPosterioresProduccionPadre(caracterPosteriorCaracter, ultimos, produccion, produccionDividida, posicionCaracterPosteriorPunto, derecha, caracterEncontrado);

            buscarCaracterEnConjuntoProduccion(caracterPosteriorPunto, estado, ultimos);

        }
    }

    private String analizarCaracteresPosterioresProduccionPadre(char caracterPosteriorCaracter, String ultimos, Produccion produccion, String[] produccionDividida, int posicionCaracterPosteriorPunto, String derecha, String caracterEncontrado) {
        if (caracterPosteriorCaracter == ',') {

            ultimos = ultimosProduccion(produccion);

        } else if (Character.isUpperCase(caracterPosteriorCaracter)) {

            ultimos = primerosProduccionStr(produccionDividida[posicionCaracterPosteriorPunto + 1], new ArrayList<>());

        } else {
            String[] produccionDivididaPorComa = derecha.split(",");
            String izquierdaDeComa = produccionDivididaPorComa[0];

            String[] izquierdaDeComaDivididaPorCaracter = izquierdaDeComa.split(caracterEncontrado);

            String derechaDeCaracter = izquierdaDeComaDivididaPorCaracter[1];
            char[] caracteresDerecha = derechaDeCaracter.toCharArray();

            for (int i = 0; i < caracteresDerecha.length; i++) {
                if (!Character.isUpperCase(caracteresDerecha[i])) {

                    ultimos += Character.toString(caracteresDerecha[i]);

                }
            }
        }
        return ultimos;
    }

    private String primerosProduccionStr(String caracterABuscar, ArrayList<String> primeros) {
        ArrayList<String> primerosStr = primerosProduccion(caracterABuscar, primeros);
        String unir = "";

        for (int i = 0; i < primerosStr.size(); i++) {
            if (i == 0) {
                unir = primerosStr.get(i);
            } else {
                unir += "|" + primerosStr.get(i);
            }

        }

        return unir;
    }

    /**
     * Método que recorre una producción, verifica todos sus primeros y
     * finalmente los retorna
     *
     * @param caracterABuscar a
     * @param primeros
     * @return arreglo de String con los primeros de la producción.
     */
    public ArrayList<String> primerosProduccion(String caracterABuscar, ArrayList<String> primeros) {
        char[] caracterProduccion = caracterABuscar.toCharArray();
        ArrayList<Produccion> produccionesABuscar = buscarCaracterEnConjuntoProduccion(caracterProduccion[0]);

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

    public ArrayList<Produccion> buscarCaracterEnConjuntoProduccion(Character simbolo) {
        ArrayList<Produccion> producciones = new ArrayList<>();
        String simboloStr = simbolo.toString();
        for (Produccion produccion : gramaticaExtendida.getConjuntoProduccion()) {

            if (produccion.getIzquierda().equals(simboloStr)) {
                producciones.add(produccion);

            }
        }
        return producciones;
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

//    public int buscarCaracterEnConjuntoProduccionNumVeces(Character simbolo) {
//        String simboloStr = simbolo.toString();
//        int cont = 0;
//        for (Produccion produccion : gramaticaExtendida.getConjuntoProduccion()) {
//
//            if (produccion.getIzquierda().equals(simboloStr)) {
//
//                cont++;
//            }
//        }
//        return cont;
//    }
    private void guardarProduccionEstado(Estado estado, Produccion produccion) {
        estado.getConjuntoProduccion().add(produccion);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AnalizadorLR1 ana = new AnalizadorLR1(new Gramatica(new ArrayList<>()), new ArrayList<Estado>());
        String rutaArchivo = "C:\\Users\\jdavi\\Desktop\\Proyecto Lenguajes\\Parcial2.json";
        ana.extenderGramatica(rutaArchivo);
        System.out.println(ana.gramaticaExtendida);

        //ArrayList<String> primeros = new ArrayList<>();
//        Produccion produccion = ana.gramaticaExtendida.getConjuntoProduccion().get(4);
        // ana.primerosProduccion(produccion.getIzquierda(), primeros);
        //System.out.println(primeros);
        // System.out.println(ana.primerosProduccionStr(produccion.getIzquierda(), primeros));
        ana.crearEstadoInicial();
        ArrayList<Estado> estad = ana.estados;

        //ana.crearNuevoEstado('(', estad.get(0), 1);
        System.out.println(ana.estados);

    }

}
