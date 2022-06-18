public class shotgunMain {
    static boolean keepOnRunning = true;
    static int ups = 50;
    static double updatetime = 1000.0d / ups;

    public static void main(String[] args) {
        double previous = System.currentTimeMillis();
        double steps = 0.0;

        while (keepOnRunning) {
            double current = System.currentTimeMillis();
            double elapsed = current - previous;
            previous = current;
            steps += elapsed;

            handleInput();

            while (steps >= updatetime) {
                updateGameState();
                steps -= updatetime;
            }

            render();
            sync(current);
        }
    }

    static int fps = 50;
    static double frametime = 1000.0d / fps;
    private static void sync(double loopStartTime) {
        double endTime = loopStartTime + frametime;
        if (System.currentTimeMillis() < endTime) {
            double sleepTime = endTime - System.currentTimeMillis();
            try {
                Thread.sleep((long) sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void render() {
    }

    private static void handleInput() {

    }

    private static void updateGameState() {

    }

}
