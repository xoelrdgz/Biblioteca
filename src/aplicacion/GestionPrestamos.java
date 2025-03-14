/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplicacion;

import gui.FachadaGui;
import baseDatos.FachadaBaseDatos;
import java.util.ArrayList;

/**
 *
 * @author xoel
 */
public class GestionPrestamos {
    private FachadaGui fgui;
    private FachadaBaseDatos fbd;
    
    public GestionPrestamos(FachadaGui fgui, FachadaBaseDatos fbd) {
        this.fgui = fgui;
        this.fbd = fbd;
    }
    
    public int numPrestamosVencidos(String idUsuario) {
        return fbd.numPrestamosVencidos(idUsuario);
    }
    
    public boolean prestarEjemplar(String idUsuario, Integer idLibro, Integer numEjemplar) {
        // Validaciones básicas
        if (idUsuario == null || idUsuario.isEmpty()) {
            fgui.muestraExcepcion("Debe seleccionar un usuario para el préstamo.");
            return false;
        }
        
        if (idLibro == null || numEjemplar == null) {
            fgui.muestraExcepcion("Debe seleccionar un ejemplar para prestar.");
            return false;
        }
        
        // Verificar que el usuario no tenga préstamos vencidos
        if (numPrestamosVencidos(idUsuario) > 0) {
            fgui.muestraExcepcion("El usuario tiene préstamos vencidos. No puede realizar nuevos préstamos.");
            return false;
        }
        
        // Verificar que el ejemplar no esté ya prestado
        if (fbd.ejemplarPrestado(idLibro, numEjemplar)) {
            fgui.muestraExcepcion("El ejemplar seleccionado ya está prestado.");
            return false;
        }
        
        // Realizar el préstamo
        return fbd.prestarEjemplar(idUsuario, idLibro, numEjemplar);
    }
    
    public boolean devolverEjemplar(Integer idLibro, Integer numEjemplar) {
        // Validaciones básicas
        if (idLibro == null || numEjemplar == null) {
            fgui.muestraExcepcion("Debe seleccionar un ejemplar para devolver.");
            return false;
        }
        
        // Verificar que el ejemplar esté prestado
        if (!fbd.ejemplarPrestado(idLibro, numEjemplar)) {
            fgui.muestraExcepcion("El ejemplar seleccionado no está prestado.");
            return false;
        }
        
        // Realizar la devolución
        return fbd.devolverEjemplar(idLibro, numEjemplar);
    }
    
    public void mostrarVentanaPrestamos(Integer idLibro, Integer numEjemplar) {
        fgui.mostrarVentanaPrestamos(idLibro, numEjemplar);
    }
    
    public java.util.List<Usuario> buscarUsuariosPrestamo(String id, String nombre) {
        return fbd.buscarUsuariosPrestamo(id, nombre);
    }
    
    public boolean ejemplarTienePrestamos(Integer idLibro, Integer numEjemplar) {
        return fbd.ejemplarTienePrestamos(idLibro, numEjemplar);
    }
}
