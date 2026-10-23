import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class EffectsDisplay {

    private final CBLProject ui;
    private final ActiveEffects effects;
    private final JLabel list;
    private String lastText = "";

    public EffectsDisplay(CBLProject ui, ActiveEffects effects) {
        this.ui = ui;
        this.effects = effects;

        list = ui.getEffectsList();
        list.setFont(new Font("Candara", Font.BOLD, 18));
        list.setVerticalAlignment(SwingConstants.TOP);

        refreshList();
    }

    public void announce(Effect effect) {
        ui.showEffectPopup(effect);
    }

    public void update() {
        refreshList();
    }

    private void refreshList() {
        String lines = effects.snapshot().stream()
                .map(i -> "<font color='"
                        + getEffectColor(i.buff())
                        + "'>" + i.name() + " "
                        + i.secondsLeft() + "s</font>")
                .collect(java.util.stream.Collectors.joining("<br>"));

        if (lines.isEmpty()) {
            lines = "<i>none</i>";
        }

        String text = "<html><font face='Candara' size='5'><b>Active effects:</b></font><br>"
            + "<font face='Candara' size='3'>" + lines + "</font></html>";

        if (text.equals(lastText)) {
            return;
        }

        lastText = text;
        list.setText(text);
    }
    
    private String getEffectColor(boolean buff) {
        if (buff) {
            return "#2e8b57";
        } else {
            return "#c0392b";
        }
    }
}