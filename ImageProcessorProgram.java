package assign11;

/**
 * contains the main method to launch the Image Processor GUI.
 * It creates an instance of the ImageProcessorFrame and makes it visible.
 * 
 * @author Rasim Crnica, Dillon Obrien
 * @version 4.30.0
 */
public class ImageProcessorProgram {
	
	
	/**
     * The main method to launch the Image Processor GUI.
     * 
     * @param args 
     */
    public static void main(String[] args) {
        // Create and launch the GUI
        ImageProcessorFrame frame = new ImageProcessorFrame();
        frame.setVisible(true);
    }
}
