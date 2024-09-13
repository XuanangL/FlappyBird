import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Class contains attributes and methods of steel pipe.
 */
public class SteelPipe extends PipeSet {
    private final Image PIPE_IMAGE = new Image("res/level-1/steelPipe.png");
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final int FLAME_DURATION = 30;    //Follow demo video
    private final int FLAME_FREQ = 20;
    private final int FLAME_OFFSET = 10;      //Follow demo constant
    private int frameCount;
    private boolean flameOn = false;
    private boolean hitBird = false;

    /**
     * This is the default constructor for Steel Pipe.
     * @param timescale The current timescale of the game.
     */
    public SteelPipe(int timescale) {
        super.setName("Steel Pipe");
        super.setPipeImage(PIPE_IMAGE);
        super.randomPipePosition();
        super.updateCurrSpeed(timescale);
    }
    /**
     * This method updates the movement of a pipe.
     */
    @Override
    public void update() {
        frameCount++;
        if(frameCount == FLAME_FREQ && flameOn == false) {
            frameCount = 0;
            flameOn = true;
        }

        if(frameCount < FLAME_DURATION && flameOn == true) {
            renderFlame();
        }else if(flameOn == true && frameCount >= FLAME_DURATION) {
            flameOn = false;
            frameCount = 0;
        }
        super.update();
    }

    /**
     * This method renders flame of steel pipe.
     */
    public void renderFlame() {
        FLAME_IMAGE.drawFromTopLeft(pipeX,
                topPipeY + pipeImage.getHeight() - FLAME_IMAGE.getHeight()/2 + FLAME_OFFSET);
        FLAME_IMAGE.drawFromTopLeft(pipeX, botPipeY - FLAME_IMAGE.getHeight()/2 - FLAME_OFFSET, ROTATOR);
    }

    /**
     * This method get the bounding box of top flame.
     * @return Rectangle This method returns the rectangle of the top flame.
     */
    public Rectangle getTopFlameBox() {
        Point flamePoint = new Point(pipeX,
                topPipeY + pipeImage.getHeight() - FLAME_IMAGE.getHeight()/2 + FLAME_OFFSET);
        Rectangle flameBox = new Rectangle(flamePoint, FLAME_IMAGE.getWidth(), FLAME_IMAGE.getHeight());
        return flameBox;
    }
    /**
     * This method get the bounding box of bottom flame.
     * @return Rectangle This method returns the rectangle of the bottom flame.
     */
    public Rectangle getBotFlameBox() {
        Point flamePoint = new Point(pipeX, botPipeY - FLAME_IMAGE.getHeight()/2 - FLAME_OFFSET);
        Rectangle flameBox = new Rectangle(flamePoint, FLAME_IMAGE.getWidth(), FLAME_IMAGE.getHeight());
        return flameBox;
    }
}
