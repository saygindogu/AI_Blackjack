import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by saygin on 4/4/2016.
 */
public class GamePanel extends JPanel {
    public static final int HEIGHT = 500;
    public static final int WIDTH = 800;
    public static final int DEALER_HAND_X = 10;
    public static final int DEALER_HAND_Y = 60;
    public static final int HAND_X = 10;
    public static final int HAND_Y = HEIGHT - 150;
    private Blackjack bj;

    public GamePanel( Blackjack bj){
        setPreferredSize( new Dimension( WIDTH, HEIGHT));

        this.bj = bj;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Image background = Toolkit.getDefaultToolkit().createImage("Images/background.jpg");
        BufferedImage background = null;
        try {
            background = ImageIO.read(new File("Images/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage( background, 0,0, null);
        Hand dealerHand = bj.getDealerHand();
        for (int i = 0; i < dealerHand.getCards().size(); i++) {
            CardView c = null;
            if( i == 0 && bj.isUsersTurn()) {
                c = new CardView(dealerHand.getCards().get(i), false);
            }
            else {
                c = new CardView(dealerHand.getCards().get(i), true);
            }
            g.drawImage( c.getImage(), DEALER_HAND_X + i * 75, DEALER_HAND_Y, null);
        }

        Hand userHand = bj.getUserHand();
        for( int i = 0; i < userHand.getCards().size(); i++){
            CardView c = new CardView( userHand.getCards().get(i), true);
            g.drawImage( c.getImage(), HAND_X + i * 75, HAND_Y, null);
        }
    }
}
