import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame implements KeyListener {
    private static final long serialVersionUID = 1L;

    private Image platformImage;
    private Image playerImage;
    private Image collectibleImage;
    private Image enemyImage;
    private Player player;
    private Collectible[] collectibles;
    private Enemy[] enemies;

    private int collectedItems;
    private int lives;

    public Game() {
        setTitle("Your Game Title");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load images
        platformImage = Toolkit.getDefaultToolkit().getImage("enemy.jpeg");
        playerImage = Toolkit.getDefaultToolkit().getImage("enemy.jpeg");
        collectibleImage = Toolkit.getDefaultToolkit().getImage("enemy.jpeg");
        enemyImage = Toolkit.getDefaultToolkit().getImage("enemy.jpeg");

        // Initialize player, collectibles, and enemies
        player = new Player(playerImage, 50, 50);
        collectibles = new Collectible[]{
                new Collectible(collectibleImage, 200, 200),
                new Collectible(collectibleImage, 400, 300),
                new Collectible(collectibleImage, 600, 100)
        };
        enemies = new Enemy[]{
                new Enemy(enemyImage, 300, 400),
                new Enemy(enemyImage, 500, 200)
        };

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        collectedItems = 0;
        lives = 3;

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw platform, player, collectibles, enemies
        g2d.drawImage(platformImage, 100, 300, null);
        g2d.drawImage(playerImage, player.getX(), player.getY(), null);
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected())
                g2d.drawImage(collectibleImage, collectible.getX(), collectible.getY(), null);
        }
        for (Enemy enemy : enemies) {
            g2d.drawImage(enemyImage, enemy.getX(), enemy.getY(), null);
        }

        // Display statistics
        displayStatistics(g2d);
    }

    public void displayStatistics(Graphics2D g2d) {
        g2d.drawString("Lives: " + lives, 10, 20);
        g2d.drawString("Collected Items: " + collectedItems, 10, 40);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            player.moveLeft();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            player.moveRight();
        } else if (keyCode == KeyEvent.VK_UP) {
            player.jump();
        }
        checkCollisions();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void checkCollisions() {
        Rectangle playerBounds = player.getBounds();
        for (Collectible collectible : collectibles) {
            if (!collectible.isCollected() && playerBounds.intersects(collectible.getBounds())) {
                collectible.setCollected(true);
                collectedItems++;
            }
        }
        for (Enemy enemy : enemies) {
            if (playerBounds.intersects(enemy.getBounds())) {
                lives--;
                if (lives <= 0) {
                    gameOver();
                } else {
                    resetPlayerPosition();
                }
            }
        }
    }

    private void resetPlayerPosition() {
        player.setX(50);
        player.setY(50);
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        new Game();
    }

    class Player {
        private Image image;
        private int x, y;
        private static final int SPEED = 5;
        private boolean jumping;
        private static final int GRAVITY = 1;
        private int ySpeed;

        public Player(Image image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
            jumping = false;
            ySpeed = 0;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
        }

        public void moveLeft() {
            x -= SPEED;
        }

        public void moveRight() {
            x += SPEED;
        }

        public void jump() {
            if (!jumping) {
                jumping = true;
                ySpeed = -15; // Initial jump speed
            }
        }

        public void update() {
            if (jumping) {
                ySpeed += GRAVITY;
                y += ySpeed;
                if (y >= 500) {
                    y = 500;
                    jumping = false;
                }
            }
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    class Collectible {
        private Image image;
        private int x, y;
        private boolean collected;

        public Collectible(Image image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
            collected = false;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }
    }

    class Enemy {
        private Image image;
        private int x, y;

        public Enemy(Image image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
        }
    }
}