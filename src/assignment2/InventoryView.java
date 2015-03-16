package assignment2;

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

@SuppressWarnings("serial")
public class InventoryView extends JFrame {
	private PartsInventoryModel partsModel;
	private InventoryItemModel model;
	private JPanel inventoryFrame;
	private JButton addPart, deletePart, viewPart;
	private int GUIWidth;
	private int GUIHeight;
	private String[] columnNames = {"ID", "Part ID", "Part Name", "Part Number", "Location", "Quantity"};
	private JTable table;
	private JScrollPane tableScrollPane;
	private JPanel p;
	private ListSelectionModel tableSelectionModel;
	private DefaultTableModel tableModel;
	private Object[] rowData;
	private JLabel errorMessage;
	private int buttonH, buttonW, buttonX, buttonY, errorW, errorH, errorX, errorY, tableMargin, tableW, tableH;
	
	public InventoryView(PartsInventoryModel partsModel, InventoryItemModel model) {
		super("Cabinetron: Inventory");
		this.partsModel = partsModel;
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
		this.setVisible(true);
		this.setLocation(0, 50);
		
		// Sets up the inventory frame 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inventoryFrame = new JPanel();
		inventoryFrame.setSize(1200, 1000);
		inventoryFrame.setBackground(Color.LIGHT_GRAY);
		inventoryFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		inventoryFrame.setOpaque(true);
		setContentPane(inventoryFrame);
		inventoryFrame.setLayout(null);
		
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
		
		refreshInventoryList();
		
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
			if (column.getHeaderValue().toString().equals("ID") ||
				column.getHeaderValue().toString().equals("Part ID")) {
				column.setPreferredWidth(GUIWidth / 15);
			}
			else if (column.getHeaderValue().toString().equals("Quantity")) {
				column.setPreferredWidth(GUIWidth / 10);
			}
			else {
				column.setPreferredWidth(GUIWidth / 5);
			}
		}

		p.add(tableScrollPane);
	
		p.setVisible(true);
		inventoryFrame.add(p);
		inventoryFrame.setVisible(true);
		this.setVisible(true);
		
		repaint();
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
			rowData = new Object[] { p.getID(), part.getID(), part.getPartName(), part.getPartNumber(), p.getLocation(), p.getQuantity()};
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
//		this.addWindowFocusListener(controller);
	}
	
	public int getWidth() {
		return GUIWidth;
	}
	
	public int getHeight() {
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
	
}
