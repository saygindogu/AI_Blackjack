import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

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
    private int analysisScore;
    private static Thread thread;

    private Blackjack(){
        analysisScore = 0;
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
        JButton analyze = new JButton( "ANALYZE");
        JButton stop = new JButton( "STOP ANALYSIS");

        JTextField tf = new JTextField( "100");

        JProgressBar progressBar = new JProgressBar();
        progressBar.setVisible(false);

        JLabel adviceLabel = new JLabel( "");

        buttonPanel.add(newGame);
        buttonPanel.add(hit);
        buttonPanel.add(stand);
        buttonPanel.add(advice);
        buttonPanel.add( tf);
        buttonPanel.add( analyze);
        buttonPanel.add(stop);

        advicePanel.add(adviceLabel);
        advicePanel.add(progressBar);
        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(advicePanel, BorderLayout.NORTH);

        GamePanel gp = new GamePanel( bj);
        bj.setGamePanel(gp);
        frame.add(gp, BorderLayout.CENTER);

        hit.addActionListener(e -> {
            if (bj.isUsersTurn) {
                bj.hit(bj.getUserHand(), false);
                adviceLabel.setText("");
                gp.repaint();
            }
        });

        stand.addActionListener(e -> {
            bj.stand(false);
            adviceLabel.setText("");
            gp.repaint();
        });

        newGame.addActionListener(e -> {
            bj.newGame();
            adviceLabel.setText("");
            gp.repaint();
        });

        advice.addActionListener(e -> {
            //TODO give an advice wihle pretending you dont have acces to card 0
            boolean hit1 = true;
            State state = new State();
            ArrayList<Card> seenDealerCards = new ArrayList<Card>();
            seenDealerCards.add(Blackjack.getInstance().dealerHand.getCards().get(1));
            Hand seenDealerHand = new Hand(seenDealerCards);
            state.generateState( Blackjack.getInstance().userHand, seenDealerHand);
            double hitProb, stand1;
            hitProb = state.getHitWinProb();
            stand1 = state.getStandWinProb();
            String advice1 = hitProb >= stand1 ? "HIT" : "STAND";
            NumberFormat formatter = new DecimalFormat("#0.00");
            adviceLabel.setText("Hit:" + formatter.format(hitProb) + " Stand:" + formatter.format(stand1) + " so we advice you to " + advice1);
        });

        analyze.addActionListener(e -> {
            thread = new Thread(() -> {
                int count = 0;
                try {
                    count = Integer.parseInt(tf.getText());
                }
                catch ( NumberFormatException nfe){
                    adviceLabel.setText( "Invalid Input as number");
                    return;
                }
                newGame.setEnabled( false);
                hit.setEnabled( false);
                stand.setEnabled( false);
                advice.setEnabled( false);
                analyze.setEnabled( false);
                bj.analysisScore = 0;
                progressBar.setMinimum( 0);
                progressBar.setMaximum(count);
                progressBar.setVisible( true);
                for (int i = 0; i < count; i++) {
                    if( thread.isInterrupted() ){
                        count = i;
                        break;
                    }
                    progressBar.setValue( i);
                    //Test Algorithm
                    State state = new State();
                    ArrayList<Card> seenDealerCards = new ArrayList<Card>();
                    seenDealerCards.add(Blackjack.getInstance().dealerHand.getCards().get(1));
                    Hand seenDealerHand = new Hand(seenDealerCards);
                    state.generateState( Blackjack.getInstance().userHand, seenDealerHand);
                    double hitProb, stand1;
                    hitProb = state.getHitWinProb();
                    stand1 = state.getStandWinProb();
                    if ( hitProb > stand1){
                        bj.hit( bj.getUserHand(), true);
                    }
                    else{
                        bj.stand( true);
                    }
                    //Until Here algorithm
                    //Test 17 rule
//                            if(bj.userHand.getScore() <= 16){
//                                bj.hit( bj.getUserHand(), true);
//                            }
//                            else{
//                                bj.stand( true);
//                            }
                    //until here 17 rule
                    bj.newGame();
                }
                double overallScore = (double) bj.analysisScore / count;
                adviceLabel.setText( "Analysis Score:" + overallScore);
                progressBar.setVisible( false);
                newGame.setEnabled( true);
                hit.setEnabled( true);
                stand.setEnabled( true);
                advice.setEnabled(true);
                analyze.setEnabled(true);
            });
            thread.start();
            adviceLabel.setText("Analysis Thread is running:");
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread.interrupt();
            }
        });

        frame.pack();
        frame.setVisible(true);

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

    private void stand( boolean analysis) {
        isUsersTurn = false;
        dealerTurn( analysis);
    }

    private void dealerTurn( boolean analysis) {
        if ( dealerHand.getScore() > 17 ) {
            endGame( analysis);
        }
        else{
            hit(dealerHand, analysis);
            if( !gameIsEnded) {
                dealerTurn( analysis);
            }
        }
    }


    private void hit(Hand hand, boolean analysis) {
        hand.addCardToHand( deck.pop());
        if( hand.getScore() > 21){
            endGame( analysis);
        }
    }

    private void endGame( boolean analysis){
        gameIsEnded = true;
        isUsersTurn = false;
        int dealerScore = dealerHand.getScore();
        int userScore = userHand.getScore();
        if( analysis){
            if (userScore > 21) {
                analysisScore += 0;
            } else if (dealerScore > 21) {
                analysisScore++;
            } else if (userScore > dealerScore) {
                analysisScore++;
            } else {
                analysisScore += 0;
            }
        }
        else {
            if (userScore > 21) {
                gamePanel.repaint();
                final JOptionPane pane = new JOptionPane("User Busted");
                final JDialog d = pane.createDialog(null, "Game is finished");
                d.setLocation(300, 200);
                d.setVisible(true);
                //JOptionPane.showMessageDialog( null, "User Busted", "Game is finished", JOptionPane.INFORMATION_MESSAGE);
            } else if (dealerScore > 21) {
                gamePanel.repaint();
                final JOptionPane pane = new JOptionPane("Dealer Busted");
                final JDialog d = pane.createDialog(null, "Game is finished");
                d.setLocation(300, 200);
                d.setVisible(true);
                //JOptionPane.showMessageDialog( null, "Dealer Busted", "Game is finished", JOptionPane.INFORMATION_MESSAGE);
            } else if (userScore > dealerScore) {
                gamePanel.repaint();
                final JOptionPane pane = new JOptionPane("User Wins");
                final JDialog d = pane.createDialog(null, "Game is finished");
                d.setLocation(300, 200);
                d.setVisible(true);
                //JOptionPane.showMessageDialog( null, "User Wins", "Game is finished", JOptionPane.INFORMATION_MESSAGE);
            } else {
                gamePanel.repaint();
                final JOptionPane pane = new JOptionPane("Dealer Wins");
                final JDialog d = pane.createDialog(null, "Game is finished");
                d.setLocation(300, 200);
                d.setVisible(true);
                //JOptionPane.showMessageDialog( null, "Dealer Wins", "Game is finished", JOptionPane.INFORMATION_MESSAGE);
            }
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
