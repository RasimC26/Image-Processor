package assign11;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author Rasim Crnica and Dillon Obrien
 * @version 4.30.0
 */
public class ImageProcessorFrame extends JFrame implements ActionListener, ChangeListener {

	private static final long serialVersionUID = 1L;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private ImagePanel panel;
	private Image image;
	private JSlider slider;
	private JLabel label;
	private boolean cropMode;
	private ArrayList<Image> arr, redoArr;
	private Image adjustedImage;

	/**
	 * Constructs an ImageProcessorFrame object. Initializes the frame sets its size
	 * to 800x600 pixels. Adds menu items for opening and saving images, and for
	 * applying filters and sets tool tips for menu items and initializes them as
	 * enabled or disabled.
	 */
	public ImageProcessorFrame() {
		setTitle("Image Processor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);

		arr = new ArrayList<Image>();
		redoArr = new ArrayList<Image>();
		
		panel = new ImagePanel(image);
		label = new JLabel();
		slider = new JSlider(-200, 200, 0);

		slider.setPreferredSize(new Dimension(500, 100));

		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.setMajorTickSpacing(25);
		slider.setPaintLabels(true);
		slider.setFont(new Font("Arial", Font.PLAIN, 15));
		slider.setOrientation(SwingConstants.HORIZONTAL);
		slider.addChangeListener(this);

		cropMode = false;
		panel.setCropMode(false);

		label.setFont(new Font("Arial", Font.PLAIN, 15));

		slider.setVisible(false);
		label.setVisible(false);

		panel.setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu filterMenu = new JMenu("Filter");
		JMenu featureMenu = new JMenu("Features");

		JMenuItem redBlueMenuItem = new JMenuItem("Red-Blue swap");
		JMenuItem invertColorMenuItem = new JMenuItem("Invert Color");
		JMenuItem blackWhiteMenuItem = new JMenuItem("Black and White");
		JMenuItem clockwiseMenuItem = new JMenuItem("Rotate Clockwise");
		JMenuItem flipMenuItem = new JMenuItem("Flip Image");
		JMenuItem brightnessItem = new JMenuItem("Brighten Image");
		JMenuItem cropItem = new JMenuItem("Crop Image");
		JMenuItem undoItem = new JMenuItem("Undo");
		JMenuItem redoItem = new JMenuItem("Redo");

		openMenuItem = new JMenuItem("Open");
		saveMenuItem = new JMenuItem("Save");

		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);

		filterMenu.add(redBlueMenuItem);
		filterMenu.add(invertColorMenuItem);
		filterMenu.add(blackWhiteMenuItem);
		filterMenu.add(clockwiseMenuItem);
		filterMenu.add(flipMenuItem);

		featureMenu.add(brightnessItem);
		featureMenu.add(cropItem);
		featureMenu.add(undoItem);
		featureMenu.add(redoItem);

		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

		menuBar.add(filterMenu);
		setJMenuBar(menuBar);

		menuBar.add(featureMenu);
		setJMenuBar(menuBar);

		openMenuItem.addActionListener(this);
		saveMenuItem.addActionListener(this);

		redBlueMenuItem.addActionListener(this);
		invertColorMenuItem.addActionListener(this);
		blackWhiteMenuItem.addActionListener(this);
		clockwiseMenuItem.addActionListener(this);
		flipMenuItem.addActionListener(this);
		brightnessItem.addActionListener(this);
		cropItem.addActionListener(this);
		undoItem.addActionListener(this);
		redoItem.addActionListener(this);

		openMenuItem.setToolTipText("Open an image file");
		saveMenuItem.setToolTipText("Save the filtered image to file");

		redBlueMenuItem.setToolTipText("Swap the red and blue amounts of the RGB model for color");
		invertColorMenuItem.setToolTipText("Inverts the colors of an Image");
		blackWhiteMenuItem.setToolTipText("Make an image black and white");
		clockwiseMenuItem.setToolTipText("Turn the image in the clockwise direction");
		flipMenuItem.setToolTipText("Flips the image horizontally");
		brightnessItem.setToolTipText("Brighten Image");
		cropItem.setToolTipText("Crop Image");
		undoItem.setToolTipText("Undo");
		redoItem.setToolTipText("Redo");

		openMenuItem.setEnabled(true);
		saveMenuItem.setEnabled(false);

	}

	/**
	 * Handles action events triggered by menu items. If the open or save menu item
	 * is clicked, calls the corresponding method. If a filter menu item is clicked,
	 * applies the corresponding filter.
	 * 
	 * @param e the action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand() == "Open") {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "bmp",
					"gif");
			fileChooser.setFileFilter(filter);

			int returnVal = fileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File imgFile = fileChooser.getSelectedFile();
				image = new Image(imgFile.getAbsolutePath());
				arr.add(image.copy());
				this.panel = new ImagePanel(image);
				setContentPane(panel);
				revalidate();

			}

			else {
				JOptionPane.showMessageDialog(this, "No file selected");
			}

		} else if (e.getActionCommand() == "Save") {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG Image", "jpg"));
			int returnValue = fileChooser.showSaveDialog(this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				
				File file = fileChooser.getSelectedFile();

				if (adjustedImage == null) {
					image.writeImage(file.getAbsolutePath() + ".jpg");
					JOptionPane.showMessageDialog(this, "Image saved successfully.");
				} else if(adjustedImage != null) {
					adjustedImage.writeImage(file.getAbsolutePath() + ".jpg");
					JOptionPane.showMessageDialog(this, "Image saved successfully.");
				}
				
				else {
				JOptionPane.showMessageDialog(this, "Error saving image");
				}
			}

		} else if (e.getActionCommand() == "Red-Blue swap") {
			slider.setVisible(false);
        	label.setVisible(false);
        	
        	panel.setCropMode(false);
        	
        	 if (adjustedImage == null) {
	        	  image.redBlueSwapFilter();
	        	  refreshImagePanel(image);
	          }
	          else if (adjustedImage != null) {
	        	  adjustedImage.redBlueSwapFilter();
	        	  refreshImagePanel(adjustedImage);
	        	  
	          }
            
            saveMenuItem.setEnabled(true); 

			arr.add(image.copy());

		} else if (e.getActionCommand() == "Invert Color") {
			 slider.setVisible(false);
	          label.setVisible(false);
	          cropMode = false;
	          panel.setCropMode(false);
	          if (adjustedImage == null) {
	        	  image.invertColorFilter();
	        	  refreshImagePanel(image);
	          }
	          else if (adjustedImage != null) {
	        	  adjustedImage.invertColorFilter();
	        	  refreshImagePanel(adjustedImage);
	        	  
	          }
	          saveMenuItem.setEnabled(true);

			arr.add(image.copy());

		} else if (e.getActionCommand() == "Black and White") {
			 slider.setVisible(false);
        	 label.setVisible(false);
        	 cropMode = false;
	         panel.setCropMode(false);
	         if (adjustedImage == null) {
	        	  image.blackAndWhiteFilter();
	        	  refreshImagePanel(image);
	          }
	          else if (adjustedImage != null) {
	        	  adjustedImage.blackAndWhiteFilter();
	        	  refreshImagePanel(adjustedImage);  
	          }
	         saveMenuItem.setEnabled(true);

			arr.add(image.copy());

		} else if (e.getActionCommand() == "Rotate Clockwise") {
			 slider.setVisible(false);
        	 label.setVisible(false);
        	 cropMode = false;
        	 if (adjustedImage == null) {
	        	  image.rotateClockwiseFilter();
	        	  refreshImagePanel(image);
	          }
	          else if (adjustedImage != null) {
	        	  adjustedImage.rotateClockwiseFilter();
	        	  refreshImagePanel(adjustedImage);  
	          }
	         saveMenuItem.setEnabled(true);

			 arr.add(image.copy());

		} else if (e.getActionCommand() == "Flip Image") {
			slider.setVisible(false);
       	 	label.setVisible(false);
       	 	cropMode = false;
       	 	if (adjustedImage == null) {
	        	  image.flipImage();
	        	  refreshImagePanel(image);
	          }
	          else if (adjustedImage != null) {
	        	  adjustedImage.flipImage();
	        	  refreshImagePanel(adjustedImage);  
	          }
       	 
	         saveMenuItem.setEnabled(true); 

			arr.add(image.copy());
		} else if (e.getActionCommand() == "Brighten Image") {
			panel.setLayout(new BorderLayout());
        	panel.add(slider, BorderLayout.SOUTH);
        	panel.add(label, BorderLayout.NORTH);
        	cropMode = false;
            panel.setCropMode(false);
        	slider.setVisible(true);
        	label.setVisible(true);
        	refreshImagePanel(image);
            saveMenuItem.setEnabled(true);
			arr.add(image.copy());

		} else if (e.getActionCommand() == "Crop Image") {

			cropMode = true;
			slider.setVisible(false);
			label.setVisible(false);
			panel.setCropMode(true);
			
			int x = panel.getCropX();
			int y = panel.getCropY();
			int width = panel.getCropWidth();
			int height = panel.getCropHeight();
			
			
			
			setPreferredSize(new Dimension(width, height));
			image.cropImage(x, y, width, height);
			
			refreshImagePanel(image.copy());
			
			saveMenuItem.setEnabled(true);
			revalidate();
			repaint();
			arr.add(image.copy());

		} else if (e.getActionCommand() == "Undo") {
			if(arr.size() == 0) {
				throw new IndexOutOfBoundsException("Photo needs to be edited");
			}
			redoArr.add(image.copy());
			image = arr.get(arr.size() - 2).copy();
			arr.remove(arr.size() - 1);
			refreshImagePanel(image.copy());
			
		}else if (e.getActionCommand() == "Redo") {
			if(redoArr.size() == 0) {
				throw new IndexOutOfBoundsException("Photo needs to be edited and undone");
			}
			image = redoArr.get(redoArr.size() - 1).copy();

			refreshImagePanel(image.copy());
		}
	}
		

	

	/**
	 * This method is called when the slider has been changed,it also updates the
	 * label text to display the current brightness.The method gets the brightness
	 * value from the slider to adjust the brightness of a copy of the original
	 * image. The adjusted image is then displayed in the image panel, and the save
	 * menu item is enabled to allow the user to save the adjusted image.
	 *
	 * @param e The ChangeEvent object representing the change in state.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		label.setText("Adjust Brightness: "+ slider.getValue());
 		int brightness = slider.getValue(); 
 		adjustedImage = image.copy();
	    adjustedImage.brightenImage(brightness);
	    refreshImagePanel(adjustedImage);
	    saveMenuItem.setEnabled(true);

	}

	/**
	 * Refreshes the image panel by changing the content with a new image.
	 * 
	 * @param image The new image to be displayed.
	 */
	private void refreshImagePanel(Image img) {
		if (panel != null) {
 	        panel.setImage(img); 
 	        panel.repaint(); 
 	    } else {
 	        panel = new ImagePanel(img);
 	        setContentPane(panel);
 	    }
 	    revalidate();
	}

}