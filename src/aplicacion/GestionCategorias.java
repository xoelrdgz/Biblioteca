package aplicacion;

import baseDatos.FachadaBaseDatos;
import gui.FachadaGui;

import java.util.List;

/**
 *
 * @author xoel
 */
public class GestionCategorias {
     
    FachadaGui fgui;
    FachadaBaseDatos fbd;
    
   
    public GestionCategorias(FachadaGui fgui, FachadaBaseDatos fbd){
     this.fgui = fgui;
     this.fbd = fbd;
    }  
    
    public List<Categoria> obtenerCategorias() {
        return fbd.consultarCategorias();
    }
    
    public boolean insertarCategoria(Categoria categoria) {
        // Validación de campos
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            fgui.muestraExcepcion("El nombre de la categoría no puede estar vacío");
            return false;
        }
        
        // Si pasa las validaciones, proceder a insertar
        return fbd.insertarCategoria(categoria);
    }
    
    public boolean borrarCategoria(String nombreCategoria) {
        // Validación de campos
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            fgui.muestraExcepcion("El nombre de la categoría no puede estar vacío");
            return false;
        }
        
        boolean resultado = fbd.borrarCategoria(nombreCategoria);
        
        if (!resultado) {
            // Si no se pudo borrar, podría ser porque tiene libros asignados
            fgui.muestraExcepcion("No se puede borrar la categoría. Posiblemente tenga libros asignados.");
        }
        
        return resultado;
    }
}