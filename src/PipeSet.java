import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Class provides features of pipe set.
 */
public abstract class PipeSet implements movable{
    private final int LOW_GAP_Y = 500;
    private final int MID_GAP_Y = 300;
    private final int HIGH_GAP_Y = 100;
    private final int PIPE_GAP = 168;
    private final double RATE = 0.5;
    protected final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    protected Image pipeImage;
    protected double pipeX = Window.getWidth();
    protected double topPipeY;
    protected double botPipeY;
    private double currentSpeed = INITIAL_SPEED;
    private String name;
    private int min = 1;
    private int max = 3;
    private int range = max - min + 1;
    private boolean isPassed = false;
    private int timescale = 1;
    private Point botPipesPosition;
    private Point topPipesPosition;
    private Rectangle topPipeBound;
    private Rectangle botPipeBound;

    /**
     * This is the default constructor for Pipeset.
     */
    public PipeSet() {
    }

    /**
     * This method render a pipe.
     */
    public void renderPipeSet() {
        pipeImage.drawFromTopLeft(pipeX, topPipeY);
        pipeImage.drawFromTopLeft(pipeX, botPipeY, ROTATOR);
    }


    /**
     * This method updates the movement of a pipe.
     */
    public void update() {
        renderPipeSet();
        pipeX -= currentSpeed;
    }

    /**
     * This method get the bounding box of the top pipe.
     * @return Rectangle This method returns the Rectangle of the top pipe.
     */
    public Rectangle getTopBox() {
        topPipesPosition = new Point(pipeX, topPipeY);
        topPipeBound = new Rectangle(topPipesPosition, pipeImage.getWidth(), pipeImage.getHeight());
        return topPipeBound;
    }
    /**
     * This method get the bounding box of the bottom pipe.
     * @return Rectangle This method returns the Rectangle of the bottom pipe.
     */
    public Rectangle getBottomBox() {
        botPipesPosition = new Point(pipeX, botPipeY);
        botPipeBound = new Rectangle(botPipesPosition, pipeImage.getWidth(), pipeImage.getHeight());
        return botPipeBound;
    }

    /**
     * This method checks if the pipe is out of screen.
     * @return boolean Returns true if it is out of the screen, otherwise return false.
     */
    public boolean outOfScreen() {
        if(pipeX + pipeImage.getWidth() < 0) {
            return true;
        }
        return false;
    }

    // Create pipe with random position
    /**
     * This method will generate the pipe randomly.
     */
    public void randomPipePosition() {
        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 0:
                topPipeY = -pipeImage.getHeight() + HIGH_GAP_Y;
                break;
            case 1:
                topPipeY = -pipeImage.getHeight() + MID_GAP_Y;
                break;
            case 2:
                topPipeY = -pipeImage.getHeight() + LOW_GAP_Y;
                break;
        }

        botPipeY = topPipeY + PIPE_GAP + pipeImage.getHeight();
    }

    // Update pipe travel speed
    /**
     * This method will update the speed of the pipe according to the timescale passed in.
     * @param timescale The current timescale of the game.
     */
    public void updateCurrSpeed(int timescale) {
        currentSpeed = INITIAL_SPEED;
        for(int i=1; i<timescale; i++) {
            currentSpeed = currentSpeed * (1+RATE);
        }
    }

    /**
     * This method will set the name of pipe.
     * @param name The name of the pipe.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method will set the pipe image.
     * @param pipeImage The pipe image.
     */
    public void setPipeImage(Image pipeImage) {
        this.pipeImage = pipeImage;
    }

    /**
     * This method gets if a pipe is passed a bird.
     * @return boolean Returns if a pipe is passed a bird.
     */
    public boolean isPassed() {
        return isPassed;
    }

    /**
     * This method set the isPassed attribute.
     * @param passed The status of a pipe.
     */
    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    /**
     * This method gets the name of a pipe.
     * @return String Returns the name.
     */
    public String getName() {
        return name;
    }

    // Get random type of pipe
    /**
     * This method will generate a random type of pipe
     * @param timescale The current timescale of the game.
     * @return Pipeset Returns the pipe object.
     */
    public static PipeSet getRandomPipe(int timescale) {
        PipeSet newPipe;
        Random random = new Random();
        int rand = random.nextInt(2);
        switch (rand) {
            case 0:
                newPipe = new PlasticPipe(timescale);
                break;
            case 1:
                newPipe = new SteelPipe(timescale);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rand);
        }
        return newPipe;
    }
}
