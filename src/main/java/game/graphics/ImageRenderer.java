package game.graphics;

import game.engine.GameApplication;
import game.model.AbstractGameObject;
import javafx.application.Platform;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Renderer of an image
 */
public class ImageRenderer implements Renderer {

	/**
	 * Enumerator representing a sprite. Each sprite has its own preallocated image.
	 * The use of sprites instead of new images is recommended in order to boost rendering performance. 
	 */
	public static enum Sprite {
		//...sprites to add...//
		PLAYER("/imgs/baloon.png"),
		BULLET("/imgs/bullet.png"),
		THORNBALL("/imgs/thornball.png"),
		GOLDEN_PLAYER("/imgs/golden_baloon.png"),
		SHIELD_PLAYER("/imgs/shield_baloon.png"),
		GOLDEN_SHIELD_PLAYER("/imgs/golden_shield_baloon.png"),
		POP_ANIMATION_1("/imgs/pop_1.png"),
		POP_ANIMATION_2("/imgs/pop_2.png"),
		POP_ANIMATION_3("/imgs/pop_3.png"),
		POP_ANIMATION_4("/imgs/pop_4.png"),
		PWRUP_SHIELD("/imgs/shield.png"),
		PWRUP_MULTIPLIER("/imgs/multiplier.png"),
		PWRUP_SWEEPER("/imgs/sweeper.png");
		
		
		private final Image img;
		
		private Sprite(final String path) {
//			System.out.println("Loading sprite '" + path + "'...");
			this.img = new Image(path);
//			System.out.println("Done");
		}
		
		public Image getImage() {
			return this.img;
		}
	}
	
	private Image currentImg;
	private Sprite baseSprite;
	private final AbstractGameObject obj;
	private double size;
	
	/**
	 * Creates a new Renderer with sprite and in-game width of size rotated by rotation angle.
	 * The image will be centered at obj position.
	 * @param obj - the game object linked to this renderer
	 * @param sprite
	 * @param size
	 * @param rotation - in degrees
	 */
	public ImageRenderer(final AbstractGameObject obj, final Sprite sprite, double size, double rotation) {
		this.obj = obj;
		this.size = size;
		setSprite(sprite, rotation);
	}
	
	/**
	 * Sets the current image to the newSprite image
	 * @param newSprite
	 */
	public void setSprite(Sprite newSprite) {
		setSprite(newSprite, 0);
	}
	/**
	 * Sets the current image to the newSprite image. This method is executed in the JavaFX thread
	 * @param newSprite
	 * @param rotation angle
	 */
	private void setSprite(Sprite newSprite, double rotation) {
		Platform.runLater(() -> {
			this.baseSprite = newSprite;
			this.currentImg = newSprite.getImage();
			rotate(rotation, GameApplication.convertToInt(this.size));
		});
	}
	
	/**
	 * Paints an image on the screen.
	 * @param GraphicsContext gc
	 */
	@Override
	public void paint(GraphicsContext gc) {
		//images can be null, because they are loaded in a separated FX thread
		if (this.currentImg == null) return;
		double xPos = GameApplication.convertToInt(this.obj.getPosition().getX()) - this.currentImg.getWidth()/2;
		double yPos = GameApplication.convertToInt(this.obj.getPosition().getY()) - this.currentImg.getHeight()/2;
        gc.drawImage(currentImg, xPos, yPos, currentImg.getWidth(), currentImg.getHeight());
	}
	
	/**
	 * Sets the current rotation in degrees of this image. This method is executed in the JavaFX thread
	 * @param degrees
	 */
	public void setRotation(final double degrees) {
		Platform.runLater(() -> {
			rotate(degrees, GameApplication.convertToInt(this.size));
		});
	}
	
	/**
	 * Rotates this image by degrees angle and scales it to fit in a box of size width
	 * @param degrees
	 * @param width - in pixels!
	 */
	private void rotate(final double degrees, final double width) {
		ImageView iv = new ImageView(this.baseSprite.getImage());
		iv.setFitWidth(width);
	iv.setPreserveRatio(true);
    iv.setSmooth(true);
    iv.setCache(true);
		iv.setRotate(degrees);
		SnapshotParameters param = new SnapshotParameters();
		param.setFill(Color.TRANSPARENT);
		this.currentImg = iv.snapshot(param, null);
	}
	
	/**
	 * Gets the attached game object (the one to be rendered).
	 * @return the attached game object
	 */
	protected AbstractGameObject getGameObject() {
		return this.obj;
	}
}
