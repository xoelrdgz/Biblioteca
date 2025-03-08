/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui;

import aplicacion.Categoria;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author xoel
 */
public class VCategorias extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VCategorias.class.getName());
    private aplicacion.FachadaAplicacion fa;
    private List<Categoria> categorias;
    private DefaultListModel<String> modeloLista;
    private boolean modoEdicion;  // Indica si estamos editando/añadiendo o visualizando

    /**
     * Creates new form VCategorias
     */
    public VCategorias(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /**
     * Creates new form VCategorias with application facade
     */
    public VCategorias(java.awt.Frame parent, boolean modal, aplicacion.FachadaAplicacion fa) {
        super(parent, modal);
        this.fa = fa;
        this.modoEdicion = false;
        initComponents();
        
        // Inicializar y configurar el modelo de lista
        modeloLista = new DefaultListModel<>();
        listaCategorias.setModel(modeloLista);
        
        // Configurar el evento de selección
        listaCategorias.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarDetallesCategoria();
                }
            }
        });
        
        // Configurar acción para el botón Borrar
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        
        // Configurar acción para el botón Salir
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        
        // Cargar las categorías
        cargarCategorias();
        
        // Configurar campos como no editables inicialmente
        establecerModoEdicion(false);
    }

    /**
     * Carga las categorías desde la base de datos
     */
    private void cargarCategorias() {
        try {
            // Obtener categorías desde la fachada
            categorias = fa.getCategorias();
            
            // Limpiar el modelo actual
            modeloLista.clear();
            
            // Poblar el modelo con las categorías
            for (Categoria c : categorias) {
                modeloLista.addElement(c.getNombre());
            }
            
            // Si hay categorías, seleccionar la primera
            if (!categorias.isEmpty()) {
                listaCategorias.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            logger.severe("Error al cargar las categorías: " + ex.getMessage());
            fa.muestraExcepcion("Error al cargar las categorías: " + ex.getMessage());
        }
    }
    
    /**
     * Muestra los detalles de la categoría seleccionada
     */
    private void mostrarDetallesCategoria() {
        int indiceSeleccionado = listaCategorias.getSelectedIndex();
        if (indiceSeleccionado >= 0 && indiceSeleccionado < categorias.size()) {
            Categoria categoriaSeleccionada = categorias.get(indiceSeleccionado);
            campoNombre.setText(categoriaSeleccionada.getNombre());
            jTextPane1.setText(categoriaSeleccionada.getDescripcion());
            btnBorrar.setEnabled(true);
        } else {
            campoNombre.setText("");
            jTextPane1.setText("");
            btnBorrar.setEnabled(false);
        }
    }
    
    /**
     * Establece el modo de edición o visualización
     * @param modo true para edición, false para visualización
     */
    private void establecerModoEdicion(boolean modo) {
        modoEdicion = modo;
        
        // En modo añadir/editar, los campos son editables y los botones cambian
        campoNombre.setEditable(modo);
        jTextPane1.setEditable(modo);
        
        // Cambiar el texto del botón Añadir según el modo
        if (modo) {
            btnAnadir.setText("Guardar");
            btnBorrar.setText("Cancelar");
            listaCategorias.setEnabled(false);
        } else {
            btnAnadir.setText("Añadir");
            btnBorrar.setText("Borrar");
            listaCategorias.setEnabled(true);
        }
    }
    
    /**
     * Limpia los campos para una nueva categoría
     */
    private void prepararNuevaCategoria() {
        establecerModoEdicion(true);
        listaCategorias.clearSelection();
        campoNombre.setText("");
        jTextPane1.setText("");
        campoNombre.requestFocus();
    }
    
    /**
     * Guarda una nueva categoría
     */
    private void guardarNuevaCategoria() {
        String nombre = campoNombre.getText().trim();
        String descripcion = jTextPane1.getText().trim();
        
        if (nombre.isEmpty()) {
            fa.muestraExcepcion("El nombre de la categoría no puede estar vacío");
            return;
        }
        
        Categoria nuevaCategoria = new Categoria(nombre, descripcion);
        
        if (fa.insertarCategoria(nuevaCategoria)) {
            establecerModoEdicion(false);
            cargarCategorias();
            
            // Seleccionar la categoría recién añadida
            for (int i = 0; i < modeloLista.getSize(); i++) {
                if (modeloLista.getElementAt(i).equals(nombre)) {
                    listaCategorias.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    /**
     * Acción para el botón Borrar/Cancelar
     */
    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (modoEdicion) {
            // Estamos en modo edición, cancelar la operación
            establecerModoEdicion(false);
            mostrarDetallesCategoria();
        } else {
            // Borrar la categoría seleccionada
            int indiceSeleccionado = listaCategorias.getSelectedIndex();
            if (indiceSeleccionado >= 0) {
                String nombreCategoria = categorias.get(indiceSeleccionado).getNombre();
                
                if (fa.borrarCategoria(nombreCategoria)) {
                    cargarCategorias();
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {

        textoCategorias = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaCategorias = new javax.swing.JList<>();
        textoNombre = new javax.swing.JLabel();
        textoDescripcion = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        campoNombre = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        btnAnadir = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestion de categorias");
        setResizable(false);

        textoCategorias.setText("Categorias");

        listaCategorias.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listaCategorias);

        textoNombre.setText("Nombre:");

        textoDescripcion.setText("Descripcion:");

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(campoNombre);

        jScrollPane3.setViewportView(jTextPane1);

        btnAnadir.setText("Añadir");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (modoEdicion) {
                    // Estamos en modo edición/añadir, guardar la nueva categoría
                    guardarNuevaCategoria();
                } else {
                    // Pasamos a modo añadir
                    prepararNuevaCategoria();
                }
            }
        });

        btnBorrar.setText("Borrar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAnadir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBorrar))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textoDescripcion)
                                .addGap(59, 139, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textoNombre)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2))
                            .addComponent(jScrollPane3)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSalir))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(textoCategorias)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textoCategorias)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(textoNombre)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(textoDescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBorrar)
                            .addComponent(btnAnadir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addComponent(btnSalir)))
                .addContainerGap())
        );

        pack();
    }

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                VCategorias dialog = new VCategorias(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JTextPane campoNombre;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JList<String> listaCategorias;
    private javax.swing.JLabel textoCategorias;
    private javax.swing.JLabel textoDescripcion;
    private javax.swing.JLabel textoNombre;
    // End of variables declaration
}
