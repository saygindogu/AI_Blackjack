import java.util.ArrayList;

/**
 * Created by saygin on 4/13/2016.
 */
public class State {
    int[] array;
    public static final int DECK_COUNT = 1;
    public State(){
        array = new int[10];
    }

    public void generateState( Hand userHand, Hand dealerHand){
        for (int i = 0; i < 10; i++) {
            array[i] = 4 * DECK_COUNT;
        }
        array[9] = 16 * DECK_COUNT;
        for (int i = 0; i < userHand.getCards().size(); i++) {
            int face = userHand.getCards().get(i).getFaceValue();
            if( face >= 10){
                array[9]--;
            }
            else{
                array[face-1]--;
            }

        }
        int dealerFace = dealerHand.getCards().get(1).getFaceValue();
        if( dealerFace >= 10){
            array[9]--;
        }
        else{
            array[dealerFace-1]--;
        }
    }

    public double getProbability(int value){
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += array[i];
        }
        return (double) array[value] / sum;
    }

    public double hit( int n){
        return 0;
    }

    public double stand( int n) {
        return 0;
    }

    public boolean hit(){
        double probHit = hit( 0); //TODO
        double probStand = stand(0);
        return probHit > probStand;
    }

    public double getWantedPercentage(int score) {
        int maxWanted = 21 - score;
        int wantedSum = 0;
        if( maxWanted >= 10){
            maxWanted = 10;
        }
        for (int i = 0; i < maxWanted; i++) {
            wantedSum += array[i];
        }
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += array[i];
        }
        return ((double)wantedSum) / sum * 100;

    }
}
