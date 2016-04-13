import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by saygin on 4/4/2016.
 */
public class CardView {
    Card card;
    BufferedImage image;
    boolean isOpen;

    public CardView( Card card, boolean isOpen ){
        this.isOpen = isOpen;
        this.card = card;
        String fileName = "";
        switch ( card.getSymbol()){
            case Spades: fileName += "S";
                break;
            case Clubs: fileName += "C";
                break;
            case Hearts: fileName += "H";
                break;
            case Diamonds: fileName += "D";
                break;

        }
        fileName += card.getFaceValue();
        try {
            this.image = ImageIO.read( new File("Images/"  + fileName + ".gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(){
        if(isOpen){
            return image;
        }
        else{
            BufferedImage closed = null;
            try {
                closed = ImageIO.read( new File("Images/back.gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return closed;
        }
    }
}
