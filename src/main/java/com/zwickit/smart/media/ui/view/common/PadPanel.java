package com.zwickit.smart.media.ui.view.common;

import com.google.common.eventbus.EventBus;
import com.zwickit.smart.media.ui.domain.SmartMediaConfig;
import com.zwickit.smart.media.ui.view.event.PadChangeEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import static com.zwickit.smart.media.ui.base.Constants.*;
import static com.zwickit.smart.media.ui.base.Constants.DIR_TABLET;

/**
 * Created by Thomas Philipp Zwick
 */
public class PadPanel {

    private final String BUTTON_HASH = "hash";
    private final String BUTTON_STAR = "star";

    private EventBus eventBus;

    private SmartMediaConfig smartMediaConfig;

    /**
     * Constructor
     */
    public PadPanel(EventBus eventBus, SmartMediaConfig smartMediaConfig) {
        this.eventBus = eventBus;
        this.smartMediaConfig = smartMediaConfig;
    }

    public Node createPanel() {
        final GridPane panel = new GridPane();
        panel.getStyleClass().add(STYLE_DIAL_PAD_PANEL);
        panel.setAlignment(Pos.CENTER);

        panel.add(createNumericButton("1"), 0, 0);
        panel.add(createNumericButton("2"), 1, 0);
        panel.add(createNumericButton("3"), 2, 0);

        panel.add(createNumericButton("4"), 0, 1);
        panel.add(createNumericButton("5"), 1, 1);
        panel.add(createNumericButton("6"), 2, 1);

        panel.add(createNumericButton("7"), 0, 2);
        panel.add(createNumericButton("8"), 1, 2);
        panel.add(createNumericButton("9"), 2, 2);

        panel.add(createButton(BUTTON_STAR), 0, 3);
        panel.add(createNumericButton("0"), 1, 3);
        panel.add(createButton(BUTTON_HASH), 2, 3);

        return panel;
    }

    private Button createButton(String imageName) {
        final Button button = new Button();
        button.getStyleClass().add(STYLE_PHONE_BUTTON);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setGraphic(new ImageView(toDialNoUri(imageName)));
        button.setOnAction(event -> onPadButtonClicked(BUTTON_HASH.equals(imageName) ? "#" : "*"));
        return button;
    }

    private Button createNumericButton(String number) {
        final Button button = new Button();
        button.getStyleClass().add(STYLE_PHONE_BUTTON);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setGraphic(new ImageView(toDialNoUri(number)));
        button.setOnAction(event -> onPadButtonClicked(number));
        return button;
    }

    private String toDialNoUri(String value) {
        if (smartMediaConfig.is1080p())
            return DIR_GRAPHICS + DIR_1080P + "dial-no-" + value + ".png";
        else
            return DIR_GRAPHICS + DIR_TABLET + "dial-no-" + value + ".png";
    }

    // ------------------------------------------------------------------------
    //
    // Event Handlers
    //
    // ------------------------------------------------------------------------

    private void onPadButtonClicked(String value) {
        eventBus.post(new PadChangeEvent(value));
    }

}
