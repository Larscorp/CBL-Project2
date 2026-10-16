import java.awt.event.ActionEvent;
import javax.swing.*;

public class Controls {
    private final JComponent target;

    public Controls(JComponent target) {
        this.target = target;
    }

    public void bind(String key, Runnable action) {
        target.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(KeyStroke.getKeyStroke(key), key);
        target.getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }
}