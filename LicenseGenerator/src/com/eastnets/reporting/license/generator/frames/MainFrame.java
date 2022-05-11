/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eastnets.reporting.license.generator.frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eastnets.reporting.license.generator.LicenseGenerator;
import com.eastnets.reporting.license.generator.dialogs.BICCodeDialog;
import com.eastnets.reporting.license.generator.models.BICsTableModel;
import com.eastnets.reporting.license.generator.models.ProductTableModel;
import com.eastnets.reporting.license.generator.pdfLicenseGenerator.LicensePdfGenerator;
import com.eastnets.reporting.license.generator.renderers.DateTableCellEditorRenderer;
import com.eastnets.reporting.licensing.beans.BicCode;
import com.eastnets.reporting.licensing.beans.License;
import com.eastnets.reporting.licensing.beans.Product;
import com.eastnets.reporting.licensing.exception.LicenseException;
import com.eastnets.reporting.licensing.util.BeanFactory;
import com.eastnets.reporting.licensing.util.LicenseUtils;
import com.toedter.calendar.JDateChooser;

/**
 *
 * @author Mhattab
 */
public class MainFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7189838476563127003L;

	private List<Product> productsAvailable = new ArrayList<Product>();
	private List<Product> productsSelected = new ArrayList<Product>();
	private List<Product> trafficAvailable = new ArrayList<Product>();
	private List<Product> trafficSelected = new ArrayList<Product>();
	private List<BicCode> bics = new ArrayList<BicCode>();
	Preferences prefs;

	// New parameters added when adding the custom license generator xml file input.
	String title = "EastNets en.Reporting 3.2 License Generator Tool";
	boolean isProductsEnabled = true;
	boolean isTrafficsEnabled = true;
	boolean isBICsEnabled = true;
	boolean isServersEnabled = true;

	/**
	 * Creates new form MainFrame
	 */
	public MainFrame() {
		prefs = Preferences.userRoot().node(this.getClass().getName());

		LoadLicenseParamsFromFile();

		/*
		 * Below is replaced by the above function that initiates from LicParam.xml
		 * //Initializing products... //note: adding a new product don't forget to
		 * reflect the product id to LicenseServiceImpl. getLicenseProducts(), this
		 * should change later and maybe it is already done, but as long as this comment
		 * exists you should check there.
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("01",
		 * "Archive", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("02", "Loader",
		 * null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("03", "Viewer",
		 * null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("04",
		 * "Watchdog", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("05",
		 * "Reporting", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("10",
		 * "Recovery", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("11",
		 * "Events extract", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("12",
		 * "Events viewer", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("14",
		 * "RJEDump", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("16",
		 * "Dashboard", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("17",
		 * "XmlRestore", null, false));
		 * productsAvailable.add(BeanFactory.getInstance().getNewProduct("18",
		 * "FilePayload", null, false));
		 * 
		 * 
		 * //Initializing traffic
		 * trafficAvailable.add(BeanFactory.getInstance().getNewProduct("13",
		 * "MX Messages", null, false));
		 * trafficAvailable.add(BeanFactory.getInstance().getNewProduct("15",
		 * "FILE Messages", null, false)); //
		 */
		// FIN MESSAGES SHOULD ALWAYS BE MANDATORY! DO NOT INCLUDE INTO THE
		// LICPARAMS.XML FILE.
		trafficSelected.add(BeanFactory.getInstance().getNewProduct("00", "FIN Messages", null, false));

		initComponents();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	}

	private void LoadLicenseParamsFromFile() {
		Document document;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new File(LicenseGenerator.licParamsFile));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Provide the correct parameter file as single argument in the command line.\nDefault is LicParams.xml.",
					"Error reading license parameters file: " + LicenseGenerator.licParamsFile,
					JOptionPane.ERROR_MESSAGE);
			throw new LicenseException("Error reading license parameters file.", e);
		}

		document.getDocumentElement().normalize();

		// userNumber = document.getElementsByTagName("users").item(0).getTextContent();
		// lsa = document.getElementsByTagName("left").item(0).getTextContent();
		// rsa = document.getElementsByTagName("right").item(0).getTextContent();
		lsa = "123456";
		rsa = "123456";
		// 1- title
		try {
			this.title = document.getElementsByTagName("title").item(0).getTextContent();
		} catch (Exception e) {
		}
		;

		// 2- products
		try {
			Node productsMain = document.getElementsByTagName("products").item(0);
			try {
				this.isProductsEnabled = productsMain.getAttributes().getNamedItem("enabled").getTextContent()
						.equals("1");
			} catch (Exception e) {
			}
			;
			if (this.isProductsEnabled) {
				NodeList productsList = productsMain.getChildNodes();
				for (int productsIndex = 0; productsIndex < productsList.getLength(); productsIndex++) {
					Node product = productsList.item(productsIndex);
					try {
						productsAvailable.add(BeanFactory.getInstance().getNewProduct(
								product.getAttributes().getNamedItem("id").getTextContent(), product.getTextContent(),
								null, false));
					} catch (Exception e) {
					}
					;
				}
			}
		} catch (Exception e) {
		}
		;

		// 3- traffics
		try {
			Node trafficsMain = document.getElementsByTagName("traffics").item(0);
			try {
				this.isTrafficsEnabled = trafficsMain.getAttributes().getNamedItem("enabled").getTextContent()
						.equals("1");
			} catch (Exception e) {
			}
			;
			if (this.isTrafficsEnabled) {
				NodeList trafficsList = trafficsMain.getChildNodes();
				for (int trafficsIndex = 0; trafficsIndex < trafficsList.getLength(); trafficsIndex++) {
					Node traffic = trafficsList.item(trafficsIndex);
					try {
						trafficAvailable.add(BeanFactory.getInstance().getNewProduct(
								traffic.getAttributes().getNamedItem("id").getTextContent(), traffic.getTextContent(),
								null, false));
					} catch (Exception e) {
					}
					;
				}
			}
		} catch (Exception e) {
		}
		;

		// 4- bics
		try {
			this.isBICsEnabled = document.getElementsByTagName("bics").item(0).getAttributes().getNamedItem("enabled")
					.getTextContent().equals("1");
		} catch (Exception e) {
		}
		;

		// 5- servers
		try {
			this.isServersEnabled = document.getElementsByTagName("servers").item(0).getAttributes()
					.getNamedItem("enabled").getTextContent().equals("1");
		} catch (Exception e) {
		}
		;
	}

	private void initProductsAndTrafficComponents(JTable tableAvailable, List<Product> listAvailable,
			JTable tableSelected, List<Product> listSelected, String productOrTraffic) {
		tableAvailable.setModel(new ProductTableModel(listAvailable, "ID", productOrTraffic));
		tableAvailable.getTableHeader().setResizingAllowed(false);
		tableAvailable.getTableHeader().setReorderingAllowed(false);
		tableAvailable.getColumnModel().getColumn(0).setMaxWidth(25);
		tableSelected.setModel(new ProductTableModel(listSelected, "ID", productOrTraffic, "Expiration Date"));
		tableSelected.getTableHeader().setResizingAllowed(false);
		tableSelected.getTableHeader().setReorderingAllowed(false);
		tableSelected.setRowHeight(25);
		tableSelected.getColumnModel().getColumn(0).setMaxWidth(25);
		TableColumn dateColumn = tableSelected.getColumnModel().getColumn(2);
		dateColumn.setMinWidth(125);
		dateColumn.setCellEditor(new DateTableCellEditorRenderer());
		dateColumn.setCellRenderer(new DateTableCellEditorRenderer());
	}

	private void initProductsAndTrafficComponentsListeners() {
		if (this.isProductsEnabled) {
			// Products
			j2ButtonProductsAdd.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					j2ButtonProductsAddActionPerformed(evt);
				}
			});
			j2ButtonProductsRemove.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					j2ButtonProductsRemoveActionPerformed(evt);
				}
			});
			j2TableProductsAvailable.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					j2TableProductsAvailableMouseClicked(evt);
				}
			});
			j2ScrollPaneProductsAvailable.setViewportView(j2TableProductsAvailable);

			j2TableProductsSelected.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					j2TableProductsSelectedMouseClicked(evt);
				}
			});
			j2ScrollPaneProductsSelected.setViewportView(j2TableProductsSelected);
		}

		if (this.isTrafficsEnabled) {
			// Traffic
			j2ButtonTrafficAdd.addActionListener(new java.awt.event.ActionListener() {

				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					j2ButtonTrafficAddActionPerformed(evt);
				}
			});
			j2ButtonTrafficRemove.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					j2ButtonTrafficRemoveActionPerformed(evt);
				}
			});
			j2TableTrafficAvailable.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					j2TableTrafficAvailableMouseClicked(evt);
				}
			});
			j2ScrollPaneTrafficAvailable.setViewportView(j2TableTrafficAvailable);

			j2TableTrafficSelected.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					j2TableTrafficSelectedMouseClicked(evt);
				}
			});
			j2ScrollPaneTrafficSelected.setViewportView(j2TableTrafficSelected);
		}

	}

	private void initProductsAndTrafficPanels() {
		if (this.isProductsEnabled) {
			j2ButtonProductsAdd.setText(">");
			j2ButtonProductsRemove.setText("<");
			j2LabelProductsAvailable.setText("Available");
			j2LabelProductsSelected.setText("Selected");
			initProductsAndTrafficComponents(this.j2TableProductsAvailable, this.productsAvailable,
					this.j2TableProductsSelected, this.productsSelected, "Products");
		}
		if (this.isTrafficsEnabled) {
			j2ButtonTrafficAdd.setText(">");
			j2ButtonTrafficRemove.setText("<");
			j2LabelTrafficAvailable.setText("Available");
			j2LabelTrafficSelected.setText("Selected");
			initProductsAndTrafficComponents(j2TableTrafficAvailable, this.trafficAvailable, j2TableTrafficSelected,
					this.trafficSelected, "Traffic");
		}
		initProductsAndTrafficComponentsListeners();

		if (this.isProductsEnabled) {
			javax.swing.GroupLayout j1Panel1ProductsLayout = new javax.swing.GroupLayout(j1Panel1Products);

			j1Panel1Products.setLayout(j1Panel1ProductsLayout);

			j1Panel1ProductsLayout.setHorizontalGroup(j1Panel1ProductsLayout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(j1Panel1ProductsLayout.createSequentialGroup().addContainerGap()
							.addGroup(j1Panel1ProductsLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(j2LabelProductsAvailable).addComponent(j2ScrollPaneProductsAvailable,
											javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(j2ButtonProductsRemove)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(j2ButtonProductsAdd)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(j1Panel1ProductsLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(j1Panel1ProductsLayout.createSequentialGroup()
											.addComponent(j2ScrollPaneProductsSelected,
													javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
											.addGap(16, 16, 16))
									.addGroup(j1Panel1ProductsLayout.createSequentialGroup()
											.addComponent(j2LabelProductsSelected).addGap(100)
											.addComponent(dateChooserProductsSelected, 75, 125, 150)
											.addContainerGap(10, 65)))));

			j1Panel1ProductsLayout.setVerticalGroup(j1Panel1ProductsLayout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(j1Panel1ProductsLayout.createSequentialGroup().addContainerGap()
							.addGroup(j1Panel1ProductsLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(j2LabelProductsSelected, javax.swing.GroupLayout.Alignment.TRAILING)
									.addComponent(dateChooserProductsSelected,
											javax.swing.GroupLayout.Alignment.TRAILING, 10, 25, 27)
									.addComponent(j2LabelProductsAvailable))
							.addGroup(j1Panel1ProductsLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(j1Panel1ProductsLayout.createSequentialGroup()
											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(j1Panel1ProductsLayout
													.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(j2ScrollPaneProductsAvailable,
															javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
													.addComponent(j2ScrollPaneProductsSelected,
															javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
											.addContainerGap())
									.addGroup(j1Panel1ProductsLayout.createSequentialGroup().addGap(99, 99, 99)
											.addGroup(j1Panel1ProductsLayout
													.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
													.addComponent(j2ButtonProductsRemove)
													.addComponent(j2ButtonProductsAdd))
											.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))));
			j0TabbedPane1.addTab("Products", j1Panel1Products);
		}

		if (this.isTrafficsEnabled) {
			javax.swing.GroupLayout j1Panel2TrafficLayout = new javax.swing.GroupLayout(j1Panel2Traffic);
			j1Panel2Traffic.setLayout(j1Panel2TrafficLayout);

			j1Panel2TrafficLayout.setHorizontalGroup(j1Panel2TrafficLayout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(j1Panel2TrafficLayout.createSequentialGroup().addContainerGap()
							.addGroup(j1Panel2TrafficLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(j2LabelTrafficAvailable).addComponent(j2ScrollPaneTrafficAvailable,
											javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(j2ButtonTrafficRemove)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(j2ButtonTrafficAdd)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(j1Panel2TrafficLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(j1Panel2TrafficLayout.createSequentialGroup()
											.addComponent(j2ScrollPaneTrafficSelected,
													javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
											.addGap(16, 16, 16))
									.addGroup(j1Panel2TrafficLayout.createSequentialGroup()
											.addComponent(j2LabelTrafficSelected).addGap(100)
											.addComponent(dateChooserTrafficSelected).addContainerGap(10, 65)))));

			j1Panel2TrafficLayout.setVerticalGroup(j1Panel2TrafficLayout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(j1Panel2TrafficLayout.createSequentialGroup().addContainerGap()
							.addGroup(j1Panel2TrafficLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(j2LabelTrafficSelected, javax.swing.GroupLayout.Alignment.TRAILING)
									.addComponent(dateChooserTrafficSelected,
											javax.swing.GroupLayout.Alignment.TRAILING, 10, 25, 27)
									.addComponent(j2LabelTrafficAvailable))
							.addGroup(j1Panel2TrafficLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(j1Panel2TrafficLayout.createSequentialGroup()
											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(j1Panel2TrafficLayout
													.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(j2ScrollPaneTrafficAvailable,
															javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
													.addComponent(j2ScrollPaneTrafficSelected,
															javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
											.addContainerGap())
									.addGroup(j1Panel2TrafficLayout.createSequentialGroup().addGap(99, 99, 99)
											.addGroup(j1Panel2TrafficLayout
													.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
													.addComponent(j2ButtonTrafficRemove)
													.addComponent(j2ButtonTrafficAdd))
											.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))));
			j0TabbedPane1.addTab("Traffic", j1Panel2Traffic);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		j0TabbedPane1 = new javax.swing.JTabbedPane();

		if (this.isProductsEnabled) {
			j1Panel1Products = new javax.swing.JPanel();
			j2LabelProductsAvailable = new javax.swing.JLabel();
			j2ButtonProductsAdd = new javax.swing.JButton();
			j2ButtonProductsRemove = new javax.swing.JButton();
			j2LabelProductsSelected = new javax.swing.JLabel();
			j2ScrollPaneProductsAvailable = new javax.swing.JScrollPane();
			j2TableProductsAvailable = new javax.swing.JTable();
			j2ScrollPaneProductsSelected = new javax.swing.JScrollPane();
			j2TableProductsSelected = new javax.swing.JTable();
			dateChooserProductsSelected = new JDateChooser();
			dateChooserProductsSelected.setDateFormatString("yyyy-MM-dd");
			dateChooserProductsSelected.setOpaque(true);
		}
		if (this.isTrafficsEnabled) {
			j1Panel2Traffic = new javax.swing.JPanel();
			j2LabelTrafficAvailable = new javax.swing.JLabel();
			j2ScrollPaneTrafficAvailable = new javax.swing.JScrollPane();
			j2TableTrafficAvailable = new javax.swing.JTable();
			j2ButtonTrafficRemove = new javax.swing.JButton();
			j2ButtonTrafficAdd = new javax.swing.JButton();
			j2LabelTrafficSelected = new javax.swing.JLabel();
			j2ScrollPaneTrafficSelected = new javax.swing.JScrollPane();
			j2TableTrafficSelected = new javax.swing.JTable();
			dateChooserTrafficSelected = new JDateChooser();
			dateChooserTrafficSelected.setDateFormatString("yyyy-MM-dd");
			dateChooserTrafficSelected.setOpaque(true);
		}
		if (this.isBICsEnabled) {
			j1Panel3BICs = new javax.swing.JPanel();
			j2ScrollPaneBICs = new javax.swing.JScrollPane();
			j2TableBICs = new javax.swing.JTable();
			j2ButtonBICsAdd = new javax.swing.JButton();
			j2ButtonBICsRemove = new javax.swing.JButton();
			jButtonEdit = new javax.swing.JButton();
		}
		if (this.isServersEnabled) {
			j1Panel4Servers = new javax.swing.JPanel();
			jLabelMaxUsers = new javax.swing.JLabel();
			jTextFieldMaxUsers = new javax.swing.JTextField();
			jLabelMaxConnections = new javax.swing.JLabel();
			jTextFieldMaxConnections = new javax.swing.JTextField();
			jLabelLicenseID = new javax.swing.JLabel();
			jTextFieldLicenseID = new javax.swing.JTextField();
			jLabelCutomerName = new javax.swing.JLabel();
			jTextFieldCutomerName = new javax.swing.JTextField();
			jLabelContactName = new javax.swing.JLabel();
			jTextFieldContactName = new javax.swing.JTextField();
			jLabelConcerns = new javax.swing.JLabel();
			jTextFieldConcerns = new javax.swing.JTextField();

		}
		j0ButtonCancel = new javax.swing.JButton();
		j0ButtonGenerate = new javax.swing.JButton();
		j0ButtonLoadLicense = new javax.swing.JButton();
		j0Separator1 = new javax.swing.JSeparator();

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle(String.format("%s - v%s", this.title, LicenseGenerator.VERSION));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		this.initProductsAndTrafficPanels();

		if (this.isBICsEnabled) {
			j2TableBICs.setModel(new BICsTableModel(this.bics));
			j2TableBICs.getTableHeader().setResizingAllowed(false);
			j2TableBICs.getTableHeader().setReorderingAllowed(false);
			j2TableBICs.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					j2TableBICsMouseClicked(evt);
				}
			});
			j2ScrollPaneBICs.setViewportView(j2TableBICs);
			j2TableBICs.getColumnModel().getColumn(0).setMinWidth(80);
			j2TableBICs.getColumnModel().getColumn(1).setMaxWidth(40);
			j2TableBICs.getColumnModel().getColumn(2).setMaxWidth(60);

			j2ButtonBICsAdd.setText("+");
			j2ButtonBICsAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					j2ButtonBICsAddActionPerformed(evt);
				}
			});

			j2ButtonBICsRemove.setText("-");
			j2ButtonBICsRemove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					j2ButtonBICsRemoveActionPerformed(evt);
				}
			});

			jButtonEdit.setText("Edit");
			jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jButtonEditActionPerformed(evt);
				}
			});

			javax.swing.GroupLayout j1Panel3BICsLayout = new javax.swing.GroupLayout(j1Panel3BICs);
			j1Panel3BICs.setLayout(j1Panel3BICsLayout);
			j1Panel3BICsLayout.setHorizontalGroup(j1Panel3BICsLayout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(j1Panel3BICsLayout.createSequentialGroup().addContainerGap()
							.addComponent(j2ScrollPaneBICs, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(j1Panel3BICsLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
									.addComponent(jButtonEdit, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(j2ButtonBICsAdd, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(j2ButtonBICsRemove, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(195, 195, 195)));
			j1Panel3BICsLayout.setVerticalGroup(j1Panel3BICsLayout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(j1Panel3BICsLayout.createSequentialGroup().addGap(11, 11, 11)
							.addGroup(j1Panel3BICsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(j1Panel3BICsLayout.createSequentialGroup().addComponent(j2ButtonBICsAdd)
											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(jButtonEdit)
											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(j2ButtonBICsRemove).addContainerGap(188, Short.MAX_VALUE))
									.addGroup(j1Panel3BICsLayout
											.createSequentialGroup().addComponent(j2ScrollPaneBICs,
													javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
											.addGap(11, 11, 11)))));

			j0TabbedPane1.addTab("BICs", j1Panel3BICs);
		}
		if (this.isServersEnabled) {
			jLabelMaxUsers.setText("Max users:");
			jLabelMaxConnections.setText("Max connections:");
			jLabelLicenseID.setText("License ID:");
			jLabelCutomerName.setText("Customer name:");
			jLabelContactName.setText("Contact  name:");
			jLabelConcerns.setText("Concerns:");

			javax.swing.GroupLayout j1Panel4ServersLayout = new javax.swing.GroupLayout(j1Panel4Servers);
			j1Panel4Servers.setLayout(j1Panel4ServersLayout);
			j1Panel4ServersLayout.setHorizontalGroup(j1Panel4ServersLayout
					.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(j1Panel4ServersLayout.createSequentialGroup().addContainerGap()
							.addGroup(j1Panel4ServersLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
									.addComponent(jLabelLicenseID, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(jLabelMaxConnections, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(jLabelMaxUsers, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(jLabelCutomerName, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(jLabelContactName, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(jLabelConcerns, javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))

							.addGroup(j1Panel4ServersLayout
									.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(jTextFieldMaxUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(jTextFieldMaxConnections, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(jTextFieldLicenseID, javax.swing.GroupLayout.PREFERRED_SIZE, 141,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(jTextFieldCutomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(jTextFieldContactName, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(jTextFieldConcerns, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
											javax.swing.GroupLayout.PREFERRED_SIZE))));

			j1Panel4ServersLayout.setVerticalGroup(
					j1Panel4ServersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(j1Panel4ServersLayout.createSequentialGroup().addContainerGap()
									.addGroup(j1Panel4ServersLayout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabelMaxUsers).addComponent(jTextFieldMaxUsers,
													javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGroup(j1Panel4ServersLayout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabelMaxConnections).addComponent(jTextFieldMaxConnections,
													javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGroup(j1Panel4ServersLayout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabelCutomerName).addComponent(jTextFieldCutomerName,
													javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGroup(j1Panel4ServersLayout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabelContactName)
											.addComponent(jTextFieldContactName, javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGroup(j1Panel4ServersLayout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabelConcerns).addComponent(jTextFieldConcerns,
													javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE))

									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

									.addGroup(j1Panel4ServersLayout
											.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabelLicenseID).addComponent(jTextFieldLicenseID,
													javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE))
									.addContainerGap(197, Short.MAX_VALUE)));

			j0TabbedPane1.addTab("Servers", j1Panel4Servers);
		}

		j0ButtonCancel.setText("Cancel");
		j0ButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				j0ButtonCancelActionPerformed(evt);
			}
		});

		j0ButtonGenerate.setText("Generate");
		j0ButtonGenerate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				j0ButtonGenerateActionPerformed(evt);
			}
		});

		j0ButtonLoadLicense.setText("Load License");
		j0ButtonLoadLicense.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fillFromLicense();
			}
		});

		dateChooserProductsSelected.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				for (Product product : productsSelected) {
					product.setExpirationDate(dateChooserProductsSelected.getDate() == null ? null
							: new java.sql.Date(((java.util.Date) dateChooserProductsSelected.getDate()).getTime()));
				}

				SwingUtilities.updateComponentTreeUI(j2TableProductsSelected);
			}

		});

		dateChooserTrafficSelected.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				for (Product traffic : trafficSelected) {
					traffic.setExpirationDate(dateChooserTrafficSelected.getDate() == null ? null
							: new java.sql.Date(((java.util.Date) dateChooserTrafficSelected.getDate()).getTime()));
				}
				SwingUtilities.updateComponentTreeUI(j2TableTrafficSelected);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(j0Separator1, javax.swing.GroupLayout.Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addComponent(j0ButtonLoadLicense)
										.addGap(0, 0, Short.MAX_VALUE).addComponent(j0ButtonGenerate)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(j0ButtonCancel))
								.addComponent(j0TabbedPane1))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addComponent(j0TabbedPane1)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(j0Separator1, javax.swing.GroupLayout.PREFERRED_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(j0ButtonLoadLicense).addComponent(j0ButtonCancel).addComponent(j0ButtonGenerate))
				.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void j0ButtonGenerateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_j0ButtonGenerateActionPerformed
		if (this.isProductsEnabled && this.productsSelected.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Select at least one product.");
			return;
		}
		if (this.isBICsEnabled && this.bics.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Add at least one BIC.");
			return;
		}

		int maxUsers = 1;
		int maxConnections = 1;
		String licenseID = "";
		String cutomerName = "";
		String contactName = "";
		String concerns = "";
		if (this.isServersEnabled) {
			try {
				maxUsers = Integer.parseInt(this.jTextFieldMaxUsers.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Max users should have integral value and more than 0.");
				return;
			}
			try {
				maxConnections = Integer.parseInt(this.jTextFieldMaxConnections.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Max connections should have integral value and more than 0.");
				return;
			}
			cutomerName = this.jTextFieldCutomerName.getText();
			contactName = this.jTextFieldContactName.getText();
			concerns = this.jTextFieldConcerns.getText();
			licenseID = this.jTextFieldLicenseID.getText();
		}

		try {
			String value = prefs.get("LicenseGeneratorPath", ".");
			JFileChooser fileChooser = new JFileChooser(value);
			fileChooser.setDialogTitle("Choose license file location...");
			// xml filter is added to file types in save dialog
			XmlFilter xmlFilter = new XmlFilter(".xml, .XML", new String[] { "xml", "XML" });
			fileChooser.setFileFilter(xmlFilter);

			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				License license = BeanFactory.getInstance().getNewLicense(
						BeanFactory.getInstance().getNewLicenseMisc(maxConnections, maxUsers, licenseID, ""));
				license.getBicCodes().clear();
				license.getBicCodes().addAll(this.bics);
				license.getProducts().clear();
				license.getProducts().addAll(this.productsAvailable);
				license.getProducts().addAll(this.productsSelected);

				license.getTraffics().clear();
				license.getTraffics().addAll(this.trafficAvailable);
				license.getTraffics().addAll(this.trafficSelected);
				license.getTraffics().remove(this.trafficSelected.get(0));
				license.setLicenseKeys(LicenseUtils.generateLicenseKeys(license));

				license.setLicenseDate(Calendar.getInstance().getTime());// the date when the license file was created
				String fileNameAndType = xmlFilter.fixFileExtension(fileChooser);
				if (LicenseUtils.WriteLicenseToFile(license, fileNameAndType)) {
					JOptionPane.showMessageDialog(this,
							"License file is generated successfully on:\n" + fileNameAndType);
					LicensePdfGenerator LicensePdfGenerator = new LicensePdfGenerator();

					LicensePdfGenerator.generateLicensePdf(bics, productsSelected, trafficSelected, fileNameAndType,
							cutomerName, contactName, concerns, String.valueOf(maxUsers), lsa, rsa,
							String.valueOf(maxConnections));

					// System.exit(0);
				} else {
					JOptionPane.showMessageDialog(this, "generated license file failed.", "Error occurred",
							JOptionPane.ERROR_MESSAGE);
				}

				// save the path where the license was saved
				File file = new File(xmlFilter.fixFileExtension(fileChooser));
				prefs.put("LicenseGeneratorPath", file.getParentFile().getAbsolutePath());

			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error occurred", JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_j0ButtonGenerateActionPerformed

	private void j0ButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_j0ButtonCancelActionPerformed
		if (JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exiting...",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}// GEN-LAST:event_j0ButtonCancelActionPerformed

	private void selectionChanged(JTable fromTable, JTable toTable) {
		ProductTableModel fromModel = (ProductTableModel) fromTable.getModel();
		ProductTableModel toModel = (ProductTableModel) toTable.getModel();
		List<Product> fromList = fromModel.getProducts();
		List<Product> toList = toModel.getProducts();
		for (Product product : fromModel.getSelectedProducts(fromTable.getSelectedRows())) {
			product.setLicensed(!product.isLicensed());
			fromList.remove(product);
			toList.add(product);
		}
		Product[] products;
		products = fromList.toArray(new Product[0]);
		Arrays.sort(products);
		fromList.clear();
		fromList.addAll(Arrays.asList(products));
		products = toList.toArray(new Product[0]);
		Arrays.sort(products);
		toList.clear();
		toList.addAll(Arrays.asList(products));
		fromModel.fireTableDataChanged();
		toModel.fireTableDataChanged();
	}

	private void j2ButtonProductsAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_j2ButtonProductsAddActionPerformed
		if (this.j2TableProductsAvailable.getSelectedColumnCount() <= 0) {
			JOptionPane.showMessageDialog(this, "Please select at least one product to add.");
		} else {
			this.selectionChanged(j2TableProductsAvailable, j2TableProductsSelected);
		}
	}// GEN-LAST:event_j2ButtonProductsAddActionPerformed

	private void j2TableProductsAvailableMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getClickCount() >= 2) {
			this.selectionChanged(j2TableProductsAvailable, j2TableProductsSelected);
		}
	}

	private void j2ButtonProductsRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_j2ButtonProductsRemoveActionPerformed
		if (this.j2TableProductsSelected.getSelectedColumnCount() <= 0) {
			JOptionPane.showMessageDialog(this, "Please select at least one product to remove.");
		} else {
			this.selectionChanged(j2TableProductsSelected, j2TableProductsAvailable);
		}
	}// GEN-LAST:event_j2ButtonProductsRemoveActionPerformed

	private void j2TableProductsSelectedMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_j2TableProductsSelectedMouseClicked
		if (evt.getClickCount() >= 2) {
			this.selectionChanged(j2TableProductsSelected, j2TableProductsAvailable);
		}
	}// GEN-LAST:event_j2TableProductsSelectedMouseClicked

	private void j2ButtonTrafficAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_j2ButtonTrafficAddActionPerformed
		if (this.j2TableTrafficAvailable.getSelectedColumnCount() <= 0) {
			JOptionPane.showMessageDialog(this, "Please select at least one traffic to add.");
		} else {
			this.selectionChanged(j2TableTrafficAvailable, j2TableTrafficSelected);
		}
	}// GEN-LAST:event_j2ButtonTrafficAddActionPerformed

	private void j2TableTrafficAvailableMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_j2TableTrafficAvailableMouseClicked
		if (evt.getClickCount() >= 2) {
			this.selectionChanged(j2TableTrafficAvailable, j2TableTrafficSelected);
		}
	}// GEN-LAST:event_j2TableTrafficAvailableMouseClicked

	private void j2ButtonTrafficRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_j2ButtonTrafficRemoveActionPerformed
		if (this.j2TableTrafficSelected.getSelectedColumnCount() <= 0) {
			JOptionPane.showMessageDialog(this, "Please select at least one traffic to remove - but not FIN Messages.");
		} else if (j2TableTrafficSelected.getSelectedRow() == 0) {
			JOptionPane.showMessageDialog(this, "Removing FIN Messages is not possible. Please deselect it.");
		} else {
			this.selectionChanged(j2TableTrafficSelected, j2TableTrafficAvailable);
		}
	}// GEN-LAST:event_j2ButtonTrafficRemoveActionPerformed

	private void j2TableTrafficSelectedMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_j2TableTrafficSelectedMouseClicked
		if (evt.getClickCount() >= 2) {
			if (j2TableTrafficSelected.getSelectedRow() == 0) {
				JOptionPane.showMessageDialog(this, "Removing FIN Messages is not possible. Please deselect it.");
			} else {
				this.selectionChanged(j2TableTrafficSelected, j2TableTrafficAvailable);
			}
		}
	}// GEN-LAST:event_j2TableTrafficSelectedMouseClicked

	private void j2ButtonBICsAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_j2ButtonBICsAddActionPerformed
		BICCodeDialog bicCodeDialog = new BICCodeDialog(this, null, this.bics);
		bicCodeDialog.setVisible(true);
		if (bicCodeDialog.isOK()) {
			this.bics.add(bicCodeDialog.bic);
		}
		((BICsTableModel) (this.j2TableBICs.getModel())).fireTableDataChanged();
	}// GEN-LAST:event_j2ButtonBICsAddActionPerformed

	private void j2ButtonBICsRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_j2ButtonBICsRemoveActionPerformed
		int selectedIndex = this.j2TableBICs.getSelectedRow();
		if (selectedIndex < 0) {
			JOptionPane.showMessageDialog(this, "please select a BIC code to remove.");
			return;
		}
		if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete that BIC code?",
				"Deleting BIC code...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			this.bics.remove(selectedIndex);
			((BICsTableModel) (this.j2TableBICs.getModel())).fireTableDataChanged();
		}
	}// GEN-LAST:event_j2ButtonBICsRemoveActionPerformed

	private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonEditActionPerformed
		editSelectedBIC();
	}// GEN-LAST:event_jButtonEditActionPerformed

	private void j2TableBICsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_j2TableBICsMouseClicked
		if (evt.getClickCount() == 2) {
			editSelectedBIC();
		}
	}// GEN-LAST:event_j2TableBICsMouseClicked

	private void editSelectedBIC() {
		int selectedIndex = this.j2TableBICs.getSelectedRow();
		if (selectedIndex < 0) {
			JOptionPane.showMessageDialog(this, "please select a BIC code to edit.");
			return;
		}
		BICCodeDialog bicCodeDialog = new BICCodeDialog(this, this.bics.get(selectedIndex), this.bics);
		bicCodeDialog.setVisible(true);
		if (bicCodeDialog.isOK()) {
			this.bics.remove(selectedIndex);
			this.bics.add(selectedIndex, bicCodeDialog.bic);
			((BICsTableModel) (this.j2TableBICs.getModel())).fireTableDataChanged();
		}
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosing
		this.j0ButtonCancelActionPerformed(null);
	}// GEN-LAST:event_formWindowClosing

	private void fillFromLicense() {
		License license = null;
		try {
			JFileChooser fileChooser = new JFileChooser(prefs.get("LicenseGeneratorPath", "."));
			fileChooser.setDialogTitle("Choose license file");
			// xml filter is added to file types in save dialog
			XmlFilter xmlFilter = new XmlFilter(".xml, .XML", new String[] { "xml", "XML" });
			fileChooser.setFileFilter(xmlFilter);
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				license = LicenseUtils.readLicenseFromFile(xmlFilter.fixFileExtension(fileChooser));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error occurred", JOptionPane.ERROR_MESSAGE);
		}
		if (license == null) {
			return;
		}

		if (this.isProductsEnabled) {
			// productsAvailable.clear();
			for (Product product : productsSelected) {
				product.setLicensed(false);
				productsAvailable.add(product);
			}
			productsSelected.clear();
			for (Product product : license.getProducts()) {
				if (!productsAvailable.contains(product)) {
					productsAvailable.add(product);
				}
				if (product.isLicensed()) {
					productsAvailable.remove(product);
					productsSelected.add(product);
				}
			}
			initProductsAndTrafficComponents(this.j2TableProductsAvailable, this.productsAvailable,
					this.j2TableProductsSelected, this.productsSelected, "Products");
		}
		if (this.isTrafficsEnabled) {
			// trafficAvailable.clear();
			trafficSelected.remove(0);
			for (Product product : trafficSelected) {
				product.setLicensed(false);
				trafficAvailable.add(product);
			}
			trafficSelected.clear();
			trafficSelected.add(BeanFactory.getInstance().getNewProduct("00", "FIN Messages", null, false));
			for (Product product : license.getTraffics()) {
				if (!trafficAvailable.contains(product)) {
					trafficAvailable.add(product);
				}
				if (product.isLicensed()) {
					trafficAvailable.remove(product);
					trafficSelected.add(product);
				}
			}
			trafficAvailable.removeAll(trafficSelected);
			initProductsAndTrafficComponents(this.j2TableTrafficAvailable, this.trafficAvailable,
					this.j2TableTrafficSelected, this.trafficSelected, "Traffic");
		}
		if (this.isBICsEnabled) {
			bics.clear();
			for (BicCode bicCode : license.getBicCodes()) {
				bics.add(bicCode);
			}
			((BICsTableModel) (this.j2TableBICs.getModel())).fireTableDataChanged();
		}
		if (this.isServersEnabled) {
			jTextFieldLicenseID.setText(license.getLicenseMisc().getLicenseID());
			jTextFieldMaxConnections.setText(String.format("%d", license.getLicenseMisc().getMaxConnections()));
			jTextFieldMaxUsers.setText(String.format("%d", license.getLicenseMisc().getMaxUsers()));
			// jTextFieldCutomerName.setText(license.getLicenseMisc().getCustomerName());
			// jTextFieldContactName.setText(license.getLicenseMisc().getContactName());
			// jTextFieldConcerns.setText(license.getLicenseMisc().getConcerns());
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton j0ButtonCancel;
	private javax.swing.JButton j0ButtonGenerate;
	private javax.swing.JButton j0ButtonLoadLicense;

	private javax.swing.JSeparator j0Separator1;
	private javax.swing.JTabbedPane j0TabbedPane1;
	private javax.swing.JPanel j1Panel1Products;
	private javax.swing.JPanel j1Panel2Traffic;
	private javax.swing.JPanel j1Panel3BICs;
	private javax.swing.JPanel j1Panel4Servers;
	private javax.swing.JButton j2ButtonBICsAdd;
	private javax.swing.JButton j2ButtonBICsRemove;
	private javax.swing.JButton j2ButtonProductsAdd;
	private javax.swing.JButton j2ButtonProductsRemove;
	private javax.swing.JButton j2ButtonTrafficAdd;
	private javax.swing.JButton j2ButtonTrafficRemove;
	private javax.swing.JLabel j2LabelProductsAvailable;
	private javax.swing.JLabel j2LabelProductsSelected;
	private JDateChooser dateChooserProductsSelected;
	private javax.swing.JLabel j2LabelTrafficAvailable;
	private javax.swing.JLabel j2LabelTrafficSelected;
	private JDateChooser dateChooserTrafficSelected;
	private javax.swing.JScrollPane j2ScrollPaneBICs;
	private javax.swing.JScrollPane j2ScrollPaneProductsAvailable;
	private javax.swing.JScrollPane j2ScrollPaneProductsSelected;
	private javax.swing.JScrollPane j2ScrollPaneTrafficAvailable;
	private javax.swing.JScrollPane j2ScrollPaneTrafficSelected;
	private javax.swing.JTable j2TableBICs;
	private javax.swing.JTable j2TableProductsAvailable;
	private javax.swing.JTable j2TableProductsSelected;
	private javax.swing.JTable j2TableTrafficAvailable;
	private javax.swing.JTable j2TableTrafficSelected;
	private javax.swing.JButton jButtonEdit;
	private javax.swing.JLabel jLabelLicenseID;
	private javax.swing.JLabel jLabelMaxConnections;
	private javax.swing.JLabel jLabelMaxUsers;
	private javax.swing.JLabel jLabelCutomerName;
	private javax.swing.JLabel jLabelContactName;
	private javax.swing.JLabel jLabelConcerns;
	private javax.swing.JTextField jTextFieldLicenseID;
	private javax.swing.JTextField jTextFieldMaxConnections;
	private javax.swing.JTextField jTextFieldMaxUsers;
	private javax.swing.JTextField jTextFieldCutomerName;
	private javax.swing.JTextField jTextFieldContactName;
	private javax.swing.JTextField jTextFieldConcerns;
	// End of variables declaration//GEN-END:variables

	private String userNumber;

	private String lsa;

	private String rsa;
}
