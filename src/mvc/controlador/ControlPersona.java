/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controlador;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mvc.modelo.ModeloPersona;
import mvc.modelo.Persona;
import mvc.vista.VistaPersona;

/**
 *
 * @author Patricio
 */
public class ControlPersona {
    
    private ModeloPersona modelo;
    private VistaPersona vista;

    public ControlPersona(ModeloPersona modelo, VistaPersona vista) {
        this.modelo = modelo;
        this.vista = vista;
        //Inicializaciones
        vista.setTitle("CRUD PERSONAS");
        vista.setVisible(true);
        cargaLista();
    }
    
    public void iniciaControl(){
     KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
           //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
           //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
            //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                cargaLista(vista.getTxtBuscar().getText());
            }
        };

   //Controlar los eventos de la vista
    vista.getBtnRefrescar().addActionListener(l->cargaLista());
    vista.getBtnCrear().addActionListener(l->cargarDialogo(1));
    vista.getBtnEditar().addActionListener(l->cargarDialogo(2));
    vista.getBtnExaminar().addActionListener(l->examinaFoto());
    vista.getBtnAceptar().addActionListener(l->grabaPersona());
    //Controlador Buscar
    vista.getTxtBuscar().addKeyListener(kl);
    
    }
    private void cargarDialogo(int origen){
        vista.getDlgPersona().setSize(600,300);
        vista.getDlgPersona().setLocationRelativeTo(vista);
        if(origen==1){
            vista.getDlgPersona().setTitle("Crear Persona");
        }else{
             vista.getTxtID().setText("0101201212");
             vista.getDlgPersona().setTitle("Editar Persona");
        }
        vista.getDlgPersona().setVisible(true);
    
    }
    private void cargaLista(){
    //Acciones necesarios para extraer los datos MODELO Y Mostrar en la Vista
        DefaultTableModel tblModel; //Estructura JTbable
        tblModel=(DefaultTableModel)vista.getTblPersonas().getModel();
        tblModel.setNumRows(0);
        List<Persona> lista=modelo.listaPersonas();
        lista.stream().forEach(p->{
        String[] persona={p.getIdPersona(),p.getNombre(),p.getApellido()};
        tblModel.addRow(persona);
        });
        
    }
    private void cargaLista(String aguja){
    //Acciones necesarios para extraer los datos MODELO Y Mostrar en la Vista
        DefaultTableModel tblModel; //Estructura JTbable
        tblModel=(DefaultTableModel)vista.getTblPersonas().getModel();
        tblModel.setNumRows(0);
        List<Persona> lista=modelo.listaPersonas(aguja);
        lista.stream().forEach(p->{
        String[] persona={p.getIdPersona(),p.getNombre(),p.getApellido()};
        tblModel.addRow(persona);
        });
        
    }
    private void examinaFoto(){
        JFileChooser jfc= new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int estado=jfc.showOpenDialog(null);
        if(estado==JFileChooser.APPROVE_OPTION){
            try {
                Image miImagen = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(
                        vista.getLblFoto().getWidth(),
                        vista.getLblFoto().getHeight(),
                        Image.SCALE_DEFAULT);
                Icon icon=new ImageIcon(miImagen);
                vista.getLblFoto().setIcon(icon);
                vista.getLblFoto().updateUI();
            } catch (IOException ex) {
                Logger.getLogger(ControlPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void grabaPersona(){
      String idpersona = vista.getTxtID().getText();
      String nombre = vista.getTxtNombres().getText();
      String apellido = vista.getTxtApellidos().getText();
      
      Instant instant= vista.getDtcFechaNacimiento().getDate().toInstant();
      ZoneId zid= ZoneId.of("America/Guayaquil");
      ZonedDateTime zdt=ZonedDateTime.ofInstant(instant, zid);  
      Date fecha = Date.valueOf(zdt.toLocalDate());
      
      ModeloPersona persona = new ModeloPersona();
      persona.setIdPersona(idpersona);
      persona.setNombre(nombre);
      persona.setApellido(apellido);
      persona.setFechaNacimiento(fecha);
      
     if (persona.grabar()){
         JOptionPane.showMessageDialog(vista, "Persona Creada Satisfactoriamente");
     }else{
         JOptionPane.showMessageDialog(vista, "ERROR");
     }
    }
}
