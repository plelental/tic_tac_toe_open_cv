package TicTacToe.controller;


import TicTacToe.service.video.VideoService;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Lelental on 06.04.2017.
 */
public class CameraController {

    private final int CAMERA_ID = 0;
    private final static int CAMERA_WIDTH_ID = 3;
    private static int CAMERA_HEIGHT_ID = 4;

    private GameController gameController;
    private ScheduledExecutorService timer;
    private boolean isRunning;

    CameraController() {
        this.gameController = new GameController();
        this.isRunning = true;
    }

    public void initializeCamera(ViewController viewController) {

        gameController.getVideoService().getVideoCapture().open(CAMERA_ID);
        gameController.getVideoService().getVideoCapture().set(CAMERA_WIDTH_ID, VideoService.CAMERA_WIDTH);
        gameController.getVideoService().getVideoCapture().set(CAMERA_HEIGHT_ID, VideoService.CAMERA_HEIGHT);

        if (isRunning) {

            Runnable runnable = () -> {
                Mat image = gameController.getVideoService().getFrame();
                CameraController.toFxImage(image);
                gameController.startGame();
                CameraController.onFXThread(viewController.getFrame().imageProperty(), toFxImage(image));
            };

            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(runnable, 0, 33, TimeUnit.MILLISECONDS);

        } else {
            timer.shutdown();
            gameController.getVideoService().getVideoCapture().release();
        }
    }

    private static BufferedImage matToBufferedImage(Mat original) {
        BufferedImage image;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }


    private static Image toFxImage(Mat mat) {
        return SwingFXUtils.toFXImage(matToBufferedImage(mat), null);
    }

    private static <T> void onFXThread(final ObjectProperty<T> property, final T value) {
        Platform.runLater(() -> property.set(value));
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void turnOff() {
        isRunning = false;
    }

    GameController getGameController() {
        return gameController;
    }
}