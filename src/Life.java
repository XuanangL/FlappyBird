import bagel.Image;
import org.lwjgl.system.CallbackI;
/**
 * Class to load life of the bird.
 */
public class Life {
    private final double HEART_X_COORDINATE = 100;
    private final double HEART_Y_COORDINATE = 15;
    private final double HEART_SPACE = 50;
    private final Image FULL_LIFE = new Image("res/level/fullLife.png");
    private final Image NO_LIFE = new Image("res/level/noLife.png");

    /**
     * This is the default constructor for life.
     */
    public Life() {

    }

    /**
     * This method renders the life of the bird.
     * @param currLife int The number of life the bird has.
     * @param maxLife int The maximum number of life a bird can have.
     */
    // Render life at the top left of the screen
    public void renderLife(int currLife, int maxLife) {
        for(int i=0; i<currLife; i++) {
            FULL_LIFE.drawFromTopLeft(HEART_X_COORDINATE + HEART_SPACE * i,HEART_Y_COORDINATE);
        }
        for(int j = currLife; j<maxLife; j++) {
            NO_LIFE.drawFromTopLeft(HEART_X_COORDINATE + HEART_SPACE * j,HEART_Y_COORDINATE);
        }
    }
}
