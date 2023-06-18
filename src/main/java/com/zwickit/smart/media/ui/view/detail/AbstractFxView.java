package com.zwickit.smart.media.ui.view.detail;

import com.google.common.eventbus.EventBus;
import com.zwickit.smart.media.ui.base.SmartMediaConfigLoader;
import com.zwickit.smart.media.ui.base.Resizer;
import com.zwickit.smart.media.ui.domain.SmartMediaConfig;
import com.zwickit.smart.media.ui.view.ViewUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.ResourceBundle;

import static com.zwickit.smart.media.ui.base.Constants.ICON_SIZE_4_2EM;
import static com.zwickit.smart.media.ui.base.Constants.ICON_SIZE_7EM;

/**
 * Created by Thomas Philipp Zwick
 */
public abstract class AbstractFxView extends JFXPanel implements IBaseView {

    /** The shared event-bus instance */
    protected final EventBus eventBus;

    /** The loaded smart-media-configuration */
    protected final SmartMediaConfig smartMediaConfig;

    /** The loaded resource-bundle */
    protected final ResourceBundle bundle;

    /** The resizer to help building a dynamic user-interface */
    protected final Resizer resizer = Resizer.INSTANCE;

    /** The view-scene of the panel */
    protected Scene scene;

    /**
     * Constructor
     *
     * @param eventBus
     */
    public AbstractFxView(EventBus eventBus) {
        this(eventBus, true);
    }

    /**
     * Constructor used to delay creating of the user-interface. Other stuff
     * can be done first; afterwards it must be manually started.
     *
     * @param eventBus
     * @param createView
     */
    public AbstractFxView(EventBus eventBus, boolean createView) {
        super();

        this.eventBus = eventBus;

        // Load app config
        smartMediaConfig = SmartMediaConfigLoader.getSmartMediaConfig();

        // Load resources
        bundle = ResourceBundle.getBundle("message");

        if (createView)
            createView();
    }

    protected Button createControlButton(String id, FontAwesomeIcon fontAwesomeIcon) {
        return ViewUtil.createControlButton(id, fontAwesomeIcon, smartMediaConfig.isTablet() ? ICON_SIZE_4_2EM : ICON_SIZE_7EM);
    }

    @Override
    public void destroyView() {
        scene = null;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

}
