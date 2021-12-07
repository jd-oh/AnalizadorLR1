package control;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import modelo.Gramatica;
import modelo.Produccion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 *
 * @author Juan David
 */
public class ControlJSON {
    

    public Gramatica leerGramatica(String ruta) {

        try {
            JSONParser parser = new JSONParser();
            ArrayList<Produccion> conjuntoProduccion = new ArrayList<>();
            JSONObject gramaticaJSON = (JSONObject) parser.parse(new FileReader(ruta));

            JSONArray conjProduccionJSON = (JSONArray) gramaticaJSON.get("conjuntoProduccion");

            if (conjProduccionJSON != null) {
                
                convertirJSONAString(conjProduccionJSON, conjuntoProduccion);
                
                Gramatica gramatica = new Gramatica(conjuntoProduccion);
               // mostrarProducciones(gramatica);
                return gramatica;
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void convertirJSONAString(JSONArray conjProduccionJSON, ArrayList<Produccion> conjuntoProduccion) {
        for (int i = 0; i < conjProduccionJSON.size(); i++) {
            
            JSONObject produccionJSON = (JSONObject) conjProduccionJSON.get(i);
            
            String izquierda = (String) produccionJSON.get("izquierda");
            String derecha = (String) produccionJSON.get("derecha");
            
            Produccion produccion = new Produccion(izquierda, derecha);
            conjuntoProduccion.add(produccion);
            
        }
    }

    private void mostrarProducciones(Gramatica gramatica) {
        
        for (Produccion produccion : gramatica.getConjuntoProduccion()) {

            System.out.println(produccion);

        }
    }

}
