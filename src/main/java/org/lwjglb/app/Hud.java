package org.lwjglb.app;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import org.lwjgl.Version;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjglb.engine.Utils;
import org.lwjglb.engine.Window;

public class Hud {

    private static final String FONT_NAME = "BOLD";

    // Vector Graphics rendering library
    private long vg;

    private NVGColor colour;

    private ByteBuffer fontBuffer;

    private DoubleBuffer posx;

    private DoubleBuffer posy;

    private int counter;

    public void init(Window window) throws Exception {
        this.vg = window.getOptions().antialiasing ? nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES) : nvgCreate(NVG_STENCIL_STROKES);
        if (this.vg == NULL) {
            throw new Exception("Could not init nanovg");
        }

        fontBuffer = Utils.ioResourceToByteBuffer("/fonts/OpenSans-Bold.ttf", 150 * 1024);
        int font = nvgCreateFontMem(vg, FONT_NAME, fontBuffer, 0);
        if (font == -1) {
            throw new Exception("Could not add font");
        }
        colour = NVGColor.create();

        posx = MemoryUtil.memAllocDouble(1);
        posy = MemoryUtil.memAllocDouble(1);

        counter = 0;
    }

    public void render(Window window) {
        nvgBeginFrame(vg, window.getWidth(), window.getHeight(), 1);

        // Setup nvg
        nvgFontSize(vg, 20.0f);
        nvgFontFace(vg, FONT_NAME);
        nvgFillColor(vg, rgba(0xe6, 0xea, 0xed, 255, colour));
        nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);

        // Render details
        int borderOffsetPx = 10;
        nvgText(vg, borderOffsetPx, 20, "LWJGL version " + Version.getVersion());
        nvgText(vg, borderOffsetPx, 40, "OpenGL vendor " + glGetString(GL_VENDOR));
        nvgText(vg, borderOffsetPx, 60, "OpenGL version " + glGetString(GL_VERSION));
        nvgText(vg, borderOffsetPx, 80, "OpenGL renderer " + glGetString(GL_RENDERER));

        //Mouse coordinates
        glfwGetCursorPos(window.getWindowHandle(), posx, posy);
        int x = (int) posx.get(0);
        int y = (int) posy.get(0);
        nvgText(vg, borderOffsetPx, window.getHeight() - 45, "X: " + x + " | " + "Y: " + y);

        nvgTextAlign(vg, NVG_ALIGN_RIGHT | NVG_ALIGN_TOP);

        // Controls
        nvgText(vg, window.getWidth() - borderOffsetPx, 20, "WSAD | R(up) | F(down) | Right Mouse Btn - Scene movement");
        nvgText(vg, window.getWidth() - borderOffsetPx, 40, "Left, right arrow key - move light");
        nvgText(vg, window.getWidth() - borderOffsetPx, 60, "Space (Hold) - Animate the guy");

        // Me
        nvgText(vg, window.getWidth() - borderOffsetPx, window.getHeight() - 45, "PGRF2@UHK 2021 - Tomáš Trávníček");

        nvgEndFrame(vg);

        // Restore state
        window.restoreState();
    }

    public void incCounter() {
        counter++;
        if (counter > 99) {
            counter = 0;
        }
    }

    private NVGColor rgba(int r, int g, int b, int a, NVGColor colour) {
        colour.r(r / 255.0f);
        colour.g(g / 255.0f);
        colour.b(b / 255.0f);
        colour.a(a / 255.0f);

        return colour;
    }

    public void cleanup() {
        nvgDelete(vg);
        if (posx != null) {
            MemoryUtil.memFree(posx);
        }
        if (posy != null) {
            MemoryUtil.memFree(posy);
        }
    }
}
