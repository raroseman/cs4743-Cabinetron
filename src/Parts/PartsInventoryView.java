package Parts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class PartsInventoryView extends JPanel {	
	private PartsInventoryModel model;

	private JPanel tablePanel;
	private JButton addPart, deletePart, viewPart;
	private int GUIWidth;
	private int GUIHeight;
	private String[] columnNames = {"ID", "Part Name", "Part Number", "External Part Number", "Vendor", "Quantity Unit Type"};
	private JTable table;
	private JScrollPane tableScrollPane;
	private ListSelectionModel tableSelectionModel;
	private DefaultTableModel tableModel;
	private Object[] rowData;
	private JLabel errorMessage;
	private int buttonH, buttonW, buttonX, buttonY, errorW, errorH, errorX, errorY, tableMargin, tableW, tableH, minX, minY;

	public PartsInventoryView(PartsInventoryModel model, int width, int height, int minX, int minY) {
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
		
		tablePanel = new JPanel();
		tablePanel.setBounds(0, 0, GUIWidth, GUIHeight);
		tablePanel.setLayout(null);

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
		        		c.setBackground(new Color(237, 252, 252));
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

		model.sortByCurrentSortMethod();
		for (Part p: model.getInventory()) {
			rowData = new Object[] {p.getID(), p.getPartName(), p.getPartNumber(), p.getExternalPartNumber(), p.getVendor(), p.getQuantityUnitType()};
			tableModel.addRow(rowData);
		}
	
		table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSelectionModel = table.getSelectionModel();
		
		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setBounds(tableMargin, 10, tableW, tableH);
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

		tablePanel.add(tableScrollPane);
		
		// Creates and adds the "add" button to the inventory frame
		addPart = new JButton("Add");
		addPart.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		addPart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
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
		tablePanel.setVisible(true);
		
		this.add(tablePanel, BorderLayout.CENTER);
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
	
	public int getViewWidth() {
		return GUIWidth;
	}
	
	public int getViewHeight() {
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
	
	public void setSelectedRow(Integer rowIndex) {
		tableSelectionModel.setSelectionInterval(0, rowIndex);
	}
	
	public Part getObjectInRow(int index) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			if (table.getColumnName(i).equals("Part Name")) {
				return model.findPartName(table.getValueAt(index, i).toString());
			}
		}
		return null;
	}

	public void resized() {
		this.removeAll();
		createPanel();
	}
}