import java.awt.event.ActionEvent;
import javax.swing.*;

public class Controls {
    private final JComponent target;
    private boolean paused = false;
    
    public Controls(JComponent target) {
        this.target = target;
    }

    public void bind(String key, Runnable action) {
        target.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(KeyStroke.getKeyStroke(key), key);
        target.getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paused || key.equals("ESCAPE")) {
                    action.run();
                }
            }
        });
    }
    
    public void pauseControls(boolean p) {
        paused = p;
    }
}