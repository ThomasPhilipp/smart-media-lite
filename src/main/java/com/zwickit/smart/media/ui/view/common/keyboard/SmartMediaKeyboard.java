package com.zwickit.smart.media.ui.view.common.keyboard;

import com.google.common.eventbus.EventBus;
import com.zwickit.smart.media.ui.base.DisplayType;
import com.zwickit.smart.media.ui.base.FontFx;
import com.zwickit.smart.media.ui.base.Constants;
import com.zwickit.smart.media.ui.base.Resizer;
import com.zwickit.smart.media.ui.view.event.KeyboardButtonActionEvent;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableStringValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * The smart-media keyboard.
 *
 * Created by Thomas Philipp Zwick
 */
public class SmartMediaKeyboard {

    /** The logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartMediaKeyboard.class);

    private final String RES_KEYBOARD_TYPE_PREFIX = "keyboard.type.";

    private final int SIZE_GAP = 5;
    private final int SIZE_BUTTON = 85;
    private final int SIZE_BUTTON_HD = 125;

    private final int buttonSize;

    /**
     * The event-bus instance.
     */
    private final EventBus eventBus;

    /**
     * The specific resource-bundle.
     */
    private final ResourceBundle resourceBundle;

    /**
     * The label of the header which depends on the specified keyboard-type.
     */
    private Label headerLabel;

    /**
     * The text which was entered.
     */
    private TextField inputField;

    /**
     * The display keyboard.
     */
    private GridPane keyboard;

    /**
     * The main panel of the smart-media-keyboard which consists of the header,
     * text-field and keyboard itself.
     */
    private VBox panel;

    /**
     * Track shift mode.
     */
    private ObservableBooleanValue shiftMode;

    /**
     * Parameter used on method-scope
     */
    private KeyboardType keyboardType;

    /**
     * Constructor
     */
    public SmartMediaKeyboard(EventBus eventBus, ResourceBundle resourceBundle, DisplayType displayType) {
        this.eventBus = eventBus;
        this.resourceBundle = resourceBundle;

        this.buttonSize = (displayType == DisplayType.TABLET) ? SIZE_BUTTON : SIZE_BUTTON_HD;
    }

    /**
     * Show keyboard and initialize it with given parameters
     *
     * @param keyboardType
     * @param predefinedText
     */
    public void initialize(KeyboardType keyboardType, String predefinedText) {
        LOGGER.debug("Show keyboard with type: {}", keyboardType);

        this.keyboardType = keyboardType;

        // Initialize view
        headerLabel = new Label();
        headerLabel.getStyleClass().add(STYLE_KEYBOARD_HEADER);
        headerLabel.setText(getHeaderText(keyboardType));
        headerLabel.setFont(FontFx.ROBOTO_REGULAR_70);

        inputField = new TextField();
        inputField.getStyleClass().add(STYLE_KEYBOARD_TEXT);
        inputField.setText(predefinedText);
        inputField.setFont(FontFx.ROBOTO_REGULAR_50);

        switch (keyboardType) {
            case EMAIL: inputField.setPromptText("name@domain.at"); break;
            case URL: inputField.setPromptText("http://www.domain-name.at"); break;
        }

        keyboard = new GridPane();
        keyboard.setGridLinesVisible(true);
        keyboard.setHgap(SIZE_GAP);
        keyboard.setVgap(SIZE_GAP);

        // Initialize default layout of keyboard
        createCharacterLayout();

        panel = new VBox();
        panel.getStyleClass().add(STYLE_KEYBOARD_PANEL);
        panel.setAlignment(Pos.CENTER);
        panel.setMaxWidth(Resizer.INSTANCE.get14ColumnWidth());
        panel.getChildren().addAll(headerLabel, inputField, keyboard);
    }


    public void destroy() {
        LOGGER.debug("Hide keyboard");

        // Unset parameter
        this.keyboardType = null;

        // Need to be unset manually, otherwise previous text remain
        this.headerLabel.setText(null);
        this.headerLabel = null;

        this.inputField.setText(null);
        this.inputField = null;

        this.shiftMode = null;

        this.removeNodes(keyboard);
        this.keyboard = null;

        this.removeNodes(panel);
        this.panel = null;
    }

    /**
     * Remove displayed children of given panel.
     */
    private void removeNodes(Pane panel) {
        for (int idx=panel.getChildren().size()-1; idx>=0; idx--) {
            panel.getChildren().remove(idx);
        }
    }

    private void createCharacterLayout() {
        // Remove existing layout
        for (int idx=keyboard.getChildren().size()-1; idx>=0; idx--) {
            keyboard.getChildren().remove(idx);
        }

        // Create new layout

        // Through its binding it must be implemented at the beginning
        final ToggleButton shiftBtn = createToggleButton(FontAwesomeIcon.ARROW_UP);
        shiftBtn.setPrefWidth(buttonSize * 2 + SIZE_GAP);
        shiftMode = shiftBtn.selectedProperty();

        final Button specialCharBtn = createOptionButton("&123");
        specialCharBtn.setPrefWidth(buttonSize * 2 + SIZE_GAP);
        specialCharBtn.setOnAction(event -> createSpecialCharacterLayout());

        keyboard.add(createShiftableButton("q", "Q"), 1, 1);
        keyboard.add(createShiftableButton("w", "W"), 2, 1);
        keyboard.add(createShiftableButton("e", "E"), 3, 1);
        keyboard.add(createShiftableButton("r", "R"), 4, 1);
        keyboard.add(createShiftableButton("t", "T"), 5, 1);
        keyboard.add(createShiftableButton("z", "Z"), 6, 1);
        keyboard.add(createShiftableButton("u", "U"), 7, 1);
        keyboard.add(createShiftableButton("i", "i"), 8, 1);
        keyboard.add(createShiftableButton("o", "O"), 9, 1);
        keyboard.add(createShiftableButton("p", "P"), 10, 1);
        keyboard.add(createShiftableButton("ü", "Ü"), 11, 1);
        keyboard.add(addBackspaceButton(), 12, 1, 2, 1);

        keyboard.add(createShiftableButton("a", "A"), 1, 2);
        keyboard.add(createShiftableButton("s", "S"), 2, 2);
        keyboard.add(createShiftableButton("d", "D"), 3, 2);
        keyboard.add(createShiftableButton("f", "F"), 4, 2);
        keyboard.add(createShiftableButton("g", "G"), 5, 2);
        keyboard.add(createShiftableButton("h", "H"), 6, 2);
        keyboard.add(createShiftableButton("j", "J"), 7, 2);
        keyboard.add(createShiftableButton("k", "K"), 8, 2);
        keyboard.add(createShiftableButton("l", "L"), 9, 2);
        keyboard.add(createShiftableButton("ö", "Ö"), 10, 2);
        keyboard.add(createShiftableButton("ä", "Ä"), 11, 2);
        keyboard.add(addEnterButton(), 12, 2, 2, 2);

        final Button dblPointBtn = createButton(":");
        dblPointBtn.setPrefWidth(buttonSize);
        keyboard.add(dblPointBtn, 1, 3);
        final Button slashBtn = createButton("/");
        slashBtn.setPrefWidth(buttonSize);
        keyboard.add(slashBtn, 2, 3);

        keyboard.add(createShiftableButton("y", "Y"), 3, 3);
        keyboard.add(createShiftableButton("x", "X"), 4, 3);
        keyboard.add(createShiftableButton("c", "C"), 5, 3);
        keyboard.add(createShiftableButton("v", "V"), 6, 3);
        keyboard.add(createShiftableButton("b", "B"), 7, 3);
        keyboard.add(createShiftableButton("n", "N"), 8, 3);
        keyboard.add(createShiftableButton("m", "M"), 9, 3);

        final Button pointBtn = createButton(".");
        pointBtn.setPrefWidth(buttonSize);
        keyboard.add(pointBtn, 10, 3);
        final Button hyphenBtn = createButton("-");
        hyphenBtn.setPrefWidth(buttonSize);
        keyboard.add(hyphenBtn, 11, 3);

        keyboard.add(shiftBtn, 10, 4, 2, 1);
        keyboard.add(specialCharBtn, 1, 4, 2, 1);
        final Button spaceBarBtn = addSpaceBarButton();
        spaceBarBtn.setPrefWidth(buttonSize * 7 + 30);
        keyboard.add(spaceBarBtn, 3, 4, 7, 1);
        keyboard.add(addExitButton(), 12, 4, 2, 1);
    }

    private void createSpecialCharacterLayout() {
        removeNodes(keyboard);

        final Button placeholderBtn = createButton("");
        placeholderBtn.setVisible(false);

        // Create new layout
        keyboard.add(createButton("!"), 1,1);
        keyboard.add(createButton("@"), 2,1);
        keyboard.add(createButton("#"), 3,1);
        keyboard.add(createButton("$"), 4,1);
        keyboard.add(createButton("€"), 5,1);
        keyboard.add(createButton("%"), 6,1);
        keyboard.add(createButton("&"), 7,1);
        keyboard.add(placeholderBtn, 8,1);
        keyboard.add(createButton("1"), 9,1);
        keyboard.add(createButton("2"), 10,1);
        keyboard.add(createButton("3"), 11,1);
        keyboard.add(addBackspaceButton(), 12, 1, 2, 1);

        keyboard.add(createButton("("), 1,2);
        keyboard.add(createButton(")"), 2,2);
        keyboard.add(createButton("<"), 3,2);
        keyboard.add(createButton(">"), 4,2);
        keyboard.add(createButton("="), 5,2);
        keyboard.add(createButton("*"), 6,2);
        keyboard.add(createButton("+"), 7,2);
        // placeholderBtn
        keyboard.add(createButton("4"), 9,2);
        keyboard.add(createButton("5"), 10,2);
        keyboard.add(createButton("6"), 11,2);
        keyboard.add(addEnterButton(), 12, 2, 2, 2);

        keyboard.add(createButton("\\"), 1,3);
        keyboard.add(createButton(";"), 2,3);
        keyboard.add(createButton(":"), 3,3);
        keyboard.add(createButton("\""), 4,3);
        keyboard.add(createButton("_"), 5,3);
        keyboard.add(createButton("-"), 6,3);
        keyboard.add(createButton("/"), 7,3);
        // placeholderBtn
        keyboard.add(createButton("7"), 9,3);
        keyboard.add(createButton("8"), 10,3);
        keyboard.add(createButton("9"), 11,3);

        final Button abcBtn = createOptionButton("abc");
        abcBtn.setPrefWidth(buttonSize * 2 + SIZE_GAP);
        abcBtn.setOnAction(event -> {
            createCharacterLayout();
        });
        keyboard.add(abcBtn, 1,4, 2, 1);

        final Button spaceBarBtn = addSpaceBarButton();
        spaceBarBtn.setPrefWidth(buttonSize * 5 + 20);
        keyboard.add(spaceBarBtn, 3, 4, 5, 1);

        final Button zeroBtn = createButton("0");
        zeroBtn.setPrefWidth(buttonSize * 2 + SIZE_GAP);
        keyboard.add(zeroBtn, 9,4, 2, 1);

        keyboard.add(createButton(","), 11,4);
        keyboard.add(addExitButton(), 12, 4, 2, 1);
    }

    /**
     * Returns the root-node.
     *
     * @return
     */
    public VBox view() {
        return panel;
    }

    /**
     * Determines the displayed text which depends on the given keyboard type.
     *
     * @param keyboardType
     * @return
     */
    private String getHeaderText(KeyboardType keyboardType) {
        final String key = RES_KEYBOARD_TYPE_PREFIX + keyboardType.name().toLowerCase();
        return resourceBundle.getString(key);
    }

    /**
     * Creates a keyboard-button.
     *
     * @param unshifted
     * @param shifted
     * @return
     */
    private Button createShiftableButton(String unshifted, String shifted) {
        final StringBinding displayText = Bindings.when(shiftMode).then(shifted).otherwise(unshifted);
        return createShiftableButton(displayText);
    }

    private Button createShiftableButton(ObservableStringValue text) {
        final Button button = new Button();
        button.getStyleClass().add(STYLE_KEYBOARD_BUTTON);
        button.textProperty().bind(text);
        button.setFont(FontFx.ROBOTO_REGULAR_30);
        button.setPrefSize(buttonSize, buttonSize);
        button.setOnAction(event -> {
            final Button btn = (Button) event.getSource();
            inputField.setText(inputField.getText().concat(((Button) event.getSource()).getText()));
        });
        return button;
    }

    private Button createIconButton(FontAwesomeIcon icon) {
        return createButton(null, icon, STYLE_KEYBOARD_OPTION_BUTTON);
    }

    private Button createIconButton(FontAwesomeIcon icon, String style) {
        return createButton(null, icon, style);
    }

    private Button createOptionButton(String text) {
        return createButton(text, null, STYLE_KEYBOARD_OPTION_BUTTON);
    }

    private Button createButton(String text) {
        return createButton(text, null, STYLE_KEYBOARD_BUTTON);
    }

    private Button createButton(String text, FontAwesomeIcon icon, String styleClass) {
        final Button button = new Button();
        button.getStyleClass().add(styleClass);
        button.setText(text);
        button.setFont(FontFx.ROBOTO_REGULAR_30);
        button.setPrefSize(buttonSize, buttonSize);
        button.setOnAction(event -> {
            final Button btn = (Button) event.getSource();
            inputField.setText(inputField.getText().concat(((Button) event.getSource()).getText()));
        });

        if (icon != null) {
            FontAwesomeIconFactory.get().setIcon(button, icon, "1em", ContentDisplay.GRAPHIC_ONLY);
        }

        return button;
    }

    private ToggleButton createToggleButton(String text) {
        return createToggleButton(text, null);
    }

    private ToggleButton createToggleButton(FontAwesomeIcon icon) {
        return createToggleButton(null, icon);
    }

    private ToggleButton createToggleButton(String text, FontAwesomeIcon icon) {
        final ToggleButton button = new ToggleButton();
        button.getStyleClass().add(STYLE_KEYBOARD_OPTION_BUTTON);
        button.setText(text);
        button.setFont(FontFx.ROBOTO_REGULAR_30);
        button.setPrefWidth(buttonSize);
        button.setPrefHeight(buttonSize);
//        button.setOnAction(event -> {
//            final ToggleButton btn = (ToggleButton) event.getSource();
//            System.out.println("Toggled on: " + btn.getText());
//        });

        if (icon != null) {
            FontAwesomeIconFactory.get().setIcon(button, icon, "1em", ContentDisplay.GRAPHIC_ONLY);
        }

        return button;
    }

    /**
     * A backspace button implementation.
     *
     * @return
     */
    private Node addBackspaceButton() {
        final Button button = createIconButton(FontAwesomeIcon.ARROW_LEFT);
        button.setPrefWidth(buttonSize * 2 + SIZE_GAP);
        button.setOnAction(event -> {
            if (inputField.getText().length() > 0)
                inputField.setText(inputField.getText().substring(0, inputField.getLength()-1));
        });
        return button;
    }

    /**
     * An enter button implementation.
     *
     * @return
     */
    private Button addEnterButton() {
        final Button button;

        if (keyboardType == KeyboardType.URL)
            button = createOptionButton("Enter");
        else
            button = createOptionButton("Weiter");

        button.setPrefHeight(buttonSize * 2 + SIZE_GAP);
        button.setPrefWidth(buttonSize * 2 + SIZE_GAP);
        button.setOnAction(event -> {
            final Map<String, String> parameters = new HashMap<>();
            parameters.put(Constants.PARAM_URL, inputField.getText());

            eventBus.post(new KeyboardButtonActionEvent(keyboardType, KeyboardButtonType.ENTER, parameters));
        });
        return button;
    }

    /**
     * A close button implementation.
     *
     * @return
     */
    private Button addExitButton() {
        final Button button = createIconButton(FontAwesomeIcon.TIMES, STYLE_KEYBOARD_EXIT_BUTTON);
        button.setPrefWidth(buttonSize * 2 + SIZE_GAP);
        button.setOnAction(event -> eventBus.post(new KeyboardButtonActionEvent(keyboardType, KeyboardButtonType.EXIT)));
        return button;
    }

    /**
     * A space bar button implementation.
     *
     * @return
     */
    private Button addSpaceBarButton() {
        final Button button = createButton(" ");
        return button;
    }

}
