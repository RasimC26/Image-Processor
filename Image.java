package assign11;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class represents an image as a two-dimensional array of pixels and
 * provides a number of image filters (via instance methods) for changing the
 * appearance of the image. Application of multiple filters is cumulative; e.g.,
 * obj.redBlueSwapFilter() followed by obj.rotateClockwiseFilter() results in an
 * image altered both in color and orientation.
 *
 * Note: - The pixel in the northwest corner of the image is stored in the first
 * row, first column. - The pixel in the northeast corner of the image is stored
 * in the first row, last column. - The pixel in the southeast corner of the
 * image is stored in the last row, last column. - The pixel in the southwest
 * corner of the image is stored in the last row, first column.
 *
 * @author Drs. Kabir, Martin and Rasim Crnica and Dillon Obrien
 * @version 4.30.0
 */
public class Image {

	private Pixel[][] imageArray;

	/**
	 * Creates a new Image object by reading the image file with the given filename.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param filename - name of the given image file to read
	 * @throws IOException if file does not exist or cannot be read
	 */
	public Image(String filename) {
		BufferedImage imageInput = null;
		try {
			imageInput = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("Image file " + filename + " does not exist or cannot be read.");
		}

		imageArray = new Pixel[imageInput.getHeight()][imageInput.getWidth()];
		for (int i = 0; i < imageArray.length; i++)
			for (int j = 0; j < imageArray[0].length; j++) {
				int rgb = imageInput.getRGB(j, i);
				imageArray[i][j] = new Pixel((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
			}
	}

	/**
	 * Create an Image object directly from a pre-made Pixel array. This is
	 * primarily to be used in testing.
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image(Pixel[][] imageArray) {
		this.imageArray = imageArray;
	}

	public static Image fromBufferedImage(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		Pixel[][] imageArray = new Pixel[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = bufferedImage.getRGB(x, y);
				imageArray[y][x] = new Pixel((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
			}
		}

		return new Image(imageArray);
	}

	/**
	 * Create a new "default" Image object, whose purpose is to be used in testing.
	 *
	 * The orientation of this image: cyan red green magenta yellow blue
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image() {
		imageArray = new Pixel[3][2];
		imageArray[0][0] = new Pixel(0, 255, 255); // cyan
		imageArray[0][1] = new Pixel(255, 0, 0); // red
		imageArray[1][0] = new Pixel(0, 255, 0); // green
		imageArray[1][1] = new Pixel(255, 0, 255); // magenta
		imageArray[2][0] = new Pixel(255, 255, 0); // yellow
		imageArray[2][1] = new Pixel(0, 0, 255); // blue
	}

	/**
	 * Gets the pixel at the specified row and column indexes.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param rowIndex    - given row index
	 * @param columnIndex - given column index
	 * @return the pixel at the given row index and column index
	 * @throws IndexOutOfBoundsException if row or column index is out of bounds
	 */
	public Pixel getPixel(int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= imageArray.length)
			throw new IndexOutOfBoundsException("rowIndex must be in range 0-" + (imageArray.length - 1));

		if (columnIndex < 0 || columnIndex >= imageArray[0].length)
			throw new IndexOutOfBoundsException("columnIndex must be in range 0-" + (imageArray[0].length - 1));

		return imageArray[rowIndex][columnIndex];
	}

	/**
	 * Writes the image represented by this object to file. Does nothing if the
	 * image length is 0.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param filename - name of image file to write
	 * @throws IOException if file does cannot be written
	 */
	public void writeImage(String filename) {
		if (imageArray.length > 0) {
			BufferedImage imageOutput = new BufferedImage(imageArray[0].length, imageArray.length,
					BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < imageArray.length; i++)
				for (int j = 0; j < imageArray[0].length; j++)
					imageOutput.setRGB(j, i, imageArray[i][j].getPackedRGB());

			try {
				ImageIO.write(imageOutput, "png", new File(filename));
			} catch (IOException e) {
				System.out.println("The image cannot be written to file " + filename);
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that for each
	 * pixel the red amount and blue amount are swapped.
	 *
	 * HINT: Since the Pixel class does not include setter methods for its private
	 * instance variables, create new Pixel objects with the altered colors.
	 */
	public void redBlueSwapFilter() {
		int width = imageArray[0].length;
		int height = imageArray.length;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Pixel pixel = imageArray[y][x];

				int red = pixel.getBlueAmount();
				int blue = pixel.getRedAmount();
				int green = pixel.getGreenAmount();

				Pixel newPixel = new Pixel(red, green, blue);
				imageArray[y][x] = newPixel;
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that the color
	 * of each pixel is converted to its corresponding grayscale shade, producing
	 * the effect of a black and white photo. The filter sets the amount of red,
	 * green, and blue all to the value of this average: (originalRed +
	 * originalGreen + originalBlue) / 3
	 *
	 * HINT: Since the Pixel class does not include setter methods for its private
	 * instance variables, create new Pixel objects with the altered colors.
	 */
	public void blackAndWhiteFilter() {
		int width = imageArray[0].length;
		int height = imageArray.length;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Pixel pixel = imageArray[y][x];

				int average = (pixel.getRedAmount() + pixel.getGreenAmount() + pixel.getBlueAmount()) / 3;

				Pixel newPixel = new Pixel(average, average, average);
				imageArray[y][x] = newPixel;
			}
		}

	}

	public Image copy() {
		int height = imageArray.length;
		int width = imageArray[0].length;
		Pixel[][] copiedArray = new Pixel[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Pixel originalPixel = imageArray[y][x];
				copiedArray[y][x] = new Pixel(originalPixel.getRedAmount(), originalPixel.getGreenAmount(),
						originalPixel.getBlueAmount());
			}
		}

		return new Image(copiedArray);
	}

	/**
	 * Applies a filter to the image represented by this object such that it is
	 * rotated clockwise (by 90 degrees). This filter rotates directly clockwise, it
	 * should not do this by rotating counterclockwise 3 times.
	 *
	 * HINT: If the image is not square, this filter requires creating a new array
	 * with different lengths. Use the technique of creating and reassigning a new
	 * backing array from BetterDynamicArray (assign06) as a guide for how to make a
	 * second array and eventually reset the imageArray reference to this new array.
	 * Note that we learned how to rotate a square 2D array *left* in Class Meeting
	 * 11.
	 */
	public void rotateClockwiseFilter() {
		int width = imageArray[0].length;
		int height = imageArray.length;

		Pixel[][] rotatedImageArray = new Pixel[width][height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				int newX = height - y - 1;
				int newY = x;

				rotatedImageArray[newY][newX] = imageArray[y][x];
			}
		}

		imageArray = rotatedImageArray;

	}

	/**
	 * Applies a inverted filter to the image represented by this object such that
	 * each pixel's color is inverted. This is achieved by subtracting each color
	 * component (red, green, and blue) from the maximum value (255).
	 * 
	 */
	public void invertColorFilter() {
		int width = imageArray[0].length;
		int height = imageArray.length;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Pixel pixel = imageArray[y][x];

				int invertedRed = 255 - pixel.getRedAmount();
				int invertedGreen = 255 - pixel.getGreenAmount();
				int invertedBlue = 255 - pixel.getBlueAmount();

				Pixel newPixel = new Pixel(invertedRed, invertedGreen, invertedBlue);
				imageArray[y][x] = newPixel;
			}
		}
	}

	/**
	 * Applies a flip filter to the image represented by this object. This filter
	 * swaps each pixel's X value with its corresponding pixel on the opposite side,
	 * which mirrors the image.
	 */
	public void flipImage() {
		int width = getNumberOfColumns();
		int height = getNumberOfRows();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width / 2; x++) {
				Pixel pixel = imageArray[y][x];
				imageArray[y][x] = imageArray[y][width - 1 - x];
				imageArray[y][width - 1 - x] = pixel;
			}
		}
	}

	public void brightenImage(int num) {
		int width = getNumberOfColumns();
		int height = getNumberOfRows();

		double adjustmentFactor = 1.0 + (num / 100.0);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Pixel pixel = imageArray[y][x];

				int brightenedRed = (int) (pixel.getRedAmount() * adjustmentFactor);
				int brightenedGreen = (int) (pixel.getGreenAmount() * adjustmentFactor);
				int brightenedBlue = (int) (pixel.getBlueAmount() * adjustmentFactor);

				brightenedRed = Math.min(255, Math.max(0, brightenedRed));
				brightenedGreen = Math.min(255, Math.max(0, brightenedGreen));
				brightenedBlue = Math.min(255, Math.max(0, brightenedBlue));

				Pixel newPixel = new Pixel(brightenedRed, brightenedGreen, brightenedBlue);
				imageArray[y][x] = newPixel;
			}
		}
	}

	public void cropImage(int x1, int y1, int x2, int y2) {
		int width = getNumberOfColumns();
		int height = getNumberOfRows();

		x1 = Math.max(0, Math.min(x1, width - 1));
		y1 = Math.max(0, Math.min(y1, height - 1));
		x2 = Math.max(0, Math.min(x2, width - 1));
		y2 = Math.max(0, Math.min(y2, height - 1));


		int cropWidth = x2 - x1;
		int cropHeight = y2 - y1;
		
		System.out.println(x1 + " " + x2 + " " + y1 + " " + y2);
		System.out.println(cropWidth + " " + cropHeight);
		
		Pixel[][] cropImageArray = new Pixel[cropHeight][cropWidth];

		for (int y = y1, cy = 0; y < y2; y++, cy++) {
		    for (int x = x1, cx = 0; x < x2; x++, cx++) {
		        cropImageArray[cy][cx] = imageArray[y][x];
		    }
		}
		imageArray = cropImageArray;

	}

	/**
	 * Returns the number of rows in the image.
	 * 
	 * @return the number of rows
	 */
	public int getNumberOfRows() {
		return this.imageArray.length;
	}

	/**
	 * Returns the number of columns in the image.
	 * 
	 * @return the number of columns
	 */
	public int getNumberOfColumns() {
		if (this.imageArray.length == 0)
			return 0;
		return this.imageArray[0].length;
	}
}
