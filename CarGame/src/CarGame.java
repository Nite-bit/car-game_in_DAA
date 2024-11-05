import javax.swing.JFrame;

public class CarGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Racing Game");
        Board board = new Board();
        frame.add(board);
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
