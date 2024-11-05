import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 400;
    private final int BOARD_HEIGHT = 600;
    private final int CAR_WIDTH = 50;
    private final int CAR_HEIGHT = 80;
    private final int DELAY = 10;

    private int carX = BOARD_WIDTH / 2 - CAR_WIDTH / 2;
    private int carY = BOARD_HEIGHT - CAR_HEIGHT - 10;
    private int score = 0;
    private boolean running = true;

    private Timer timer;
    private ArrayList<Obstacle> obstacles;
    private Random rand;

    public Board() {
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                moveCar(e);
            }
        });

        obstacles = new ArrayList<>();
        rand = new Random();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void moveCar(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && carX > 0) {
            carX -= 20;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && carX < BOARD_WIDTH - CAR_WIDTH) {
            carX += 20;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            drawCar(g);
            drawObstacles(g);
            drawScore(g);
        } else {
            gameOver(g);
        }
    }

    private void drawCar(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(carX, carY, CAR_WIDTH, CAR_HEIGHT);
    }

    private void drawObstacles(Graphics g) {
        g.setColor(Color.RED);
        for (Obstacle obs : obstacles) {
            g.fillRect(obs.getX(), obs.getY(), obs.getWidth(), obs.getHeight());
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 20);
    }

    private void spawnObstacle() {
        int obstacleX = rand.nextInt(BOARD_WIDTH - CAR_WIDTH);
        obstacles.add(new Obstacle(obstacleX, 0, CAR_WIDTH, CAR_HEIGHT / 2));
    }

    private void moveObstacles() {
        for (Iterator<Obstacle> iterator = obstacles.iterator(); iterator.hasNext();) {
            Obstacle obs = iterator.next();
            obs.moveDown();
            if (obs.getY() > BOARD_HEIGHT) {
                iterator.remove();
                score++;
            }
        }
    }

    private void checkCollision() {
        for (Obstacle obs : obstacles) {
            if (new Rectangle(carX, carY, CAR_WIDTH, CAR_HEIGHT).intersects(
                    new Rectangle(obs.getX(), obs.getY(), obs.getWidth(), obs.getHeight()))) {
                running = false;
                timer.stop();
            }
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(msg, (BOARD_WIDTH - metrics.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);

        String scoreMsg = "Score: " + score;
        g.drawString(scoreMsg, (BOARD_WIDTH - metrics.stringWidth(scoreMsg)) / 2, BOARD_HEIGHT / 2 + 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveObstacles();
            checkCollision();
            if (rand.nextInt(100) < 5) {
                spawnObstacle();
            }
        }
        repaint();
    }
}
