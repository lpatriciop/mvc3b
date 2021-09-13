/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.modelo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.postgresql.util.Base64;


/**
 *
 * @author Patricio
 */
public class ModeloPersona extends Persona{
    ConexionPG con=new ConexionPG();

    public ModeloPersona() {
    }

    public ModeloPersona(String idPersona, String nombre, String apellido, Date fechaNacimiento, Image foto) {
        super(idPersona, nombre, apellido, fechaNacimiento, foto);
    }
    
    public List<Persona> listaPersonas(){
    
        try {
            String sql="select * from persona";
            ResultSet rs=con.consulta(sql);
            List<Persona> lp= new ArrayList<Persona>();
            while(rs.next()){
                Persona per= new Persona();
                per.setIdPersona(rs.getString("idpersona"));//Nombre de la columna de la base de dato.
                per.setNombre(rs.getString("nombres"));//Nombre de la columna de la base de dato.
                per.setApellido(rs.getString("apellidos"));//Nombre de la columna de la base de dato.
                lp.add(per);
            }
          rs.close();
          return lp;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     
    }
    public List<Persona> listaPersonas(String aguja){
    
        try {
            String sql="select * from persona WHERE ";
            sql+=" UPPER(nombres) like UPPER('%"+aguja+"%') OR";
            sql+=" UPPER(apellidos) like UPPER('%"+aguja+"%') OR";
            sql+=" UPPER(idpersona) like UPPER('%"+aguja+"%') ";
            ResultSet rs=con.consulta(sql);
            List<Persona> lp= new ArrayList<Persona>();
            byte[] bf;
            while(rs.next()){
                Persona per= new Persona();
                per.setIdPersona(rs.getString("idpersona"));//Nombre de la columna de la base de dato.
                per.setNombre(rs.getString("nombres"));//Nombre de la columna de la base de dato.
                per.setApellido(rs.getString("apellidos"));//Nombre de la columna de la base de dato.
                bf=rs.getBytes("foto");
                if(bf!=null){
                    bf=Base64.decode(bf,0,bf.length);
                    
                    try {
                        per.setFoto(obteberImagen(bf));
                    } catch (IOException ex) {
                        Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                lp.add(per);
            }
          rs.close();
          return lp;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     
    }
    public Image obteberImagen(byte[] bytes ) throws IOException{
        ByteArrayInputStream bis= new ByteArrayInputStream(bytes);
        Iterator it= ImageIO.getImageReadersByFormatName("png");
        ImageReader reader=(ImageReader)it.next();
        Object source=bis;
        ImageInputStream iis=ImageIO.createImageInputStream(source);
        reader.setInput(iis,true);
        ImageReadParam param= reader.getDefaultReadParam();
        param.setSourceSubsampling(1, 1, 0, 0);
        return reader.read(0,param);
    
    }
    public boolean grabar(){
        String foto64=null;
        try {

            BufferedImage img=imgBimage(getFoto());
            ByteArrayOutputStream bos= new ByteArrayOutputStream();
            ImageIO.write(img, "PNG", bos);
            byte[] imgb=bos.toByteArray();
            foto64=Base64.encodeBytes(imgb);
            
        } catch (IOException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String sql; 
            sql="INSERT INTO persona(idpersona,nombres,apellidos,foto) ";
            sql+=" VALUES ('"+getIdPersona()+"','"+getNombre()+"','"+getApellido()+"','"+foto64+"')";
            //INSERT INTO persona (idpersona,nombres,apellidos) VALUES ('010101','JUAN','PEREZ','52%$#@%$#@%$@#%')
            return con.accion(sql);
       
    }
    
    
    private BufferedImage imgBimage(Image img){
        
        if (img instanceof BufferedImage){
            return (BufferedImage)img;
        }
        BufferedImage bi = new BufferedImage(
                img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D bGR = bi.createGraphics();
        bGR.drawImage(img, 0, 0,null);
        bGR.dispose();
        return bi;        
    }
}
