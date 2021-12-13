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

    /**
     * Usado por la produccion inicial. Analiza qué hay después del caracter que
     * está después del punto, para agregarlos como ultimos a las producciones
     * hijas Si es mayuscula, traerá los primeros de esa produccion y esos serán
     * los últimos Si es minuscula, esos serán los ultimos.
     *
     * @param produccionPadre
     * @param estado
     */
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

        if (Character.isUpperCase(caracterPosteriorPunto)) {

            String ultimos = ultimosProduccion(produccionPadre);
            buscarCaracterEnConjuntoProduccion(caracterPosteriorPunto, estado, ultimos);

        }
    }

    public void analizarLR1() {
        crearEstadoInicial();
        //crearNuevoEstado();

    }

    /**
     * Crea un nuevo estado junto con cada una de sus producciones y
     * transiciones.
     *
     * @param letra
     * @param estadoOrigen
     * @param identificador
     */
    public void crearNuevoEstado(char letra, Estado estadoOrigen, int identificador) {

        ArrayList<Produccion> conjProduccion = estadoOrigen.getConjuntoProduccion();
        ArrayList<Produccion> tempQueCumplen = new ArrayList<>();
        Estado nuevoEstado = new Estado(identificador,
                new ArrayList<Produccion>(), new Transicion(estadoOrigen, Character.toString(letra)));

        for (Produccion produccion : conjProduccion) {

            String izquierda = produccion.getIzquierda();
            String derecha = produccion.getDerecha();
            char[] derechaDividido = derecha.toCharArray();

            for (int i = 0; i < derechaDividido.length; i++) {

                char derechaAlPunto = ' ';

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
                        break;
                    }

                } else {

                }
            }

        }

        AnalizarYGuardarProduccionesPorLetraTransicion(tempQueCumplen, nuevoEstado);

        if(!estados.contains(nuevoEstado)){
            estados.add(nuevoEstado);
        }

        

    }

    /**
     *
     * @param tempQueCumplen las producciones que después del punto tienen la
     * transicion buscada para el nuevo estado.
     * @param nuevoEstado el estando que utilizará esa transición.
     */
    private void AnalizarYGuardarProduccionesPorLetraTransicion(ArrayList<Produccion> tempQueCumplen, Estado nuevoEstado) {
        for (Produccion produccion : tempQueCumplen) {
            String derecha = produccion.getDerecha();
            String[] produccionDividida = derecha.split("");

            String caracterEncontrado = "";

            for (int j = 0; j < produccionDividida.length; j++) {
                if (produccionDividida[j].equals(".")) {

                    caracterEncontrado = produccionDividida[j + 1];
                    break;
                }
            }

            analizarProduccionDespuesDelPunto(produccion, nuevoEstado);
            guardarProduccionEstado(nuevoEstado, produccion);

        }
    }

    /**
     * Une en un solo string los arreglos chars
     *
     * @param arregloChar
     * @return
     */
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
     * Busca dentro de la grámatica las producciones que existen con el simbolo
     * dado para analizarlas y finalmente guardarlas en el nuevo estado.
     *
     * @param simbolo la izquierda de la producción a buscar
     * @param estado el nuevo estado del que hará parte
     * @param ultimos los ultimos de su producción padre
     */
    public void buscarCaracterEnConjuntoProduccion(Character simbolo, Estado estado, String ultimos) {

        String simboloStr = simbolo.toString();
        for (Produccion produccion : gramaticaExtendida.getConjuntoProduccion()) {
            if (produccion.getIzquierda().equals(simboloStr)) {

                String izquierda = produccion.getIzquierda();
                String derecha = "." + produccion.getDerecha() + "," + ultimos;
                Produccion nuevaProduccion = new Produccion(izquierda, derecha);
                analizarProduccionDespuesDelPunto(nuevaProduccion, estado);
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
    public void analizarProduccionDespuesDelPunto(Produccion produccion, Estado estado) {
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

            ultimos = analizarCaracteresPosterioresProduccionPadre(produccion, posicionCaracterPosteriorPunto, caracterEncontrado);

            buscarCaracterEnConjuntoProduccion(caracterPosteriorPunto, estado, ultimos);

        }
    }

    /**
     * Analiza qué hay después del caracter que está después del punto, para
     * agregarlos como ultimos a las producciones hijas Si es mayuscula, traerá
     * los primeros de esa produccion y esos serán los últimos Si es minuscula,
     * esos serán los ultimos.
     *
     * @param produccion
     * @param posicionCaracterPosteriorPunto
     * @param caracterEncontrado
     * @return
     */
    private String analizarCaracteresPosterioresProduccionPadre(Produccion produccion, int posicionCaracterPosteriorPunto, String caracterEncontrado) {
        String ultimos = " ";
        String derecha = produccion.getDerecha();
        String[] produccionDividida = derecha.split("");
        char[] caracterPosteriorCaracterChar = produccionDividida[posicionCaracterPosteriorPunto + 1].toCharArray();
        char caracterPosteriorCaracter = caracterPosteriorCaracterChar[0];

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
                if (Character.isLowerCase(caracteresDerecha[i])) {

                    ultimos += Character.toString(caracteresDerecha[i]);

                }
            }
        }
        return ultimos;
    }

    /**
     * Une en un string, la lista de primeros de una produccion.
     *
     * @param caracterABuscar al que se le buscarán los primeros
     * @param primeros los primeros de la produccion que su izquierda era igual
     * a el caracterABuscar
     * @return string con los primeros de la produccion.
     */
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

    /**
     * Toma la produccion y la guarda en el estado dado.
     *
     * @param estado el estado en el que se almacenará la producción.
     * @param produccion la producción que se guardará.
     */
    private void guardarProduccionEstado(Estado estado, Produccion produccion) {

        if (estado.getIdentificador() != 0) {

            estado.getConjuntoProduccion().add(0, produccion);

        } else {
            estado.getConjuntoProduccion().add(produccion);
        }
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
        ana.crearNuevoEstado('S', estad.get(0), 1);
        ana.crearNuevoEstado('(', estad.get(0), 2);
        ana.crearNuevoEstado('L', estad.get(2), 3);
        ana.crearNuevoEstado(')', estad.get(3), 4);
        ana.crearNuevoEstado('S', estad.get(2), 5);
        ana.crearNuevoEstado('X', estad.get(5), 6);
        ana.crearNuevoEstado(';', estad.get(5), 7);
        ana.crearNuevoEstado('S', estad.get(7), 8);
        ana.crearNuevoEstado('X', estad.get(8), 9);
        ana.crearNuevoEstado('ø', estad.get(8), 10);
        ana.crearNuevoEstado('(', estad.get(7), 11);
        ana.crearNuevoEstado('L', estad.get(11), 12);
        ana.crearNuevoEstado(')', estad.get(12), 13);
        ana.crearNuevoEstado('i', estad.get(11), 14);
        ana.crearNuevoEstado('S', estad.get(11), 15);

//        String[] caracterPosteriorPunto = caracteresPosterioresPunto[1].split("");
//        char[] caracterPosteriorPuntoChar = caracterPosteriorPunto[0].toCharArray();
//        char caracter = caracterPosteriorPuntoChar[0];
//        
        for (Estado estado : ana.estados) {
            System.out.println(estado);
        }
    }

}
