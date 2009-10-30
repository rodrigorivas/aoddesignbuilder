package interfazaod.actions;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.UIManager;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ProposedSolution extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelTop;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JButton jCancel;
	private JButton jNext;
	private JList jResponsibilities;
	private JList jAttributes;
	private JPanel jPanelBottom;
	private JPanel jPanelLeft;
	private JLabel jLabel5;
	private JPanel jPanelRight;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JButton jPrevious;
	private JList jClasses;
	private JPanel jPanelBR;
	private JPanel jPanelTR;
	private JScrollPane sClasses, sAttributes, sResponsibilities;
	private FileSelector fileSelector;
	private CheckListManager cMClasses, cMAttributes, cMResponsibilities;
	private static ProposedSolution proposedSolution;

	public static ProposedSolution getInstance() {
		if (proposedSolution==null) {
			proposedSolution = new ProposedSolution();
		}
		return proposedSolution;
	}
	
	protected ProposedSolution() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jPanelTop = new JPanel();
				BorderLayout jPanelTopLayout = new BorderLayout();
				jPanelTop.setLayout(jPanelTopLayout);
				getContentPane().add(jPanelTop, BorderLayout.NORTH);
				jPanelTop.setPreferredSize(new java.awt.Dimension(583, 323));
				jPanelTop.setEnabled(false);
				{
					jPanelLeft = new JPanel();
					jPanelLeft.setLayout(null);
					jPanelTop.add(jPanelLeft, BorderLayout.WEST);
					jPanelLeft.setPreferredSize(new java.awt.Dimension(298, 323));
					{
						//En esta parte del modelo habría que cambiar el String que
						//se presenta acá por el contenido de nuestro listado de clases
						ListModel jClassesModel = 
							new DefaultComboBoxModel(
									new String[] { "1", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2"
											, "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2"});
						jClasses = new JList();
						jClasses.setModel(jClassesModel);
						jClasses.setBounds(55, 54, 233, 269);
						jClasses.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						sClasses = new JScrollPane();
						sClasses.getViewport().setView(jClasses);
						sClasses.setBounds(55, 54, 233, 269);
						jPanelLeft.add(sClasses,new GridBagConstraints (3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.NONE ,new Insets(0,0,0,0),0,0));

					}
					{
						jLabel1 = new JLabel();
						jPanelLeft.add(jLabel1);
						jLabel1.setText("Select the elements to be included in your design:");
						jLabel1.setBounds(30, 6, 258, 21);
					}
					{
						jLabel2 = new JLabel();
						jPanelLeft.add(jLabel2);
						jPanelLeft.add(getJLabel5());
						jLabel2.setText("Classes and Aspects");
						jLabel2.setBounds(55, 38, 98, 14);
						jLabel2.setFont(new java.awt.Font("Tahoma",2,11));
					}
					cMClasses = new CheckListManager(jClasses); 
				}
				{
					jPanelRight = new JPanel();
					BorderLayout jPanelRightLayout = new BorderLayout();
					jPanelRight.setLayout(jPanelRightLayout);
					jPanelTop.add(jPanelRight, BorderLayout.CENTER);
					{
						jPanelTR = new JPanel();
						jPanelRight.add(jPanelTR, BorderLayout.NORTH);
						jPanelTR.setPreferredSize(new java.awt.Dimension(285, 184));
						jPanelTR.setLayout(null);
						jPanelTR.setOpaque(false);
						{
							jLabel3 = new JLabel();
							jPanelTR.add(jLabel3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabel3.setText("Attributes");
							jLabel3.setBounds(0, 41, 48, 14);
							jLabel3.setFont(new java.awt.Font("Tahoma",2,11));
						}
						{
							sAttributes = new JScrollPane();
							jPanelTR.add(sAttributes, new GridBagConstraints(0, 1, 3, 3, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							sAttributes.getViewport().setView(jAttributes);
							sAttributes.setBounds(0, 55, 240, 127);
							{
								jAttributes = new JList();
								sAttributes.setViewportView(jAttributes);
								ListModel jAttributesModel = 
									new DefaultComboBoxModel(
											new String[] { "1", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2"});
								jAttributes.setModel(jAttributesModel);
								jAttributes.setBounds(22, 17, 47, 32);
								jAttributes.setPreferredSize(new java.awt.Dimension(221, 124));
							}
							
						}
					}
					cMAttributes = new CheckListManager(jAttributes);
					{
						jPanelBR = new JPanel();
						jPanelRight.add(jPanelBR, BorderLayout.CENTER);
						jPanelBR.setPreferredSize(new java.awt.Dimension(285, 154));
						jPanelBR.setLayout(null);
						{
							sResponsibilities = new JScrollPane();
							sResponsibilities.getViewport().setView(jResponsibilities);
							sResponsibilities.setBounds(0, 21, 243, 118);
							jPanelBR.add(sResponsibilities,new GridBagConstraints (3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.NONE ,new Insets(0,0,0,0),0,0));
							{
								jResponsibilities = new JList();
								sResponsibilities.setViewportView(jResponsibilities);
								ListModel jResponsibilitiesModel = 
									new DefaultComboBoxModel(
											new String[] { "1", "2", "2", "2", "2"});
								jResponsibilities.setModel(jResponsibilitiesModel);
								jResponsibilities.setBounds(0, 0, 23, 47);
							}
							
						}
						cMResponsibilities = new CheckListManager (jResponsibilities);
						{
							jLabel4 = new JLabel();
							jPanelBR.add(jLabel4);
							jLabel4.setText("Responsibilities");
							jLabel4.setBounds(0, 7, 72, 14);
							jLabel4.setFont(new java.awt.Font("Tahoma",2,11));
						}
					}
				}
			}
			{
				jPanelBottom = new JPanel();
				jPanelBottom.setLayout(null);
				getContentPane().add(jPanelBottom, BorderLayout.CENTER);
				jPanelBottom.setPreferredSize(new java.awt.Dimension(583, 72));
				{
					jPrevious = new JButton();
					jPanelBottom.add(jPrevious, "1, 1, 1, 2");
					jPanelBottom.add(getJNext(), "1, 1, 1, 2");
					jPanelBottom.add(getJCancel());
					jPrevious.setText("Previous");
					jPrevious.setBounds(101, 19, 101, 28);
					jPrevious.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/001_23.png")));
					jPrevious.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jPreviousMouseClicked(evt);
						}
					});
				}
			}
			this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/001_15.png")).getImage());
			pack();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			setLocationRelativeTo(null);
			this.setTitle("Design Elements");
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFileSelector(FileSelector fileSelector) {
		this.fileSelector = fileSelector;
	}
	
	private void jPreviousMouseClicked(MouseEvent evt) {
		fileSelector.setVisible(true);
		this.setVisible(false);
	}

	private void jNextMouseClicked(MouseEvent evt) {
			Integer value = JOptionPane.showConfirmDialog(null, "Are you sure you want to generate this .uml?", "Confirm UML generation", JOptionPane.YES_NO_OPTION);
			if (value == JOptionPane.OK_OPTION) {
				//Acá generar todo el UML
			}
		}
	
	private JButton getJNext() {
		if(jNext == null) {
			jNext = new JButton();
			jNext.setText("Next");
			jNext.setBounds(245, 19, 101, 28);
			jNext.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/001_21.png")));
			jNext.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					jNextMouseClicked(evt);
				}
			});
		}
		return jNext;
	}
	
	private JButton getJCancel() {
		if(jCancel == null) {
			jCancel = new JButton();
			jCancel.setText("Quit");
			jCancel.setBounds(392, 19, 101, 28);
			jCancel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/block_16.png")));
			jCancel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					jCancelMouseClicked(evt);
				}
			});
		}
		return jCancel;
	}
	
	private void jCancelMouseClicked(MouseEvent evt) {
		Integer value = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
		if (value == JOptionPane.OK_OPTION) {
			this.dispose();
			System.exit(NORMAL);
		}
	}
	
	private JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new JLabel();
			jLabel5.setBounds(5, 29, 40, 40);
			jLabel5.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Settings.png")));
		}
		return jLabel5;
	}

}
