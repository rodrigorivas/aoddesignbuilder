package interfazaod.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import util.Log4jConfigurator;
import xmi.Di2Generator;
import xmi.XMIExporter;

import beans.aodprofile.AODProfileAdvice;
import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileAssociation;
import beans.aodprofile.AODProfileAttribute;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfileJoinPoint;
import beans.aodprofile.AODProfilePointcut;
import beans.aodprofile.AODProfileResponsability;
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
public class ProposedSolutionAssociations extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelTop;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JButton jCancel;
	private JButton jNext;
	private JList jAdvices;
	private JList jJoinpoints;
	private JPanel jPanelBottom;
	private JPanel jPanelLeft;
	private JLabel jLabel5;
	private JLabel jLabel1;
	private JPanel jPanel1;
	private JPanel jPanelRight;
	private JLabel jLabel2;
	private JButton jPrevious;
	private JList jAssociations;
	private JPanel jPanelBR;
	private JPanel jPanelTR;
	private JScrollPane sClasses, sJointpoints, sAdvices;
	private CheckListManager cMAssoc, cMJoinPoint, cMAdvices;
	Object[] aodClasses;
	Object[] objects;
	JFrame previousFrame;
	Logger logger = Log4jConfigurator.getLogger();
	
	private static ProposedSolutionAssociations proposedSolution;

	public static ProposedSolutionAssociations getInstance(Object[] values) {
		if (proposedSolution==null) {
			proposedSolution = new ProposedSolutionAssociations(values);
		}
		return proposedSolution;
	}
	
	public static ProposedSolutionAssociations getInstance() {
		return proposedSolution;
	}

	
	protected ProposedSolutionAssociations(Object[] values) {
		super();
		setObjects(values);
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
                         ListModel jClassesModel =
                            new DefaultComboBoxModel(objects);
                        jAssociations = new JList();
                        jAssociations.setModel(jClassesModel);
                        jAssociations.setCellRenderer(new AODBeanCellRenderer());
                        jAssociations.setBounds(55, 54, 233, 269);
                        jAssociations.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                        jAssociations.setSelectionBackground(new java.awt.Color(249,243,183));
                        jAssociations.setSelectionForeground(new java.awt.Color(0,0,0));
						jAssociations.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								jClassesMouseClicked(evt);
							}
						});
                        sClasses = new JScrollPane();
                        sClasses.getViewport().setView(jAssociations);
                        sClasses.setBounds(21, 31, 252, 231);
                        jPanelLeft.add(sClasses,new GridBagConstraints (3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.NONE ,new Insets(0,0,0,0),0,0));

                    }
					{
						jLabel2 = new JLabel();
						jPanelLeft.add(jLabel2);
						jLabel2.setText("Associations (\"Source\".\"Target\")");
						jLabel2.setBounds(31, 11, 214, 14);
						jLabel2.setFont(new java.awt.Font("Tahoma",2,11));
					}
					cMAssoc = new CheckListManager(jAssociations, null); 
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
							jLabel3.setText("Joinpoints");
							jLabel3.setBounds(0, 11, 48, 14);
							jLabel3.setFont(new java.awt.Font("Tahoma",2,11));
						}
						{
							jJoinpoints = new JList();
							jPanelTR.add(jJoinpoints);
							setListModel(jJoinpoints, null);
							jJoinpoints.setCellRenderer(new AODBeanCellRenderer());
							jJoinpoints.setBounds(0, 25, 238, 102);
							jJoinpoints.setSelectionBackground(new java.awt.Color(249,243,183));
							jJoinpoints.setSelectionForeground(new java.awt.Color(0,0,0));
							jJoinpoints.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									jJointpointsMouseClicked(evt);
								}
							});
						}
						{
							sJointpoints = new JScrollPane();
							jPanelTR.add(sJointpoints, new GridBagConstraints(0, 1, 3, 3, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							sJointpoints.getViewport().setView(jJoinpoints);
							sJointpoints.setBounds(0, 25, 238, 101);
							jPanelTR.add(sJointpoints,new GridBagConstraints (3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.NONE ,new Insets(0,0,0,0),0,0));

						}
					}
					cMJoinPoint = new CheckListManager(jJoinpoints, jAssociations);
					{
						jPanelBR = new JPanel();
						jPanelRight.add(jPanelBR, BorderLayout.CENTER);
						jPanelBR.setPreferredSize(new java.awt.Dimension(285, 154));
						jPanelBR.setLayout(null);
						{
							jAdvices = new JList();
							jPanelBR.add(jAdvices);
							setListModel(jAdvices, null);
							jAdvices.setBounds(0, 15, 238, 110);
							jAdvices.setCellRenderer(new AODBeanCellRenderer());
							jAdvices.setSelectionBackground(new java.awt.Color(249,243,183));
							jAdvices.setSelectionForeground(new java.awt.Color(0,0,0));
						}
						{
							sAdvices = new JScrollPane();
							sAdvices.getViewport().setView(jAdvices);
							sAdvices.setBounds(0, 15, 235, 110);
							jPanelBR.add(sAdvices,new GridBagConstraints (3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,GridBagConstraints.NONE ,new Insets(0,0,0,0),0,0));
							sAdvices.setSize(238, 109);

						}
						cMAdvices = new CheckListManager (jAdvices, jAssociations);
						{
							jLabel4 = new JLabel();
							jPanelBR.add(jLabel4);
							jLabel4.setText("Advices");
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

	private void setListModel(JList list, Object[] items) {
		ListModel jModel = null;
		if (items!=null)
			jModel = new DefaultComboBoxModel(items);
		else
			jModel = new DefaultComboBoxModel();

		list.setModel(jModel);
	}

	
	private void jPreviousMouseClicked(MouseEvent evt) {
		previousFrame.setVisible(true);
		this.setVisible(false);
	}

	private void jNextMouseClicked(MouseEvent evt) {
			Integer value = JOptionPane.showConfirmDialog(null, "Are you sure you want to generate this .uml?", "Confirm UML generation", JOptionPane.YES_NO_OPTION);
			if (value == JOptionPane.OK_OPTION) {
				String fileName = openSaveDialog();
				if (fileName!=null){
					try{
						ArrayList<AODProfileBean> selectedBeans = updateSelection();
						XMIExporter xmiExporter = new XMIExporter(selectedBeans);
						xmiExporter.generateUMLFile(fileName);
						Di2Generator di2 = new Di2Generator(selectedBeans);
						di2.generateUMLFile(fileName);
						JOptionPane.showMessageDialog(this, "Generation completed succesfully!");
						System.exit(NORMAL);
					}catch (Exception e) {
						JOptionPane.showMessageDialog(this, "Generation failed!");
					}
				}
			}
		}
	
	private String openSaveDialog() {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();
		int seleccion = fileChooser.showOpenDialog(this);

		if (seleccion == JFileChooser.APPROVE_OPTION) {
			File useCaseFile = fileChooser.getSelectedFile();
			fileName = useCaseFile.getAbsolutePath();
			logger.info("File to save: " + fileName);
		}
		
		return fileName;
	}

	private ArrayList<AODProfileBean> updateSelection() {
		ArrayList<AODProfileBean> list = new ArrayList<AODProfileBean>();
		for (Object obj: aodClasses){
			AODProfileClass cl = (AODProfileClass) obj;
			//copy selected associations
			ArrayList<AODProfileAssociation> newAssocList = new ArrayList<AODProfileAssociation>();
			for (AODProfileAssociation assoc: cl.getPossibleAssociations()){
				if (assoc.isSelected()){
					//add assoc to class
					newAssocList.add(assoc);
				}
			}
			//set selected pc to class
			cl.setPossibleAssociations(newAssocList);
			
			//copy selected responsabilities
			ArrayList<AODProfileResponsability> newRespList = new ArrayList<AODProfileResponsability>();
			for (AODProfileResponsability resp: cl.getResponsabilities()){
				if (resp.isSelected()){
					//add assoc to class
					newRespList.add(resp);
				}
			}
			//set selected resp to class
			cl.setResponsabilities(newRespList);

			//copy selected attributes
			ArrayList<AODProfileAttribute> newAttrList = new ArrayList<AODProfileAttribute>();
			for (AODProfileAttribute attr: cl.getAttributes()){
				if (attr.isSelected()){
					//add assoc to class
					newAttrList.add(attr);
				}
			}
			//set selected attr to class
			cl.setAttributes(newAttrList);

			if (obj instanceof AODProfileAspect){
				AODProfileAspect asp = (AODProfileAspect) obj;
				ArrayList<AODProfilePointcut> newPClist = new ArrayList<AODProfilePointcut>();
				for (AODProfilePointcut pc: asp.getPossiblePointcuts()){
					if (pc.isSelected()){
						//copy selected JP
						ArrayList<AODProfileJoinPoint> newJP = new ArrayList<AODProfileJoinPoint>();
						for (AODProfileJoinPoint jp: pc.getJoinPoints()){
							if (jp.isSelected()){
								newJP.add(jp);
							}
						}
						pc.setJoinPoints(newJP);
						//copy selected Advices
						ArrayList<AODProfileAdvice> newAdv = new ArrayList<AODProfileAdvice>();
						for (AODProfileAdvice adv: pc.getAdvices()){
							if (adv.isSelected()){
								newAdv.add(adv);
							}
						}
						pc.setAdvices(newAdv);
						//add pointcut assoc to class
						newPClist.add(pc);
					}
				}
				//set selected pc to class
				asp.setPossiblePointcuts(newPClist);
			}
			list.add(cl);
		}
		
		return list;
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
	
	private void jCancelMouseClicked(MouseEvent evt) {
		Integer value = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
		if (value == JOptionPane.OK_OPTION) {
			this.dispose();
			System.exit(NORMAL);
		}
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

	public Object[] getObjects() {
		return objects;
	}

	public void setObjects(Object[] aodClasses) {
		this.objects = aodClasses;
	}
	
	private void jClassesMouseClicked(MouseEvent evt) {
		AODProfileAssociation assoc = (AODProfileAssociation) jAssociations.getSelectedValue();
		if (assoc instanceof AODProfilePointcut){
			AODProfilePointcut pc = (AODProfilePointcut) assoc;
			setListModel(jJoinpoints, pc.getJoinPoints().toArray());
			setListModel(jAdvices, null);
		}
		else{
			setListModel(jJoinpoints, null);
			setListModel(jAdvices, null);
		}
	}
	
	private void jJointpointsMouseClicked(MouseEvent evt) {
		AODProfileJoinPoint jp = (AODProfileJoinPoint) jJoinpoints.getSelectedValue();
		AODProfileAssociation assoc = (AODProfileAssociation) jAssociations.getSelectedValue();
		if (assoc instanceof AODProfilePointcut){
			AODProfilePointcut pc = (AODProfilePointcut) assoc;
			List<AODProfileAdvice> advices = pc.getAdvices();
			List<AODProfileAdvice> selectedAdvices = new ArrayList<AODProfileAdvice>();
			for (AODProfileAdvice adv: advices){
				if (jp.match(adv)){
					selectedAdvices.add(adv);
				}
			}		
			setListModel(jAdvices, selectedAdvices.toArray());
		}
	}

	public void setAodClasses(Object[] aodClasses) {
		this.aodClasses = aodClasses;
	}

	public void setPreviousFrame(JFrame previousFrame) {
		this.previousFrame = previousFrame;
	}

	
	
}
