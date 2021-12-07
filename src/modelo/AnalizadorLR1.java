/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juan David
 */
public class AnalizadorLR1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      Map<String,String> hm = new HashMap<String,String>();
      hm.put("1", "H->.S");
      hm.put("2", "H->.S");
        System.out.println(hm);
        System.out.println();
    }
    
}
