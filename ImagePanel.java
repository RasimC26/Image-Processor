package assign11;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * This class represents a GUI component for displaying an image.
 *
 * @author Prof. Martin, Rasim Crnicq, Dillon Obrien
 * @version 4.30.0
 */
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {
	private Image image;
	private ImageProcessorFrame frame;

	private BufferedImage bufferedImg;
	private int startX, startY, endX, endY;
	private boolean selecting = false;
	private boolean cropMode = false;
	private int currentSizeX;
	private int currentSizeY;
	private int x, y, width, height;

	/**
	 * Creates a new ImagePanel to display the given image.
	 *
	 * @param img - the given image
	 */
	public ImagePanel(Image img) {

		if (img == null) {
			int defaultWidth = 800;
			int defaultHeight = 600;
			this.bufferedImg = new BufferedImage(defaultWidth, defaultHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = this.bufferedImg.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, defaultWidth, defaultHeight);
			g.dispose();
			this.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
		} else {

			int rowCount = img.getNumberOfRows();
			int colCount = img.getNumberOfColumns();

			addMouseListener(this);
			addMouseMotionListener(this);

			this.bufferedImg = new BufferedImage(colCount, rowCount, BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < rowCount; i++)
				for (int j = 0; j < colCount; j++)
					this.bufferedImg.setRGB(j, i, img.getPixel(i, j).getPackedRGB());

			this.setPreferredSize(new Dimension(colCount, rowCount));
		}
	}

	public void setCropValues(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getCropX() {
		return x;
	}

	public int getCropY() {
		return y;
	}

	public int getCropWidth() {
		return width;
	}

	public int getCropHeight() {
		return height;
	}

	/**
	 * Sets the size of the next shape(s) to be drawn.
	 */
	public void setCurrentSize(int size, boolean isX) {
		if (isX)
			currentSizeX = size;
		else
			currentSizeY = size;
	}

	public void setImage(Image img) {

		int rowCount = img.getNumberOfRows();
		int colCount = img.getNumberOfColumns();
		this.bufferedImg = new BufferedImage(colCount, rowCount, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < rowCount; i++)
			for (int j = 0; j < colCount; j++)
				this.bufferedImg.setRGB(j, i, img.getPixel(i, j).getPackedRGB());
		this.setPreferredSize(new Dimension(colCount, rowCount));
	}

	public void setCropMode(boolean cropMode) {
		this.cropMode = cropMode;
	}

	/**
	 * This method is called by the system when a component needs to be painted.
	 * Which can be at one of three times: --when the component first appears --when
	 * the size of the component changes (including resizing by the user) --when
	 * repaint() is called
	 *
	 * Partially overrides the paintComponent method of JPanel.
	 *
	 * @param g -- graphics context onto which we can draw
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(this.bufferedImg, 0, 0, this);
			width = endX - startX;
			height = endY - startY;
			g.drawRect(startX, startY, width, height);
	}


	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		if (selecting) {
			endX = e.getX();
			endY = e.getY();
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		endX = Math.min(Math.max(endX, 0), bufferedImg.getWidth());
		endY = Math.min(Math.max(endY, 0), bufferedImg.getHeight());

		x = Math.min(startX, endX);
		y = Math.min(startY, endY);
		width = Math.abs(endX - startX);
		height = Math.abs(endY - startY);

		setCropValues(startX, startY, endX, endY);
		setPreferredSize(new Dimension(width, height));
		BufferedImage croppedBufferedImage = bufferedImg.getSubimage(startX, startY, endX, endY);
		Image croppedImage = Image.fromBufferedImage(croppedBufferedImage);

		
		System.out.println("X" + getCropX());
		System.out.println("Y" + getCropY());
		System.out.println("EX" + endX);
		System.out.println("EY" + endY);
		
		revalidate();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		startX = e.getX();
		startY = e.getY();
		selecting = true;
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
	// Required by a serializable class (ignore for now)
		private static final long serialVersionUID = 1L;
}