package InventoryItems;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
public class ItemView extends JPanel {
	private InventoryItemModel model;
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;
	private JLabel ID, productTemplateID, description, errorMessage, mergeMessage, oldColumn = null, newColumn = null;
	private JLabel partIDLabel, itemQuantityLabel, partLocationLabel, productTemplateIDLabel, timestampLabel;
	private JLabel oldPartID = null, oldItemQuantity = null, oldProductTemplateID = null, oldPartLocation = null;
	private JLabel newPartID = null, newItemQuantity = null, newProductTemplateID = null, newPartLocation = null;
	private JLabel oldTimestamp = null, newTimestamp = null;
	private JTextField quantityField;
	private JComboBox<String> partField, locationField, productTemplateField;
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonX, buttonY, fieldW, fieldH, fieldLeft, fieldTop, buttonBottom,
				labelW, labelH, labelTop, labelLeft,
				column1Left, column2Left, column3Left, center, centerW, sideW;
	private Font labelFont;
	private int minX, minY;
	private boolean inConflictWindow = false;
	
	private int labelWidth = 2;
	private int oldDataWidth = 3;
	private int currentDataWidth = 3;
	private int newDataWidth = 3;
	private int totalWidth = labelWidth + oldDataWidth + currentDataWidth + newDataWidth + labelWidth; // add margin 
	private int x = 0, y = 0;
	
	void format(JLabel label, int width, int alignment, int gridX, int gridY) {
		label.setHorizontalAlignment(alignment);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.gridx = gridX;
		constraints.gridy = gridY;
		constraints.gridwidth = width;
		x += width;
		partFrame.add(label, constraints);
	}
	
	public void showEditConflictWindow(InventoryItem oldDatabaseItem, InventoryItem userModifiedItem, InventoryItem newDatabaseItem, int width) {		
		inConflictWindow = true;
		viewWidth = width;
		viewHeight = 325;
		this.setSize(viewWidth, viewHeight);
		partFrame.removeAll();
		partFrame.setLayout(new GridBagLayout());
		GridBagConstraints constraints;
		x = 0; y = 0;
		
		ID = new JLabel("ID: " + newDatabaseItem.getID());
		ID.setFont(labelFont);
		format(ID, totalWidth, SwingConstants.CENTER, x, y);
	
		x = 0; y = 1;
		
		timestampLabel = new JLabel("Timestamp");
		timestampLabel.setFont(labelFont);
		format(timestampLabel, labelWidth, SwingConstants.RIGHT, x, y);
		
		oldTimestamp = new JLabel(oldDatabaseItem.getTimestamp().substring(0, oldDatabaseItem.getTimestamp().length() - 2));
		format(oldTimestamp, oldDataWidth, SwingConstants.CENTER, x, y);
		
		x += currentDataWidth;
		
		newTimestamp = new JLabel(newDatabaseItem.getTimestamp().substring(0, newDatabaseItem.getTimestamp().length() - 2));
		format(newTimestamp, newDataWidth, SwingConstants.CENTER, x, y);
		
		x = 0; y = 2;
		
		partIDLabel = new JLabel("Part Number");
		partIDLabel.setFont(labelFont);
		format(partIDLabel, labelWidth, SwingConstants.RIGHT, x, y);
		
		oldPartID = new JLabel(oldDatabaseItem.getPart().getPartNumber());
		format(oldPartID, oldDataWidth, SwingConstants.CENTER, x, y);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = currentDataWidth;
		x += currentDataWidth;
		partFrame.add(partField, constraints);
		
		newPartID = new JLabel(newDatabaseItem.getPart().getPartNumber());
		format(newPartID, newDataWidth, SwingConstants.CENTER, x, y);
		
		x = 0; y = 3;
		
		itemQuantityLabel = new JLabel("Quantity");
		itemQuantityLabel.setFont(labelFont);
		format(itemQuantityLabel, labelWidth, SwingConstants.RIGHT, x, y);
		
		oldItemQuantity = new JLabel(oldDatabaseItem.getQuantity().toString());
		format(oldItemQuantity, oldDataWidth, SwingConstants.CENTER, x, y);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = currentDataWidth;
		x += currentDataWidth;
		partFrame.add(quantityField, constraints);
		
		newItemQuantity = new JLabel(newDatabaseItem.getQuantity().toString());
		format(newItemQuantity, newDataWidth, SwingConstants.CENTER, x, y);
		
		x = 0; y = 4;
		
		productTemplateIDLabel = new JLabel("Product Template ID");
		productTemplateIDLabel.setFont(labelFont);
		format(productTemplateIDLabel, labelWidth, SwingConstants.RIGHT, x, y);
		
		oldProductTemplateID = new JLabel(oldDatabaseItem.getProductTemplateID().toString());
		format(oldProductTemplateID, oldDataWidth, SwingConstants.CENTER, x, y);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = currentDataWidth;
		x += currentDataWidth;
		partFrame.add(productTemplateID, constraints);
		
		newProductTemplateID = new JLabel(newDatabaseItem.getProductTemplateID().toString());
		format(newProductTemplateID, newDataWidth, SwingConstants.CENTER, x, y);
		
		x = 0; y = 5;
		
		partLocationLabel = new JLabel("Location");
		partLocationLabel.setFont(labelFont);
		format(partLocationLabel, labelWidth, SwingConstants.RIGHT, x, y);
		
		oldPartLocation = new JLabel(oldDatabaseItem.getLocation());
		format(oldPartLocation, oldDataWidth, SwingConstants.CENTER, x, y);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = currentDataWidth;
		x += currentDataWidth;
		partFrame.add(locationField, constraints);
		
		newPartLocation = new JLabel(newDatabaseItem.getLocation());
		format(newPartLocation, newDataWidth, SwingConstants.CENTER, x, y);
		
		x = 0; y = 6;
		
		format(errorMessage, totalWidth, SwingConstants.CENTER, x, y);
		
		x = 0; y = 7;
		
		if (oldDatabaseItem.getQuantity() != newDatabaseItem.getQuantity()) {
			int dataDiff = newDatabaseItem.getQuantity() - oldDatabaseItem.getQuantity();
			int userDiff = getQuantity() - oldDatabaseItem.getQuantity();
			int mergeQuantity = getQuantity() + (dataDiff);
			String dataMod = "";
			String userMod = "";
			if (dataDiff > 0) {
				dataMod = "The latest change added " + dataDiff + ". ";
			}
			else {
				dataMod = "The latest change removed "  + Math.abs(dataDiff) + ". ";
			}
			if (userDiff > 0) {
				userMod = "You added " + userDiff + ". ";
			}
			else {
				userMod = "You removed " + Math.abs(dataDiff) + ". ";
			}
			mergeMessage = new JLabel("");
			mergeMessage.setForeground(Color.red);
			mergeMessage.setText("Quantity: " + userMod + dataMod + "Recommended value to save: " + mergeQuantity);
			format(mergeMessage, totalWidth, SwingConstants.CENTER, x, y);
		}
		
		x = 0 + labelWidth; y = 7;
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = oldDataWidth;
		x += oldDataWidth;
		partFrame.add(cancel, constraints);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = newDataWidth;
		partFrame.add(ok, constraints);
		partFrame.add(edit, constraints);
		save.setText("Override");
		partFrame.add(save, constraints);
		partFrame.validate();
		partFrame.setVisible(true);
		this.validate();
		this.repaint();
	}
	
	public ItemView(InventoryItemModel model, int width, int height, int minX, int minY) {
		this.model = model;
		this.setLayout(new BorderLayout());
		this.setSize(width, height);
		this.minX = minX;
		this.minY = minY;
		createPanel();
	}
	
	void createPanel() {
		viewWidth = Math.max(minX, this.getWidth());
		viewHeight = Math.max(minY, this.getHeight());
		labelW = viewWidth / 3;
		labelH = 32;
		labelTop = 10;
		labelLeft = 15;
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = viewWidth / 3;
		buttonH = 32;
		buttonX = viewWidth / 3;
		buttonY = viewHeight - 100;
		fieldW = viewWidth / 2;
		fieldH = 32;
		fieldLeft = labelW + 25;
		fieldTop = labelTop;
		
		this.setSize(viewWidth, viewHeight);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		partFrame = new JPanel();
		partFrame.setSize(viewWidth, viewHeight);
		partFrame.setBackground(Color.LIGHT_GRAY);
		partFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		partFrame.setLayout(null);
		
		ID = new JLabel();
		// Initialize the label font style (bold) used for all labels
		labelFont = ID.getFont();
		labelFont = new Font(labelFont.getFontName(), Font.BOLD, labelFont.getSize());
		ID.setFont(labelFont);
		ID.setHorizontalAlignment(SwingConstants.CENTER);
		ID.setBounds((viewWidth / 2) - (fieldW / 2), fieldTop + (fieldH * 0), fieldW, fieldH);
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
		
		description = new JLabel("");
		description.setHorizontalAlignment(SwingConstants.CENTER);
		description.setBounds(labelLeft, labelTop + (labelH * 5), errorW, errorH);
		description.setAutoscrolls(true);
		partFrame.add(description);

		productTemplateID = new JLabel("");
		productTemplateID.setHorizontalAlignment(SwingConstants.CENTER);
		productTemplateID.setBounds(labelLeft, labelTop + (labelH * 6), errorW, errorH);
		partFrame.add(productTemplateID);
		
		errorMessage = new JLabel("");
		errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 7), errorW, errorH);
		partFrame.add(errorMessage);
		
		cancel = new JButton("Cancel");
		cancel.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(cancel);
		
		ok = new JButton("OK");
		ok.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(ok);
		
		edit = new JButton("Edit");
		edit.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(edit);
		
		save = new JButton("Save");
		save.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
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
		partField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(partField);
		
		productTemplateField = new JComboBox<String>();
		try {
			for (String template : model.getProductTemplates()) {
				productTemplateField.addItem(template);
			}
		} 
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		productTemplateField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(productTemplateField);
		
		quantityField = new JTextField();
		quantityField.setBounds(fieldLeft, fieldTop + (fieldH * 3), fieldW, fieldH);
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
		locationField.setBounds(fieldLeft, fieldTop + (fieldH * 4), fieldW, fieldH);
		partFrame.add(locationField);
		this.add(partFrame, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private void resizePanel() {
		viewWidth = Math.max(minX, this.getWidth());
		viewHeight = Math.max(minY, this.getHeight());
		labelW = viewWidth / 3;
		labelH = 32;
		labelTop = 10;
		labelLeft = 15;
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = viewWidth / 3;
		buttonH = 32;
		buttonX = viewWidth / 3;
		buttonY = viewHeight - 100;
		fieldW = viewWidth / 2;
		fieldH = 32;
		fieldLeft = labelW + 25;
		fieldTop = labelTop;
		
		this.setSize(viewWidth, viewHeight);
		partFrame.setSize(viewWidth, viewHeight);
		ID.setBounds((viewWidth / 2) - (fieldW / 2), fieldTop + (fieldH * 0), fieldW, fieldH);
		partIDLabel.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		itemQuantityLabel.setBounds(labelLeft, labelTop + (labelH * 3), labelW, labelH);
		partLocationLabel.setBounds(labelLeft, labelTop + (labelH * 4), labelW, labelH);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 5), errorW, errorH);
		cancel.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		ok.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		edit.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		save.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		partField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		productTemplateField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		quantityField.setBounds(fieldLeft, fieldTop + (fieldH * 3), fieldW, fieldH);
		locationField.setBounds(fieldLeft, fieldTop + (fieldH * 4), fieldW, fieldH);
		
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
			//String IDstr = ID.getText().split("/ /")[1]; // Splits "ID: ###";
			String IDstr = ID.getText().substring(4, ID.getText().length());
			i = Integer.parseInt(IDstr.trim());
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
	
	public Integer getProductTemplateID() {
		return Integer.parseInt(this.productTemplateID.getText());
	}
	
	public String getErrorMessage() {
		return errorMessage.getText();
	}
	
	public String getDescription() {
		return this.description.getText();
	}
	
	public String getProductNumber() {
		int index = productTemplateField.getSelectedIndex();
		return productTemplateField.getItemAt(index);
	}
	
	public void setDescription(String description) {
		this.description.setText(description);
	}
	
	public void setErrorMessage(String error) {
		errorMessage.setText(error);
	}
	
	public void setPartNumber(String partNumber) {
		partField.setSelectedItem(partNumber);
	}
	
	public void setProductNumber(String productNumber) {
		productTemplateField.setSelectedItem(productNumber);
	}
	
	public void setProductTemplateID(Integer id) {
		productTemplateID.setText(id.toString());
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
		ID.setVisible(false);
	}
	
	public void hideEditButton() {
		edit.setVisible(false);
	}
	
	public void hideSaveButton() {
		save.setVisible(false);
	}
	
	public void partsMode() {
		partField.setVisible(true);
		productTemplateField.setVisible(false);
	}
	
	public void productMode() {
		partField.setVisible(false);
		productTemplateField.setVisible(true);
	}
	
	public void disableEdit() {
		edit.setEnabled(false);
	}
	
	public void disableEditable() {
		ok.setVisible(false);
		save.setVisible(false);
		partField.setEnabled(false);
		productTemplateField.setEnabled(false);
		quantityField.setEnabled(false);
		locationField.setEnabled(false);
	}
	
	public void enableEditable() {
		save.setVisible(true);
		partField.setEnabled(true);
		productTemplateField.setEnabled(true);
		quantityField.setEnabled(true);
		locationField.setEnabled(true);
	}
	
	public void resized() {
		if (!inConflictWindow) {
			resizePanel();
		}
	}
}