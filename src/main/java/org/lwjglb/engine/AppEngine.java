package org.lwjglb.engine;

import org.lwjglb.app.Hud;

public class AppEngine implements Runnable {

    public static final int TARGET_FPS = 300;

    public static final int TARGET_UPS = 150;

    private final Window window;

    private final String windowTitle;

    private final Timer timer;

    private final IAppLogic gameLogic;

    private final MouseInput mouseInput;

    private final Hud hud;

    private double lastFps;
    
    private int fps;
    
    public AppEngine(String windowTitle, Hud hud, Window.WindowOptions opts, IAppLogic gameLogic) {
        this(windowTitle, 0, 0, hud, opts, gameLogic);
    }

    public AppEngine(String windowTitle, int width, int height, Hud hud, Window.WindowOptions opts, IAppLogic gameLogic) {
        this.windowTitle = windowTitle;
        window = new Window(windowTitle, width, height, opts);
        mouseInput = new MouseInput();
        this.hud = hud;
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    @Override
    public void run() {
        try {
            init();
            appLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        mouseInput.init(window);
        hud.init(window);
        gameLogic.init(window);
        lastFps = timer.getTime();
        fps = 0;
    }

    protected void appLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;


        while (!window.shouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();


            if ( !window.getOptions().vSync ) {
                sync();
            }
        }
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }
    
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                System.err.print(ie.getMessage());
            }
        }
    }

    protected void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    protected void update(float interval) {
        gameLogic.update(interval, mouseInput, window);
    }

    protected void render() {
        if ( window.getWindowOptions().showFps && timer.getLastLoopTime() - lastFps > 1 ) {
            lastFps = timer.getLastLoopTime();
            window.setWindowTitle(windowTitle + " - " + fps + " FPS");
            fps = 0;
        }
        fps++;
        gameLogic.render(window);
        hud.render(window);
        window.update();
    }

}
