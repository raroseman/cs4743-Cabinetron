package InventoryItems;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import Parts.Part;
import Parts.PartsInventoryModel;

@SuppressWarnings("serial")
public class InventoryView extends JPanel  {
	private PartsInventoryModel partsModel;
	private InventoryItemModel model;
	private JPanel tablePanel;
	private JButton addPart, deletePart, viewPart;
	private int GUIWidth;
	private int GUIHeight;
	private String[] columnNames = {"ID", "Part ID", "Part Name", "Part Number", "Product Template ID", "Product Description", "Location", "Quantity"};
	private JTable table;
	private JScrollPane tableScrollPane;
	private ListSelectionModel tableSelectionModel;
	private DefaultTableModel tableModel;
	private Object[] rowData;
	private JLabel errorMessage;
	private int buttonH, buttonW, buttonX, buttonY, errorW, errorH, errorX, errorY, tableMargin, tableW, tableH;
	private int minX, minY;
	
	public InventoryView(PartsInventoryModel partsModel, InventoryItemModel model, int width, int height, int minX, int minY) {
		this.partsModel = partsModel;
		this.model = model;
		this.minX = minX;
		this.minY = minY;
		this.setSize(width, height);
		createPanel();
	}
	
	private void createPanel() {
		tableMargin = 15;
		GUIWidth = Math.max(minX - tableMargin * 2, this.getWidth());
		GUIHeight = Math.max(minY - tableMargin * 2, this.getHeight());
		tableW = GUIWidth - (tableMargin * 2);
		tableH = GUIHeight - 85;
		errorW = GUIWidth - 20;
		errorH = 32;
		errorX = tableMargin;
		errorY = GUIHeight - 85;
		buttonW = GUIWidth / 4;
		buttonH = 30;
		buttonX = GUIWidth / 4;
		buttonY = GUIHeight - 50;
		
		
		this.setPreferredSize(new Dimension(GUIWidth, GUIHeight));
		this.setLayout(new BorderLayout());
		
		// Sets up the inventory frame 
		tablePanel = new JPanel();
		tablePanel.setBounds(0, 0, GUIWidth, GUIHeight);
		tablePanel.setLayout(null);
		
		// Creates and adds the "add" button to the inventory frame
		addPart = new JButton("Add");
		addPart.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		tablePanel.add(addPart);
		
		// Creates and adds the "delete" button to the inventory frame
		deletePart = new JButton("Delete");
		deletePart.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		disableDelete();
		tablePanel.add(deletePart);
		
		// Creates and adds the "view" button to the inventory frame
		viewPart = new JButton("View");
		viewPart.setBounds((buttonX * 3) - (buttonW / 2), buttonY, buttonW, buttonH);
		disableView();
		tablePanel.add(viewPart);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(errorX, errorY, errorW, errorH);
		tablePanel.add(errorMessage);
		
		table = new JTable() {
			public boolean isCellEditable(int row, int col)
		    {
		        return false;
		    }
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
		    {
		        Component c = super.prepareRenderer(renderer, row, column);

		        if (!isRowSelected(row)) {
		        	if (row % 2 == 0) {
		        		c.setBackground(new Color(236, 249, 221));
		        	}
		        	else {
		        		c.setBackground(getBackground());
		        	}
		        }
		        return c;
		    }
		};
		
		tableModel = (DefaultTableModel) table.getModel();
		table.setColumnSelectionAllowed(false);
		tableModel.setColumnIdentifiers(columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(GUIWidth, GUIHeight));		
		
		refreshInventoryList(); // sets the table model data

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSelectionModel = table.getSelectionModel();
		
		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setBounds(tableMargin, 10, tableW, tableH);
		tableScrollPane.setVisible(true);
		
		TableColumn column = null;
		
		for (int i = 0; i < columnNames.length; i++) {
			column = table.getColumnModel().getColumn(i);
			if (column.getHeaderValue().toString().equals("ID") ||
				column.getHeaderValue().toString().equals("Part ID")) {
				column.setPreferredWidth(GUIWidth / 32);
			}
			else {
				column.setPreferredWidth(GUIWidth / 16);
			}
		}
		
		tablePanel.add(tableScrollPane);

		tablePanel.setVisible(true);
		
		this.add(tablePanel, BorderLayout.CENTER);
		this.setVisible(true);
		
		repaint();
	}
	
	private void resizePanel() {
		tableMargin = 15;
		GUIWidth = Math.max(minX - tableMargin * 2, this.getWidth());
		GUIHeight = Math.max(minY - tableMargin * 2, this.getHeight());
		tableW = GUIWidth - (tableMargin * 2);
		tableH = GUIHeight - 85;
		errorW = GUIWidth - 20;
		errorH = 32;
		errorX = tableMargin;
		errorY = GUIHeight - 85;
		buttonW = GUIWidth / 4;
		buttonH = 30;
		buttonX = GUIWidth / 4;
		buttonY = GUIHeight - 50;
		
		this.setPreferredSize(new Dimension(GUIWidth, GUIHeight));

		tablePanel.setBounds(0, 0, GUIWidth, GUIHeight);
		addPart.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		deletePart.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		viewPart.setBounds((buttonX * 3) - (buttonW / 2), buttonY, buttonW, buttonH);
		errorMessage.setBounds(errorX, errorY, errorW, errorH);
		table.setPreferredScrollableViewportSize(new Dimension(GUIWidth, GUIHeight));		
		tableScrollPane.setBounds(tableMargin, 10, tableW, tableH);
		
		TableColumn column = null;
		for (int i = 0; i < columnNames.length; i++) {
			column = table.getColumnModel().getColumn(i);
			if (column.getHeaderValue().toString().equals("ID") ||
				column.getHeaderValue().toString().equals("Part ID")) {
				column.setPreferredWidth(GUIWidth / 32);
			}
			else {
				column.setPreferredWidth(GUIWidth / 16);
			}
		}
	}
	
	public void updatePanel() { // tears down the entire table and re-populates it
		refreshInventoryList();
	}
	
	private void refreshInventoryList() {
		tableModel.setRowCount(0);
		model.refreshInventory();
		model.sortByCurrentSortMethod();
		for (InventoryItem p: model.getInventory()) {
			Part part = p.getPart();
			if (part != null) {
				rowData = new Object[] { p.getID(), part.getID(), part.getPartName(), part.getPartNumber(), "", "", p.getLocation(), p.getQuantity()};
			}
			else {
				rowData = new Object[] { p.getID(), "", "", "", p.getProductTemplateID(), p.getDescription(), p.getLocation(), p.getQuantity()};	
			}
			tableModel.addRow(rowData);
		}
		if (partsModel.getSize() == 0) {
			disableAdd();
		}
		else {
			enableAdd();
		}
		table.setModel(tableModel);
	}
	
	public void register(InventoryController controller) {
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
		        case "Part ID":
		        	model.sortByPartID();
		        	break;
		        case "Part Name":
		        	model.sortByPartName();
		        	break;
		        case "Part Number":
		        	model.sortByPartNumber();
		        	break;
		        case "Location":
		        	model.sortByLocation();
		        	break;
		        case "Quantity":
		        	model.sortByQuantity();
		        	break;
		        }
		        updatePanel();
		    }
		});
	}
	
	public int getViewWidth() {
		return GUIWidth;
	}
	
	public int getViewHeight() {
		return GUIHeight;
	}
	
	public void disableAdd() {
		addPart.setEnabled(false);
	}
	
	public void enableAdd() {
		addPart.setEnabled(true);
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
	
	public void hideView() {
		viewPart.setVisible(false);
	}
	
	public void hideAdd() {
		addPart.setVisible(false);
	}
	
	public void hideDelete() {
		deletePart.setVisible(false);
	}
	
	public void clearErrorMessage() {
		errorMessage.setText("");
	}
	
	public void setErrorMessage(String message) {
		errorMessage.setText(message);
	}
	
	public void setSelectedRow(Integer rowIndex) {
		tableSelectionModel.setSelectionInterval(0, rowIndex);
	}
	
	public InventoryItem getObjectInRow(int index) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			if (table.getColumnName(i).equals("ID")) {
				return model.findItemByID(Integer.parseInt(table.getValueAt(index, i).toString()));
			}
		}
		return null;
	}
	public void resized() {
		resizePanel();
	}
}
