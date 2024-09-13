import bagel.Image;

/**
 * Class contains attributes of plastic pipe
 */
public class PlasticPipe extends PipeSet {
    private final Image PIPE_IMAGE = new Image("res/level/plasticPipe.png");

    /**
     * This is the default constructor for Plastic Pipe.
     * @param timescale The current timescale of the game.
     */
    public PlasticPipe(int timescale) {
        super.setName("Plastic Pipe");
        super.setPipeImage(PIPE_IMAGE);
        super.randomPipePosition();
        super.updateCurrSpeed(timescale);
    }

}
