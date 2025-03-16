/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baseDatos;

import aplicacion.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xoel
 */
public class DAOPrestamos extends AbstractDAO {
    
    public DAOPrestamos(Connection conexion, aplicacion.FachadaAplicacion fa) {
        super.setConexion(conexion);
        super.setFachadaAplicacion(fa);
    }
    
    public int numPrestamosVencidos(String idUsuario) {
        int numPrestamosVencidos = 0;
        Connection con;
        PreparedStatement stmPrestamos = null;
        ResultSet rsPrestamos;
        
        con = this.getConexion();
        
        try {
            stmPrestamos = con.prepareStatement(
                "SELECT COUNT(*) as num " +
                "FROM prestamo " +
                "WHERE usuario = ? " +
                "AND fecha_devolucion IS NULL " +
                "AND fecha_prestamo < CURRENT_DATE - INTERVAL '30' DAY");
            
            stmPrestamos.setString(1, idUsuario);
            rsPrestamos = stmPrestamos.executeQuery();
            
            if (rsPrestamos.next()) {
                numPrestamosVencidos = rsPrestamos.getInt("num");
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmPrestamos != null) stmPrestamos.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return numPrestamosVencidos;
    }
    
    public boolean prestarEjemplar(String idUsuario, Integer idLibro, Integer numEjemplar) {
        Connection con;
        PreparedStatement stmPrestamo = null;
        boolean resultado = false;
        
        con = this.getConexion();
        
        try {
            if (numPrestamosVencidos(idUsuario) > 0) {
                this.getFachadaAplicacion().muestraExcepcion("El usuario tiene préstamos vencidos. No se puede realizar un nuevo préstamo.");
                return false;
            }
            
            PreparedStatement stmCheckDisponible = con.prepareStatement(
                "SELECT COUNT(*) as prestado " +
                "FROM prestamo " +
                "WHERE libro = ? AND ejemplar = ? AND fecha_devolucion IS NULL");
            
            stmCheckDisponible.setInt(1, idLibro);
            stmCheckDisponible.setInt(2, numEjemplar);
            ResultSet rsDisponible = stmCheckDisponible.executeQuery();
            
            if (rsDisponible.next() && rsDisponible.getInt("prestado") > 0) {
                this.getFachadaAplicacion().muestraExcepcion("El ejemplar ya está prestado.");
                return false;
            }
            
            stmPrestamo = con.prepareStatement(
                "INSERT INTO prestamo (usuario, libro, ejemplar, fecha_prestamo, fecha_devolucion) " +
                "VALUES (?, ?, ?, CURRENT_DATE, NULL)");
            
            stmPrestamo.setString(1, idUsuario);
            stmPrestamo.setInt(2, idLibro);
            stmPrestamo.setInt(3, numEjemplar);
            
            int filasAfectadas = stmPrestamo.executeUpdate();
            resultado = filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmPrestamo != null) stmPrestamo.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return resultado;
    }
    
    public boolean devolverEjemplar(Integer idLibro, Integer numEjemplar) {
        Connection con;
        PreparedStatement stmDevolucion = null;
        boolean resultado = false;
        
        con = this.getConexion();
        
        try {
            PreparedStatement stmCheck = con.prepareStatement(
                "SELECT COUNT(*) as prestado " +
                "FROM prestamo " +
                "WHERE libro = ? AND ejemplar = ? AND fecha_devolucion IS NULL");
            
            stmCheck.setInt(1, idLibro);
            stmCheck.setInt(2, numEjemplar);
            ResultSet rsCheck = stmCheck.executeQuery();
            
            if (rsCheck.next() && rsCheck.getInt("prestado") == 0) {
                this.getFachadaAplicacion().muestraExcepcion("El ejemplar no está prestado actualmente.");
                return false;
            }
            
            stmDevolucion = con.prepareStatement(
                "UPDATE prestamo " +
                "SET fecha_devolucion = CURRENT_DATE " +
                "WHERE libro = ? AND ejemplar = ? AND fecha_devolucion IS NULL");
            
            stmDevolucion.setInt(1, idLibro);
            stmDevolucion.setInt(2, numEjemplar);
            
            int filasAfectadas = stmDevolucion.executeUpdate();
            resultado = filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmDevolucion != null) stmDevolucion.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return resultado;
    }
    
    public java.util.List<Usuario> buscarUsuariosPrestamo(String id, String nombre) {
        java.util.List<Usuario> resultado = new ArrayList<>();
        Connection con;
        PreparedStatement stmUsuarios = null;
        ResultSet rsUsuarios;
        
        con = this.getConexion();
        
        try {
            StringBuilder consulta = new StringBuilder();
            consulta.append("SELECT u.id_usuario, u.clave, u.nombre, u.direccion, u.email, u.tipo_usuario, ");
            consulta.append("COUNT(CASE WHEN p.fecha_devolucion IS NULL AND p.fecha_prestamo < CURRENT_DATE - INTERVAL '15' DAY THEN 1 END) AS prestamos_vencidos ");
            consulta.append("FROM usuario u ");
            consulta.append("LEFT JOIN prestamo p ON u.id_usuario = p.usuario ");
            consulta.append("WHERE TRUE ");
            
            if (id != null && !id.isEmpty()) {
                consulta.append("AND UPPER(u.id_usuario) LIKE UPPER(?) ");
            }
            
            if (nombre != null && !nombre.isEmpty()) {
                consulta.append("AND UPPER(u.nombre) LIKE UPPER(?) ");
            }
            
            consulta.append("GROUP BY u.id_usuario, u.clave, u.nombre, u.direccion, u.email, u.tipo_usuario ");
            consulta.append("ORDER BY u.id_usuario");
            
            stmUsuarios = con.prepareStatement(consulta.toString());
            
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
                    aplicacion.TipoUsuario.valueOf(rsUsuarios.getString("tipo_usuario"))
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
    
    public boolean ejemplarPrestado(Integer idLibro, Integer numEjemplar) {
        Connection con;
        PreparedStatement stmCheck = null;
        boolean prestado = false;
        
        con = this.getConexion();
        
        try {
            stmCheck = con.prepareStatement(
                "SELECT COUNT(*) as prestado " +
                "FROM prestamo " +
                "WHERE libro = ? AND ejemplar = ? AND fecha_devolucion IS NULL");
            
            stmCheck.setInt(1, idLibro);
            stmCheck.setInt(2, numEjemplar);
            ResultSet rsCheck = stmCheck.executeQuery();
            
            if (rsCheck.next()) {
                prestado = rsCheck.getInt("prestado") > 0;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmCheck != null) stmCheck.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return prestado;
    }
    
    public Map<String, Object> obtenerInfoPrestamo(Integer idLibro, Integer numEjemplar) {
        Connection con;
        PreparedStatement stmPrestamo = null;
        ResultSet rsPrestamo;
        Map<String, Object> infoPrestamo = new HashMap<>();
        
        con = this.getConexion();
        
        try {
            stmPrestamo = con.prepareStatement(
                "SELECT p.usuario, u.nombre, p.fecha_prestamo, " +
                "(p.fecha_prestamo + INTERVAL '30' DAY) as fecha_vencimiento " +
                "FROM prestamo p " +
                "JOIN usuario u ON p.usuario = u.id_usuario " +
                "WHERE p.libro = ? AND p.ejemplar = ? AND p.fecha_devolucion IS NULL");
            
            stmPrestamo.setInt(1, idLibro);
            stmPrestamo.setInt(2, numEjemplar);
            rsPrestamo = stmPrestamo.executeQuery();
            
            if (rsPrestamo.next()) {
                infoPrestamo.put("idUsuario", rsPrestamo.getString("usuario"));
                infoPrestamo.put("nombreUsuario", rsPrestamo.getString("nombre"));
                infoPrestamo.put("fechaPrestamo", rsPrestamo.getDate("fecha_prestamo"));
                infoPrestamo.put("fechaVencimiento", rsPrestamo.getDate("fecha_vencimiento"));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmPrestamo != null) stmPrestamo.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return infoPrestamo;
    }

    public boolean ejemplarTienePrestamos(Integer idLibro, Integer numEjemplar) {
        Connection con;
        PreparedStatement stmConsulta = null;
        ResultSet rsConsulta;
        boolean tienePrestamos = false;
        
        con = this.getConexion();
        
        try {
            stmConsulta = con.prepareStatement(
                "SELECT COUNT(*) as num_prestamos " +
                "FROM prestamo " +
                "WHERE libro = ? AND ejemplar = ?");
            
            stmConsulta.setInt(1, idLibro);
            stmConsulta.setInt(2, numEjemplar);
            rsConsulta = stmConsulta.executeQuery();
            
            if (rsConsulta.next()) {
                tienePrestamos = rsConsulta.getInt("num_prestamos") > 0;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        } finally {
            try {
                if (stmConsulta != null) stmConsulta.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        
        return tienePrestamos;
    }
}
