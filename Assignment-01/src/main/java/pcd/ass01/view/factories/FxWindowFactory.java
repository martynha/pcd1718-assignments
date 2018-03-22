package pcd.ass01.view.factories;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pcd.ass01.domain.Board;
import pcd.ass01.domain.Boards;
import pcd.ass01.domain.Cell;

import static pcd.ass01.util.Preconditions.checkState;

/**
 * Utility class to create JavaFx windows using pattern Static Factory.
 */
public final class FxWindowFactory implements WindowFactory{

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String APP_TITLE = "Game Of Life";
    public static final String APP_ICON_PATH = "/game_of_life_icon.png";
    private static final String GAME_FXML_PATH = "/game_of_life.fxml";
    private static final String SETTINGS_FXML_PATH = "/initial_settings.fxml";
    private static final String GAME_CSS_PATH = "/game_of_life_style.css";
    private static final String SETTINGS_CSS_PATH = "/initial_settings_style.css";

    private static FXMLLoader loader;

    public static FxWindowFactory defaultInstance() {
        return Holder.INSTANCE;
    }

    /**
     *
     * @return reference to view handler.
     * @param <T>
     *            type of the handler
     */
    public static <T> T getHandler() {
        return loader == null ? null : loader.getController();
    }

    /**
     * Load a new window. If it is contained in a menu, the method return the
     * root of the new scene.
     *
     * @param fxmlPath
     *            path of the GUI structure file FXML.
     *
     *
     * @return root.
     */
    public static BorderPane openWindow(final String fxmlPath, final String cssPath, final boolean resizable) throws IOException {
            loader = new FXMLLoader(
                    FxWindowFactory.class.getResource(fxmlPath));
            final BorderPane root = loader.load();
            final Stage stage = new Stage();
            stage.setResizable(resizable);
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(FxWindowFactory.class
                    .getResource(cssPath).toExternalForm());
            stage.setTitle(APP_TITLE);
            stage.getIcons().add(new Image(APP_ICON_PATH));
            stage.setScene(scene);
            stage.show();
        return root;
    }

    /**
     * Close a JavaFx window.
     *
     * @param sceneToClose
     *            link to the window to close.
     */
    public static void closeWindow(final Scene sceneToClose) {
        final Stage sceneStage = (Stage) sceneToClose.getWindow();
        sceneStage.close();
    }

    /**
     * Replace a old window with a new one.
     *
     * @param fxmlPath
     *            path of the GUI structure file FXML to open.
     *
     * @param sceneToClose
     *            link to the window to close.
     */
    public static void replaceWindow(final String fxmlPath, final Scene sceneToClose) throws IOException {
        FxWindowFactory.openWindow(fxmlPath, GAME_CSS_PATH, true);
        FxWindowFactory.closeWindow(sceneToClose);
    }

    /**
     * Show a simple info dialog with a optional image.
     *
     * @param title
     *            header of the show dialog.
     * @param message
     *            content of the dialog.
     * @param alertType
     *            to select the type of dialog.
     */
    public static void showDialog(final String title, final String message,
                                  final AlertType alertType) {
        final Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(APP_ICON_PATH));
        alert.showAndWait();
    }


    /**
     *
     * @param title
     *            of dialog window.
     * @param message
     *            to user.
     * @param inputText
     *            to show in input text field.
     * @return input string written by user.
     */
    public static String createInputDialog(final String title, final String message, final String inputText) {
        final TextInputDialog dialog = new TextInputDialog(inputText);
        dialog.setTitle(title);
        dialog.setHeaderText("You have to input the requested data!");
        dialog.setContentText(message);
        final Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    @Override
    public void openStartWindow() throws IOException {
        openWindow(SETTINGS_FXML_PATH, SETTINGS_CSS_PATH, false);
    }

    @Override
    public void openGameWindow(int width, int height) throws IOException {
        BorderPane gamePane = openWindow(GAME_FXML_PATH, GAME_CSS_PATH, true);
        ScrollPane scrollPane = new ScrollPane();
        Canvas gameBoard = createCanvas(width, height);
        drawBoard(gameBoard, Boards.randomBoard(height, width));
        scrollPane.setContent(gameBoard);
        gamePane.setCenter(scrollPane);
    }


    private Canvas createCanvas(int width, int height) {
       return new Canvas(width, height);
    }

    private static final class Holder {
        static final FxWindowFactory INSTANCE = new FxWindowFactory();
    }

    public static Stage getStage(Node node){
        final Window window = node.getScene().getWindow();
        checkState(window instanceof Stage, "window (%s) is not an instance of %s", window.getClass().getName(), Stage.class.getName());
        return (Stage) window;
    }

    public static Stage getStage(ActionEvent event) {
        final Object source = event.getSource();
        checkState(source instanceof Node, "source (%s) is not an instance of %s", source.getClass().getName(), Node.class.getName());
        return getStage((Node) source);
    }

    private static void drawBoard(final Canvas canvas, final Board board) {
        final PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();
        for (int y = 0; y < board.getWidth(); y++)
            for (int x = 0; x < board.getHeight(); x++)
                pw.setColor(y, x, getColor(board.getCell(x, y)));
    }

    private static Color getColor(final Cell cell) {
        switch (cell) {
            case DEAD:
                return Color.WHITE;
            case ALIVE:
                return Color.BLACK;
            default:
                throw new IllegalStateException("Unknown cell state: " + cell);
        }
    }

}
