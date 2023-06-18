package com.zwickit.smart.media.ui.view.detail;

import com.google.common.eventbus.EventBus;
import com.zwickit.smart.media.ui.base.SmartMediaConfigLoader;
import com.zwickit.smart.media.ui.base.Resizer;
import com.zwickit.smart.media.ui.domain.SmartMediaConfig;
import com.zwickit.smart.media.ui.view.ViewUtil;
import com.zwickit.smart.media.ui.view.event.NavigationTypeChangeEvent;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.Button;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

import static com.zwickit.smart.media.ui.base.Constants.*;

/**
 * Created by Thomas Philipp Zwick
 */
public abstract class AbstractSwingView extends JPanel implements IBaseView {

    /** The frame of the window */
    protected final JFrame frame;

    /** The shared event-bus instance */
    protected final EventBus eventBus;

    protected final Map<String, Object> context;

    /** The loaded smart-media-configuration */
    protected final SmartMediaConfig smartMediaConfig;

    /** The loaded resource-bundle */
    protected final ResourceBundle bundle;

    /** The resizer to help building a dynamic user-interface */
    protected final Resizer resizer = Resizer.INSTANCE;

    /**
     * Constructor
     *
     * @param frame
     * @param eventBus
     */
    public AbstractSwingView(JFrame frame, EventBus eventBus) {
        this(frame, eventBus, null);
    }

    /**
     * Constructor
     *
     * @param frame
     * @param eventBus
     */
    public AbstractSwingView(JFrame frame, EventBus eventBus, Map<String, Object> context) {
        super();

        this.frame = frame;
        this.eventBus = eventBus;
        this.context = context;

        // Load app config
        smartMediaConfig = SmartMediaConfigLoader.getSmartMediaConfig();

        // Load resources
        bundle = ResourceBundle.getBundle("message");

        createView();
    }

    @Override
    public void destroyView() {
        // empty
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            final Image backgroundImage = ImageIO.read(ViewUtil.loadGraphic(PNG_CONTENT_LOADING, smartMediaConfig.getDisplayTypeEnum()));
            g.drawImage(backgroundImage, 0, 0,  null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Button createControlButton(String id, FontAwesomeIcon fontAwesomeIcon) {
        return ViewUtil.createControlButton(id, fontAwesomeIcon, smartMediaConfig.isTablet() ? ICON_SIZE_4_2EM : ICON_SIZE_7EM);
    }

    protected void invokeNavigationTypeChange(NavigationTypeChangeEvent navigationTypeChangeEvent) {
        SwingUtilities.invokeLater(() -> eventBus.post(navigationTypeChangeEvent));
    }

}
