package com.zwickit.smart.media.ui.view.common.keyboard;

import com.google.common.eventbus.EventBus;
import com.zwickit.smart.media.ui.base.DisplayType;
import com.zwickit.smart.media.ui.base.Resizer;

import javax.swing.*;
import java.awt.BorderLayout;

/**
 * Created by Thomas Philipp Zwick
 */
public class SmartMediaKeyboardSample {

    public static void main(String[] args) {
        final Resizer resizer = Resizer.getInstance(DisplayType.TABLET);

        final EventBus eventBus = new EventBus();

        final KeyboardFxPanel keyboardPanel = new KeyboardFxPanel(eventBus);
        keyboardPanel.showKeyboard(KeyboardType.TEXT, "http://www.orf.at");

        final JFrame frame = new JFrame();
        frame.setSize(resizer.getWindowWidth(), resizer.getWindowHeight());
        frame.setLocationRelativeTo(null);

        final JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(keyboardPanel, BorderLayout.CENTER);
        frame.add(rootPanel);

        frame.setVisible(true);
    }
}
