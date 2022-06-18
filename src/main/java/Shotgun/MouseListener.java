package Shotgun;

import static org.lwjgl.glfw.GLFW.*;

public enum MouseListener {
    instance;

    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[GLFW_MOUSE_BUTTON_LAST]; // GLFW supports max of 8 mouseButtons
    private boolean isDragging;

    MouseListener() {
        this.scrollX = 0.0f;
        this.scrollY = 0.0f;
        this.xPos = 0.0f;
        this.yPos = 0.0f;
        this.lastX = 0.0f;
        this.lastY = 0.0f;

    }

    // Notify when the cursor moves over the window
    public static void mousePosCallback(long window, double xpos, double ypos) {
        instance.lastX = instance.xPos;
        instance.lastY = instance.yPos;
        instance.xPos = xpos;
        instance.yPos = ypos;
        // If a button is pressed, the mouse is being dragged
        for (int i = 0; i < instance.mouseButtonPressed.length; i++) {
            if (instance.mouseButtonPressed[i]) {
                instance.isDragging = true;
                break;
            }
        }
    }

    // Notify when a mouse button is pressed or released
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button < instance.mouseButtonPressed.length) {
            if (action == GLFW_PRESS) {
                instance.mouseButtonPressed[button] = true;
            } else if (action == GLFW_RELEASE) {
                instance.mouseButtonPressed[button] = false;
                instance.isDragging = false;
            }
        }
    }

    // Notify when the user scrolls
    public static void scrollCallback(long window, double xoffset, double yoffset) {
        instance.scrollX = xoffset;
        instance.scrollY = yoffset;
    }

    public static void endFrame() {
        instance.scrollX = 0;
        instance.scrollY = 0;
        instance.lastX = instance.xPos;
        instance.lastY = instance.yPos;
    }

    // Getters
    public static float getX() {
        return (float) instance.xPos;
    }

    public static float getY() {
        return (float) instance.yPos;
    }

    public static float getDx() {
        return (float) (instance.lastX - instance.xPos);
    }

    public static float getDy() {
        return (float) (instance.lastY - instance.yPos);
    }

    public static float getScrollX() {
        return (float) instance.scrollX;
    }

    public static float getScrollY() {
        return (float) instance.scrollY;
    }

    public static boolean isDragging() {
        return instance.isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        return instance.mouseButtonPressed[button];
    }
}
