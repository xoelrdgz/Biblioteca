/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aplicacion;
import gui.FachadaGui;
import baseDatos.FachadaBaseDatos;
import java.util.regex.Pattern;
/**
 *
 * @author basesdatos
 */
public class GestionUsuarios {
     
    FachadaGui fgui;
    FachadaBaseDatos fbd;
    
   
    public GestionUsuarios(FachadaGui fgui, FachadaBaseDatos fbd){
     this.fgui=fgui;
     this.fbd=fbd;
    }  
    
    
  public Boolean comprobarAutentificacion(String idUsuario, String clave){
      Usuario u;

      u=fbd.validarUsuario(idUsuario, clave);
      if (u!=null){
          return u.getTipoUsuario()==TipoUsuario.Administrador;
      } else return false;
  }
  
  public boolean insertarUsuario(Usuario usuario){
      // Validación de campos
      if (usuario.getIdUsuario() == null || usuario.getIdUsuario().trim().isEmpty()) {
          fgui.muestraExcepcion("El ID de usuario no puede estar vacío");
          return false;
      }
      
      if (usuario.getClave() == null || usuario.getClave().trim().isEmpty()) {
          fgui.muestraExcepcion("La clave no puede estar vacía");
          return false;
      }
      
      if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
          fgui.muestraExcepcion("El nombre no puede estar vacío");
          return false;
      }
      
      // Validación de correo electrónico
      if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
          String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
          if (!Pattern.matches(emailRegex, usuario.getEmail())) {
              fgui.muestraExcepcion("El formato del correo electrónico no es válido");
              return false;
          }
      }
      
      // Si pasa las validaciones, proceder a insertar
      return fbd.insertarUsuario(usuario);
  }
  
  public boolean modificarUsuario(Usuario usuario) {
      // Validación de campos
      if (usuario.getIdUsuario() == null || usuario.getIdUsuario().trim().isEmpty()) {
          fgui.muestraExcepcion("El ID de usuario no puede estar vacío");
          return false;
      }
      
      if (usuario.getClave() == null || usuario.getClave().trim().isEmpty()) {
          fgui.muestraExcepcion("La clave no puede estar vacía");
          return false;
      }
      
      if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
          fgui.muestraExcepcion("El nombre no puede estar vacío");
          return false;
      }
      
      // Validación de correo electrónico
      if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
          String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
          if (!Pattern.matches(emailRegex, usuario.getEmail())) {
              fgui.muestraExcepcion("El formato del correo electrónico no es válido");
              return false;
          }
      }
      
      // Si pasa las validaciones, proceder a actualizar
      return fbd.modificarUsuario(usuario);
  }
  
  public java.util.List<Usuario> buscarUsuarios(String id, String nombre) {
      return fbd.buscarUsuarios(id, nombre);
  }
  
  public boolean borrarUsuario(String idUsuario) {
      // Validación de campos
      if (idUsuario == null || idUsuario.trim().isEmpty()) {
          fgui.muestraExcepcion("El ID de usuario no puede estar vacío");
          return false;
      }
      
      boolean resultado = fbd.borrarUsuario(idUsuario);
      
      if (!resultado) {
          // Si no se pudo borrar, podría ser por préstamos pendientes
          fgui.muestraExcepcion("No se puede borrar el usuario. Es posible que tenga préstamos pendientes.");
      }
      
      return resultado;
  }
  
}
