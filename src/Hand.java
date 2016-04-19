import java.util.ArrayList;

/**
 * Created by saygin on 4/4/2016.
 */
public class Hand {
    ArrayList<Card> cardsAtHand;

    public Hand(){
        cardsAtHand = new ArrayList<>();
    }

    public Hand(ArrayList<Card> cardsAtHand){
        this.cardsAtHand = cardsAtHand;
    }

    public int getScore(){
        int score = 0;
        int aceCount = 0;
        for (Card aCardAtHand : cardsAtHand) {
            if (aCardAtHand.getFaceValue() > 10) {
                score += 10;
            } else if (aCardAtHand.getFaceValue() == 1) {
                aceCount += 1;
                score += 1;
            } else {
                score += aCardAtHand.getFaceValue();
            }
        }
        for (int i = 0; i < aceCount; i++) {
            if( score + 10 <= 21){
                score += 10;
            }
        }
        return score;
    }

    //Cout Aces as 1
    public int getAlternativeScore(){
        int score = 0;
        for (Card aCardAtHand : cardsAtHand) {
            if (aCardAtHand.getFaceValue() > 10) {
                score += 10;
            } else {
                score += aCardAtHand.getFaceValue();
            }
        }
        return score;
    }

    public void addCardToHand( Card card){
        cardsAtHand.add( card);
    }

    public ArrayList<Card> getCards() {
        return cardsAtHand;
    }

}
