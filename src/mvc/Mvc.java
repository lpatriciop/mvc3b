/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import mvc.controlador.ControlPersona;
import mvc.controlador.ControlPrincipal;
import mvc.modelo.ModeloPersona;
import mvc.vista.VistaPersona;
import mvc.vista.VistaPrincipal;

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
        VistaPrincipal vista= new VistaPrincipal();
        ControlPrincipal control= new ControlPrincipal(vista);
        control.iniciaControl();
        
    }
    
}
