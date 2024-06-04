package org.easylauncher.framework.gamerender.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memUTF8;

@Getter
public final class Window {

    private final Runnable resizeFunction;
    private final MouseInput mouseInput;

    private long windowHandle;
    private int width, height;

    public Window(String title, Options options, Runnable resizeFunction) {
        this.resizeFunction = resizeFunction;
        initialize(title, options.width, options.height, options.compatibleProfile);
        this.mouseInput = new MouseInput(windowHandle);
    }

    private void initialize(String title, int width, int height, boolean compatibleProfile) {
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        glfwWindowHint(GLFW_SAMPLES, 16);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        if (compatibleProfile) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int posX = -1, posY = -1;

        if (width > 0 && height > 0) {
            this.width = width;
            this.height = height;

            posX = (vidMode.width() - width) / 2;
            posY = (vidMode.height() - height) / 2;
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            width = vidMode.width();
            height = vidMode.height();
        }

        this.windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        if (posX > 0 || posY > 0)
            glfwSetWindowPos(windowHandle, posX, posY);

        glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> onResize(w, h));
        glfwSetErrorCallback((int code, long msgPtr) -> System.err.printf("[GLFW %d]: %s%n", code, memUTF8(msgPtr)));
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> onKeyAction(key, action));

        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);

        glfwShowWindow(windowHandle);

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];

        glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight);

        this.width = arrWidth[0];
        this.height = arrHeight[0];
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
    }

    public void cleanup() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();

        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    private void onResize(int width, int height) {
        this.width = width;
        this.height = height;

        try {
            resizeFunction.run();
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Failed to handle GLFW resize", ex);
        }
    }

    private void onKeyAction(int key, int action) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(windowHandle, true);
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true, fluent = true)
    public static final class Options {
        private boolean compatibleProfile;
        private int width, height;
        private int fps;
        private int ups = Engine.DEFAULT_TARGET_UPS;
    }

}
