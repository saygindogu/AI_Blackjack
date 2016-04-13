import java.util.ArrayList;

/**
 * Created by saygin on 4/4/2016.
 */
public class Deck {
    ArrayList<Card> cards;
    
    //Construct a sorted deck
    public Deck(){
        cards = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            cards.add( new Card(i));
        }
    }

    public void shuffle(){
        for (int i = 0; i < 5000; i++) {
            int x = (int) (Math.random() * cards.size());
            int y = (int) (Math.random() * cards.size());
            Card temp = cards.get(x);
            cards.set(x, cards.get(y));
            cards.set(y, temp);
        }
    }

    public Card pop(){
        Card retVal = cards.get( cards.size() - 1);
        cards.remove( cards.size() - 1 );
        return retVal;
    }
}
