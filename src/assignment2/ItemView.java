package assignment2;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ItemView extends JFrame {
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;


	private JLabel partName, itemQuantity, partLocationType, partID, errorMessage;
	private JTextField nameField, idField, quantityField;
	private JComboBox<String> locationUnitTypeField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonLeft, buttonBottom,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop;
	
	public ItemView(InventoryItemModel model, String title) {
		super(title);
		
		viewWidth = 400;
		viewHeight = 400;
		labelW = viewWidth / 4;
		labelH = 32;
		labelTop = 15;
		labelLeft = 15;
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = 96;
		buttonH = 32;
		buttonLeft = viewWidth / 3 - buttonW / 3;
		buttonBottom = viewHeight - buttonH - 64;
		fieldW = 200;
		fieldH = 32;
		fieldLeft = labelW + 25;
		fieldTop = labelTop;
		
		this.setSize(viewWidth, viewHeight);
		this.setVisible(true);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (viewWidth / 2) + 50, 
				 (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (viewHeight / 2));
		
		partFrame = new JPanel();
		partFrame.setBackground(Color.LIGHT_GRAY);
		partFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(partFrame);
		partFrame.setLayout(null);
		
		partName = new JLabel("Name");
		partName.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(partName);
		
		partID = new JLabel("ID");
		partID.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		partFrame.add(partID);
		
		itemQuantity = new JLabel("Quantity");
		itemQuantity.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		partFrame.add(itemQuantity);
	
		partLocationType = new JLabel("Location");
		partLocationType.setBounds(labelLeft, labelTop + (labelH * 3), labelW, labelH);
		partFrame.add(partLocationType);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 6), errorW, errorH);
		partFrame.add(errorMessage);
		
		cancel = new JButton("Cancel");
		cancel.setBounds(buttonLeft, buttonBottom, buttonW, buttonH);
		partFrame.add(cancel);
		
		ok = new JButton("OK");
		ok.setBounds(buttonLeft * 2, buttonBottom, buttonW, buttonH);
		partFrame.add(ok);
		
		edit = new JButton("Edit");
		edit.setBounds(buttonLeft * 2, buttonBottom, buttonW, buttonH);
		partFrame.add(edit);
		
		save = new JButton("Save");
		save.setBounds(buttonLeft * 2, buttonBottom, buttonW, buttonH);
		partFrame.add(save);
		
		nameField = new JTextField();
		nameField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		partFrame.add(nameField);
		
		idField = new JTextField();
		idField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		partFrame.add(idField);
		
		quantityField = new JTextField();
		quantityField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(quantityField);
		
		locationUnitTypeField = new JComboBox<String>();
		for (String unitType : model.getValidLocationTypes()) {
			locationUnitTypeField.addItem(unitType);
		}
		locationUnitTypeField.setBounds(fieldLeft, fieldTop + (fieldH * 3), fieldW, fieldH);
		partFrame.add(locationUnitTypeField);
	}
	
	public void register(InventoryController controller) {
		ok.addActionListener(controller);
		cancel.addActionListener(controller);
		edit.addActionListener(controller);
		save.addActionListener(controller);
	}
}
