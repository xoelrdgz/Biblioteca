/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import aplicacion.Usuario;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 *
 * @author xoel
 */
public class ModeloTablaUsuarios extends AbstractTableModel {
    
    private ArrayList<Usuario> usuarios;
    private String[] columnas = {"ID", "Nombre", "Email", "Tipo"};
    
    public ModeloTablaUsuarios() {
        usuarios = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return usuarios.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < usuarios.size()) {
            switch (columnIndex) {
                case 0:
                    return usuarios.get(rowIndex).getIdUsuario();
                case 1:
                    return usuarios.get(rowIndex).getNombre();
                case 2:
                    return usuarios.get(rowIndex).getEmail();
                case 3:
                    return usuarios.get(rowIndex).getTipoUsuario().toString();
                default:
                    return null;
            }
        }
        return null;
    }
    
    public void setFilas(java.util.List<Usuario> usuariosEncontrados) {
        this.usuarios.clear();
        if (usuariosEncontrados != null) {
            this.usuarios.addAll(usuariosEncontrados);
        }
        fireTableDataChanged();
    }
    
    public Usuario obtenerUsuario(int indice) {
        if (indice >= 0 && indice < usuarios.size()) {
            return usuarios.get(indice);
        }
        return null;
    }
}