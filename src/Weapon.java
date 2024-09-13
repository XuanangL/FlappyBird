import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;
/**
 * Class provides the functionality of weapon.
 */
public abstract class Weapon implements movable {

    private final int MIN_Y = 100;
    private final int MAX_Y = 500;
    private final int range = MAX_Y - MIN_Y;
    private final int SHOOTING_SPEED = 5;
    private final double RATE = 0.5;
    private String name;
    private double yCoordinate;
    private double xCoordinate = Window.getWidth();
    private Image weaponImage;
    private double currentSpeed = INITIAL_SPEED;
    private Rectangle weaponBound;
    private boolean isActive = false;
    private boolean isPicked = false;
    private double activateYCoordinate;
    private int shootingRange;
    private int shootingCount = 0;
    private String[] targetType;

    /**
     * This is the default constructor of weapon
     */
    public Weapon() {
    }

    /**
     * This method renders weapon
     */
    public void renderWeapon() {
        if(isActive && shootingCount <= shootingRange){
            weaponImage.drawFromTopLeft(xCoordinate - weaponImage.getHeight()/2, activateYCoordinate);
        }else if(isPicked){
            weaponImage.drawFromTopLeft(xCoordinate - weaponImage.getHeight()/2, yCoordinate);
        }else {
            weaponImage.drawFromTopLeft(xCoordinate, yCoordinate);
        }
    }

    /**
     * This method will update the movement of the weapon
     */
    public void update() {
        renderWeapon();
        if(isActive) {
            xCoordinate += SHOOTING_SPEED;
            shootingCount++;
        }else if(isPicked) {
            //do nothing
        }
        else{
            xCoordinate -= currentSpeed;
        }
    }

    /**
     * This method get the bounding box of weapon.
     * @return Rectangle This method returns the rectangle of the weapon.
     */
    public Rectangle getWeaponBox() {
        Point weaponPosition = new Point(xCoordinate, yCoordinate);
        weaponBound = new Rectangle(weaponPosition, weaponImage.getWidth(), weaponImage.getHeight());
        return weaponBound;
    }

    /**
     * This method will activate the weapon.
     * @param xCoordinate The X-coordinate of the weapon when activating.
     * @param yCoordinate The Y-coordinate of the weapon when activating.
     */
    public void activate(double xCoordinate, double yCoordinate) {
        isActive = true;
        activateYCoordinate = yCoordinate;
        this.xCoordinate = xCoordinate;
    }

    /**
     * This method will make sure the bird is carrying the weapon.
     * @param xCoordinate The X-coordinate of the weapon when carrying.
     * @param yCoordinate The Y-coordinate of the weapon when carrying.
     */
    public void carrying(double xCoordinate, double yCoordinate) {
        isPicked = true;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    // Create weapon with random position
    /**
     * This method generates weapon at random position.
     */
    public void randomWeaponPosition() {
        double rand = (Math.random() * range) + MIN_Y;
        yCoordinate = rand;
    }

    /**
     * The method checks if a weapon is out of screen.
     * @return boolean Returns whether a weapon is out of screen
     */
    public boolean outOfScreen() {
        if(xCoordinate + weaponImage.getWidth() < 0) {
            return true;
        }
        return false;
    }

    /**
     * This method updates the speed of the weapon based on the timescale.
     * @param timescale The current timescale of the game.
     */
    public void updateCurrSpeed(int timescale) {
        currentSpeed = INITIAL_SPEED;
        for(int i=1; i<timescale; i++) {
            currentSpeed = currentSpeed * (1+RATE);
        }
    }

    /**
     * This method checks if the weapon is out of its shooting range.
     * @return boolean Returns whether a weapon is out of its shooting range.
     */
    public boolean outOfShootingRange() {
        if(isActive && shootingCount >= shootingRange) {
            return true;
        }
        return false;
    }

    /**
     * This method sets the weapon image.
     * @param weaponImage The weapon image.
     */
    public void setWeaponImage(Image weaponImage) {
        this.weaponImage = weaponImage;
    }

    /**
     * This method sets the target types of weapon.
     * @param targetType The target types of weapon.
     */
    public void setTargetType(String[] targetType) {
        this.targetType = targetType;
    }

    /**
     * This method gets the target type of weapon.
     * @return String[] Returns the target types
     */
    public String[] getTargetType() {
        return targetType;
    }

    /**
     * This method sets the name of weapon.
     * @param name The name of weapon.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the shooting range of weapon.
     * @param shootingRange Shooting range of weapon.
     */
    public void setShootingRange(int shootingRange) {
        this.shootingRange= shootingRange;
    }

    /**
     * This method gets the picking status of weapon.
     * @return boolean Returns the picking status of weapon.
     */
    public boolean isPicked() {
        return isPicked;
    }

    /**
     * This method gets the active status of weapon.
     * @return boolean Returns whether the weapon is activated.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * This method will generate a random type of weapon.
     * @return Weapon Returns a random weapon object.
     */
    // Get random type of pipe
    public static Weapon getRandomWeapon(int timescale) {
        Weapon newWeapon;
        Random random = new Random();
        int rand = random.nextInt(2);
        switch (rand) {
            case 0:
                newWeapon = new Rock();
                break;
            case 1:
                newWeapon = new Bomb();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rand);
        }
        newWeapon.updateCurrSpeed(timescale);
        return newWeapon;
    }
}
