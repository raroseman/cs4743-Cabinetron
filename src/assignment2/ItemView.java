package assignment2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ItemView extends JFrame {
	private InventoryItemModel model;
	private JPanel partFrame, sideSeparator = null, topSeparator = null;
	private JButton cancel, ok, edit, save;
	private JLabel ID, errorMessage, oldColumn = null, newColumn = null;
	private JLabel partIDLabel, itemQuantityLabel, partLocationLabel, timestampLabel;
	private JLabel oldPartID = null, oldItemQuantity = null, oldPartLocation = null;
	private JLabel newPartID = null, newItemQuantity = null, newPartLocation = null;
	private JLabel oldTimestamp = null, newTimestamp = null;
	private JTextField idField, quantityField;
	private JComboBox<String> partField, locationField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonBottom,
				labelW, labelH, labelTop, labelLeft,
				column1Left, column2Left, column3Left, center, centerW, sideW;
	private Font labelFont;
	
	public void showEditConflictWindow(InventoryItem oldDatabaseItem, InventoryItem userModifiedItem, InventoryItem newDatabaseItem) {
		viewWidth = 800;
		viewHeight = 300;
		
		labelW = viewWidth / 9 + (viewWidth / 18);
		sideW = (viewWidth / 9) * 2;
		centerW = (viewWidth / 9) * 3;
		labelH = 32;
		labelTop = 15;
		
		labelLeft = 0;
		column1Left = labelW; // start at 1/9 (2/9 width)
		column2Left = sideW + column1Left; // 3/9 start (3/9 + 1/18 width) = 7/18
		column3Left = centerW + column2Left; // 7/9 start (2/9 width)
		
		center = viewWidth / 2;
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = (viewWidth / 6);
		buttonH = 32;
		buttonBottom = viewHeight - buttonH - 64;
		
		this.setSize(viewWidth, viewHeight);
		
		// Only need one ID - all should be the same ID from database
		partFrame.remove(ID);
		partFrame.remove(partIDLabel);
		partFrame.remove(itemQuantityLabel);
		partFrame.remove(partLocationLabel);
		if (timestampLabel != null) partFrame.remove(timestampLabel); //4
		if (oldPartID != null) partFrame.remove(oldPartID);
		if (oldItemQuantity != null) partFrame.remove(oldItemQuantity);
		if (oldPartLocation != null) partFrame.remove(oldPartLocation);
		if (newPartID != null) partFrame.remove(newPartID);
		if (newItemQuantity != null) partFrame.remove(newItemQuantity);
		if (newPartLocation != null) partFrame.remove(newPartLocation);
		if (oldColumn != null) partFrame.remove(oldColumn);
		if (newColumn != null) partFrame.remove(newColumn);
		if (oldTimestamp != null) partFrame.remove(oldTimestamp);
		if (newTimestamp != null) partFrame.remove(newTimestamp);

		/*
		if (sideSeparator != null) partFrame.remove(sideSeparator);
		if (topSeparator != null) partFrame.remove(topSeparator);
		
		sideSeparator = new JPanel();
		sideSeparator.setBackground(Color.DARK_GRAY);
		sideSeparator.setBounds(labelW + 4, labelTop + (labelH * 1), 2, viewHeight);
		sideSeparator.setVisible(true);
		partFrame.add(sideSeparator);
		
		topSeparator = new JPanel();
		topSeparator.setBackground(Color.DARK_GRAY);
		topSeparator.setBounds(labelW + 4, labelTop + (labelH * 1), viewWidth, 2);
		topSeparator.setVisible(true);
		partFrame.add(topSeparator);
		*/
		
//4		// Show timestamp?	
		
		timestampLabel = new JLabel("Timestamp");
		timestampLabel.setFont(labelFont);
		timestampLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		timestampLabel.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		partFrame.add(timestampLabel);
		
		oldTimestamp = new JLabel(oldDatabaseItem.getTimestamp());
	//	oldTimestamp.setFont(labelFont);
		oldTimestamp.setHorizontalAlignment(SwingConstants.CENTER);
		oldTimestamp.setBounds(column1Left, labelTop + (labelH * 1), sideW, labelH);
		partFrame.add(oldTimestamp);
		
		newTimestamp = new JLabel(newDatabaseItem.getTimestamp());
	//	newTimestamp.setFont(labelFont);
		newTimestamp.setHorizontalAlignment(SwingConstants.CENTER);
		newTimestamp.setBounds(column3Left, labelTop + (labelH * 1), sideW, labelH);
		partFrame.add(newTimestamp);
		
		oldColumn = new JLabel("Last seen as: "); // maybe show timestamp as well?
		oldColumn.setFont(labelFont);
		oldColumn.setHorizontalAlignment(SwingConstants.CENTER);
		oldColumn.setBounds(column1Left, labelTop + (labelH * 0), sideW, labelH);
		partFrame.add(oldColumn);
		
		ID = new JLabel("ID: " + newDatabaseItem.getID());
		ID.setFont(labelFont);
		ID.setHorizontalAlignment(SwingConstants.CENTER);
		ID.setBounds(center - labelW / 2, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(ID);
		
		newColumn = new JLabel("Updated elsewhere as: ");
		newColumn.setFont(labelFont);
		newColumn.setHorizontalAlignment(SwingConstants.CENTER);
		newColumn.setBounds(column3Left, labelTop + (labelH * 0), sideW, labelH);
		partFrame.add(newColumn);
		
		partIDLabel = new JLabel("Part Number");
		partIDLabel.setFont(labelFont);
		partIDLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		partIDLabel.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		partFrame.add(partIDLabel);
		
		itemQuantityLabel = new JLabel("Quantity");
		itemQuantityLabel.setFont(labelFont);
		itemQuantityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		itemQuantityLabel.setBounds(labelLeft, labelTop + (labelH * 3), labelW, labelH);
		partFrame.add(itemQuantityLabel);

		partLocationLabel = new JLabel("Location");
		partLocationLabel.setFont(labelFont);
		partLocationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		partLocationLabel.setBounds(labelLeft, labelTop + (labelH * 4), labelW, labelH);
		partFrame.add(partLocationLabel);
		
		oldPartID = new JLabel(oldDatabaseItem.getPart().getPartNumber());
		oldPartID.setHorizontalAlignment(SwingConstants.CENTER);
		oldPartID.setBounds(column1Left, labelTop + (labelH * 2), sideW, labelH);
		partFrame.add(oldPartID);
		
		oldItemQuantity = new JLabel(oldDatabaseItem.getQuantity().toString());
		oldItemQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		oldItemQuantity.setBounds(column1Left, labelTop + (labelH * 3), sideW, labelH);
		partFrame.add(oldItemQuantity);

		oldPartLocation = new JLabel(oldDatabaseItem.getLocation());
		oldPartLocation.setHorizontalAlignment(SwingConstants.CENTER);
		oldPartLocation.setBounds(column1Left, labelTop + (labelH * 4), sideW, labelH);
		partFrame.add(oldPartLocation);
		
		partField.setBounds(column2Left, labelTop + (labelH * 2), centerW, labelH);
		quantityField.setBounds(column2Left, labelTop + (labelH * 3), centerW, labelH);
		locationField.setBounds(column2Left, labelTop + (labelH * 4), centerW, labelH);
		
		newPartID = new JLabel(newDatabaseItem.getPart().getPartNumber());
		newPartID.setHorizontalAlignment(SwingConstants.CENTER);
		newPartID.setBounds(column3Left, labelTop + (labelH * 2), sideW, labelH);
		partFrame.add(newPartID);
		
		newItemQuantity = new JLabel(newDatabaseItem.getQuantity().toString());
		newItemQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		newItemQuantity.setBounds(column3Left, labelTop + (labelH * 3), sideW, labelH);
		partFrame.add(newItemQuantity);

		newPartLocation = new JLabel(newDatabaseItem.getLocation());
		newPartLocation.setHorizontalAlignment(SwingConstants.CENTER);
		newPartLocation.setBounds(column3Left, labelTop + (labelH * 4), sideW, labelH);
		partFrame.add(newPartLocation);
		
		
		errorMessage.setBounds(center - (errorW / 2), labelTop + (labelH * 5), errorW, errorH);
		cancel.setBounds((int) (center - buttonW), buttonBottom, buttonW, buttonH);		
		ok.setBounds((int) (center), buttonBottom, buttonW, buttonH);
		edit.setBounds((int) (center), buttonBottom, buttonW, buttonH);
		save.setBounds((int) (center), buttonBottom, buttonW, buttonH);

		
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (viewWidth / 2) + 50, 
				 (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (viewHeight / 2));
		this.repaint();
	}
	
	public ItemView(InventoryItemModel model, String title) {
		super(title);
		this.model = model;
		
		viewWidth = 400;
		viewHeight = 300;
		
		labelW = (viewWidth / 9) * 2; // 2/9 wide
		centerW = (viewWidth / 9) * 6; // 6/9 wide (about 266px)

		labelH = 32;
		labelTop = 15;
		
		labelLeft = 4;
		column1Left = labelW + (viewWidth / 18); // start at 2/9 + 1/18 (6/9 width with 1/18 margin on both sides)
		
		center = viewWidth / 2;
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = viewWidth / 3;
		buttonH = 32;
		buttonBottom = viewHeight - buttonH - 64;
		
		this.setSize(viewWidth, viewHeight);
		this.setVisible(true);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (viewWidth / 2) + 50, 
				 (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (viewHeight / 2));
		
		partFrame = new JPanel();
		partFrame.setBackground(Color.LIGHT_GRAY);
		partFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(partFrame);
		partFrame.setLayout(null);
		
		ID = new JLabel();
		// Initialize the label font style (bold) used for all labels
		labelFont = ID.getFont();
		labelFont = new Font(labelFont.getFontName(), Font.BOLD, labelFont.getSize());
		ID.setFont(labelFont);
		ID.setHorizontalAlignment(SwingConstants.CENTER);
		ID.setBounds(center - labelW / 2, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(ID);
		
		
		partIDLabel = new JLabel("Part Number");
		partIDLabel.setFont(labelFont);
		partIDLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		partIDLabel.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		partFrame.add(partIDLabel);
		
		itemQuantityLabel = new JLabel("Quantity");
		itemQuantityLabel.setFont(labelFont);
		itemQuantityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		itemQuantityLabel.setBounds(labelLeft, labelTop + (labelH * 3), labelW, labelH);
		partFrame.add(itemQuantityLabel);
	
		partLocationLabel = new JLabel("Location");
		partLocationLabel.setFont(labelFont);
		partLocationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		partLocationLabel.setBounds(labelLeft, labelTop + (labelH * 4), labelW, labelH);
		partFrame.add(partLocationLabel);
		
		errorMessage = new JLabel("");
		errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 5), errorW, errorH);
		partFrame.add(errorMessage);
		
		cancel = new JButton("Cancel");
		cancel.setBounds((int) (center - buttonW), buttonBottom, buttonW, buttonH);
		partFrame.add(cancel);
		
		ok = new JButton("OK");
		ok.setBounds((int) (center), buttonBottom, buttonW, buttonH);
		partFrame.add(ok);
		
		edit = new JButton("Edit");
		edit.setBounds((int) (center), buttonBottom, buttonW, buttonH);
		partFrame.add(edit);
		
		save = new JButton("Save");
		save.setBounds((int) (center), buttonBottom, buttonW, buttonH);
		partFrame.add(save);
		
		partField = new JComboBox<String>();
		try {
			for (String part : model.getParts()) {
				partField.addItem(part);
			}
		} 
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		partField.setBounds(column1Left, labelTop + (labelH * 2), centerW, labelH);
		partFrame.add(partField);
		
		quantityField = new JTextField();
		quantityField.setBounds(column1Left, labelTop + (labelH * 3), centerW, labelH);
		partFrame.add(quantityField);
		
		locationField = new JComboBox<String>();
		try {
			for (String unitType : model.getLocations()) {
				locationField.addItem(unitType);
			}
		} 
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		locationField.setSelectedItem("Unknown"); // default Unknown; if it is not in the list, defaults to first item
		locationField.setBounds(column1Left, labelTop + (labelH * 4), centerW, labelH);
		partFrame.add(locationField);
	}
	
	public void register(InventoryController controller) {
		ok.addActionListener(controller);
		cancel.addActionListener(controller);
		edit.addActionListener(controller);
		save.addActionListener(controller);
	}
	
	public Integer getPartID() {
		Integer i = 0;
		try {
			i = model.getPartIDByPartNumber(partField.getSelectedItem().toString());
		}
		catch (SQLException e) {
			errorMessage.setText(e.getMessage());
		}
		return i;
	}
	
	public String getPartNumber() {
		return partField.getSelectedItem().toString();
	}
	
	public Integer getID() throws NumberFormatException {
		Integer i = 0;
		try {
			i = Integer.parseInt(idField.getText().trim());
			return i;
		}
		catch (NumberFormatException nfe) {
			throw new NumberFormatException("Error: id must be in the form of an integer.");
		}
	}
	
	public Integer getQuantity() throws NumberFormatException {
		Integer i = 0;
		try {
			i = Integer.parseInt(quantityField.getText().trim());
			return i;
		}
		catch (NumberFormatException nfe) {
			throw new NumberFormatException("Error: quantity must be in the form of an integer.");
		}
	}
	
	public String getLocationName() {
		int index = locationField.getSelectedIndex();
		return locationField.getItemAt(index);
	}
	
	public void setErrorMessage(String error) {
		errorMessage.setText(error);
	}
	
	public void setPartNumber(String partNumber) {
		partField.setSelectedItem(partNumber);
	}
	
	public void setPartNumberByID(Integer ID) {

		try {
			partField.setSelectedItem(model.getPartNumberByID(ID));
		} 
		catch (SQLException e) {
			errorMessage.setText(e.getMessage());
		}
	}
	
	public void setID(Integer id) {
		ID.setText("ID: " + String.valueOf(id));
	}
	
	public void setQuantity(Integer quantity) {
		quantityField.setText(String.valueOf(quantity));
	}
	
	public void setLocationType(String quantityUnitType) {
		locationField.setSelectedItem(quantityUnitType);
	}
	
	public void hideID() {
		idField.setVisible(false);
		ID.setVisible(false);
	}
	
	public void hideEditButton() {
		edit.setVisible(false);
	}
	
	public void hideSaveButton() {
		save.setVisible(false);
	}
	
	public void disableEditable() {
		ok.setVisible(false);
		save.setVisible(false);
		partField.setEnabled(false);
		quantityField.setEnabled(false);
		locationField.setEnabled(false);
	}
	
	public void enableEditable() {
		save.setVisible(true);
		partField.setEnabled(true);
		quantityField.setEnabled(true);
		locationField.setEnabled(true);
	}
}