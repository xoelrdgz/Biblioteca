/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package baseDatos;
import aplicacion.Usuario;
import aplicacion.TipoUsuario;
import java.sql.*;
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
    
}
