/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package baseDatos;

import aplicacion.TipoUsuario;
import aplicacion.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author basesdatos
 */
public class DAOUsuarios extends AbstractDAO {

   public DAOUsuarios (Connection conexion, aplicacion.FachadaAplicacion fa){
        super.setConexion(conexion);
        super.setFachadaAplicacion(fa);
    }

    public Usuario validarUsuario(String idUsuario, String clave){
        Usuario resultado=null;
        Connection con;
        PreparedStatement stmUsuario=null;
        ResultSet rsUsuario;

        con=this.getConexion();

        try {
        stmUsuario=con.prepareStatement("select id_usuario, clave, nombre, direccion, email, tipo_usuario "+
                                        "from usuario "+
                                        "where id_usuario = ? and clave = ?");
        stmUsuario.setString(1, idUsuario);
        stmUsuario.setString(2, clave);
        rsUsuario=stmUsuario.executeQuery();
        if (rsUsuario.next())
        {
            resultado = new Usuario(rsUsuario.getString("id_usuario"), rsUsuario.getString("clave"),
                                      rsUsuario.getString("nombre"), rsUsuario.getString("direccion"),
                                      rsUsuario.getString("email"), TipoUsuario.valueOf(rsUsuario.getString("tipo_usuario")));

        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
          this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        }finally{
          try {stmUsuario.close();} catch (SQLException e){System.out.println("Imposible cerrar cursores");}
        }
        return resultado;
    }
    
    public boolean insertarUsuario(Usuario usuario){
        Connection con;
        PreparedStatement stmUsuario = null;
        boolean resultado = false;
        
        con = this.getConexion();
        
        try{
            stmUsuario = con.prepareStatement(
                "INSERT INTO usuario (id_usuario, clave, nombre, direccion, email, tipo_usuario) " +
                "VALUES (?, ?, ?, ?, ?, ?)");
            
            stmUsuario.setString(1, usuario.getIdUsuario());
            stmUsuario.setString(2, usuario.getClave());
            stmUsuario.setString(3, usuario.getNombre());
            stmUsuario.setString(4, usuario.getDireccion());
            stmUsuario.setString(5, usuario.getEmail());
            stmUsuario.setString(6, usuario.getTipoUsuario().name());
            
            int filasAfectadas = stmUsuario.executeUpdate();
            resultado = filasAfectadas > 0;
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally{
            try{
                if (stmUsuario != null) stmUsuario.close();
            } catch (SQLException e){
                System.out.println("Imposible cerrar cursores");
            }
        }
        return resultado;
    }
    
    public java.util.List<Usuario> buscarUsuarios(String id, String nombre) {
        java.util.List<Usuario> resultado = new ArrayList<>();
        Connection con;
        PreparedStatement stmUsuarios = null;
        ResultSet rsUsuarios;
        
        con = this.getConexion();
        
        try {
            String consulta = "SELECT id_usuario, clave, nombre, direccion, email, tipo_usuario " +
                             "FROM usuario " +
                             "WHERE TRUE ";
            
            if (id != null && !id.isEmpty()) {
                consulta += "AND UPPER(id_usuario) LIKE UPPER(?) ";
            }
            
            if (nombre != null && !nombre.isEmpty()) {
                consulta += "AND UPPER(nombre) LIKE UPPER(?) ";
            }
            
            consulta += "ORDER BY id_usuario";
            
            stmUsuarios = con.prepareStatement(consulta);
            
            int i = 1;
            if (id != null && !id.isEmpty()) {
                stmUsuarios.setString(i++, "%" + id + "%");
            }
            
            if (nombre != null && !nombre.isEmpty()) {
                stmUsuarios.setString(i++, "%" + nombre + "%");
            }
            
            rsUsuarios = stmUsuarios.executeQuery();
            
            while (rsUsuarios.next()) {
                Usuario usuario = new Usuario(
                    rsUsuarios.getString("id_usuario"),
                    rsUsuarios.getString("clave"),
                    rsUsuarios.getString("nombre"),
                    rsUsuarios.getString("direccion"),
                    rsUsuarios.getString("email"),
                    TipoUsuario.valueOf(rsUsuarios.getString("tipo_usuario"))
                );
                resultado.add(usuario);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmUsuarios != null) stmUsuarios.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return resultado;
    }
    
    public boolean modificarUsuario(Usuario usuario) {
        Connection con;
        PreparedStatement stmUsuario = null;
        boolean resultado = false;
        
        con = this.getConexion();
        
        try {
            stmUsuario = con.prepareStatement(
                "UPDATE usuario SET clave = ?, nombre = ?, direccion = ?, email = ?, tipo_usuario = ? " +
                "WHERE id_usuario = ?");
            
            stmUsuario.setString(1, usuario.getClave());
            stmUsuario.setString(2, usuario.getNombre());
            stmUsuario.setString(3, usuario.getDireccion());
            stmUsuario.setString(4, usuario.getEmail());
            stmUsuario.setString(5, usuario.getTipoUsuario().name());
            stmUsuario.setString(6, usuario.getIdUsuario());
            
            int filasAfectadas = stmUsuario.executeUpdate();
            resultado = filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmUsuario != null) stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return resultado;
    }
    
    public boolean borrarUsuario(String idUsuario) {
        Connection con;
        PreparedStatement stmComprobarPrestamos = null;
        PreparedStatement stmBorrarUsuario = null;
        ResultSet rsPrestamos;
        boolean resultado = false;
        
        con = this.getConexion();
        
        try {
            // Primero comprobamos si el usuario tiene préstamos activos
            // Como los préstamos aún no están implementados, comprobamos si existiría
            // la tabla préstamos y la relación con usuario
            stmComprobarPrestamos = con.prepareStatement(
                "SELECT EXISTS (SELECT 1 FROM information_schema.tables " +
                "WHERE table_name = 'prestamo') AS existe_tabla");
            
            rsPrestamos = stmComprobarPrestamos.executeQuery();
            boolean existeTablaPrestamos = false;
            
            if (rsPrestamos.next()) {
                existeTablaPrestamos = rsPrestamos.getBoolean("existe_tabla");
            }
            
            // Si existe la tabla de préstamos, comprobaríamos si tiene préstamos activos
            if (existeTablaPrestamos) {
                stmComprobarPrestamos.close();
                stmComprobarPrestamos = con.prepareStatement(
                    "SELECT COUNT(*) AS num_prestamos FROM prestamo WHERE usuario = ?");
                stmComprobarPrestamos.setString(1, idUsuario);
                rsPrestamos = stmComprobarPrestamos.executeQuery();
                
                if (rsPrestamos.next() && rsPrestamos.getInt("num_prestamos") > 0) {
                    // El usuario tiene préstamos, no podemos borrarlo
                    return false;
                }
            }
            
            // Si no hay préstamos o no existe la tabla, procedemos a borrar
            stmBorrarUsuario = con.prepareStatement("DELETE FROM usuario WHERE id_usuario = ?");
            stmBorrarUsuario.setString(1, idUsuario);
            
            int filasAfectadas = stmBorrarUsuario.executeUpdate();
            resultado = filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmComprobarPrestamos != null) stmComprobarPrestamos.close();
                if (stmBorrarUsuario != null) stmBorrarUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return resultado;
    }
}
