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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.Holder;
import mvc.modelo.ConexionPG;
import mvc.modelo.ModeloPersona;
import mvc.modelo.Persona;
import mvc.vista.VistaPersona;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import sun.swing.table.DefaultTableCellHeaderRenderer;

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
        cargaLista("");
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
    vista.getBtnRefrescar().addActionListener(l->cargaLista(""));
    vista.getBtnCrear().addActionListener(l->cargarDialogo(1));
    vista.getBtnEditar().addActionListener(l->cargarDialogo(2));
    vista.getBtnExaminar().addActionListener(l->examinaFoto());
    vista.getBtnAceptar().addActionListener(l->grabaPersona());
    //Controlador Buscar
    vista.getTxtBuscar().addKeyListener(kl);
    //Imprimir
    vista.getBtnImprimir().addActionListener(l->imprimir());
    }
    
    /*
    IMPRESION Y VISTA PRELIMINAR DE REPORTES DE JASPER STUDIO
    
    */
    
    private void imprimir(){
        ConexionPG con=new ConexionPG();
        try {
            JasperReport jr= (JasperReport)JRLoader.loadObject(getClass().getResource("/mvc/vista/reportes/Blank_1.jasper"));
            JasperPrint jp = JasperFillManager.fillReport(jr,null,con.getCon());
            JasperViewer jv= new JasperViewer(jp);
            jv.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(ControlPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
    private void cargaLista(String aguja){
    //Acciones necesarios para extraer los datos MODELO Y Mostrar en la Vista
        vista.getTblPersonas().setDefaultRenderer(Object.class, new ImgenTabla());
        vista.getTblPersonas().setRowHeight(100);
        DefaultTableCellRenderer renderer= new DefaultTableCellHeaderRenderer();
        
        DefaultTableModel tblModel; //Estructura JTbable
        tblModel=(DefaultTableModel)vista.getTblPersonas().getModel();
        tblModel.setNumRows(0);
        List<Persona> lista=modelo.listaPersonas(aguja);
        int ncols=tblModel.getColumnCount();
        Holder<Integer> i=new Holder<>(0);
        lista.stream().forEach(p->{
            
         tblModel.addRow(new Object[ncols]);
           vista.getTblPersonas().setValueAt(p.getIdPersona(), i.value, 0);
           vista.getTblPersonas().setValueAt(p.getNombre(), i.value, 1);
           vista.getTblPersonas().setValueAt(p.getApellido(), i.value, 2);
           //completar datos.
           Image img= p.getFoto();
           if(img!=null){
               Image nimg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
               ImageIcon icon = new ImageIcon(nimg);
               renderer.setIcon(icon);
               vista.getTblPersonas().setValueAt(new JLabel(icon), i.value, 4);
           }else{
                vista.getTblPersonas().setValueAt(null, i.value, 4);
           }
          i.value++;  
//        String[] persona={p.getIdPersona(),p.getNombre(),p.getApellido()};
//        tblModel.addRow(persona);
        
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
      //FOTO
      ImageIcon ic=(ImageIcon)vista.getLblFoto().getIcon();
      persona.setFoto(ic.getImage());
      
     if (persona.grabar()){
         JOptionPane.showMessageDialog(vista, "Persona Creada Satisfactoriamente");
     }else{
         JOptionPane.showMessageDialog(vista, "ERROR");
     }
    }
}
