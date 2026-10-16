import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private int spawnPointX;
    private int spawnPointY;
    public Ghosts(CBLProject ui) {
        spawnPointX = ui.getGhostSpawnX();
    }
    
    public void spawnGhost() {
        
    }
}
