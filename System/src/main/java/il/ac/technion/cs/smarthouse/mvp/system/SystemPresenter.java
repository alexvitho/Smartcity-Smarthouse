package il.ac.technion.cs.smarthouse.mvp.system;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemFailureDetector;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGuiController;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A presenter class for the whole system. The model and gui should be started
 * from here.
 * 
 * @author RON
 * @author Yarden
 * @since 07-06-2017
 */
public class SystemPresenter {
    static final Logger log = LoggerFactory.getLogger(SystemPresenter.class);

    static final String APP_ROOT_FXML = "main_system_ui.fxml";

    static final String APP_NAME = "Smarthouse";
    static final String APP_LOGO = "/icons/smarthouse-icon.png";
    static final double APP_WIDTH = 1000;
    static final double APP_HEIGHT = 800;

    final SystemCore model;
    SystemMode systemMode;
    MainSystemGuiController viewController;
    Stage viewPrimaryStage;
    final List<Runnable> viewOnCloseListeners = new ArrayList<>();

    SystemPresenter(final boolean createGui, final boolean createPrimaryStage, final boolean showModePopup,
                    final SystemMode defaultMode, final boolean enableFailureDetector) {
        model = new SystemCore();
        systemMode = defaultMode;

        if (enableFailureDetector)
            SystemFailureDetector.enable();

        if (!createGui)
            return;
        
        viewOnCloseListeners.add(() -> model.shutdown());
        viewOnCloseListeners.add(() -> System.exit(0));
        if (createPrimaryStage)
            JavaFxHelper.startGui(new MainSystemGui(showModePopup));
        else
            viewController = SystemGuiController.createRootController(APP_ROOT_FXML, model, systemMode);

    }

    public SystemCore getSystemModel() {
        return model;
    }

    public MainSystemGuiController getSystemView() {
        return viewController;
    }

    public MainSystemGuiController getViewController() {
        return viewController;
    }

    public void addOnCloseListener(final Runnable onCloseListener) {
        viewOnCloseListeners.add(onCloseListener);
    }

    public void close() {
        if (viewPrimaryStage != null)
            viewPrimaryStage.close();
    }

    class MainSystemGui extends Application {
        final boolean showModePopup;

        public MainSystemGui(final boolean showModePopup) {
            this.showModePopup = showModePopup;
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            getMode();

            log.info("Initializing system ui in " + systemMode + "...");

            viewController = SystemGuiController.createRootController(APP_ROOT_FXML, model, systemMode);
            viewPrimaryStage = primaryStage;

            final Scene scene = new Scene(viewController.getRootViewNode(), APP_WIDTH, APP_HEIGHT);
            primaryStage.setTitle(APP_NAME);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(APP_LOGO)));
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(e -> {
                log.info("System ui closing...");
                viewOnCloseListeners.forEach(a -> a.run());
            });

            primaryStage.show();
        }

        private void getMode() {
            if (!showModePopup)
                return;

            log.info("Asking for mode...");

            ButtonType userType = new ButtonType("User Mode");
            ButtonType devType = new ButtonType("Developer Mode");
            Alert alert = new Alert(AlertType.INFORMATION, "Please select a mode:", userType, devType);
            alert.setTitle("Smarthouse Mode Selection");
            alert.setHeaderText("The Smarthouse has two operation modes.");

            ButtonType response = alert.showAndWait().get();
            if (response != null)
                systemMode = response == devType ? SystemMode.DEVELOPER_MODE : SystemMode.USER_MODE;
        }
    }
}
