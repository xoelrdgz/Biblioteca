/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aplicacion;


/**
 *
 * @author basesdatos
 */
public class FachadaAplicacion {
    gui.FachadaGui fgui;
    baseDatos.FachadaBaseDatos fbd;
    GestionLibros cl;
    GestionUsuarios cu;
    GestionCategorias cc;
    GestionPrestamos cp;
    
    
 public FachadaAplicacion(){
   fgui=new gui.FachadaGui(this);
   fbd= new baseDatos.FachadaBaseDatos(this);
   cl= new GestionLibros(fgui, fbd);
   cu= new GestionUsuarios(fgui, fbd);
   cc= new GestionCategorias(fgui, fbd);
   cp= new GestionPrestamos(fgui, fbd);
 }

 public static void main(String args[]) {
     FachadaAplicacion fa;
     
     fa= new FachadaAplicacion();
     fa.iniciaInterfazUsuario();
 }
 
 public void iniciaInterfazUsuario(){
     fgui.iniciaVista();
 }

 public void muestraExcepcion(String e){
     fgui.muestraExcepcion(e);
 }
 
public java.util.List<Libro> obtenerLibros(Integer id, String titulo, String isbn, String autor){
  return cl.obtenerLibros(id, titulo,  isbn,  autor);
};

public void visualizarLibro(Integer idLibro){
 cl.visualizarLibro(idLibro);
}

public void nuevoLibro(){
 cl.nuevoLibro();
}

public Integer actualizarLibro(Libro l){
  return cl.actualizarLibro(l);
}

public void borrarLibro(Integer idLibro){
   cl.borrarLibro(idLibro);
}

public void actualizarCategoriasLibro(Integer idLibro, java.util.List<String> categorias){
 cl.actualizarCategoriasLibro(idLibro, categorias);
}

public java.util.List<Ejemplar> actualizarEjemplaresLibro(Integer idLibro, java.util.List<Ejemplar> ejemplares, java.util.List<Integer> borrar){
  return cl.actualizarEjemplaresLibro(idLibro, ejemplares, borrar);
}


public Boolean comprobarAutentificacion(String idUsuario, String clave){
  return cu.comprobarAutentificacion(idUsuario, clave);
}

public boolean insertarUsuario(Usuario usuario) {
    return cu.insertarUsuario(usuario);
}

public boolean modificarUsuario(Usuario usuario) {
    return cu.modificarUsuario(usuario);
}

public boolean borrarUsuario(String idUsuario) {
    return cu.borrarUsuario(idUsuario);
}

public java.util.List<Usuario> buscarUsuarios(String id, String nombre) {
    return cu.buscarUsuarios(id, nombre);
}

public void gestionarUsuarios() {
    fgui.gestionarUsuarios();
}

public java.util.List<Categoria> getCategorias() {
    return fbd.consultarCategorias();
}

public boolean insertarCategoria(Categoria categoria) {
    return cc.insertarCategoria(categoria);
}

public boolean borrarCategoria(String nombreCategoria) {
    return cc.borrarCategoria(nombreCategoria);
}

public void gestionarCategorias() {
    fgui.gestionarCategorias();
}

public int numPrestamosVencidos(String idUsuario) {
    return cp.numPrestamosVencidos(idUsuario);
}

public boolean prestarEjemplar(String idUsuario, Integer idLibro, Integer numEjemplar) {
    return cp.prestarEjemplar(idUsuario, idLibro, numEjemplar);
}

public boolean devolverEjemplar(Integer idLibro, Integer numEjemplar) {
    return cp.devolverEjemplar(idLibro, numEjemplar);
}

public void mostrarVentanaPrestamos(Integer idLibro, Integer numEjemplar) {
    cp.mostrarVentanaPrestamos(idLibro, numEjemplar);
}

public java.util.List<Usuario> buscarUsuariosPrestamo(String id, String nombre) {
    return cp.buscarUsuariosPrestamo(id, nombre);
}

public boolean ejemplarPrestado(Integer idLibro, Integer numEjemplar) {
    return fbd.ejemplarPrestado(idLibro, numEjemplar);
}

public java.util.Map<String, Object> obtenerInfoPrestamo(Integer idLibro, Integer numEjemplar) {
    return fbd.obtenerInfoPrestamo(idLibro, numEjemplar);
}

public Libro consultarLibro(Integer idLibro) {
    return fbd.consultarLibro(idLibro);
}

public boolean ejemplarTienePrestamos(Integer idLibro, Integer numEjemplar) {
    return cp.ejemplarTienePrestamos(idLibro, numEjemplar);
}
}
