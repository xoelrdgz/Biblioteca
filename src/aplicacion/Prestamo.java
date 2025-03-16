/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplicacion;

import java.util.Date;

/**
 *
 * @author xoel
 */
public class Prestamo {
    private Usuario usuario;
    private Libro libro;
    private Integer ejemplar;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    
    public Prestamo(Usuario usuario, Libro libro, Integer ejemplar, Date fechaPrestamo, Date fechaDevolucion) {
        this.usuario = usuario;
        this.libro = libro;
        this.ejemplar = ejemplar;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public Libro getLibro() {
        return libro;
    }
    
    public Integer getEjemplar() {
        return ejemplar;
    }
    
    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }
    
    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }
    
    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    
    public boolean isVencido() {
        if (fechaDevolucion != null) {
            return false;
        }
        
        long DIAS_PRESTAMO = 30;
        Date today = new Date();
        long difEnMilisegundos = today.getTime() - fechaPrestamo.getTime();
        long difEnDias = difEnMilisegundos / (1000 * 60 * 60 * 24);
        
        return difEnDias > DIAS_PRESTAMO;
    }
}
