import bagel.Image;
/**
 * Class contains attributes of rock
 */
public class Rock extends Weapon{
    private final String[] TARGET_TYPE = {"PlasticPipe"};
    private final Image WEAPON_IMAGE = new Image("res/level-1/rock.png");
    private final int SHOOTING_RANGE = 25;

    /**
     * This is the default constructor for Rock.
     */
    public Rock() {
        super.setName("Rock");
        super.setWeaponImage(WEAPON_IMAGE);
        super.randomWeaponPosition();
        super.setShootingRange(SHOOTING_RANGE);
        super.setTargetType(TARGET_TYPE);
    }
}
