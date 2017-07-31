package es.charlye.coches.Frames;


import es.charlye.coches.DAO.DAOManager;
import es.charlye.coches.DAO.MariaDB.MariaDBDAOManager;
import es.charlye.coches.Exception.DAOException;
import es.charlye.coches.Modelo.Vehiculo;
import es.charlye.coches.TableModel.AlarmTableModel;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.GroupLayout.Alignment;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;

import java.util.Calendar;
import java.util.Locale;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class JFrameMain extends JFrame {

	private static final long serialVersionUID = 1524105002819671891L;
	private static Vehiculo vehiculo=null;
	private JPanel contentPane;
	private JTable table;
	private AlarmTableModel model;
	private JTextField txtID;
	private JTextField txtModelo;
	private JTextField txtPropietario;
	private JTextField txtMarca;
	private JTextField txtUser;
	private JFormattedTextField txtRecambios;
	private JFormattedTextField txtCobrado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DAOManager manager=new MariaDBDAOManager("localhost", "coches", "coches", "Coches");
					JFrameMain main=new JFrameMain(manager,"root");
					main.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JFrameMain(DAOManager manager,String usuario) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(JFrameAutenticator.class.getResource("/es/charlye/coches/Resources/ico_taller1.png")));
		setTitle("Principal - "+usuario);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 785, 498);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnInicio = new JMenu("Inicio");
		menuBar.add(mnInicio);
		
		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(mntmSalir, "Seguro que quieres Salir.","Salir", JOptionPane.YES_NO_OPTION)==0)
					System.exit(1);
			}
		});
		
		JMenuItem mntmCerrarSesion = new JMenuItem("Cerrar sesión");
		mntmCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(JOptionPane.showConfirmDialog(mntmSalir, "Seguro que quieres Cerrar Sesión.","Cerrar sesión", JOptionPane.YES_NO_OPTION)==0){	
						JFrameAutenticator frame = new JFrameAutenticator(manager);
						frame.setLocationRelativeTo(null);
						frame.setVisible(true);
						dispose();
					}
				} catch (Exception arg0) {
					arg0.printStackTrace();
				}
			}
		});
		mnInicio.add(mntmCerrarSesion);
		mnInicio.add(mntmSalir);
		
		JMenu mnEditor = new JMenu("Editor");
		mnEditor.setEnabled(false);
		menuBar.add(mnEditor);
		
		JMenu mnCuenta = new JMenu("Cuenta");
		menuBar.add(mnCuenta);
		
		JMenuItem mntmCambiarContrasea = new JMenuItem("Cambiar Contraseña");
		mnCuenta.add(mntmCambiarContrasea);
		
		JMenuItem mntmEliminarCuenta = new JMenuItem("Eliminar cuenta");
		mntmEliminarCuenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(mntmSalir, "¿Seguro que quieres eliminar la cuenta?","Eliminar cuenta",JOptionPane.YES_NO_OPTION)==0)
					try {
						JDialogDeleteUser dialog = new JDialogDeleteUser(manager,usuario);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
							if(manager.getUsuarioDAO().existe(usuario))
								dispose();
					} catch (DAOException e1) {
						e1.printStackTrace();
					}
			}
		});
		mnCuenta.add(mntmEliminarCuenta);
		
		
		mntmCambiarContrasea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JDialogChangePassword dialog = new JDialogChangePassword(manager.getUsuarioDAO(),usuario);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setModal(true);
					dialog.setVisible(true);
				} catch (Exception arg1) {
					arg1.printStackTrace();
				}
			}
		});	
		
		if(usuario=="root")
			mntmEliminarCuenta.setEnabled(false);
		
		if(usuario=="root"){
			JMenu mnAdministrador = new JMenu("Administrador");
			menuBar.add(mnAdministrador);
			
			JMenuItem mntmUsuarios = new JMenuItem("Usuarios");
			mntmUsuarios.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JDialogUsuarios dialog;
					try {
						dialog = new JDialogUsuarios(manager);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setModal(true);
						dialog.setVisible(true);
					} catch (DAOException e) {
						e.printStackTrace();
					}
				}
			});
			mnAdministrador.add(mntmUsuarios);
			
			JMenuItem mntmCrearNuevoUsuario = new JMenuItem("Crear Nuevo Usuario");
			mntmCrearNuevoUsuario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialogNewUser dialog = new JDialogNewUser(manager);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
			mnAdministrador.add(mntmCrearNuevoUsuario);
		}
		
		JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);
		
		JMenuItem mntmAcercaDe = new JMenuItem("Acerca de");
		mntmAcercaDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JDialogAcercaDe dialog = new JDialogAcercaDe();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setModal(true);
					dialog.setVisible(true);
				} catch (Exception arg0) {
					arg0.printStackTrace();
				}
			}
		});
		
		mnAyuda.add(mntmAcercaDe);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel insertarAveria = new JPanel();
		insertarAveria.setBorder(new LineBorder(Color.GRAY));
		
		JLabel lblUsuario = new JLabel("Usuario: ");
		
		txtUser = new JTextField();
		txtUser.setEnabled(false);
		txtUser.setText(usuario);
		txtUser.setColumns(10);
		
		JPanel Mensual = new JPanel();
		Mensual.setBorder(new LineBorder(Color.GRAY));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(scrollPane, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblUsuario)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(Mensual, GroupLayout.PREFERRED_SIZE, 247, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(insertarAveria, GroupLayout.PREFERRED_SIZE, 498, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(29, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUsuario)
								.addComponent(txtUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(Mensual, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
						.addComponent(insertarAveria, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JMonthChooser monthChooser = new JMonthChooser();
		JYearChooser yearChooser = new JYearChooser();
		
		JButton btnReparaciones = new JButton("Reparaciones");
		btnReparaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fecha=yearChooser.getValue()+"-"+(monthChooser.getMonth()+1);
				try {
					JDialogFechaAverias dialog = new JDialogFechaAverias(manager,fecha);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception arg0) {
					arg0.printStackTrace();
				}
			}
		});
		
		GroupLayout gl_Mensual = new GroupLayout(Mensual);
		gl_Mensual.setHorizontalGroup(
			gl_Mensual.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_Mensual.createSequentialGroup()
					.addGroup(gl_Mensual.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_Mensual.createSequentialGroup()
							.addGap(24)
							.addComponent(btnReparaciones, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
						.addGroup(gl_Mensual.createSequentialGroup()
							.addContainerGap()
							.addComponent(monthChooser, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(yearChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(60))
		);
		gl_Mensual.setVerticalGroup(
			gl_Mensual.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Mensual.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_Mensual.createParallelGroup(Alignment.LEADING)
						.addComponent(yearChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(monthChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnReparaciones)
					.addContainerGap())
		);
		Mensual.setLayout(gl_Mensual);
		
		JLabel lblInsertarAveria = new JLabel("Insertar Reparación");
		lblInsertarAveria.setFont(new Font("DejaVu Sans", Font.BOLD | Font.ITALIC, 12));
		lblInsertarAveria.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblId = new JLabel("ID:");
		
		txtID = new JTextField();
		txtID.setEnabled(false);
		txtID.setColumns(10);
		
		JLabel lblNombrePropietario = new JLabel("Nombre Propietario:");
		
		txtModelo = new JTextField();
		txtModelo.setEnabled(false);
		txtModelo.setColumns(10);
		
		Calendar c = Calendar.getInstance();
		
		JDateChooser dateChooser = new JDateChooser(c.getTime());
		dateChooser.setBackground(new Color(214, 217, 223));
		dateChooser.setDateFormatString("dd-MM-YYYY");
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
		editor.setEditable(false);
		
		JLabel lblCoche = new JLabel("Coche:");
		lblCoche.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtPropietario = new JTextField();
		
		 int condition = JComponent.WHEN_FOCUSED;
		  InputMap iMap = txtPropietario.getInputMap(condition);
		  ActionMap aMap = txtPropietario.getActionMap();

		  String enter = "enter";
		  iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
		  aMap.put(enter, new AbstractAction() {
			private static final long serialVersionUID = 3378381940748924908L;

			@Override
		     public void actionPerformed(ActionEvent arg0) {
		    	 try {
	    			 JDialogPropietarios dialog = new JDialogPropietarios(manager,txtPropietario.getText());
	    			 dialog.setVisible(true);
	    			 if(vehiculo!=null){
	    				 txtID.setText(vehiculo.getId_vehi().toString());
	    				 txtMarca.setText(vehiculo.getMarca());
	    				 txtModelo.setText(vehiculo.getModelo());
	    			 }
				} catch(Exception e) {
					e.printStackTrace();
				}
		     }
		  });

		txtPropietario.setColumns(10);
		
		txtMarca = new JTextField();
		txtMarca.setEnabled(false);
		txtMarca.setColumns(10);
		
		JEditorPane comenAveria = new JEditorPane();
		
		JLabel lblComentarioAveria = new JLabel("Comentario Reparación");
		
		JButton btnCrearReparacion = new JButton("Crear Reparación");
		btnCrearReparacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				vehiculo=null;
				 txtID.setText("");
				 txtMarca.setText("");
				 txtModelo.setText("");
			}
		});
		
		JLabel lblPrecioRecambios = new JLabel("Precio Recambios:");
		
		txtRecambios = new JFormattedTextField();
		txtRecambios.setColumns(10);
		// Formato de visualización
		NumberFormat dispFormat = NumberFormat.getCurrencyInstance();
		// Formato de edición: inglés (separador decimal: el punto)
		NumberFormat editFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
		// Para la edición, no queremos separadores de millares
		editFormat.setGroupingUsed(false);
		// Creamos los formateadores de números
		NumberFormatter dnFormat = new NumberFormatter(dispFormat);
		NumberFormatter enFormat = new NumberFormatter(editFormat);
		// Creamos la factoría de formateadores especificando los
		// formateadores por defecto, de visualización y de edición
		DefaultFormatterFactory currFactory = new DefaultFormatterFactory(dnFormat, dnFormat, enFormat);
		// El formateador de edición admite caracteres incorrectos
		enFormat.setAllowsInvalid(true);
		// Asignamos la factoría al campo
		txtRecambios.setFormatterFactory(currFactory);
		
		
		
		JLabel lblPrecioCobrado = new JLabel("Precio Cobrado:");
		
		txtCobrado = new JFormattedTextField();
		txtCobrado.setColumns(10);
		txtCobrado.setFormatterFactory(currFactory);
		
		GroupLayout gl_insertarAveria = new GroupLayout(insertarAveria);
		gl_insertarAveria.setHorizontalGroup(
			gl_insertarAveria.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_insertarAveria.createSequentialGroup()
					.addGroup(gl_insertarAveria.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_insertarAveria.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_insertarAveria.createSequentialGroup()
									.addComponent(lblCoche, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtMarca, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(txtModelo))
								.addGroup(gl_insertarAveria.createSequentialGroup()
									.addComponent(lblNombrePropietario)
									.addGap(18)
									.addComponent(txtPropietario, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblId, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtID, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
							.addGap(36)
							.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addGap(88)
							.addComponent(lblComentarioAveria, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addGap(121)
							.addComponent(btnCrearReparacion, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addGap(143)
							.addComponent(lblInsertarAveria, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addContainerGap()
							.addComponent(comenAveria, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_insertarAveria.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPrecioRecambios)
						.addComponent(lblPrecioCobrado)
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addGap(6)
							.addComponent(txtCobrado, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addGap(6)
							.addComponent(txtRecambios, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(32))
		);
		gl_insertarAveria.setVerticalGroup(
			gl_insertarAveria.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_insertarAveria.createSequentialGroup()
					.addComponent(lblInsertarAveria, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addGroup(gl_insertarAveria.createParallelGroup(Alignment.LEADING, false)
						.addComponent(dateChooser, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_insertarAveria.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblId, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(11)
					.addGroup(gl_insertarAveria.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombrePropietario, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtPropietario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPrecioRecambios))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_insertarAveria.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtMarca, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCoche, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtModelo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtRecambios, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_insertarAveria.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addGap(1)
							.addComponent(lblComentarioAveria, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPrecioCobrado)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_insertarAveria.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_insertarAveria.createSequentialGroup()
							.addComponent(comenAveria, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCrearReparacion))
						.addComponent(txtCobrado, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		insertarAveria.setLayout(gl_insertarAveria);
		
		table = new JTable();
		table.setForeground(Color.WHITE);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		model= new AlarmTableModel(manager.getAveriaDAO());
		table.setModel(model);
		scrollPane.setViewportView(table);
		contentPane.setLayout(gl_contentPane);
	    setLocationRelativeTo(null);
	    setResizable(false);
	}

	public static Vehiculo getVehiculo() {
		return vehiculo;
	}

	public static void setVehiculo(Vehiculo vehiculo) {
		JFrameMain.vehiculo = vehiculo;
	}
}