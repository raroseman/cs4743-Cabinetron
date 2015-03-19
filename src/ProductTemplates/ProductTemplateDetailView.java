package ProductTemplates;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ProductTemplateDetailView extends JFrame {
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;


	private JLabel productID, productNumber, productDescription, errorMessage;
	private JTextField idField, numberField, descriptionField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonLeft, buttonBottom,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop;
	
	public ProductTemplateDetailView(ProductTemplateModel model, String title) {
		super(title);
		
		viewWidth = 400;
		viewHeight = 350;
		labelW = viewWidth / 3;
		labelH = 32;
		labelTop = 15;
		labelLeft = 15;
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = viewWidth / 4;
		buttonH = 32;
		buttonLeft = viewWidth / 3 - buttonW / 3;
		buttonBottom = viewHeight - buttonH - 64;
		fieldW = viewWidth / 2;
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
		
		idField = new JTextField();
		idField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		partFrame.add(idField);
		
		numberField = new JTextField();
		numberField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		partFrame.add(numberField);
		
		descriptionField = new JTextField();
		descriptionField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(descriptionField);
		
	}
	
	public void register(ProductTemplateListController controller) {
		ok.addActionListener(controller);
		cancel.addActionListener(controller);
		edit.addActionListener(controller);
		save.addActionListener(controller);
		this.addWindowFocusListener(controller);
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
}