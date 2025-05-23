/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aplicacion;

import baseDatos.FachadaBaseDatos;
import gui.FachadaGui;
/**
 *
 * @author basesdatos
 */
public class GestionLibros {
    FachadaGui fgui;
    FachadaBaseDatos fbd;
    
    public GestionLibros(FachadaGui fgui, FachadaBaseDatos fbd){
     this.fgui=fgui;
     this.fbd=fbd;
    }

    public java.util.List<Libro> obtenerLibros(Integer id, String titulo, String isbn, String autor){
        return fbd.consultarCatalogo(id, titulo, isbn, autor);
    }

    public void visualizarLibro(Integer idLibro){
        java.util.List<String> restoCategorias;
        java.util.List<Ejemplar> ejemplares;
        Libro l;
        l=fbd.consultarLibro(idLibro);
        restoCategorias=fbd.obtenerRestoCategorias(idLibro);
        fgui.visualizaLibro(l, restoCategorias);
    }

    public void nuevoLibro(){
        java.util.List<String> restoCategorias = new java.util.ArrayList<String> ();

        for(Categoria c:fbd.consultarCategorias()){
            restoCategorias.add(c.getNombre());
        }

        fgui.nuevoLibro(restoCategorias);
    }
    
    public Integer actualizarLibro(Libro l){
        
       Integer idLibro;

       if (l.getIdLibro()==null)
           idLibro=fbd.insertarLibro(l);
       else{
          fbd.modificarLibro(l);
          idLibro=l.getIdLibro();
       }

       return idLibro;
    }

    public void borrarLibro(Integer idLibro){
       fbd.borrarLibro(idLibro);
    }

    public void actualizarCategoriasLibro(Integer idLibro, java.util.List<String> categorias){
       fbd.modificarCategoriasLibro(idLibro, categorias);
    }

    public java.util.List<Ejemplar> actualizarEjemplaresLibro(Integer idLibro, java.util.List<Ejemplar> ejemplares, java.util.List<Integer> borrar){

       for (Ejemplar e:ejemplares){
        if (e.getNumEjemplar()==null) fbd.insertarEjemplarLibro(idLibro, e);
        else fbd.modificarEjemplarLibro(idLibro, e);
       }
       
       fbd.borrarEjemplaresLibro(idLibro, borrar);

       // Obtener los ejemplares actualizados de la base de datos
       java.util.List<Ejemplar> ejemplaresActualizados = fbd.consultarEjemplaresLibro(idLibro);
       
       // Obtener el objeto libro completo
       Libro libro = fbd.consultarLibro(idLibro);
       
       // Asignar el libro a cada ejemplar para que ModeloTablaEjemplares pueda mostrar el estado correcto
       for (Ejemplar e : ejemplaresActualizados) {
           e.setLibro(libro);
       }
       
       return ejemplaresActualizados;
    }
}
