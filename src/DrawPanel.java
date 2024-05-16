import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.util.ArrayList;

class DrawPanel extends JPanel implements MouseListener {

    private ArrayList<Card> hand;
    private Rectangle button;
    private Rectangle startOverButton;
    private ArrayList<Card> deck = Card.buildDeck();

    public DrawPanel() {
        button = new Rectangle(72, 230, 160, 26);
        this.addMouseListener(this);
        startOverButton = new Rectangle(272, 100, 125, 26);

        hand = Card.buildHand();
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
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("GET NEW CARDS", 75, 250);
        g.setFont(new Font("Courier New", Font.BOLD, 16));
        g.drawString("Cards Remaining: " + (deck.size() - hand.size()), 10, 300);
        g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.setColor(Color.RED);
        g.drawString("PLAY AGAIN", 275, 120);
        g.drawRect((int)startOverButton.getX(), (int)startOverButton.getY(), (int)startOverButton.getWidth(), (int)startOverButton.getHeight());
    }

    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        if (e.getButton() == 1) {
            if (button.contains(clicked) || startOverButton.contains(clicked)) {
                hand = Card.buildHand();
            }

            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    hand.get(i).flipCard();
                }
            }
        }

        if (e.getButton() == 3) {
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
}