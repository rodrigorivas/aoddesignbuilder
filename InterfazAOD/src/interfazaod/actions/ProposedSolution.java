package interfazaod.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BorderFactory;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.UIManager;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

import util.Log4jConfigurator;

import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileAssociation;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfilePointcut;
import main.AODBuilder;


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
	private JLabel jLabel1;
	private JPanel jPanel1;
	private JPanel jPanelRight;
	private JLabel jLabel2;
	private JButton jPrevious;
	private JList jClasses;
	private JPanel jPanelBR;
	private JPanel jPanelTR;
	private JScrollPane sClasses, sAttributes, sResponsibilities;
	private JFrame previousFrame;
	private CheckListManager cMClasses, cMAttributes, cMResponsibilities;
	Object[] aodClasses;
	Logger logger = Log4jConfigurator.getLogger();

	private static ProposedSolution proposedSolution;

	public static ProposedSolution getInstance() {
		return proposedSolution;
	}
	
	public static ProposedSolution getInstance(Object[] values) {
		if (proposedSolution==null) {
			proposedSolution = new ProposedSolution(values);
		}
		return proposedSolution;
	}

	
	protected ProposedSolution(Object[] values) {
		super();
		setAodClasses(values);
		initGUI();
	}
	
	public Object[] getAodClasses() {
		return aodClasses;
	}

	private JButton getJCancel() {
		if(jCancel == null) {
			jCancel = new JButton();
			jCancel.setText("Quit");
			jCancel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/block_16.png")));
			jCancel.setPreferredSize(new java.awt.Dimension(93, 28));
			jCancel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					jCancelMouseClicked(evt);
				}
			});
		}
		return jCancel;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Select the elements to be included in your design");
			jLabel1.setBounds(155, 14, 297, 21);
			jLabel1.setFont(new java.awt.Font("Tahoma",1,11));
		}
		return jLabel1;
	}
	
	private JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new JLabel();
			jLabel5.setBounds(24, 6, 51, 45);
			jLabel5.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Settings.png")));
		}
		return jLabel5;
	}

	private JButton getJNext() {
		if(jNext == null) {
			jNext = new JButton();
			jNext.setText("Next");
			jNext.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/001_21.png")));
			jNext.setPreferredSize(new java.awt.Dimension(93, 28));
			jNext.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					jNextMouseClicked(evt);
				}
			});
		}
		return jNext;
	}
	
	private JPanel getJPanel1() {
		if(jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setPreferredSize(new java.awt.Dimension(583, 50));
			jPanel1.setLayout(null);
			jPanel1.add(getJLabel1());
			jPanel1.add(getJLabel5());
		}
		return jPanel1;
	}

	private ArrayList<AODProfileClass> getSelectedClasses() {
		ArrayList<AODProfileClass> classes = new ArrayList<AODProfileClass>();
		for (Object o: aodClasses){
			AODProfileClass bean = (AODProfileClass) o;
			if (bean.isSelected()){
				classes.add(bean);
			}
		}		
		return classes;
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
                         ListModel jClassesModel =
                            new DefaultComboBoxModel(aodClasses);
                        jClasses = new JList();
                        jClasses.setModel(jClassesModel);
                        jClasses.setCellRenderer(new AODBeanCellRenderer());
                        jClasses.setBounds(0, 0, 233, 269);
                        jClasses.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						jClasses.setSelectionBackground(new java.awt.Color(249,243,183));
						jClasses.setSelectionForeground(new java.awt.Color(0,0,0));
						jClasses.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								jClassesMouseClicked(evt);
							}
						});
                        sClasses = new JScrollPane();
                        sClasses.getViewport().setView(jClasses);
                        sClasses.setBounds(21, 31, 252, 231);
                        jPanelLeft.add(sClasses,new GridBagConstraints (3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.NONE ,new Insets(0,0,0,0),0,0));

                    }
					{
						jLabel2 = new JLabel();
						jPanelLeft.add(jLabel2);
						jLabel2.setText("Classes and Aspects");
						jLabel2.setBounds(31, 11, 98, 14);
						jLabel2.setFont(new java.awt.Font("Tahoma",2,11));
					}
					cMClasses = new CheckListManager(jClasses, null); 
				}
				{
					jPanelRight = new JPanel();
					BorderLayout jPanelRightLayout = new BorderLayout();
					jPanelRight.setLayout(jPanelRightLayout);
					jPanelTop.add(jPanelRight, BorderLayout.CENTER);
					jPanelTop.add(getJPanel1(), BorderLayout.NORTH);
					{
						jPanelTR = new JPanel();
						jPanelRight.add(jPanelTR, BorderLayout.NORTH);
						jPanelTR.setPreferredSize(new java.awt.Dimension(287, 138));
						jPanelTR.setLayout(null);
						jPanelTR.setOpaque(false);
						{
							jLabel3 = new JLabel();
							jPanelTR.add(jLabel3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabel3.setText("Attributes");
							jLabel3.setBounds(0, 11, 48, 14);
							jLabel3.setFont(new java.awt.Font("Tahoma",2,11));
						}
						{
							jAttributes = new JList();
							jPanelTR.add(jAttributes);
							setListModel(jAttributes, null);
							jAttributes.setBounds(0, 25, 238, 102);
							jAttributes.setCellRenderer(new AODBeanCellRenderer());
							jAttributes.setSelectionBackground(new java.awt.Color(249,243,183));
							jAttributes.setSelectionForeground(new java.awt.Color(0,0,0));
						}
						{
							sAttributes = new JScrollPane();
							jPanelTR.add(sAttributes, new GridBagConstraints(0, 1, 3, 3, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							sAttributes.getViewport().setView(jAttributes);
							sAttributes.setBounds(0, 25, 238, 101);
							jPanelTR.add(sAttributes,new GridBagConstraints (3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.NONE ,new Insets(0,0,0,0),0,0));

						}
					}
					cMAttributes = new CheckListManager(jAttributes, jClasses);
					{
						jPanelBR = new JPanel();
						jPanelRight.add(jPanelBR, BorderLayout.CENTER);
						jPanelBR.setPreferredSize(new java.awt.Dimension(285, 154));
						jPanelBR.setLayout(null);
						{
							jResponsibilities = new JList();
							jPanelBR.add(jResponsibilities);
							setListModel(jResponsibilities, null);
							jResponsibilities.setBounds(0, 15, 238, 110);
							jResponsibilities.setCellRenderer(new AODBeanCellRenderer());
							jResponsibilities.setSelectionBackground(new java.awt.Color(249,243,183));
							jResponsibilities.setSelectionForeground(new java.awt.Color(0,0,0));
						}
						{
							sResponsibilities = new JScrollPane();
							sResponsibilities.getViewport().setView(jResponsibilities);
							sResponsibilities.setBounds(0, 15, 235, 110);
							jPanelBR.add(sResponsibilities,new GridBagConstraints (3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.NONE ,new Insets(0,0,0,0),0,0));
							sResponsibilities.setSize(238, 109);

						}
						cMResponsibilities = new CheckListManager (jResponsibilities, jClasses);
						{
							jLabel4 = new JLabel();
							jPanelBR.add(jLabel4);
							jLabel4.setText("Responsibilities");
							jLabel4.setBounds(0, 1, 72, 14);
							jLabel4.setFont(new java.awt.Font("Tahoma",2,11));
						}
					}
				}
			}
			{
				jPanelBottom = new JPanel();
				FlowLayout jPanelBottomLayout = new FlowLayout();
				getContentPane().add(jPanelBottom, BorderLayout.CENTER);
				jPanelBottom.setLayout(jPanelBottomLayout);
				jPanelBottom.setPreferredSize(new java.awt.Dimension(583, 72));
				{
					jPrevious = new JButton();
					jPanelBottom.add(jPrevious);
					jPanelBottom.add(getJNext());
					jPanelBottom.add(getJCancel());
					jPrevious.setText("Previous");
					jPrevious.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/001_23.png")));
					jPrevious.setPreferredSize(new java.awt.Dimension(93, 28));
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
			this.setResizable(false);
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jCancelMouseClicked(MouseEvent evt) {
		Integer value = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
		if (value == JOptionPane.OK_OPTION) {
			this.dispose();
		}
	}
	
	private void jClassesMouseClicked(MouseEvent evt) {
		AODProfileClass cl = (AODProfileClass) jClasses.getSelectedValue();
		setListModel(jAttributes, cl.getAttributes().toArray());
		setListModel(jResponsibilities, cl.getResponsabilities().toArray());
	}

	private void jNextMouseClicked(MouseEvent evt) {
		ArrayList<AODProfileClass> selectedClasses = getSelectedClasses();
		ArrayList<AODProfileAssociation> selectedAssociations = updateAssociationSelection(selectedClasses);

		ProposedSolutionAssociations psassoc = new ProposedSolutionAssociations(selectedAssociations.toArray());
		psassoc.setAodClasses(selectedClasses.toArray());
		psassoc.setPreviousFrame(this);
		psassoc.setVisible(true);
		this.setVisible(false);
	}
	private void jPreviousMouseClicked(MouseEvent evt) {
		previousFrame.setVisible(true);
		this.setVisible(false);
	}
	public void setAodClasses(Object[] aodClasses) {
		this.aodClasses = aodClasses;
	}

	private void setListModel(JList list, Object[] items) {
		ListModel jModel = null;
		if (items!=null)
			jModel = new DefaultComboBoxModel(items);
		else
			jModel = new DefaultComboBoxModel();

		list.setModel(jModel);
	}

	public void setPreviousFrame(JFrame previousFrame) {
		this.previousFrame = previousFrame;
	}
	
	private ArrayList<AODProfileAssociation> updateAssociationSelection(ArrayList<AODProfileClass> classes) {
		ArrayList<AODProfileAssociation> assocList = new ArrayList<AODProfileAssociation>();
		for (AODProfileClass cl: classes){
			//unselect associations that points to unselected classes
			for (AODProfileAssociation assoc: cl.getPossibleAssociations()){
				if (!classes.contains(assoc.getTarget()))
					assoc.setSelected(false);
				else{
					assocList.add(assoc);
				}
			}
			//unselect pointcuts that points to unselected classes
			if (cl instanceof AODProfileAspect){
				for (AODProfilePointcut pc: ((AODProfileAspect)cl).getPossiblePointcuts()){
					if (!classes.contains(pc.getTarget()))
						pc.setSelected(false);
					else{
						assocList.add(pc);
					}
				}				
			}
		}
		return assocList;
	}

}
