import bagel.Image;
/**
 * Class contains attributes of bomb
 */
public class Bomb extends Weapon {
    private final String[] TARGET_TYPE = {"PlasticPipe", "SteelPipe"};
    private final Image WEAPON_IMAGE = new Image("res/level-1/bomb.png");
    private final int SHOOTING_RANGE = 50;

    /**
     * This is the default constructor for Bomb.
     */
    public Bomb() {
        super.setName("Bomb");
        super.setWeaponImage(WEAPON_IMAGE);
        super.randomWeaponPosition();
        super.setShootingRange(SHOOTING_RANGE);
        super.setTargetType(TARGET_TYPE);
    }

}