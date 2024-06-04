package org.easylauncher.framework.gamerender.engine;

import lombok.Getter;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

@Getter
public final class MouseInput {

    private final Vector2f currentPos;
    private final Vector2f previousPos;
    private final Vector2f displVec;

    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    public MouseInput(long windowHandle) {
        this.currentPos = new Vector2f();
        this.previousPos = new Vector2f(-1, -1);
        this.displVec = new Vector2f();

        this.leftButtonPressed = false;
        this.rightButtonPressed = false;
        this.inWindow = false;

        glfwSetCursorEnterCallback(windowHandle, (handle, entered) -> inWindow = entered);

        glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            currentPos.x = (float) xpos;
            currentPos.y = (float) ypos;
        });

        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;

        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;

            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;

            if (rotateX) {
                displVec.y = (float) deltax;
            }

            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }

        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

}
