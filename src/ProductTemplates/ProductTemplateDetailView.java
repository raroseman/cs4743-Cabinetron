package ProductTemplates;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ProductTemplateDetailView extends JPanel {
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;
	private ProductTemplateModel model;

	private JLabel productID, productNumber, productDescription, errorMessage;
	private JTextField idField, numberField, descriptionField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonX, buttonY, buttonLeft, buttonBottom,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop;
	private int minX, minY;
	
	public ProductTemplateDetailView(ProductTemplateModel model, int width, int height, int minX, int minY) {
		this.model = model;
		this.minX = minX;
		this.minY = minY;
		this.setSize(new Dimension(width, height));
		createPanel();
	}
	
	private void createPanel() {
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
		
		productID = new JLabel("ID");
		productID.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(productID);
		
		productNumber = new JLabel("Product Number");
		productNumber.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		partFrame.add(productNumber);
		
		productDescription = new JLabel("Product Description");
		productDescription.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		partFrame.add(productDescription);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 5), errorW, errorH);
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
		
		idField = new JTextField();
		idField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		partFrame.add(idField);
		
		numberField = new JTextField();
		numberField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		partFrame.add(numberField);
		
		descriptionField = new JTextField();
		descriptionField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(descriptionField);
		
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

		productID.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		productNumber.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		productDescription.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 5), errorW, errorH);
		cancel.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		ok.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		edit.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		save.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		idField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		numberField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		descriptionField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);

	}
	
	public void register(ProductTemplateListController controller) {
		ok.addActionListener(controller);
		cancel.addActionListener(controller);
		edit.addActionListener(controller);
		save.addActionListener(controller);
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
	public String getNumber() {
		return numberField.getText();
	}
	
    public String getDescription() {
    	return descriptionField.getText();
    }
	
	public void setErrorMessage(String error) {
		errorMessage.setText(error);
	}
	
	public void setID(Integer id) {
		idField.setText(String.valueOf(id));
	}
	
	public void setNumber(String number) {
		numberField.setText(number);
	}
	
	public void setDescription(String description) {
		descriptionField.setText(description);
	}
	
	public void hideID() {
		idField.setVisible(false);
		productID.setVisible(false);
	}
	
	public void hideEditButton() {
		edit.setVisible(false);
	}
	
	public void hideSaveButton() {
		save.setVisible(false);
	}
	
	public void disableIDEdit() {
		idField.setEnabled(false);
	}
	
	public void disableEditable() {
		ok.setVisible(false);
		save.setVisible(false);
		idField.setEnabled(false);
		numberField.setEnabled(false);
		descriptionField.setEnabled(false);
	}
	
	public void enableEditable() {
		save.setVisible(true);
		numberField.setEnabled(true);
		descriptionField.setEnabled(true);
	}
	
	public void resized() {
		resizePanel();
	}
}