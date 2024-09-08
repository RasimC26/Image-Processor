package assign11;

/**
 * This class represents a pixel in an image, containing red, green, and blue color components.
 * Each color component is represented as an integer value in the range 0 to 255 (inclusive).
 * The class provides methods to access the individual color components and to pack them into
 * a single integer representing the RGB color.
 *
 * @author Rasim Crnica
 * @version 4.30.0
 */
public class Pixel {
	
	private int redAmount;
    private int greenAmount;
    private int blueAmount;

    
    /**
     * Constructs a Pixel object with the specified red, green, and blue color components.
     *
     * @param redAmount   the amount of red component (0-255)
     * @param greenAmount the amount of green component (0-255)
     * @param blueAmount  the amount of blue component (0-255)
     * @throws IllegalArgumentException if any color value is outside the range 0 to 255
     */
    public Pixel(int redAmount, int greenAmount, int blueAmount) {
        if (!isValidColor(redAmount) || !isValidColor(greenAmount) || !isValidColor(blueAmount)) {
            throw new IllegalArgumentException("Color values must be in the range 0 to 255 (inclusive)");
        }
        this.redAmount = redAmount;
        this.greenAmount = greenAmount;
        this.blueAmount = blueAmount;
    }
    
    
    /**
     * Gets the amount of red component in this pixel.
     *
     * @return the amount of red component (0-255)
     */
    public int getRedAmount() {
        return redAmount;
    }
    
    /**
     * Gets the amount of green component in this pixel.
     *
     * @return the amount of green component (0-255)
     */
    public int getGreenAmount() {
        return greenAmount;
    }
    
    /**
     * Gets the amount of blue component in this pixel.
     *
     * @return the amount of blue component (0-255)
     */
    public int getBlueAmount() {
        return blueAmount;
    }
    
    /**
     * Packs the red, green, and blue color components into a single integer representing the RGB color.
     *
     * @return the packed RGB color integer
     */
    public int getPackedRGB() {
        return (redAmount << 16) | (greenAmount << 8) | blueAmount;
    }
    
    /**
     * Checks if the given color value is valid, i.e., within the range 0 to 255 (inclusive).
     *
     * @param colorValue the color value to be checked
     * @return true if the color value is within the valid range, false otherwise
     */
    private boolean isValidColor(int colorValue) {
        return colorValue >= 0 && colorValue <= 255;
    }

}
