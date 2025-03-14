/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package baseDatos;

import aplicacion.Ejemplar;
import aplicacion.Usuario;
import aplicacion.Categoria;
import aplicacion.Libro;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author basesdatos
 */
public class FachadaBaseDatos {
    private aplicacion.FachadaAplicacion fa;
    private java.sql.Connection conexion;
    private DAOLibros daoLibros;
    private DAOCategorias daoCategorias;
    private DAOUsuarios daoUsuarios;
    private DAOPrestamos daoPrestamos;

    public FachadaBaseDatos (aplicacion.FachadaAplicacion fa){
        
        Properties configuracion = new Properties();
        this.fa=fa;
        FileInputStream arqConfiguracion;

        try {
            arqConfiguracion = new FileInputStream("baseDatos.properties");
            configuracion.load(arqConfiguracion);
            arqConfiguracion.close();

            Properties usuario = new Properties();
     

            String gestor = configuracion.getProperty("gestor");

            usuario.setProperty("user", configuracion.getProperty("usuario"));
            usuario.setProperty("password", configuracion.getProperty("clave"));
            this.conexion=java.sql.DriverManager.getConnection("jdbc:"+gestor+"://"+
                    configuracion.getProperty("servidor")+":"+
                    configuracion.getProperty("puerto")+"/"+
                    configuracion.getProperty("baseDatos"),
                    usuario);

            daoLibros = new DAOLibros(conexion, fa);
            daoCategorias = new DAOCategorias(conexion, fa);
            daoUsuarios = new DAOUsuarios(conexion, fa);
            daoPrestamos = new DAOPrestamos(conexion, fa);
          


        } catch (FileNotFoundException f){
            System.out.println(f.getMessage());
            fa.muestraExcepcion(f.getMessage());
        } catch (IOException i){
            System.out.println(i.getMessage());
            fa.muestraExcepcion(i.getMessage());
        } 
        catch (java.sql.SQLException e){
            System.out.println(e.getMessage());
            fa.muestraExcepcion(e.getMessage());
        }
        
        
        
    }
    
    

    public java.util.List<Libro> consultarCatalogo(Integer id, String titulo, String isbn, String autor){
        return daoLibros.consultarCatalogo(id, titulo, isbn, autor);
    }

    public Libro consultarLibro(Integer idLibro){
        return daoLibros.consultarLibro(idLibro);
    }
    public java.util.List<Ejemplar> consultarEjemplaresLibro(Integer idLibro){
        return daoLibros.consultarEjemplaresLibro(idLibro);
    }
    public java.util.List<String> obtenerRestoCategorias(Integer idLibro){
        return daoLibros.obtenerRestoCategorias(idLibro);
    }
    public Integer insertarLibro(Libro libro){
       return daoLibros.insertarLibro(libro);
    }
    public void borrarLibro(Integer idLibro){
        daoLibros.borrarLibro(idLibro);
    }
    public void modificarLibro(Libro libro){
         daoLibros.modificarLibro(libro);
    }
    public void modificarCategoriasLibro(Integer idLibro, java.util.List<String> categorias){
       daoLibros.modificarCategoriasLibro(idLibro, categorias);
    }
    public void insertarEjemplarLibro(Integer idLibro, Ejemplar ejemplar){
        daoLibros.insertarEjemplarLibro(idLibro, ejemplar);
    }
    public void borrarEjemplaresLibro(Integer idLibro, java.util.List<Integer> numsEjemplar){
        daoLibros.borrarEjemplaresLibro(idLibro, numsEjemplar);
    }
    public void modificarEjemplarLibro(Integer idLibro, Ejemplar ejemplar){
        daoLibros.modificarEjemplarLibro(idLibro, ejemplar);
    }

    public Usuario validarUsuario(String idUsuario, String clave){
        return daoUsuarios.validarUsuario(idUsuario, clave);
    }
    
    public boolean insertarUsuario(Usuario usuario){
        return daoUsuarios.insertarUsuario(usuario);
    }
    
    public java.util.List<Usuario> buscarUsuarios(String id, String nombre){
        return daoUsuarios.buscarUsuarios(id, nombre);
    }
   
    public boolean modificarUsuario(Usuario usuario) {
        return daoUsuarios.modificarUsuario(usuario);
    }
    
    public boolean borrarUsuario(String idUsuario) {
        return daoUsuarios.borrarUsuario(idUsuario);
    }
   
    public java.util.List<Categoria> consultarCategorias(){
        return daoCategorias.consultarCategorias();
    }
    
    public boolean insertarCategoria(Categoria categoria){
        return daoCategorias.insertarCategoria(categoria);
    }
    
    public boolean borrarCategoria(String nombreCategoria){
        return daoCategorias.borrarCategoria(nombreCategoria);
    }
    
    // Métodos de préstamos
    public int numPrestamosVencidos(String idUsuario) {
        return daoPrestamos.numPrestamosVencidos(idUsuario);
    }
    
    public boolean prestarEjemplar(String idUsuario, Integer idLibro, Integer numEjemplar) {
        return daoPrestamos.prestarEjemplar(idUsuario, idLibro, numEjemplar);
    }
    
    public boolean devolverEjemplar(Integer idLibro, Integer numEjemplar) {
        return daoPrestamos.devolverEjemplar(idLibro, numEjemplar);
    }
    
    public java.util.List<Usuario> buscarUsuariosPrestamo(String id, String nombre) {
        return daoPrestamos.buscarUsuariosPrestamo(id, nombre);
    }
    
    public boolean ejemplarPrestado(Integer idLibro, Integer numEjemplar) {
        return daoPrestamos.ejemplarPrestado(idLibro, numEjemplar);
    }
    
    public java.util.Map<String, Object> obtenerInfoPrestamo(Integer idLibro, Integer numEjemplar) {
        return daoPrestamos.obtenerInfoPrestamo(idLibro, numEjemplar);
    }
    
    public boolean ejemplarTienePrestamos(Integer idLibro, Integer numEjemplar) {
        return daoPrestamos.ejemplarTienePrestamos(idLibro, numEjemplar);
    }

}
