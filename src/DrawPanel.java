import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.util.ArrayList;

class DrawPanel extends JPanel implements MouseListener {

    private ArrayList<Card> hand;
    private Rectangle button;
    private Rectangle startOverButton;
    private ArrayList<Card> deck;
    private boolean lost;
    private boolean win;

    public DrawPanel() {
        button = new Rectangle(72, 230, 160, 26);
        this.addMouseListener(this);
        startOverButton = new Rectangle(272, 100, 125, 26);
        hand = Card.buildHand();
        deck = Card.buildDeck();
        lost = false;
        win = false;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50;
        int y = 10;
        int cardsInRow = 0;
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c.getHighlight()) {
                g.drawRect(x, y, c.getImage().getWidth(), c.getImage().getHeight());
            }
            c.setRectangleLocation(x, y);
            g.drawImage(c.getImage(), x, y, null);
            cardsInRow++;
            x = x + c.getImage().getWidth() + 10;
            if (cardsInRow == 3) {
                x = 50;
                y += 75;
                cardsInRow = 0;
            }
        }
        for (int i = 0; i < hand.size(); i++) {
            for (int j = 0; j < deck.size(); j++) {
                if (hand.get(i).getSuit().equals(deck.get(j).getSuit()) && hand.get(i).getValue().equals(deck.get(j).getValue())) {
                    deck.remove(j);
                    j--;
                }
            }
        }

        if (!anotherPlayIsPossible()) {
            lost = true;
        }

        if (hand.isEmpty() && deck.isEmpty()) {
            win = true;
        }

        if (!lost && !win) {
            g.setFont(new Font("Courier New", Font.BOLD, 20));
            g.drawString("REPLACE CARDS", 75, 250);
            g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());
            g.setFont(new Font("Courier New", Font.BOLD, 16));
            g.drawString("Cards Remaining: " + deck.size(), 10, 300);

        }
        else if (lost) {
            g.setFont(new Font("Courier New", Font.BOLD, 20));
            g.setColor(Color.RED);
            g.drawString("PLAY AGAIN", 275, 120);
            g.drawRect((int)startOverButton.getX(), (int)startOverButton.getY(), (int)startOverButton.getWidth(), (int)startOverButton.getHeight());
            g.drawString("YOU LOST :(", 75, 250);
            g.setFont(new Font("Courier New", Font.BOLD, 16));
            g.setColor(Color.BLACK);
            g.drawString("Cards Remaining: " + deck.size(), 10, 300);
        }
        else if (win) {
            g.setFont(new Font("Courier New", Font.BOLD, 20));
            g.setColor(Color.GREEN);
            g.drawString("YOU WON! :)", 75, 250);
        }
    }

    public void mousePressed(MouseEvent e) {
            Point clicked = e.getPoint();

            if (e.getButton() == 1) {
                if (startOverButton.contains(clicked) && lost) {
                    hand = Card.buildHand();
                    deck = Card.buildDeck();
                    lost = !lost;
                } else if (button.contains(clicked) && !lost) {
                    ArrayList<Card> selectedCards = new ArrayList<>();
                    for (int i = 0; i < hand.size(); i++) {
                        if (hand.get(i).getHighlight()) {
                            selectedCards.add(hand.get(i));
                        }
                    }
                    int sum = 0;
                    boolean jack = false;
                    boolean queen = false;
                    boolean king = false;
                    for (int i = 0; i < selectedCards.size(); i++) {
                        if (selectedCards.size() == 3) {
                            if (selectedCards.get(i).getValue().equals("J")) {
                                jack = true;
                            } else if (selectedCards.get(i).getValue().equals("Q")) {
                                queen = true;
                            } else if (selectedCards.get(i).getValue().equals("K")) {
                                king = true;
                            }
                        }
                        sum += selectedCards.get(i).getPointValue();
                    }
                    if (jack && queen && king) {
                        for (int i = 0; i < selectedCards.size(); i++) {
                            for (int j = 0; j < hand.size(); j++) {
                                if (selectedCards.get(i).getValue().equals(hand.get(j).getValue()) && selectedCards.get(i).getSuit().equals(hand.get(j).getSuit())) {
                                    hand.set(j, deck.remove((int) (Math.random() * deck.size())));
                                }
                            }
                        }
                    } else if (sum == 11) {
                        for (int i = 0; i < selectedCards.size(); i++) {
                            for (int j = 0; j < hand.size(); j++) {
                                if (selectedCards.get(i).getPointValue() == hand.get(j).getPointValue() && selectedCards.get(i).getSuit().equals(hand.get(j).getSuit())) {
                                    hand.set(j, deck.remove((int) (Math.random() * deck.size())));
                                }
                            }
                        }
                    }
                }

                if (!lost) {
                    for (int i = 0; i < hand.size(); i++) {
                        Rectangle box = hand.get(i).getCardBox();
                        if (box.contains(clicked)) {
                            hand.get(i).flipCard();
                        }
                    }
                }
            }

            if (e.getButton() == 3 && !lost) {
                for (int i = 0; i < hand.size(); i++) {
                    Rectangle box = hand.get(i).getCardBox();
                    if (box.contains(clicked)) {
                        hand.get(i).flipHighlight();
                    }
                }
            }
    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }

    public boolean anotherPlayIsPossible() {
        if (contains11(hand) || containsJQK(hand)) {
            return true;
        }
        return false;
    }

    public boolean contains11(ArrayList<Card> hand) {
        for (int i = 0; i < hand.size(); i++) {
            int num1 = hand.get(i).getPointValue();
            for (int j = i + 1; j < hand.size(); j++) {
                int num2 = hand.get(j).getPointValue();
                if (num1 + num2 == 11) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsJQK(ArrayList<Card> hand) {
        boolean j = false;
        boolean q = false;
        boolean k = false;
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getValue().equals("J")) {
                j = true;
            }
            else if (hand.get(i).getValue().equals("Q")) {
                q = true;
            }
            else if (hand.get(i).getValue().equals("K")) {
                k = true;
            }
        }
        return (j && q && k);
    }
}