package assignment4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import InventoryItems.InventoryController;
import InventoryItems.InventoryView;
import InventoryItems.ItemView;
import Parts.PartView;
import Parts.PartsInventoryController;
import Parts.PartsInventoryView;
import ProductTemplateParts.ProductTemplatePartDetailController;
import ProductTemplateParts.ProductTemplatePartDetailView;
import ProductTemplateParts.ProductTemplatePartView;
import ProductTemplates.ProductTemplate;
import ProductTemplates.ProductTemplateDetailView;
import ProductTemplates.ProductTemplateListController;
import ProductTemplates.ProductTemplateListView;

@SuppressWarnings("serial")
public class CabinetronView extends JFrame {
	private int GUIWidth;
	private int GUIHeight;
	private JMenuBar menuBar;
	private JMenu menuParts, menuTemplates;
	private JMenuItem itemParts, itemInventory, itemTemplates;
	private int minX = 300;
	private int minY = 300;
	
	private CabinetronModel model;
	private CabinetronView thisView;
	
	private PartsInventoryView partsInventoryView = null;
	private InventoryView inventoryView = null;
	private ProductTemplateListView productTemplateListView;
	private ProductTemplatePartView productTemplatePartListView;
	private ProductTemplateDetailView templateDetailView;
	
	private JInternalFrame partListWindow = null;
	private JInternalFrame partDetailWindow = null;
	private JInternalFrame inventoryListWindow = null;
	private JInternalFrame inventoryDetailWindow = null;
	private JInternalFrame templateListWindow = null;
	private JInternalFrame templateDetailWindow = null;
	private JInternalFrame templatePartListWindow = null;
	private JInternalFrame templatePartDetailWindow = null;
	
	private PartsInventoryController partsController;
	private InventoryController inventoryController;
	private ProductTemplateListController productTemplateListController;
	private ProductTemplatePartDetailController productTemplatePartDetailController;
	
	private JDesktopPane desktop;
	
	public CabinetronView(CabinetronModel model) {
		super("Cabinetron");
		this.thisView = this;
		this.model = model;
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (model.GetGUIWidth() / 2), 
				 (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (model.GetGUIHeight() / 2));
		this.setSize(model.GetGUIWidth(), model.GetGUIHeight());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setPreferredSize(new Dimension(model.GetGUIWidth(), model.GetGUIHeight()));
		getContentPane().setBackground(new Color(225, 215, 193));
		getContentPane().setLayout(new BorderLayout());

		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		menuParts = new JMenu("Parts");
		menuTemplates = new JMenu("Templates");
		
		itemParts = new JMenuItem("Part");
		menuParts.add(itemParts);
		menuParts.addSeparator();
		
		itemInventory = new JMenuItem("Inventory");
		menuParts.add(itemInventory);
		
		itemTemplates = new JMenuItem("View Product Template List");
		menuTemplates.add(itemTemplates);
		
		menuBar.add(menuParts);
		menuBar.add(menuTemplates);
		
		desktop = new JDesktopPane();
		desktop.setBackground(new Color(211, 229, 235));
		this.add(desktop);
		this.setVisible(true);

	
	}
	
	public void register(CabinetronController controller) {
		itemParts.addActionListener(controller);
		itemInventory.addActionListener(controller);
		itemTemplates.addActionListener(controller);
	}
	
	
	public PartsInventoryView ViewParts() {
		if (partListWindow == null) {
			partsInventoryView = new PartsInventoryView(model.GetPartsModel(), model.GetGUIWidth() / 2, model.GetGUIHeight() / 2, minX, minY);
			partsController = new PartsInventoryController(model.GetPartsModel(), partsInventoryView, thisView);
			partsInventoryView.register(partsController);
			partListWindow = new JInternalFrame("Parts List", true, true, true, true );
			partListWindow.setMinimumSize(new Dimension(minX, minY));
			partListWindow.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					partsInventoryView.resized();
					
				}
			});
			partListWindow.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosed(InternalFrameEvent e) {
					partListWindow = null;
				}
			});
			partListWindow.add(partsInventoryView, BorderLayout.CENTER);
			partListWindow.pack();
			partListWindow.setLocation(0, 0); // child window position in desktop 
			desktop.add(partListWindow);
			partListWindow.setVisible(true);
		}
		else {
			displayOpenFrame(partListWindow);
		}
		return partsInventoryView;
	}
	
	public PartView ViewPartDetails(String title) {
		int prevX, prevY;
		PartView partDetailView = new PartView(model.GetPartsModel(), (model.GetGUIWidth() / 5) * 2, (model.GetGUIHeight() / 5) * 2, minX, minY);
		if (partDetailWindow != null) {
			prevX = partDetailWindow.getX();
			prevY = partDetailWindow.getY();
			closePartDetailView();
		}
		else {  // set an initial location for the detail view window
			prevX = 20;
			prevY = 20;
		}
		partDetailWindow = new JInternalFrame(title, true, true, true, true );
		partDetailWindow.setMinimumSize(new Dimension(minX, minY));
		partDetailWindow.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				partDetailView.resized();
			}
		});
		partDetailWindow.add(partDetailView, BorderLayout.CENTER);
		partDetailWindow.pack();
		partDetailWindow.setLocation(prevX, prevY); // child window position in desktop 
		desktop.add(partDetailWindow);
		partDetailWindow.setVisible(true);
		return partDetailView;
	}
	
	public void closePartDetailView() {
		partDetailWindow.dispose();
	}
	
	public InventoryView ViewInventoryItems() {
		if (inventoryListWindow == null) {
			inventoryView = new InventoryView(model.GetPartsModel(), model.GetInventoryItemModel(), model.GetGUIWidth() / 2, model.GetGUIHeight() / 2, minX, minY);
			inventoryController = new InventoryController(model.GetInventoryItemModel(), inventoryView, thisView);
			inventoryView.register(inventoryController);
			inventoryListWindow = new JInternalFrame("Inventory Item List", true, true, true, true );
			inventoryListWindow.setMinimumSize(new Dimension(minX, minY));
			inventoryListWindow.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					inventoryView.resized();
					
				}
			});
			inventoryListWindow.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosed(InternalFrameEvent e) {
					inventoryListWindow = null;
				}
			});
			inventoryListWindow.add(inventoryView, BorderLayout.CENTER);
			inventoryListWindow.pack();
			inventoryListWindow.setLocation(0, 0); // child window position in desktop 
			desktop.add(inventoryListWindow);
			inventoryListWindow.setVisible(true);
		}
		else {
			displayOpenFrame(inventoryListWindow);
		}
		return inventoryView;
	}
	
	void displayOpenFrame(JInternalFrame frame) {
		frame.toFront();
		try {
			frame.setIcon(false);
			frame.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	public ItemView ViewInventoryItemDetails(String title) {
		int prevX, prevY;
		ItemView inventoryItemDetailView = new ItemView(model.GetInventoryItemModel(), (model.GetGUIWidth() / 5) * 2, (model.GetGUIHeight() / 5) * 2, minX, minY);
		if (inventoryDetailWindow != null) {
			prevX = inventoryDetailWindow.getX();
			prevY = inventoryDetailWindow.getY();
			closeInventoryItemDetailView();
		}
		else {  // set an initial location for the detail view window
			prevX = 20;
			prevY = 20;
		}
		inventoryDetailWindow = new JInternalFrame(title, true, true, true, true );
		inventoryDetailWindow.setMinimumSize(new Dimension(minX, minY));
		inventoryDetailWindow.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				inventoryItemDetailView.resized();
			}
		});
		inventoryDetailWindow.add(inventoryItemDetailView, BorderLayout.CENTER);
		inventoryDetailWindow.pack();
		inventoryDetailWindow.setLocation(prevX, prevY); // child window position in desktop 
		desktop.add(inventoryDetailWindow);
		inventoryDetailWindow.setVisible(true);
		return inventoryItemDetailView;
	}
	
	public void closeInventoryItemDetailView() {
		inventoryDetailWindow.dispose();
	}
	
	public void showInventoryItemEditConflictWindow() {
		inventoryDetailWindow.setResizable(false);
		inventoryDetailWindow.setSize(model.GetGUIWidth(), Math.max(inventoryDetailWindow.getHeight(), minY));
		inventoryDetailWindow.setLocation(0, 0); // child window position in desktop 
	}
	
	public ProductTemplateListView ViewProductTemplateList() {
		if (templateListWindow == null) {
			productTemplateListView = new ProductTemplateListView(model.GetProductTemplateModel(), model.GetGUIWidth() / 2, model.GetGUIHeight() / 2, minX, minY);
			productTemplateListController = new ProductTemplateListController(model.GetProductTemplateModel(), productTemplateListView, thisView);
			productTemplateListView.register(productTemplateListController);
			templateListWindow = new JInternalFrame("Product Template List", true, true, true, true );
			templateListWindow.setMinimumSize(new Dimension(minX, minY));
			templateListWindow.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					productTemplateListView.resized();
					
				}
			});
			templateListWindow.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosed(InternalFrameEvent e) {
					templateListWindow = null;
				}
			});
			templateListWindow.add(productTemplateListView, BorderLayout.CENTER);
			templateListWindow.pack();
			templateListWindow.setLocation(0, 0); // child window position in desktop 
			desktop.add(templateListWindow);
			templateListWindow.setVisible(true);
		}
		else {
			displayOpenFrame(templateListWindow);
		}
		return productTemplateListView;
	}
	
	public ProductTemplatePartView ViewProductTemplatePartList(ProductTemplate template) {
		if (templatePartListWindow == null) {
			productTemplatePartListView = new ProductTemplatePartView(template.getProductTemplatePartModel(), model.GetGUIWidth() / 2, model.GetGUIHeight() / 2, minX, minY);
			productTemplatePartDetailController = new ProductTemplatePartDetailController(template.getProductTemplatePartModel(), productTemplatePartListView, thisView);
			productTemplatePartListView.register(productTemplatePartDetailController);
			templatePartListWindow = new JInternalFrame("Product Template Parts List", true, true, true, true );
			templatePartListWindow.setMinimumSize(new Dimension(minX, minY));
			templatePartListWindow.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					productTemplateListView.resized();
					
				}
			});
			templatePartListWindow.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosed(InternalFrameEvent e) {
					templatePartListWindow = null;
				}
			});
			templatePartListWindow.add(productTemplatePartListView, BorderLayout.CENTER);
			templatePartListWindow.pack();
			templatePartListWindow.setLocation(0, 0); // child window position in desktop 
			desktop.add(templatePartListWindow);
			templatePartListWindow.setVisible(true);
		}
		else {
			displayOpenFrame(templateListWindow);
		}
		return productTemplatePartListView;
	}
	
	public void closeProductTemplatePartListView() {
		templatePartListWindow.dispose();
	}
	
	public ProductTemplateDetailView ViewProductTemplateDetails(String title) {
		int prevX, prevY;
		templateDetailView = new ProductTemplateDetailView(model.GetProductTemplateModel(), (model.GetGUIWidth() / 5) * 2, (model.GetGUIHeight() / 5) * 2, minX, minY);
		if (templateDetailWindow != null) {
			prevX = templateDetailWindow.getX();
			prevY = templateDetailWindow.getY();
			closeProductTemplateDetailView();
		}
		else {  // set an initial location for the detail view window
			prevX = 20;
			prevY = 20;
		}
		templateDetailWindow = new JInternalFrame(title, true, true, true, true );
		templateDetailWindow.setMinimumSize(new Dimension(minX, minY));
		templateDetailWindow.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				templateDetailView.resized();
			}
		});
		templateDetailWindow.add(templateDetailView, BorderLayout.CENTER);
		templateDetailWindow.pack();
		templateDetailWindow.setLocation(prevX, prevY); // child window position in desktop 
		desktop.add(templateDetailWindow);
		templateDetailWindow.setVisible(true);
		return templateDetailView;
	}
	
	public void closeProductTemplateDetailView() {
		templateDetailWindow.dispose();
	}
	
	
	
	public ProductTemplatePartDetailView ViewProductTemplatePartDetails(String title, Integer templateID) {
		int prevX, prevY;
		ProductTemplatePartDetailView templatePartDetailView = new ProductTemplatePartDetailView(model.GetProductTemplateModel().getProductTemplate(templateID).getProductTemplatePartModel(), (model.GetGUIWidth() / 5) * 2, (model.GetGUIHeight() / 5) * 2, minX, minY);
		if (templatePartDetailWindow != null) {
			prevX = templatePartDetailWindow.getX();
			prevY = templatePartDetailWindow.getY();
			closeProductTemplatePartDetailView();
		}
		else {  // set an initial location for the detail view window
			prevX = 20;
			prevY = 20;
		}
		templatePartDetailWindow = new JInternalFrame(title, true, true, true, true );
		templatePartDetailWindow.setMinimumSize(new Dimension(minX, minY));
		templatePartDetailWindow.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				templatePartDetailView.resized();
			}
		});
		templatePartDetailWindow.add(templatePartDetailView, BorderLayout.CENTER);
		templatePartDetailWindow.pack();
		templatePartDetailWindow.setLocation(prevX, prevY); // child window position in desktop 
		desktop.add(templatePartDetailWindow);
		templatePartDetailWindow.setVisible(true);
		return templatePartDetailView;
	}
	
	public void closeProductTemplatePartDetailView() {
		templatePartDetailWindow.dispose();
	}
	
}
