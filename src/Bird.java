import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;
/**
 * Class provides features of bird.
 */
public class Bird {
    private final Image WING_DOWN_IMAGE_LEVEL0 = new Image("res/level-0/birdWingDown.png");
    private final Image WING_UP_IMAGE_LEVEL0 = new Image("res/level-0/birdWingUp.png");
    private final Image WING_DOWN_IMAGE_LEVEL1 = new Image("res/level-1/birdWingDown.png");
    private final Image WING_UP_IMAGE_LEVEL1 = new Image("res/level-1/birdWingUp.png");
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private Image wingDownImage;
    private Image wingUpImage;
    private int frameCount = 0;
    private double y;
    private double yVelocity;
    private Rectangle boundingBox;
    private Weapon weaponHolding;

    /** This is the constructor for Bird.
     * @param currLevel This is the current level of the game
     */
    public Bird(int currLevel) {
        y = INITIAL_Y;
        yVelocity = 0;
        if(currLevel == 0) {
            wingDownImage = WING_DOWN_IMAGE_LEVEL0;
            wingUpImage = WING_UP_IMAGE_LEVEL0;
        }else if(currLevel == 1) {
            wingUpImage = WING_UP_IMAGE_LEVEL1;
            wingDownImage = WING_DOWN_IMAGE_LEVEL1;
        }
        boundingBox = wingDownImage.getBoundingBoxAt(new Point(X, y));
    }

    /** This method updates the movement of the bird.
     * @param input This is the input from user
     * @return Rectangle This returns the bounding box of the bird.
     */
    public void update(Input input) {
        frameCount += 1;
        renderWeapon();
        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            wingDownImage.draw(X, y);
            boundingBox = wingDownImage.getBoundingBoxAt(new Point(X, y));
        }
        else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);
            if (frameCount % SWITCH_FRAME == 0) {
                wingUpImage.draw(X, y);
                boundingBox = wingUpImage.getBoundingBoxAt(new Point(X, y));
            }
            else {
                wingDownImage.draw(X, y);
                boundingBox = wingDownImage.getBoundingBoxAt(new Point(X, y));
            }
        }
        y += yVelocity;
    }

    /**
     * This method checks if there is a weapon, if no, bring the weapon.
     * @param weapon The weapon object
     */
    // Pick up the weapon if the bird is not holding any weapon
    public void pickUpWeapon(Weapon weapon) {
        if (weaponHolding == null && weapon.isPicked() == false) {
            weaponHolding = weapon;
        }
    }

    /**
     * This method will call activate method in Class Weapon. And the weapon
     * will be shot.
     */
    public void shoot() {
        if(weaponHolding != null) {
            weaponHolding.activate(X,y - wingUpImage.getHeight()/2 );
            weaponHolding = null;
        }
    }

    /**
     * This method will render weapon.
     */
    public void renderWeapon() {
        if(weaponHolding != null){
            weaponHolding.carrying(X + (wingUpImage.getWidth()/2), y - wingUpImage.getHeight()/2);
        }
    }

    /**
     * This method will respawn the bird to its starting position.
     */
    public void respawn() {
        y = INITIAL_Y;
    }

    /**
     * This method get the value of y.
     * @return double This method returns the y value.
     */
    public double getY() {
        return y;
    }

    /**
     * This method get the value of X.
     * @return double This method returns the X value.
     */
    public double getX() {
        return X;
    }

    /**
     * This method get the bounding box of the bird.
     * @return Rectangle This method returns the bounding box of the bird.
     */
    public Rectangle getBox() {
        return boundingBox;
    }

}