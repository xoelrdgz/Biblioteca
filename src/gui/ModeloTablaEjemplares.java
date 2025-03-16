/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import aplicacion.Ejemplar;

import javax.swing.table.AbstractTableModel;
/**
 *
 * @author basesdatos
 */
public class ModeloTablaEjemplares extends AbstractTableModel{
    private java.util.List<Ejemplar> ejemplares;
    private aplicacion.FachadaAplicacion fa;

    public ModeloTablaEjemplares(){
        this.ejemplares=new java.util.ArrayList<Ejemplar>();
        this.fa = null;
    }

    public ModeloTablaEjemplares(aplicacion.FachadaAplicacion fa){
        this.ejemplares=new java.util.ArrayList<Ejemplar>();
        this.fa = fa;
    }

    public int getColumnCount (){
        return 5;
    }

    public int getRowCount(){
        return ejemplares.size();
    }

    @Override
    public String getColumnName(int col){
        String nombre="";

        switch (col){
            case 0: nombre= "Id"; break;
            case 1: nombre= "Localizador"; break;
            case 2: nombre="Año de compra"; break;
            case 3: nombre="Estado"; break;
            case 4: nombre="Usuario/Fecha venc."; break;
        }
        return nombre;
    }

    @Override
    public Class getColumnClass(int col){
        Class clase=null;

        switch (col){
            case 0: clase= java.lang.Integer.class; break;
            case 1: clase= java.lang.String.class; break;
            case 2: clase=java.lang.String.class; break;
            case 3: clase=java.lang.String.class; break;
            case 4: clase=java.lang.String.class; break;
        }
        return clase;
    }

    @Override
    public boolean isCellEditable(int row, int col){
       return col>0 && col<3; // Solo son editables el localizador y año de compra
    }

    public Object getValueAt(int row, int col){
        Object resultado=null;

        switch (col){
            case 0: resultado= ejemplares.get(row).getNumEjemplar(); break;
            case 1: resultado= ejemplares.get(row).getLocalizador(); break;
            case 2: resultado=ejemplares.get(row).getAnoCompra();break;
            case 3: 
                if (fa != null && ejemplares.get(row).getNumEjemplar() != null && ejemplares.get(row).getLibro() != null) {
                    boolean prestado = fa.ejemplarPrestado(ejemplares.get(row).getLibro().getIdLibro(), 
                                                          ejemplares.get(row).getNumEjemplar());
                    resultado = prestado ? "Prestado" : "Disponible";
                } else {
                    resultado = "Desconocido";
                }
                break;
            case 4:
                if (fa != null && ejemplares.get(row).getNumEjemplar() != null && ejemplares.get(row).getLibro() != null) {
                    boolean prestado = fa.ejemplarPrestado(ejemplares.get(row).getLibro().getIdLibro(), 
                                                         ejemplares.get(row).getNumEjemplar());
                    if (prestado) {
                        // Obtener información detallada del préstamo
                        java.util.Map<String, Object> infoPrestamo = fa.obtenerInfoPrestamo(
                            ejemplares.get(row).getLibro().getIdLibro(),
                            ejemplares.get(row).getNumEjemplar()
                        );
                        
                        if (!infoPrestamo.isEmpty()) {
                            String idUsuario = (String) infoPrestamo.get("idUsuario");
                            String nombreUsuario = (String) infoPrestamo.get("nombreUsuario");
                            java.util.Date fechaVencimiento = (java.util.Date) infoPrestamo.get("fechaVencimiento");
                            
                            // Formatear la fecha de vencimiento
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                            String fechaVenc = sdf.format(fechaVencimiento);
                            
                            resultado = idUsuario + " - " + nombreUsuario + " (Venc: " + fechaVenc + ")";
                        } else {
                            resultado = "Info no disponible";
                        }
                    } else {
                        resultado = "-";
                    }
                } else {
                    resultado = "-";
                }
                break;
        }
        return resultado;
    }

    @Override
    public void setValueAt(Object v, int row, int col){
        switch (col){
            case 1: ejemplares.get(row).setLocalizador((String) v); break;
            case 2: ejemplares.get(row).setAnoCompra((String) v);break;
        }
    }

    public void setFachadaAplicacion(aplicacion.FachadaAplicacion fa) {
        this.fa = fa;
    }

    public void setFilas(java.util.List<Ejemplar> ejemplares){
        this.ejemplares=ejemplares;
        fireTableDataChanged();
    }

    public void nuevoEjemplar(Ejemplar e){
        this.ejemplares.add(e);
        fireTableRowsInserted(this.ejemplares.size()-1, this.ejemplares.size()-1);
    }

    public void borrarEjemplar(int indice){
        this.ejemplares.remove(indice);
        fireTableRowsDeleted(indice, indice);
    }

    public java.util.List<Ejemplar> getFilas(){
        return this.ejemplares;
    }

    public Ejemplar obtenerEjemplar(int i){
        return this.ejemplares.get(i);
    }
}
