import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class EffectsDisplay {
    private static final int POPUP_MS = 2000;
    private static final int IMAGE_POPUP_MS = 2000;
    private static final Color BUFF_COLOR = new Color(30, 140, 60);
    private static final Color DEBUFF_COLOR = new Color(190, 40, 40);
    private static final float TEXT_ALPHA = 0.75f; //1.0 = fully, 0.0 = invisible
    private static final float IMAGE_ALPHA = 0.5f;

    private final CBLProject ui;
    private final ActiveEffects effects;
    private final JLabel list = new JLabel();
    private final JLabel popup = new TranslucentLabel(TEXT_ALPHA);
    private final JLabel imagePopup = new TranslucentLabel(IMAGE_ALPHA);
    private final Timer hideTimer;
    private final Timer imageHideTimer;
    private String lastText = "";

    public EffectsDisplay(CBLProject ui, ActiveEffects effects) {
        this.ui = ui;
        this.effects = effects;

        list.setFont(new Font("Candara", Font.PLAIN, 12));
        ui.getLayeredPane().add(list, JLayeredPane.PALETTE_LAYER);

        popup.setForeground(Color.WHITE);
        popup.setFont(new Font("Candara", Font.BOLD, 16));
        popup.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        popup.setVisible(false);
        ui.getLayeredPane().add(popup, JLayeredPane.POPUP_LAYER);

        hideTimer = new Timer(POPUP_MS, e -> popup.setVisible(false));
        hideTimer.setRepeats(false);
        
        imagePopup.setForeground(Color.WHITE);
        imagePopup.setFont(new Font("Candara", Font.BOLD, 16));
        imagePopup.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        imagePopup.setHorizontalAlignment(SwingConstants.CENTER);
        imagePopup.setHorizontalTextPosition(SwingConstants.CENTER);
        imagePopup.setVerticalTextPosition(SwingConstants.BOTTOM);
        imagePopup.setVisible(false);
        ui.getLayeredPane().add(imagePopup, JLayeredPane.POPUP_LAYER);

        imageHideTimer = new Timer(IMAGE_POPUP_MS, e -> imagePopup.setVisible(false));
        imageHideTimer.setRepeats(false);

        refreshList();
    }

    public void announce(Effect effect) {
        if (effect.getImagePath() != null && showImage(effect)) {
            return;
        }

        String kind = effect.isBuff() ? "BUFF" : "DEBUFF";
        String detail = effect.isInstant()
                ? effect.getName()
                : effect.getName() + " (" + effect.getSeconds() + "s)";

        popup.setText("<html><center><b>" + kind + "</b><br>" + detail + "</center></html>");
        popup.setBackground(effect.isBuff() ? BUFF_COLOR : DEBUFF_COLOR);
        popup.setSize(popup.getPreferredSize());

        Rectangle board = ui.getBoardBounds();
        popup.setLocation(board.x + (board.width - popup.getWidth()) / 2,
                          board.y + board.height / 4);
        popup.setVisible(true);
        hideTimer.restart();
    }

    private boolean showImage(Effect effect) {
        URL url = getClass().getResource(effect.getImagePath());
        if (url == null) {
            System.out.println("Popup image not found: " + effect.getImagePath());
            return false;
        }

        Rectangle board = ui.getBoardBounds();
        ImageIcon original = new ImageIcon(url);
        int w = (int) (board.width * 0.9);                     
        int h = w * original.getIconHeight() / original.getIconWidth();
        imagePopup.setIcon(new ImageIcon(
                original.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));

        imagePopup.setText((effect.isBuff() ? "BUFF: " : "DEBUFF: ") + effect.getName());
        imagePopup.setBackground(effect.isBuff() ? BUFF_COLOR : DEBUFF_COLOR);
        imagePopup.setSize(imagePopup.getPreferredSize());
        imagePopup.setLocation(board.x + (board.width - imagePopup.getWidth()) / 2,
                               board.y + (board.height - imagePopup.getHeight()) / 2);
        imagePopup.setVisible(true);
        imageHideTimer.restart();
        return true;
    }

    public void update() {
        refreshList();
    }

    private void refreshList() {
        Rectangle score = ui.getScorePanelBounds();
        Rectangle board = ui.getBoardBounds();
        int width = Math.max(60, board.x - score.x - 6);   //space between the left edge and the board

        String lines = effects.snapshot().stream()
                .map(i -> "<font color='" + (i.buff() ? "#2e8b57" : "#c0392b") + "'>"
                        + i.name() + " " + i.secondsLeft() + "s</font>")
                .collect(Collectors.joining("<br>"));
        if (lines.isEmpty()) {
            lines = "<i>none</i>";
        }

        String text = "<html><body style='width:" + width + "px'><b>Effects:</b><br>"
                + lines + "</body></html>";
        if (text.equals(lastText)) {
            return;
        }
        lastText = text;

        list.setText(text);
        list.setSize(list.getPreferredSize());
        list.setLocation(score.x, score.y + score.height + 10);   // under Score/Level
    }
}