package ProductTemplates;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class ProductTemplateListView extends JPanel  {	
	private ProductTemplateModel model;

	private JPanel tablePanel;
	private JButton addPart, deletePart, viewPart, templatePart;
	private int GUIWidth;
	private int GUIHeight;
	private String[] columnNames = {"ID", "Product Number", "Product Description"};
	private JTable table;
	private JScrollPane tableScrollPane;
	private JPanel p;
	private ListSelectionModel tableSelectionModel;
	private DefaultTableModel tableModel;
	private Object[] rowData;
	private JLabel errorMessage;
	private int buttonH, buttonW, buttonX, buttonY, errorW, errorH, errorX, errorY, tableMargin, tableW, tableH;
	private int minX, minY;
	
	public ProductTemplateListView(ProductTemplateModel model, int width, int height, int minX, int minY) {
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
		buttonW = GUIWidth / 5;
		buttonH = 30;
		buttonX = GUIWidth / 5;
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
		        		c.setBackground(new Color(241, 209, 243));
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
		for (ProductTemplate p: model.getProductTemplates()) {
			rowData = new Object[] {p.getID(), p.getProductNumber(), p.getDescription()};
			tableModel.addRow(rowData);
		}
	
		table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSelectionModel = table.getSelectionModel();
		
		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setBounds(tableMargin, 10, tableW, tableH);
		tableScrollPane.setVisible(true);

		tablePanel.add(tableScrollPane);
		
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
		
		templatePart = new JButton("Parts List");
		templatePart.setBounds((buttonX * 4) - (buttonW / 2), buttonY, buttonW, buttonH);
		disablePartsList();
		tablePanel.add(templatePart);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(errorX, errorY, errorW, errorH);
		tablePanel.add(errorMessage);
		
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
		buttonW = GUIWidth / 5;
		buttonH = 30;
		buttonX = GUIWidth / 5;
		buttonY = GUIHeight - 50;

		this.setPreferredSize(new Dimension(GUIWidth, GUIHeight));
		tablePanel.setBounds(0, 0, GUIWidth, GUIHeight);
		tableScrollPane.setBounds(tableMargin, 10, tableW, tableH);
		addPart.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		deletePart.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		viewPart.setBounds((buttonX * 3) - (buttonW / 2), buttonY, buttonW, buttonH);
		templatePart.setBounds((buttonX * 4) - (buttonW / 2), buttonY, buttonW, buttonH);
		errorMessage.setBounds(errorX, errorY, errorW, errorH);
	}
	
	public void register(ProductTemplateListController controller) {
		templatePart.addActionListener(controller);
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
		        case "Part Number":
		        	model.sortByPartNumber();
		        	break;
		        case "Product Description":
		        	model.sortByDescription();
		        	break;
		        }
		        updatePanel();
		    }
		});
	}
	
	public void updatePanel() { // tears down the entire table and re-populates it
		tableModel.setRowCount(0);
		model.sortByCurrentSortMethod();
		for (ProductTemplate p: model.getProductTemplates()) {
			rowData = new Object[] {p.getID(), p.getProductNumber(), p.getDescription()};
			tableModel.addRow(rowData);
		}

		table.setModel(tableModel);
	}
	
	public void disableView() {
		viewPart.setEnabled(false);
	}
	
	public void disableDelete() {
		deletePart.setEnabled(false);
	}
	
	public void disableTemplateParts() {
		templatePart.setEnabled(false);
	}
	
	public void disablePartsList() {
		templatePart.setEnabled(false);
	}
	
	public void enablePartsList() {
		templatePart.setEnabled(true);
	}
	
	public void enableDelete() {
		deletePart.setEnabled(true);
	}
	
	public void enableView() {
		viewPart.setEnabled(true);
	}
	
	public void enableTemplateParts() {
		templatePart.setEnabled(true);
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
	
	public ProductTemplate getObjectInRow(int index) {
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
