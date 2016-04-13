/**
 * Created by saygin on 4/4/2016.
 */
public class Card {

    public enum Symbol{
        Spades(0),
        Hearts(1),
        Diamonds(2),
        Clubs(3);

        private int symbol;
        Symbol( int val){
            symbol = val;
        }

        public int getSymbol(){
            return symbol;
        }
    }
    int value;

    public Card( Symbol face, int faceValue){
        value = face.getSymbol() * 13 + faceValue;
    }

    public Card( int val){
        value = val;
    }

    public Symbol getSymbol(){
        int face = value / 13;
        switch ( face){
            case 0: return Symbol.Spades;
            case 1: return Symbol.Hearts;
            case 2: return Symbol.Diamonds;
            case 3: return Symbol.Clubs;
            default: return Symbol.Hearts; // Kalpleri severiz.
        }
    }

    public int getFaceValue(){
        return (value % 13)+1;
    }

    // TODO bunu ikili kartlý olacak þekilde oyun içinde kullan. ( As için) Hand'e yaz
//    public int getScore(){
//        int faceValue = value % 13;
//        if( faceValue > 10 ){
//            return 10;
//        }
//        else{
//            return faceValue;
//        }
//    }

}
