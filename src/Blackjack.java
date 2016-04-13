import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by saygin on 4/4/2016.
 */
public class Blackjack {
    private static Blackjack instance = null;
    private Hand userHand;
    private Hand dealerHand;
    private Deck deck;
    private boolean isUsersTurn;
    private GamePanel gamePanel;
    private boolean gameIsEnded;

    private Blackjack(){
        deck = new Deck();
        deck.shuffle();
        userHand = new Hand();
        dealerHand = new Hand();
        userHand.addCardToHand(deck.pop());
        userHand.addCardToHand(deck.pop());
        dealerHand.addCardToHand(deck.pop());
        dealerHand.addCardToHand(deck.pop());
        isUsersTurn = true;
        gameIsEnded = false;
    }

    public static void main(String[] args) {
        Blackjack bj = Blackjack.getInstance();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE);
        JPanel buttonPanel = new JPanel();
        JPanel advicePanel = new JPanel();

        JButton newGame = new JButton( "NEW GAME");
        JButton hit = new JButton( "HIT");
        JButton stand = new JButton( "STAND");
        JButton advice = new JButton( "ADVICE");

        JLabel adviceLabel = new JLabel( "");

        buttonPanel.add(newGame);
        buttonPanel.add(hit);
        buttonPanel.add(stand);
        buttonPanel.add(advice);

        advicePanel.add( adviceLabel);
        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(advicePanel, BorderLayout.NORTH);

        GamePanel gp = new GamePanel( bj);
        bj.setGamePanel(gp);
        frame.add(gp, BorderLayout.CENTER);

        hit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bj.isUsersTurn) {
                    bj.hit(bj.getUserHand());
                    adviceLabel.setText("");
                    gp.repaint();
                }
            }
        });

        stand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bj.stand();
                adviceLabel.setText( "");
                gp.repaint();
            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bj.newGame();
                adviceLabel.setText( "");
                gp.repaint();
            }
        });

        advice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO give an advice wihle pretending you dont have acces to card 0
                boolean hit = true;
                State state = new State();
                state.generateState( Blackjack.getInstance().userHand, Blackjack.getInstance().dealerHand);
                int score = Blackjack.getInstance().userHand.getScore();
                if( handHasAce(  Blackjack.getInstance().userHand) ){
                    if( score > 18){
                        hit = false;
                    }
                    else{
                        if( state.getWantedPercentage( score) > 30.27){
                            hit = true;
                        }
                        else {
                            hit = false;
                        }
                    }
                }
                else{
                    if( state.getWantedPercentage( score) > 30.27){
                        hit = true;
                    }
                    else {
                        hit = false;
                    }
                }

                if( hit){
                    adviceLabel.setText( "HIT!");
                }
                else{
                    adviceLabel.setText( "STAND!");
                }
            }
        });

        frame.pack();
        frame.setVisible(true);

    }

    private static boolean handHasAce(Hand hand) {
        boolean retVal = false;
        for (int i = 0; i < hand.getCards().size(); i++) {
            retVal|= hand.getCards().get(i).getFaceValue() == 1;
        }
        return retVal;
    }

    private static Blackjack getInstance() {

        if( instance == null){
            instance = new Blackjack();
        }
        return instance;
    }

    private void newGame() {
        deck = new Deck();
        deck.shuffle();
        userHand = new Hand();
        dealerHand = new Hand();
        userHand.addCardToHand(deck.pop());
        userHand.addCardToHand(deck.pop());
        dealerHand.addCardToHand(deck.pop());
        dealerHand.addCardToHand(deck.pop());
        isUsersTurn = true;
        gameIsEnded = false;
    }

    private void stand() {
        isUsersTurn = false;
        dealerTurn();
    }

    private void dealerTurn() {
        if ( dealerHand.getScore() > 17 ) {
            endGame();
        }
        else{
            hit( dealerHand);
            if( !gameIsEnded) {
                dealerTurn();
            }
        }
    }


    private void hit(Hand hand) {
        hand.addCardToHand( deck.pop());
        if( hand.getScore() > 21){
            endGame();
        }
    }

    private void endGame(){
        gameIsEnded = true;
        isUsersTurn = false;
        int dealerScore = dealerHand.getScore();
        int userScore = userHand.getScore();
        if( userScore > 21 ){
            gamePanel.repaint();
            final JOptionPane pane = new JOptionPane("User Busted");
            final JDialog d = pane.createDialog( null, "Game is finished");
            d.setLocation(300,200);
            d.setVisible(true);
            //JOptionPane.showMessageDialog( null, "User Busted", "Game is finished", JOptionPane.INFORMATION_MESSAGE);
        }
        else if( dealerScore > 21){
            gamePanel.repaint();
            final JOptionPane pane = new JOptionPane("Dealer Busted");
            final JDialog d = pane.createDialog( null, "Game is finished");
            d.setLocation(300,200);
            d.setVisible(true);
            //JOptionPane.showMessageDialog( null, "Dealer Busted", "Game is finished", JOptionPane.INFORMATION_MESSAGE);
        }
        else if( userScore > dealerScore){
            gamePanel.repaint();
            final JOptionPane pane = new JOptionPane("User Wins");
            final JDialog d = pane.createDialog( null, "Game is finished");
            d.setLocation(300,200);
            d.setVisible(true);
            //JOptionPane.showMessageDialog( null, "User Wins", "Game is finished", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            gamePanel.repaint();
            final JOptionPane pane = new JOptionPane("Dealer Wins");
            final JDialog d = pane.createDialog( null, "Game is finished");
            d.setLocation(300,200);
            d.setVisible(true);
            //JOptionPane.showMessageDialog( null, "Dealer Wins", "Game is finished", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public Hand getUserHand() {
        return userHand;
    }

    public boolean isUsersTurn() {
        return isUsersTurn;
    }

    public void setIsUsersTurn(boolean isUsersTurn) {
        this.isUsersTurn = isUsersTurn;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
}
