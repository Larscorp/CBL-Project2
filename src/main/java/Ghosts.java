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
    private Timer timer = new Timer(50, e -> update());
    private double ghostSpeed;
    private long lastDropTime = System.nanoTime();
    
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
        ghostAmount++;
        if (slot >= 30) {
            return;
        }
        int imageIndex = random.nextInt(imagePaths.length);
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePaths[imageIndex]));
        JLabel label = new JLabel(icon);
        int y = random.nextInt(61);
        label.setBounds(x, random.nextInt(61), icon.getIconWidth(), icon.getIconHeight());
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
        for (int i = 0; i < labels.length; i++) {
            
            if (labels[i] != null) {
                labels[i].setLocation(labels[i].getX() - 1, labels[i].getY());
            
                if (labels[i].getX() <= 0) {
                    removeGhost();
                    ui.setHPText(1, false);
                }
            }
        }
        panel.repaint();
    }
    
    public void removeGhost() {
        panel.remove(labels[0]);
        labels[0] = null;
        ghosts[0] = 0;
        ghostAmount--;
        panel.repaint();
        System.arraycopy(labels, 1, labels, 0, ghostAmount);
        System.arraycopy(ghosts, 1, ghosts, 0, ghostAmount);
    }
    
    public boolean ghostMoves() {
        long now = System.nanoTime();
        long timeElapsed = now - lastDropTime;
        
        if (timeElapsed >= ghostSpeed * 1000000000) {
            lastDropTime = now;
            return true;
        }
        return false;
    }
    
    public void pause() {
        timer.stop();
    }
    
    public void play() {
        timer.start();
    }
    
    public void setGhostSpeed(double speed) {
        ghostSpeed = speed;
    }
    
    public int getGhostAmount() {
        return ghostAmount;
    }
}
