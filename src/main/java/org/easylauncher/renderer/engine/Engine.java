package org.easylauncher.renderer.engine;

import org.easylauncher.renderer.context.RenderOptions;
import org.easylauncher.renderer.context.RendererContext;
import org.easylauncher.renderer.context.ViewDesire;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Engine {

    public static final Engine CURRENT = new Engine();

    private final List<RendererContext> contexts;
    private boolean working;

    private Engine() {
        this.contexts = new ArrayList<>();
        this.working = true;
    }

    public RendererContext loadContext(RenderOptions renderOptions, ViewDesire viewDesire) {
        if (!working)
            return null;

        Render render = new Render(renderOptions);
        RendererContext context = new RendererContext(this, render, viewDesire);
        contexts.add(context);
        return context;
    }

    public void unloadContext(RendererContext context) {
        if (!working)
            return;

        if (!contexts.contains(context))
            return;

        contexts.remove(context);
    }

    public void shutdown() {
        System.out.println("[Engine] Shutting down...");
        this.working = false;

        Iterator<RendererContext> iterator = contexts.iterator();
        while (iterator.hasNext()) {
            iterator.next().cleanup();
            iterator.remove();
        }

        System.out.println("[Engine] Goodbye!");
    }

}
