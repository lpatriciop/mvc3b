/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controlador;

import mvc.modelo.ModeloPersona;
import mvc.vista.VistaPersona;
import mvc.vista.VistaPrincipal;

/**
 *
 * @author Patricio
 */
public class ControlPrincipal {
    private VistaPrincipal vp;

    public ControlPrincipal(VistaPrincipal vp) {
        this.vp = vp;
        vp.setVisible(true);
    }
    
    public void iniciaControl(){
        vp.getMnuCrudPersonas().addActionListener(l->crudPersonas());
        vp.getTlbCrudPersona().addActionListener(l->crudPersonas());
    }
    
    private void crudPersonas(){
        ModeloPersona m=new ModeloPersona();
        VistaPersona v= new VistaPersona();
        vp.getDskPrincipal().add(v);
        ControlPersona c= new ControlPersona(m, v);
        c.iniciaControl();
    }
}
