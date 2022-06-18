package Shotgun;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import java.io.Console;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    // The window handle
    private long windowHandle;
    private int width;
    private int height;
    private String title;

    public float r = 1.0f;
    public float g = 1.0f;
    public float b = 1.0f;
    public  float a = 1.0f;


    private static Window instance = null;

    private static Scene currentScene;


    private Window () {
        this.width = 1920;
        this.height = 1080;
        this.title = "Window";
    }
    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
            System.out.println("Creating new window ...");
        }
        return instance;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
        }
    }

    public void run() {
        System.out.println("Starting with LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup the callback for errors. Prints the error in System.err
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work without doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initalize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // Set the window hints to its defaults
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Hide the window after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // The window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // TODO: Setup various callbacks
        // Set callbacks for mouse events
        glfwSetCursorPosCallback(windowHandle, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(windowHandle, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(windowHandle, MouseListener::scrollCallback);

        // Set callbacks for Keyboard
        glfwSetKeyCallback(windowHandle, KeyListener::keyCallback);


        // TODO: do some stuff with teh thread stack I guess whatevs

        /// Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowHandle);

        System.out.println("Initialized window");

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Window.changeScene(0);
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = 1.0f;

        // Run the rendering loop until the user has attempted to close the window
        while (!glfwWindowShouldClose(windowHandle)) {
            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();

            // Color the screen
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT); // Clear the framebuffer

            if (dt >= 0) {
                System.out.printf("fps: %.2f dt: %.12f\r", 1/dt, dt);
                currentScene.update(dt);
            }


            glfwSwapBuffers(windowHandle); // Swap the color buffers

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        System.out.println("Window has been closed, goodbye!");
    }


    public static void main(String[] args) {
        Window.getInstance().run();
    }
}
