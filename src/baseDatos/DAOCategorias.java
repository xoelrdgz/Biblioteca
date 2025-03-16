/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package baseDatos;

import aplicacion.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author basesdatos
 */
public class DAOCategorias extends AbstractDAO {
   
    
    public DAOCategorias (Connection conexion, aplicacion.FachadaAplicacion fa){
        super.setConexion(conexion);
        super.setFachadaAplicacion(fa);
    }

    public java.util.List<Categoria> consultarCategorias(){
        java.util.List<Categoria> resultado = new java.util.ArrayList<Categoria>();
        Categoria categoriaActual;
        Connection con;
        PreparedStatement stmCategorias=null;
        ResultSet rsCategorias;

        con=this.getConexion();

        try  {
        stmCategorias=con.prepareStatement("select nombre, descripcion from categoria");
        rsCategorias=stmCategorias.executeQuery();
        while (rsCategorias.next())
        {
            categoriaActual = new Categoria(rsCategorias.getString("nombre"), rsCategorias.getString("descripcion"));
            resultado.add(categoriaActual);
        }

        } catch (SQLException e){
          System.out.println(e.getMessage());
          this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        }finally{
          try {stmCategorias.close();} catch (SQLException e){System.out.println("Imposible cerrar cursores");}
        }
        return resultado;
    }

    public boolean insertarCategoria(Categoria categoria) {
        Connection con;
        PreparedStatement stmCategoria = null;
        boolean resultado = false;
        
        con = this.getConexion();
        
        try {
            // Verificar si ya existe una categoría con ese nombre
            PreparedStatement stmVerificar = con.prepareStatement(
                    "SELECT nombre FROM categoria WHERE nombre = ?");
            stmVerificar.setString(1, categoria.getNombre());
            ResultSet rs = stmVerificar.executeQuery();
            
            if (rs.next()) {
                // Ya existe una categoría con este nombre
                this.getFachadaAplicacion().muestraExcepcion("Ya existe una categoría con el nombre '" + 
                        categoria.getNombre() + "'. El nombre de cada categoría debe ser único.");
                stmVerificar.close();
                return false;
            }
            stmVerificar.close();
            
            // Si no existe, insertar la nueva categoría
            stmCategoria = con.prepareStatement(
                    "INSERT INTO categoria(nombre, descripcion) VALUES (?, ?)");
            stmCategoria.setString(1, categoria.getNombre());
            stmCategoria.setString(2, categoria.getDescripcion());
            
            int filas = stmCategoria.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmCategoria != null) stmCategoria.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return resultado;
    }

    public boolean borrarCategoria(String nombreCategoria) {
        Connection con;
        PreparedStatement stmCategoria = null;
        boolean resultado = false;
        
        con = this.getConexion();
        
        try {
            // Verificar si la categoría está siendo usada por algún libro
            PreparedStatement stmVerificar = con.prepareStatement(
                    "SELECT COUNT(*) as total FROM cat_tiene_libro WHERE categoria = ?");
            stmVerificar.setString(1, nombreCategoria);
            ResultSet rs = stmVerificar.executeQuery();
            
            if (rs.next() && rs.getInt("total") > 0) {
                // La categoría tiene libros asignados
                this.getFachadaAplicacion().muestraExcepcion("No se puede eliminar la categoría '" + 
                        nombreCategoria + "' porque tiene libros asignados.");
                stmVerificar.close();
                return false;
            }
            stmVerificar.close();
            
            // Si no tiene libros, eliminar la categoría
            stmCategoria = con.prepareStatement(
                    "DELETE FROM categoria WHERE nombre = ?");
            stmCategoria.setString(1, nombreCategoria);
            
            int filas = stmCategoria.executeUpdate();
            resultado = (filas > 0);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmCategoria != null) stmCategoria.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return resultado;
    }
}
