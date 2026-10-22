import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;

public class Ghosts {
    private final String[] imagePaths = {
        "/newpackage/spookje_paars.png",
        "/newpackage/spookje_blauw.png",
        "/newpackage/spookje_groen.png",
        "/newpackage/spookje_wit.png",
        "/newpackage/spookje_rood.png"
    };

    private int[] ghosts = new int[30];
    private JLabel[] labels = new JLabel[30];
    private int spawnPointX;
    private int spawnPointY;
    private CBLProject ui;
    private Random random = new Random();
    private int ghostAmount = 0;
    private final JPanel panel;
    private Timer timer = new Timer(10, e -> update());
    private double ghostSpeed = 1;               
    private long lastDropTime = System.nanoTime();
    private long lastPause;
    private double speedMultiplier = 1.0;        
    private int shields = 0;                     

    public Ghosts(CBLProject ui) {
        this.ui = ui;
        this.panel = ui.getGhostPanel();
        panel.setPreferredSize(panel.getSize());
        panel.setLayout(null);
        timer.start();
    }

    public void spawnGhost() {
        spawnGhost(ui.getGhostSpawnX());
    }

    public void spawnGhost(int x) {
        int slot = ghostAmount;
        if (slot >= 30) {
            return;
        }
        ghostAmount++;
        int imageIndex = random.nextInt(imagePaths.length);
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePaths[imageIndex]));
        JLabel label = new JLabel(icon);
        int y = random.nextInt(61);
        label.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
        panel.add(label);
        panel.revalidate();
        panel.repaint();
        ghosts[slot] = imageIndex + 1;
        labels[slot] = label;
    }

    public void update() {
        if (!ghostMoves()) {
            return;
        }
        for (int i = 0; i < ghostAmount; i++) {
            labels[i].setLocation(labels[i].getX() - 1, labels[i].getY());
        }
        // ghosts are kept in order, so only the front ones can have reached the castle
        while (ghostAmount > 0 && labels[0].getX() <= 0) {
            removeGhost();
            if (shields == 0) {
                ui.setHPText(1, false);
            }
        }
        panel.repaint();
    }

    public void removeGhost() {
        if (ghostAmount == 0) {
            return;
        }
        panel.remove(labels[0]);
        labels[0] = null;
        ghosts[0] = 0;
        ghostAmount--;
        System.arraycopy(labels, 1, labels, 0, ghostAmount);
        System.arraycopy(ghosts, 1, ghosts, 0, ghostAmount);
        labels[ghostAmount] = null;
        ghosts[ghostAmount] = 0;
        panel.repaint();
    }

    public boolean ghostMoves() {
        long now = System.nanoTime();
        long timeElapsed = now - lastDropTime;

        if (timeElapsed >= (ghostSpeed / speedMultiplier) * 1000000000) {
            lastDropTime = now;
            return true;
        }
        return false;
    }

    public void multiplySpeed(double factor) {
        speedMultiplier *= factor;
    }

    public void addShield() {
        shields++;
    }

    public void removeShield() {
        shields--;
    }

    public void pause() {
        lastPause = System.nanoTime();
        timer.stop();
    }

    public void play() {
        lastDropTime = lastDropTime + (System.nanoTime() - lastPause);
        timer.start();
    }

    public void setGhostSpeed(double level) {
        double targetArrivalTime = Math.max(
            30.0, 190.0 * Math.pow(0.95, level - 1)
        );
        ghostSpeed = targetArrivalTime / 190.0;
    }    

    public int getGhostAmount() {
        return ghostAmount;
    }
    
    public void reset() {
        timer.stop();

        for (int i = 0; i < ghostAmount; i++) {
            panel.remove(labels[i]);
            labels[i] = null;
            ghosts[i] = 0;
        }

        ghostAmount = 0;
        shields = 0;
        speedMultiplier = 1.0;
        ghostSpeed = 1.0;
        lastDropTime = System.nanoTime();
        panel.revalidate();
        panel.repaint();
        timer.start();
    }
}