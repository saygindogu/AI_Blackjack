import java.util.ArrayList;

/**
 * Created by saygin on 4/13/2016.
 */
public class State {
    int[] array;
    public static final int DECK_COUNT = 1;
    // int userScore;
    // int dealerScore;
    Hand userHand;
    Hand dealerHand;

    public State() {
        array = new int[10];
    }

    public void generateState(Hand userHand, Hand dealerHand) {
        this.userHand = userHand;
        this.dealerHand = dealerHand;
        for (int i = 0; i < 10; i++) {
            array[i] = 4 * DECK_COUNT;
        }
        array[9] = 16 * DECK_COUNT;
        for (int i = 0; i < userHand.getCards().size(); i++) {
            int face = userHand.getCards().get(i).getFaceValue();
            if (face >= 10) {
                array[9]--;
            } else {
                array[face - 1]--;
            }

        }
        for (int i = 0; i < dealerHand.getCards().size(); i++) {
            int face = dealerHand.getCards().get(i).getFaceValue();
            if (face >= 10) {
                array[9]--;
            } else {
                array[face - 1]--;
            }

        }
    }


    public double getProbability(int value) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += array[i];
        }
        return (double) array[value - 1] / sum;
    }

    public double hit(int n) {
        return 0;
    }

    public double getHitWinProb() {
        // int neededScore = 21 - userHand.getScore();
        double prob = 0;
        for (int i = 0; i < 10; i++) {
            ArrayList<Card> newUserCards = new ArrayList<Card>(userHand.getCards());
            newUserCards.add(new Card(i));
            Hand newHand = new Hand(newUserCards);
            int userScore = newHand.getScore();
            if (userScore < 21) {
                State s = new State();
                s.generateState(newHand, dealerHand);
                prob += s.getWinProb() * getProbability(i + 1);
            } else if (userScore == 21) {
                State s = new State();
                s.generateState(newHand, dealerHand);
                double cardProb = getProbability(i + 1);
                double winProb = s.getStandWinProb();
                prob +=  winProb*cardProb;
            } else {
                prob += 0;
            }
        }
        return prob;
    }

    public double getStandWinProb() {
        return 1 - getWinProbofDealer();
    }

    public double getWinProb() {
        double hit = getHitWinProb();
        double stand = getStandWinProb();

        return hit + stand;
    }

    public double getWinProbofDealer() {
        double prob = 0;
        for (int i = 0; i < 10; i++) {
            ArrayList<Card> userCards = new ArrayList<Card>(userHand.getCards());
            Hand userHand = new Hand(userCards);
            int userScore = userHand.getScore();
            ArrayList<Card> newDealerCards = new ArrayList<Card>(dealerHand.getCards());
            newDealerCards.add(new Card(i));
            Hand newDealerHand = new Hand(newDealerCards);
            int newDealerScore = newDealerHand.getScore();
            if (newDealerScore < 17) {
                State s = new State();
                s.generateState(userHand, newDealerHand);
                prob += s.getWinProbofDealer() * getProbability(i + 1);
            } else if (17 <= newDealerScore && newDealerScore <= 21) {
                if (newDealerScore >= userScore) {
                    prob += 1 * getProbability(i + 1);
                } else {
                    prob += 0;
                }
            } else {
                prob += 0;
            }
        }
        return prob;
    }

    public double stand() {
        return 0;
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
