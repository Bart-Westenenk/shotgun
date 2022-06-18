package Shotgun;

import static org.lwjgl.glfw.GLFW.*;

public enum KeyListener {
    instance;

    private boolean[] keyPressed = new boolean[GLFW_KEY_LAST];

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            instance.keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            instance.keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key) {
        return instance.keyPressed[key];
    }
}
