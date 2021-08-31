/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.modelo;

import java.awt.Image;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public boolean grabar(){
        String sql;
        sql="INSERT INTO persona(idpersona,nombres,apellidos) ";
        sql+=" VALUES ('"+getIdPersona()+"','"+getNombre()+"','"+getApellido()+"')";
        return con.accion(sql);
    }
}
