/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import mvc.controlador.ControlPersona;
import mvc.modelo.ModeloPersona;
import mvc.vista.VistaPersona;

/**
 *
 * @author Patricio
 */
public class Mvc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ModeloPersona modelo= new ModeloPersona();
        VistaPersona vista =new VistaPersona();
        ControlPersona control= new ControlPersona(modelo, vista);
        control.iniciaControl();
        
    }
    
}
