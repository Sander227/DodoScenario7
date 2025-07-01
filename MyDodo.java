import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 *
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.1 -- 29-07-2017
 */
public class MyDodo extends Dodo
{
    /* ATTRIBUTE DECLARATIONS: */
    private int myNrOfStepsTaken;

    private int score;

    public MyDodo() {
        super( EAST );
        /* INITIALISATION OF ATTRIBUTES: */
        myNrOfStepsTaken = 0;
    }

    /* METHODS OF THE CLASS: */

    public void act() {
    }

    /**
     * Move one cell forward in the current direction.
     * 
     * <P> Initial: Dodo is somewhere in the world
     * <P> Final: If possible, Dodo has moved forward one cell
     *
     */
    public void move() {
        if ( canMove() ) {
            step();
            myNrOfStepsTaken++;
            updateScore();
        } else {
            showError("I cannot move");
        }
    }

    /**
     * Test if Dodo can move forward, 
     * i.e. there are no obstructions or end of world in the cell in front of her.
     * 
     * <p> Initial:   Dodo is somewhere in the world
     * <p> Final:     Same as initial situation
     * 
     * @return  boolean true if Dodo can move (thus, no obstructions ahead)
     *                  false if Dodo can't move
     *                      there is an obstruction or end of world ahead
     */
    public boolean canMove() {
        if ( borderAhead() || fenceAhead() || Mauritius.MAXSTEPS <= myNrOfStepsTaken ) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Move given number of cells forward in the current direction.
     * 
     * <p> Initial:   
     * <p> Final:  
     * 
     * @param   int distance: the number of steps made
     */
    public void jump( int distance ) {
        int nrStepsTaken = 0;               // set counter to 0
        while ( nrStepsTaken < distance ) { // check if more steps must be taken  
            move();                         // take a step
            nrStepsTaken++;                 // increment the counter
        }
    }

    /**
     * Places all the Egg objects in the world in a list.
     * 
     * @return List of Egg objects in the world
     */
    public List<Egg> getListOfEggsInWorld() {
        return getWorld().getObjects(Egg.class);
    }
    
    /**
     * Creates a list of numbers
     */
    public List<Integer> createListOfNumbers() {
        return new ArrayList<> (Arrays.asList( 2, 43, 7, -5, 12, 7 ));
    }

    /**
     * Method for praciticing with lists.
     */
    public void practiceWithLists( ){
        List<Integer> listOfNumbers = createListOfNumbers();
        System.out.println("First element: " + listOfNumbers.get(1) ); 
    }
    /**
     * Randomly puts 12 surprise eggs in the world.
     */
    public void practiceWithListsOfSurpriseEgss( ){
        List<SurpriseEgg>  listOfEgss = SurpriseEgg.generateListOfSurpriseEggs( 12, getWorld() );
    }
    
    /**
     * Dodo moves 40 random spaces, it changes direction after each move.
     */
    public void moveRandomly(){

        Mauritius world = (Mauritius) getWorld();
        for (; myNrOfStepsTaken < Mauritius.MAXSTEPS; myNrOfStepsTaken++) {
            world.updateScore(Mauritius.MAXSTEPS - myNrOfStepsTaken);
            do {
                faceDirection(randomDirection());
            } while (borderAhead() || fenceAhead());

            move();

        }
        world.updateScore(Mauritius.MAXSTEPS - myNrOfStepsTaken);
    }

    /**
     * Faces in the direction that is given.
     */
    public void faceDirection(int direction){
        if (direction < NORTH || direction > WEST){
            return;
        }
        while (getDirection() != direction) {
            turnRight();
        }
    }
    /**
     * He first goes to the nearest egg, then he picks it up, then he goes to another nearest egg and collects it. This all happens in the max moves of 40.
     */
    public void collectEggs(){

        Egg egg = getNearestEgg();
        
        while (egg != null) {
            
            goToEgg(egg);
            
            if (Mauritius.MAXSTEPS <= myNrOfStepsTaken) {
                break;
            }
            
            score += egg.getValue();
            pickUpEgg();

            egg = getNearestEgg();
        }

    }
    /**
     * Prints out which egg is the closest.
     */
    public Egg getNearestEgg(){

        List<Egg> eggs = getWorld().getObjects(Egg.class);

        Egg nearestEgg = null;
        int nearestDistance = Integer.MAX_VALUE;

        for (Egg egg : eggs) {

            int xDistance = Math.abs(egg.getX() - getX());
            int yDistance = Math.abs(egg.getY() - getY());
            int totalDistance = xDistance + yDistance;

            if (totalDistance < nearestDistance) {

                nearestEgg = egg;
                nearestDistance = totalDistance;
            }
        } 
        return nearestEgg;  
    } 
    /**
     * goes to an egg.
     */
    public void goToEgg(Egg egg){

        int eggX = egg.getX();
        int eggY = egg.getY();

        if (getX() <= eggX) {
            faceDirection(EAST);
        } else {
            faceDirection(WEST);
        }

        while (eggX != getX() && canMove()) {
            move();
        }

        if (getY() <= eggY) {
            faceDirection(SOUTH);
        } else {
            faceDirection(NORTH);
        }

        while (eggY != getY() && canMove()) {
            move();
        }
    }
    /**
     * Updates the score of the leaderboard. It updates the moves left and what dodo's score is.
     */
    public void updateScore(){
        Mauritius world = (Mauritius) getWorld();
        world.updateScore(Mauritius.MAXSTEPS - myNrOfStepsTaken, score);
    }
}
