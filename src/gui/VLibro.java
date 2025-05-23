/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VLibro.java
 *
 * Created on 16-feb-2011, 18:17:07
 */

package gui;

import aplicacion.Ejemplar;
import aplicacion.Libro;

import javax.swing.*;

/**
 *
 * @author basesdatos
 */
public class VLibro extends javax.swing.JDialog {

     private Integer idLibro;
     private java.util.List<Integer> ejemplaresBorrados;
     private VPrincipal padre;
     private aplicacion.FachadaAplicacion fa;

    /** Creates new form VLibro */
    public VLibro(java.awt.Frame parent, boolean modal, aplicacion.FachadaAplicacion fa, java.util.List<String> restoCategorias) {
        super(parent, modal);
        this.fa=fa;
        initComponents();
        padre=(VPrincipal) parent;
        btnActualizarCategoriasLibro.setEnabled(false);
        btnActualizarEjemplaresLibro.setEnabled(false);
        btnBorrarLibro.setEnabled(false);
        this.idLibro=null;
        this.ejemplaresBorrados = new java.util.ArrayList<Integer>();

        ModeloListaStrings mListaRC=new ModeloListaStrings();
        lstRestoCategorias.setModel(mListaRC);
        mListaRC.setElementos(restoCategorias);
        if (mListaRC.getSize()>0) {
            lstRestoCategorias.setSelectedIndex(0);
            btnDerecha.setEnabled(true);
        } else btnDerecha.setEnabled(false);
        
        btnIzquierda.setEnabled(false);
        btnBorrarEjemplar.setEnabled(false);
        btnPrestamo.setEnabled(false);
        btnDevolver.setEnabled(false);
        
        // Añadir ActionListener para btnPrestamo
        btnPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrestamoActionPerformed(evt);
            }
        });
        
        // Añadir ActionListener para btnDevolver
        btnDevolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverActionPerformed(evt);
            }
        });
        
        // Inicializar tabla de ejemplares con modelo que muestra información de préstamos
        ModeloTablaEjemplares mTEjemplares = new ModeloTablaEjemplares(fa);
        tablaEjemplares.setModel(mTEjemplares);
        
        // Añadir ListSelectionListener para tablaEjemplares
        tablaEjemplares.getSelectionModel().addListSelectionListener(e -> {
            boolean filaSeleccionada = tablaEjemplares.getSelectedRow() != -1;
            btnBorrarEjemplar.setEnabled(filaSeleccionada);
            btnPrestamo.setEnabled(filaSeleccionada);
            btnDevolver.setEnabled(filaSeleccionada);
        });
    }

    public VLibro(java.awt.Frame parent, boolean modal, aplicacion.FachadaAplicacion fa, Libro libro, java.util.List<String> categorias, java.util.List<String> restoCategorias) {
        super(parent, modal);
        this.fa=fa;
        initComponents();
        padre=(VPrincipal) parent;
        idLibro=libro.getIdLibro();
        textoAno.setText(libro.getAno());
        textoEditorial.setText(libro.getEditorial());
        textoIsbn.setText(libro.getIsbn());
        textoPaginas.setText((libro.getPaginas()).toString());
        textoTitulo.setText(libro.getTitulo());
        textoId.setText(idLibro.toString());

        ModeloListaStrings mListaAutores=new ModeloListaStrings();
        lstAutores.setModel(mListaAutores);
        mListaAutores.setElementos(libro.getAutores());
        if (mListaAutores.getSize()>0){
            lstAutores.setSelectedIndex(0);
            btnBorrarAutor.setEnabled(true);
        } else btnBorrarAutor.setEnabled(false);
        
        ModeloListaStrings mListaRC=new ModeloListaStrings();
        lstRestoCategorias.setModel(mListaRC);
        mListaRC.setElementos(restoCategorias);
        if (mListaRC.getSize()>0) {
            lstRestoCategorias.setSelectedIndex(0);
            btnDerecha.setEnabled(true);
        } else btnDerecha.setEnabled(false);
        
        ModeloListaStrings mListaC=new ModeloListaStrings();
        lstCategoriasLibro.setModel(mListaC);
        mListaC.setElementos(categorias);
        if (mListaC.getSize()>0) {
            lstCategoriasLibro.setSelectedIndex(0);
            btnIzquierda.setEnabled(true);
        } else btnIzquierda.setEnabled(false);

        // Inicializar tabla de ejemplares con modelo que muestra información de préstamos
        ModeloTablaEjemplares mTEjemplares = new ModeloTablaEjemplares(fa);
        tablaEjemplares.setModel(mTEjemplares);
        mTEjemplares.setFilas(libro.getEjemplares());
        
        if (mTEjemplares.getRowCount()>0) {
            tablaEjemplares.setRowSelectionInterval(0, 0);
            btnBorrarEjemplar.setEnabled(true);
            btnPrestamo.setEnabled(true);
            btnDevolver.setEnabled(true);
        } else {
            btnBorrarEjemplar.setEnabled(false);
            btnPrestamo.setEnabled(false);
            btnDevolver.setEnabled(false);
        }
        
        this.ejemplaresBorrados = new java.util.ArrayList<Integer>();
        
        // Añadir ActionListener para btnPrestamo
        btnPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrestamoActionPerformed(evt);
            }
        });
        
        // Añadir ActionListener para btnDevolver
        btnDevolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverActionPerformed(evt);
            }
        });
        
        // Añadir ListSelectionListener para tablaEjemplares
        tablaEjemplares.getSelectionModel().addListSelectionListener(e -> {
            boolean filaSeleccionada = tablaEjemplares.getSelectedRow() != -1;
            btnBorrarEjemplar.setEnabled(filaSeleccionada);
            
            // Habilitar/deshabilitar los botones de préstamo y devolución según el estado del ejemplar
            if (filaSeleccionada) {
                Ejemplar ejemplar = mTEjemplares.obtenerEjemplar(tablaEjemplares.getSelectedRow());
                if (ejemplar.getNumEjemplar() != null && ejemplar.getLibro() != null) {
                    boolean prestado = fa.ejemplarPrestado(idLibro, ejemplar.getNumEjemplar());
                    btnPrestamo.setEnabled(!prestado);  // Solo permitir prestar si no está prestado
                    btnDevolver.setEnabled(prestado);   // Solo permitir devolver si está prestado
                } else {
                    btnPrestamo.setEnabled(false);
                    btnDevolver.setEnabled(false);
                }
            } else {
                btnPrestamo.setEnabled(false);
                btnDevolver.setEnabled(false);
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLibro = new javax.swing.JTabbedPane();
        panelGeneralAutores = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        textoTitulo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        textoIsbn = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        textoEditorial = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        textoPaginas = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        textoAno = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstAutores = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        textoNuevoAutor = new javax.swing.JTextField();
        btnNuevoAutor = new javax.swing.JButton();
        btnBorrarAutor = new javax.swing.JButton();
        btnActualizarLibro = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        textoId = new javax.swing.JTextField();
        panelCategorias = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstRestoCategorias = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstCategoriasLibro = new javax.swing.JList();
        btnDerecha = new javax.swing.JButton();
        btnIzquierda = new javax.swing.JButton();
        btnActualizarCategoriasLibro = new javax.swing.JButton();
        panelEjemplares = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaEjemplares = new javax.swing.JTable();
        btnBorrarEjemplar = new javax.swing.JButton();
        btnNuevoEjemplar = new javax.swing.JButton();
        btnActualizarEjemplaresLibro = new javax.swing.JButton();
        btnPrestamo = new javax.swing.JButton();
        btnDevolver = new javax.swing.JButton();
        btnBorrarLibro = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestión de libros");
        setResizable(false);

        jLabel2.setText("Título:");

        jLabel3.setText("Isbn:");

        jLabel4.setText("Editorial:");

        jLabel5.setText("Páginas:");

        jLabel6.setText("Año:");

        lstAutores.setModel(new ModeloListaStrings());
        lstAutores.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lstAutores);

        jLabel7.setText("Autores:");

        btnNuevoAutor.setText("Añadir");
        btnNuevoAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoAutorActionPerformed(evt);
            }
        });

        btnBorrarAutor.setText("Borrar");
        btnBorrarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarAutorActionPerformed(evt);
            }
        });

        btnActualizarLibro.setText("Actualizar");
        btnActualizarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarLibroActionPerformed(evt);
            }
        });

        jLabel1.setText("Id:");

        textoId.setEditable(false);
        textoId.setEnabled(false);

        javax.swing.GroupLayout panelGeneralAutoresLayout = new javax.swing.GroupLayout(panelGeneralAutores);
        panelGeneralAutores.setLayout(panelGeneralAutoresLayout);
        panelGeneralAutoresLayout.setHorizontalGroup(
            panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralAutoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralAutoresLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                    .addGroup(panelGeneralAutoresLayout.createSequentialGroup()
                        .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralAutoresLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textoIsbn, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralAutoresLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textoPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textoAno)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textoEditorial, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addGroup(panelGeneralAutoresLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textoId, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 126, Short.MAX_VALUE))))
                    .addComponent(jLabel7)
                    .addGroup(panelGeneralAutoresLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnBorrarAutor, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGeneralAutoresLayout.createSequentialGroup()
                                .addComponent(btnNuevoAutor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textoNuevoAutor, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                            .addComponent(btnActualizarLibro))))
                .addContainerGap())
        );
        panelGeneralAutoresLayout.setVerticalGroup(
            panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralAutoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textoTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textoIsbn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(textoEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(textoPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(textoAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(textoId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGeneralAutoresLayout.createSequentialGroup()
                        .addGroup(panelGeneralAutoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevoAutor)
                            .addComponent(textoNuevoAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnBorrarAutor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnActualizarLibro))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelLibro.addTab("Libro", panelGeneralAutores);

        jLabel8.setText("Categorías disponibles");

        jLabel9.setText("Categorías del libro");

        lstRestoCategorias.setModel(new ModeloListaStrings());
        jScrollPane2.setViewportView(lstRestoCategorias);

        lstCategoriasLibro.setModel(new ModeloListaStrings());
        jScrollPane3.setViewportView(lstCategoriasLibro);

        btnDerecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/flechaD.jpg"))); // NOI18N
        btnDerecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDerechaActionPerformed(evt);
            }
        });

        btnIzquierda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/flechaI.jpg"))); // NOI18N
        btnIzquierda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIzquierdaActionPerformed(evt);
            }
        });

        btnActualizarCategoriasLibro.setText("Actualizar");
        btnActualizarCategoriasLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarCategoriasLibroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCategoriasLayout = new javax.swing.GroupLayout(panelCategorias);
        panelCategorias.setLayout(panelCategoriasLayout);
        panelCategoriasLayout.setHorizontalGroup(
            panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCategoriasLayout.createSequentialGroup()
                .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCategoriasLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel8))
                    .addGroup(panelCategoriasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCategoriasLayout.createSequentialGroup()
                        .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnDerecha, 0, 0, Short.MAX_VALUE)
                            .addComponent(btnIzquierda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCategoriasLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(65, 65, 65))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCategoriasLayout.createSequentialGroup()
                .addContainerGap(387, Short.MAX_VALUE)
                .addComponent(btnActualizarCategoriasLibro)
                .addContainerGap())
        );
        panelCategoriasLayout.setVerticalGroup(
            panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCategoriasLayout.createSequentialGroup()
                .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCategoriasLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(btnDerecha, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnIzquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCategoriasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizarCategoriasLibro)
                .addContainerGap())
        );

        panelLibro.addTab("Categorías", panelCategorias);

        tablaEjemplares.setModel(new ModeloTablaEjemplares());
        jScrollPane4.setViewportView(tablaEjemplares);

        btnBorrarEjemplar.setText("Borrar");
        btnBorrarEjemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarEjemplarActionPerformed(evt);
            }
        });

        btnNuevoEjemplar.setText("Nuevo");
        btnNuevoEjemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoEjemplarActionPerformed(evt);
            }
        });

        btnActualizarEjemplaresLibro.setText("Actualizar");
        btnActualizarEjemplaresLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarEjemplaresLibroActionPerformed(evt);
            }
        });

        btnPrestamo.setText("Prestamo");
        btnPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrestamoActionPerformed(evt);
            }
        });

        btnDevolver.setText("Devolver");
        btnDevolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEjemplaresLayout = new javax.swing.GroupLayout(panelEjemplares);
        panelEjemplares.setLayout(panelEjemplaresLayout);
        panelEjemplaresLayout.setHorizontalGroup(
            panelEjemplaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
            .addGroup(panelEjemplaresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevoEjemplar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrarEjemplar)
                .addGap(18, 18, 18)
                .addComponent(btnPrestamo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDevolver)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizarEjemplaresLibro)
                .addContainerGap())
        );
        panelEjemplaresLayout.setVerticalGroup(
            panelEjemplaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEjemplaresLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEjemplaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevoEjemplar)
                    .addComponent(btnBorrarEjemplar)
                    .addComponent(btnActualizarEjemplaresLibro)
                    .addComponent(btnPrestamo)
                    .addComponent(btnDevolver))
                .addGap(24, 24, 24))
        );

        panelLibro.addTab("Ejemplares", panelEjemplares);

        btnBorrarLibro.setText("Borrar Libro");
        btnBorrarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarLibroActionPerformed(evt);
            }
        });

        btnSalir.setText("Salír");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBorrarLibro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 335, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBorrarLibro)
                    .addComponent(btnSalir))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        panelLibro.getAccessibleContext().setAccessibleName("Libro");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        padre.buscarLibros();
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoAutorActionPerformed
        // TODO add your handling code here:
     ModeloListaStrings ma;
     ma=(ModeloListaStrings) lstAutores.getModel();

     if ((textoNuevoAutor.getText()!=null) && !(textoNuevoAutor.getText().isEmpty())) {
         ma.nuevoElemento(textoNuevoAutor.getText());
         lstAutores.setSelectedIndex(ma.getSize()-1);
         btnBorrarAutor.setEnabled(true);
     }
    }//GEN-LAST:event_btnNuevoAutorActionPerformed

    private void btnBorrarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarAutorActionPerformed
        // TODO add your handling code here:
     ModeloListaStrings ma;
     ma=(ModeloListaStrings) lstAutores.getModel();

     ma.borrarElemento(lstAutores.getSelectedIndex());
     if (ma.getSize()==0) btnBorrarAutor.setEnabled(false);
     else lstAutores.setSelectedIndex(0);
    }//GEN-LAST:event_btnBorrarAutorActionPerformed

    private void btnDerechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDerechaActionPerformed
        // TODO add your handling code here:
     ModeloListaStrings mRC;
     ModeloListaStrings mC;

     mRC = (ModeloListaStrings) lstRestoCategorias.getModel();
     mC = (ModeloListaStrings) lstCategoriasLibro.getModel();
     mC.nuevoElemento(mRC.getElementAt(lstRestoCategorias.getSelectedIndex()));
     mRC.borrarElemento(lstRestoCategorias.getSelectedIndex());
     if (mRC.getSize()==0) btnDerecha.setEnabled(false);
     else lstRestoCategorias.setSelectedIndex(0);
     lstCategoriasLibro.setSelectedIndex(mC.getSize()-1);
     btnIzquierda.setEnabled(true);
    }//GEN-LAST:event_btnDerechaActionPerformed

    private void btnIzquierdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIzquierdaActionPerformed
        // TODO add your handling code here:
     ModeloListaStrings mRC;
     ModeloListaStrings mC;

     mRC = (ModeloListaStrings) lstRestoCategorias.getModel();
     mC = (ModeloListaStrings) lstCategoriasLibro.getModel();
     mRC.nuevoElemento(mC.getElementAt(lstCategoriasLibro.getSelectedIndex()));
     mC.borrarElemento(lstCategoriasLibro.getSelectedIndex());
     if (mC.getSize()==0) btnIzquierda.setEnabled(false);
     else lstCategoriasLibro.setSelectedIndex(0);
     lstRestoCategorias.setSelectedIndex(mRC.getSize()-1);
     btnDerecha.setEnabled(true);
    }//GEN-LAST:event_btnIzquierdaActionPerformed

    private void btnNuevoEjemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoEjemplarActionPerformed
        // TODO add your handling code here:
        ModeloTablaEjemplares me;
        Ejemplar e;

        me=(ModeloTablaEjemplares) tablaEjemplares.getModel();
        e=new Ejemplar(null, null, null, null);
        me.nuevoEjemplar(e);
        tablaEjemplares.setRowSelectionInterval(me.getRowCount()-1, me.getRowCount()-1);
        btnBorrarEjemplar.setEnabled(true);

    }//GEN-LAST:event_btnNuevoEjemplarActionPerformed

    private void btnBorrarEjemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarEjemplarActionPerformed
        // TODO add your handling code here:
        ModeloTablaEjemplares me;
        me = (ModeloTablaEjemplares) tablaEjemplares.getModel();
        int filaSeleccionada = tablaEjemplares.getSelectedRow();
        
        if (filaSeleccionada != -1) {
            Ejemplar ejemplar = me.obtenerEjemplar(filaSeleccionada);
            if (ejemplar.getNumEjemplar() != null) {
                // Verificar si el ejemplar tiene préstamos asociados (actuales o históricos)
                if (fa.ejemplarTienePrestamos(idLibro, ejemplar.getNumEjemplar())) {
                    fa.muestraExcepcion("No se puede borrar el ejemplar porque tiene préstamos asociados.");
                    return;
                }
                ejemplaresBorrados.add(ejemplar.getNumEjemplar());
            }
            
            me.borrarEjemplar(filaSeleccionada);
            if (me.getRowCount() == 0) {
                btnBorrarEjemplar.setEnabled(false);
                btnPrestamo.setEnabled(false);
                btnDevolver.setEnabled(false);
            } else {
                tablaEjemplares.setRowSelectionInterval(0, 0);
            }
        }
    }//GEN-LAST:event_btnBorrarEjemplarActionPerformed

    private void btnBorrarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarLibroActionPerformed
        // TODO add your handling code here:
    fa.borrarLibro(idLibro);
    padre.buscarLibros();
    this.dispose();
    }//GEN-LAST:event_btnBorrarLibroActionPerformed

    private void btnActualizarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarLibroActionPerformed
        // TODO add your handling code here:
        Libro l;
        l=new Libro(idLibro, textoTitulo.getText(), textoIsbn.getText(), textoEditorial.getText(),
                    Integer.parseInt(textoPaginas.getText()), textoAno.getText());
        ModeloListaStrings ma= (ModeloListaStrings)lstAutores.getModel();
        l.setAutores(ma.getElementos());
        idLibro=fa.actualizarLibro(l);
        textoId.setText(idLibro.toString());
        btnActualizarCategoriasLibro.setEnabled(true);
        btnActualizarEjemplaresLibro.setEnabled(true);
        btnBorrarLibro.setEnabled(true);
    }//GEN-LAST:event_btnActualizarLibroActionPerformed

    private void btnActualizarCategoriasLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarCategoriasLibroActionPerformed
        // TODO add your handling code here:
      ModeloListaStrings ma= (ModeloListaStrings)lstCategoriasLibro.getModel();
      fa.actualizarCategoriasLibro(idLibro, ma.getElementos());
    }//GEN-LAST:event_btnActualizarCategoriasLibroActionPerformed

    private void btnActualizarEjemplaresLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarEjemplaresLibroActionPerformed
        // TODO add your handling code here:
      java.util.List<Ejemplar> ejemplares;
      ModeloTablaEjemplares me= (ModeloTablaEjemplares)tablaEjemplares.getModel();
      ejemplares=fa.actualizarEjemplaresLibro(idLibro, me.getFilas(), ejemplaresBorrados);
      me.setFilas(ejemplares);
      if (me.getRowCount()>0) {
            tablaEjemplares.setRowSelectionInterval(0, 0);
            btnBorrarEjemplar.setEnabled(true);
        } else btnBorrarEjemplar.setEnabled(false);
    }//GEN-LAST:event_btnActualizarEjemplaresLibroActionPerformed

    private void btnPrestamoActionPerformed(java.awt.event.ActionEvent evt) {
        ModeloTablaEjemplares me = (ModeloTablaEjemplares) tablaEjemplares.getModel();
        int filaSeleccionada = tablaEjemplares.getSelectedRow();
        
        if (filaSeleccionada != -1) {
            Ejemplar ejemplar = me.obtenerEjemplar(filaSeleccionada);
            
            // Comprobar si el ejemplar ya está prestado
            if (fa.ejemplarPrestado(idLibro, ejemplar.getNumEjemplar())) {
                fa.muestraExcepcion("Este ejemplar ya está prestado. No se puede realizar un nuevo préstamo.");
                return;
            }
            
            // Mostrar la ventana de préstamos
            fa.mostrarVentanaPrestamos(idLibro, ejemplar.getNumEjemplar());
            
            // Actualizar la tabla de ejemplares después del posible préstamo
            if (idLibro != null) {
                Libro libro = fa.consultarLibro(idLibro);
                if (libro != null) {
                    me.setFilas(libro.getEjemplares());
                    tablaEjemplares.repaint();
                    
                    // Actualizar estado de botones
                    if (tablaEjemplares.getSelectedRow() != -1) {
                        ejemplar = me.obtenerEjemplar(tablaEjemplares.getSelectedRow());
                        boolean prestado = fa.ejemplarPrestado(idLibro, ejemplar.getNumEjemplar());
                        btnPrestamo.setEnabled(!prestado);
                        btnDevolver.setEnabled(prestado);
                    }
                }
            }
        }
    }
    
    private void btnDevolverActionPerformed(java.awt.event.ActionEvent evt) {
        ModeloTablaEjemplares me = (ModeloTablaEjemplares) tablaEjemplares.getModel();
        int filaSeleccionada = tablaEjemplares.getSelectedRow();
        
        if (filaSeleccionada != -1) {
            Ejemplar ejemplar = me.obtenerEjemplar(filaSeleccionada);
            
            // Comprobar si el ejemplar está prestado
            if (!fa.ejemplarPrestado(idLibro, ejemplar.getNumEjemplar())) {
                fa.muestraExcepcion("Este ejemplar no está prestado. No se puede realizar una devolución.");
                return;
            }
            
            // Realizar la devolución
            if (fa.devolverEjemplar(idLibro, ejemplar.getNumEjemplar())) {
                JOptionPane.showMessageDialog(this, 
                        "Libro devuelto correctamente", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar la tabla de ejemplares después de la devolución
                if (idLibro != null) {
                    Libro libro = fa.consultarLibro(idLibro);
                    if (libro != null) {
                        me.setFilas(libro.getEjemplares());
                        tablaEjemplares.repaint();
                        
                        // Actualizar estado de botones
                        if (tablaEjemplares.getSelectedRow() != -1) {
                            ejemplar = me.obtenerEjemplar(tablaEjemplares.getSelectedRow());
                            boolean prestado = fa.ejemplarPrestado(idLibro, ejemplar.getNumEjemplar());
                            btnPrestamo.setEnabled(!prestado);
                            btnDevolver.setEnabled(prestado);
                        }
                    }
                }
            }
        }
    }

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarCategoriasLibro;
    private javax.swing.JButton btnActualizarEjemplaresLibro;
    private javax.swing.JButton btnActualizarLibro;
    private javax.swing.JButton btnBorrarAutor;
    private javax.swing.JButton btnBorrarEjemplar;
    private javax.swing.JButton btnBorrarLibro;
    private javax.swing.JButton btnDerecha;
    private javax.swing.JButton btnDevolver;
    private javax.swing.JButton btnIzquierda;
    private javax.swing.JButton btnNuevoAutor;
    private javax.swing.JButton btnNuevoEjemplar;
    private javax.swing.JButton btnPrestamo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList lstAutores;
    private javax.swing.JList lstCategoriasLibro;
    private javax.swing.JList lstRestoCategorias;
    private javax.swing.JPanel panelCategorias;
    private javax.swing.JPanel panelEjemplares;
    private javax.swing.JPanel panelGeneralAutores;
    private javax.swing.JTabbedPane panelLibro;
    private javax.swing.JTable tablaEjemplares;
    private javax.swing.JTextField textoAno;
    private javax.swing.JTextField textoEditorial;
    private javax.swing.JTextField textoId;
    private javax.swing.JTextField textoIsbn;
    private javax.swing.JTextField textoNuevoAutor;
    private javax.swing.JTextField textoPaginas;
    private javax.swing.JTextField textoTitulo;
    // End of variables declaration//GEN-END:variables

}
