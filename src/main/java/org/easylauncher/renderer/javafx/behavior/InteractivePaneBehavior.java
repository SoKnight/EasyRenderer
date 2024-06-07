package org.easylauncher.renderer.javafx.behavior;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.easylauncher.renderer.context.RendererContext;
import org.easylauncher.renderer.javafx.RendererPane;

public final class InteractivePaneBehavior implements RendererPaneBehavior {

    private final RendererPane rendererPane;
    private final RendererContext rendererContext;
    private final float mouseSensitivity;
    private double previousX, previousY;

    private EventHandler<MouseEvent> mouseMovedHandler;
    private EventHandler<MouseEvent> mouseDraggedHandler;

    public InteractivePaneBehavior(RendererPane rendererPane, float mouseSensitivity) {
        this.rendererPane = rendererPane;
        this.rendererContext = rendererPane.getRenderContext();
        this.mouseSensitivity = mouseSensitivity;
    }

    @Override
    public void initialize() throws Exception {
        this.mouseMovedHandler = event -> {
            this.previousX = event.getX();
            this.previousY = event.getY();
        };

        this.mouseDraggedHandler = event -> {
            if (previousX != 0D || previousY != 0D) {
                double dx = event.getX() - previousX;
                double dy = event.getY() - previousY;

                if (dx != 0D || dy != 0D) {
                    rendererContext.rotateSceneBy((float) dy * mouseSensitivity, (float) dx * mouseSensitivity);
                    rendererPane.requestRender();
                }
            }

            this.previousX = event.getX();
            this.previousY = event.getY();
        };
    }

    @Override
    public void bind() {
        rendererPane.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
        rendererPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
    }

    @Override
    public void unbind() {
        rendererPane.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
        rendererPane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
    }

}
