package org.lwjglb;

import org.lwjglb.app.App;
import org.lwjglb.app.Hud;
import org.lwjglb.engine.AppEngine;
import org.lwjglb.engine.IAppLogic;
import org.lwjglb.engine.Window;

public class Main {

    public static void main(String[] args) {
        try {
            IAppLogic appLogic = new App();
            Hud appHud = new Hud();
            Window.WindowOptions opts = new Window.WindowOptions();
            opts.cullFace = false;
            opts.showFps = true;
            opts.compatibleProfile = true;
            opts.antialiasing = true;
            opts.vSync = false;
            opts.frustumCulling = false;
            AppEngine engine = new AppEngine("PGRF2 LWJGL Project", appHud, opts, appLogic);
            engine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
