package assignment2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class PartsInventoryView extends JFrame  {	
	private PartsInventoryModel model;

	private JPanel inventoryFrame;
	private JButton addPart, deletePart, viewPart;
	private int GUIWidth;
	private int GUIHeight;
	private String[] columnNames = {"ID", "Part Name", "Part Number", "External Part Number", "Vendor", "Quantity Unit Type"};
	private JTable table;
	private JScrollPane tableScrollPane;
	private JPanel p;
	private ListSelectionModel tableSelectionModel;
	private DefaultTableModel tableModel;
	private Object[] rowData;
	private JLabel errorMessage;
	private int buttonH, buttonW, buttonX, buttonY, errorW, errorH, errorX, errorY, tableMargin, tableW, tableH;

	public PartsInventoryView(PartsInventoryModel model) {
		super("Cabinetron: Parts");
		this.model = model;

		GUIWidth = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		GUIHeight = Toolkit.getDefaultToolkit().getScreenSize().height - 100;
		tableMargin = 15;
		tableW = GUIWidth - (tableMargin * 2);
		tableH = GUIHeight - 100;
		buttonW = 125;
		buttonH = 30;
		buttonX = 100;
		buttonY = GUIHeight - 75;
		errorW = GUIWidth - 20;
		errorH = 32;
		errorX = tableMargin;
		errorY = GUIHeight - 100;

		this.setSize(GUIWidth, GUIHeight);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2, 50);
		
		// Sets up the inventory frame 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inventoryFrame = new JPanel();
		inventoryFrame.setSize(GUIWidth, GUIHeight);
		inventoryFrame.setBackground(Color.LIGHT_GRAY);
		inventoryFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		inventoryFrame.setOpaque(true);
		setContentPane(inventoryFrame);
		inventoryFrame.setLayout(null);

		table = new JTable() {
			public boolean isCellEditable(int row, int col)
		    {
		        return false;
		    }
		};
		tableModel = (DefaultTableModel) table.getModel();
		table.setColumnSelectionAllowed(false);
		tableModel.setColumnIdentifiers(columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(GUIWidth, GUIHeight));
		

		
		for (Part p: model.getInventory()) {
			rowData = new Object[] {p.getID(), p.getPartName(), p.getPartNumber(), p.getExternalPartNumber(), p.getVendor(), p.getQuantityUnitType()};
			tableModel.addRow(rowData);
		}
	
		table.setModel(tableModel);
		p = new JPanel();
		p.setBounds(0, 0, GUIWidth, GUIHeight);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSelectionModel = table.getSelectionModel();
		
		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(tableW, tableH));
		tableScrollPane.setVisible(true);
		
		TableColumn column = null;
		
		for (int i = 0; i < columnNames.length; i++) {
			column = table.getColumnModel().getColumn(i);
			if (column.getHeaderValue().toString() == "ID") {
				column.setPreferredWidth(GUIWidth / 32);
			} if (column.getHeaderValue().toString() == "Vendor") {
				column.setPreferredWidth(GUIWidth / 16);
			} 
		}

		p.add(tableScrollPane);
		
		// Creates and adds the "add" button to the inventory frame
		addPart = new JButton("Add");
		addPart.setBounds(buttonX, buttonY, buttonW, buttonH);
		inventoryFrame.add(addPart);
		
		// Creates and adds the "delete" button to the inventory frame
		deletePart = new JButton("Delete");
		deletePart.setBounds((GUIWidth / 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		disableDelete();
		inventoryFrame.add(deletePart);
		
		// Creates and adds the "view" button to the inventory frame
		viewPart = new JButton("View");
		viewPart.setBounds((GUIWidth - buttonX) - buttonW, buttonY, buttonW, buttonH);
		disableView();
		inventoryFrame.add(viewPart);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(errorX, errorY, errorW, errorH);
		inventoryFrame.add(errorMessage);
		
		p.setVisible(true);
		inventoryFrame.add(p);
		inventoryFrame.setVisible(true);
		this.setVisible(true);
		
		repaint();
	}
	
	public void updatePanel() { // tears down the entire table and re-populates it
		tableModel.setRowCount(0);
		model.sortByCurrentSortMethod();
		for (Part p: model.getInventory()) {
			rowData = new Object[] {p.getID(), p.getPartName(), p.getPartNumber(), p.getExternalPartNumber(), p.getVendor(), p.getQuantityUnitType()};
			tableModel.addRow(rowData);
		}

		table.setModel(tableModel);
	}

	
	public void register(PartsInventoryController controller) {
		addPart.addActionListener(controller);
		deletePart.addActionListener(controller);
		viewPart.addActionListener(controller);
		tableSelectionModel.addListSelectionListener(controller);
		table.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int col = table.columnAtPoint(e.getPoint());
		        String columnName = table.getColumnName(col);
		        switch (columnName) {
		        case "ID":
		        	model.sortByID();
		        	break;
		        case "Part Name":
		        	model.sortByPartName();
		        	break;
		        case "Part Number":
		        	model.sortByPartNumber();
		        	break;
		        case "Vendor":
		        	model.sortByVendor();
		        	break;
		        case "Quantity Unit Type":
		        	model.sortByQuantityUnitType();
		        	break;
		        case "External Part Number":
		        	model.sortByPartName();
		        	break;
		        }
		        updatePanel();
		    }
		});
	}
	
	public int getWidth() {
		return GUIWidth;
	}
	
	public int getHeight() {
		return GUIHeight;
	}
	
	public void disableDelete() {
		deletePart.setEnabled(false);
	}
	
	public void enableDelete() {
		deletePart.setEnabled(true);
	}
	
	public void disableView() {
		viewPart.setEnabled(false);
	}
	
	public void enableView() {
		viewPart.setEnabled(true);
	}
	
	public void clearErrorMessage() {
		errorMessage.setText("");
	}
	
	public void setErrorMessage(String message) {
		errorMessage.setText(message);
	}
	
	public Part getObjectInRow(int index) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			if (table.getColumnName(i).equals("Part Name")) {
				return model.findPartName(table.getValueAt(index, i).toString());
			}
		}
		return null;
	}
}