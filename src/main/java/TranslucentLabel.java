import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;

public class TranslucentLabel extends JLabel {
    private final float alpha;

    public TranslucentLabel(float alpha) {
        this.alpha = alpha;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g2);
        g2.dispose();
    }
}