import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;


/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Class for ShadowFlay game.
 * @author: Betty Lin(Use the solution of the project 1 as skeletion code) & Li Xuanang.
 */
public class ShadowFlap extends AbstractGame {
    private final Image LEVEL0_BACKGROUND = new Image("res/level-0/background.png");
    private final Image LEVEL1_BACKGROUND = new Image("res/level-1/background.png");
    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final String SHOOT_MSG = "PRESS 'S' TO SHOOT";
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private final int SHOOT_MSG_OFFSET = 68;
    private final int INITIAL_SPAWN_FREQ = 100;
    private final int INITIAL_WEAPON_SPAWN = 150;
    private final int LEVEL0_MAX = 10;
    private final int LEVEL1_MAX = 30;
    private final int LEVEL0_LIFE = 3;
    private final int LEVEL1_LIFE = 6;
    private final int LEVEL_UP_PAUSE = 150;              //Follow Demo Video constant
    private final int SCORE_X_COORDINATE = 100;
    private final int SCORE_Y_COORDINATE = 100;
    private final int MAX_TIMESCALE = 5;
    private final int MIN_TIMESCALE = 1;
    private final double TIMESCALE_RATE = 0.5;
    private int currWeaponSpawnFreq;
    private int currSpawnFreq;
    private Bird bird;
    private ArrayList<PipeSet> pipeSet;
    private ArrayList<Weapon> weapons;
    private Life life;
    private int score;
    private boolean gameOn;
    private boolean collision;
    private boolean win;
    private int currLevel;
    private int currLife;
    private int frameCount;
    private int pauseCount;
    private int timescale;


    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        currLevel = 0;
        bird = new Bird(currLevel);
        pipeSet = new ArrayList<>();
        weapons = new ArrayList<>();
        life = new Life();
        score = 0;
        gameOn = false;
        collision = false;
        win = false;
        currLife = LEVEL0_LIFE;
        frameCount = 0;
        pauseCount = 0;
        timescale = MIN_TIMESCALE;
        currSpawnFreq = INITIAL_SPAWN_FREQ;
        currWeaponSpawnFreq = INITIAL_WEAPON_SPAWN;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input The input from user.
     */
    @Override
    public void update(Input input) {
        if(currLevel == 0) {
            LEVEL0_BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }
        if(currLevel == 1) {
            LEVEL1_BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn && pauseCount == 0) {
            renderInstructionScreen(input);
            if(currLevel == 1) {
                renderShootMsg();
            }
        }

        // Lose life
        if (birdOutOfBound()) {
            currLife--;
            bird.respawn();
        }

        //Game over
        if (currLife <= 0) {
            renderGameOverScreen();
        }

        // Level 0 won, Level Up!
        if(win && currLevel == 0) {
            gameOn = false;
            renderLevelUpScreen();
            pauseCount++;
            if(pauseCount == LEVEL_UP_PAUSE) {
                bird = new Bird(currLevel);
                resetAttributes();
            }
        }

        // game won
        if (win && currLevel == 1) {
            renderWinScreen();
        }

        if (input.wasPressed(Keys.L)) {
            increaseTimescale();
        }

        if (input.wasPressed(Keys.K)) {
            decreaseTimescale();
        }
        // game is active
        if (gameOn && !win && currLife > 0) {

            if(pipeSet.size() == 0) {
                String scoreMsg = SCORE_MSG + score;
                FONT.drawString(scoreMsg, SCORE_X_COORDINATE, SCORE_Y_COORDINATE);
            }
            if (currLevel == 0) {
                life.renderLife(currLife, LEVEL0_LIFE);
            }
            if (currLevel == 1) {
                life.renderLife(currLife, LEVEL1_LIFE);
            }

            bird.update(input);
            Rectangle birdBox = bird.getBox();
            // Shoot weapon if 'S' is pressed
            if (input.wasPressed(Keys.S)) {
                bird.shoot();
            }

            // Generate pipes
            if((frameCount % currSpawnFreq == 0)  && (frameCount != 0)) {
                if(currLevel == 0)
                    pipeSet.add(new PlasticPipe(timescale));
                if(currLevel == 1)
                    pipeSet.add(PipeSet.getRandomPipe(timescale));
            }

            // Generate weapons
            if(((frameCount-currWeaponSpawnFreq) % (currSpawnFreq*2) == 0)  && (frameCount >= currWeaponSpawnFreq)) {
                if(currLevel == 1)
                    weapons.add(Weapon.getRandomWeapon(timescale));
            }
            frameCount++;

            // Initialise an Arraylist to store the weapons to be removed
            ArrayList<Weapon> weaponRemove = new ArrayList<>();
            // Generate Weapons and update its movement
            for(Weapon weapon: weapons) {
                weapon.update();
                Rectangle weaponBox = weapon.getWeaponBox();
                collision = detectWeaponBirdCollision(birdBox, weaponBox);
                if (collision == true) {
                    bird.pickUpWeapon(weapon);
                }
                if(weapon.outOfScreen() || weapon.outOfShootingRange()) {
                    weaponRemove.add(weapon);
                }
            }

            // Initialise an Arraylist to store the pipes to be removed
            ArrayList<PipeSet> pipeRemove = new ArrayList<>();
            // Update pipes movement
            for(PipeSet pipe: pipeSet) {
                pipe.update();
                Rectangle topPipeBox = pipe.getTopBox();
                Rectangle bottomPipeBox = pipe.getBottomBox();

                // Check if a bird is burnt by flame
                if(pipe.getName().equals("Steel Pipe")) {
                    Rectangle topFlameBox = ((SteelPipe) pipe).getTopFlameBox();
                    Rectangle botFlameBox = ((SteelPipe) pipe).getBotFlameBox();
                    boolean birdFlameCollision = detectFlameBirdCollision(birdBox, topFlameBox, botFlameBox);
                    if(birdFlameCollision == true ) {
                        currLife--;
                        pipeRemove.add(pipe);
                        break;
                    }
                }
                collision = detectCollision(birdBox, topPipeBox, bottomPipeBox);

                // Check if pipe is hit by weapon
                for(Weapon weapon: weapons) {
                    Rectangle weaponBox = weapon.getWeaponBox();
                    boolean weaponPipeCollision = detectWeaponPipeCollision(weaponBox, topPipeBox, bottomPipeBox);
                    if(weaponPipeCollision && weapon.isActive()) {
                        weaponRemove.add(weapon);
                        if (inTargetType(pipe.getClass().getName(), weapon.getTargetType()) ) {
                            score++;
                            pipeRemove.add(pipe);
                        }
                    }
                }
                // Remove weapons
                weapons.removeAll(weaponRemove);
                // Add pipes to pipeRemove
                if(collision == true) {
                    currLife--;
                    pipeRemove.add(pipe);
                    break;
                }
                if(pipe.outOfScreen()) {
                    pipeRemove.add(pipe);
                }
                updateScore(pipe);
            }
            pipeSet.removeAll(pipeRemove);
        }
    }

    /**
     * This method will check if the bird is out of screen.
     * @return boolean Returns if the bird is out of screen.
     * @author Betty Lin
     */
    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }

    /**
     * This method will render the instruction screen.
     * @author Betty Lin
     */
    public void renderInstructionScreen(Input input) {
        // paint the instruction on screen
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }

    /**
     * This method will render the game-over screen.
     * @author Betty Lin
     */
    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * This method will render the shoot instruction message
     */
    public void renderShootMsg() {
        FONT.drawString(SHOOT_MSG, (Window.getWidth()/2.0-(FONT.getWidth(SHOOT_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SHOOT_MSG_OFFSET);
    }

    /**
     * This method will render level-up screen.
     */
    public void renderLevelUpScreen() {
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
    }

    /**
     * This method will render win screen.
     * @author Betty Lin
     */
    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * This method will detect if there is collision between bird and pipe.
     * @return boolean Returns whether bird collides with pipe.
     * @author Betty Lin
     */
    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        return birdBox.intersects(topPipeBox) ||
                birdBox.intersects(bottomPipeBox);
    }
    /**
     * This method will detect if there is collision between bird and weapon.
     * @return boolean Returns whether bird collides with a weapon.
     */
    public boolean detectWeaponBirdCollision(Rectangle birdBox, Rectangle weaponBox) {
        return birdBox.intersects(weaponBox);
    }
    /**
     * This method will detect if there is collision between weapon and pipe.
     * @return boolean Returns whether a weapon collides with pipe.
     */
    public boolean detectWeaponPipeCollision(Rectangle weaponBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        return weaponBox.intersects(topPipeBox) || weaponBox.intersects(bottomPipeBox);
    }

    /**
     * This method will reset the game to initial state.
     */
    public void resetAttributes() {
        win = false;
        currLevel++;
        pauseCount = 0;
        pipeSet = new ArrayList<PipeSet>();
        frameCount = 0;
        timescale = MIN_TIMESCALE;
        currSpawnFreq = INITIAL_SPAWN_FREQ;
        currWeaponSpawnFreq = INITIAL_WEAPON_SPAWN;
    }

    /**
     * This method will detect if there is collision between bird and flame.
     * @return boolean Returns whether bird collides with flame.
     */
    public boolean detectFlameBirdCollision(Rectangle birdBox, Rectangle topFlameBox, Rectangle botFlameBox) {
        if(topFlameBox == null || botFlameBox == null) {
            return false;
        }
        return birdBox.intersects(topFlameBox) || birdBox.intersects(botFlameBox);
    }

    /**
     * This method increases the timescale of the game.
     */
    public void increaseTimescale() {
        if(this.timescale < MAX_TIMESCALE) {
            this.timescale++;
            currSpawnFreq /= (1 + TIMESCALE_RATE);
            currWeaponSpawnFreq /= (1 + TIMESCALE_RATE);
            for(PipeSet pipe: pipeSet) {
                pipe.updateCurrSpeed(timescale);
            }
            for(Weapon weapon: weapons) {
                weapon.updateCurrSpeed(timescale);
            }
        }
    }

    /**
     * This method decreases the timescale of the game.
     */
    public void decreaseTimescale() {
        if(this.timescale > MIN_TIMESCALE) {
            this.timescale--;
            currSpawnFreq *= (1 + TIMESCALE_RATE);
            currWeaponSpawnFreq *= (1 + TIMESCALE_RATE);
            for(PipeSet pipe: pipeSet) {
                pipe.updateCurrSpeed(timescale);
            }
            for(Weapon weapon: weapons) {
                weapon.updateCurrSpeed(timescale);
            }
        }
    }

    /**
     * This method will update the score of the game.
     * @author Betty Lin
     */
    public void updateScore(PipeSet pipe) {
        if (bird.getX() > pipe.getTopBox().right() && pipe.isPassed() == false) {
            score += 1;
            pipe.setPassed(true);
        }
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, SCORE_X_COORDINATE, SCORE_Y_COORDINATE);

        // detect win and Level Up
        if (score == LEVEL0_MAX && currLevel == 0) {
            win = true;
            score = 0;
            currLife = LEVEL1_LIFE;
        }
        // Level 1
        if (score == LEVEL1_MAX && currLevel == 1) {
            win = true;
        }
    }

    /**
     * This method checks if the target is in targetType.
     * @param target The target string.
     * @param targetType The array of targets.
     * @return boolean Returns whether the target is in target type.
     */
    public boolean inTargetType(String target, String[] targetType) {
        for(int i=0; i<targetType.length; i++) {
            if(target.equals(targetType[i])) {
                return true;
            }
        }
        return false;
    }

}