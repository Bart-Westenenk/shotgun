package Shotgun;

public class LevelScene extends Scene {

    public LevelScene() {
        System.out.println("Inside LevelScene");
        Window.getInstance().r = 1.0f;
        Window.getInstance().g = 1.0f;
        Window.getInstance().b = 1.0f;
        Window.getInstance().a = 1.0f;
    }

    @Override
    public void update(float dt) {

    }
}
