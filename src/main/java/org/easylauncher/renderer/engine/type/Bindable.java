package org.easylauncher.renderer.engine.type;

public interface Bindable {

    void bind();

    default void unbind() {};

}
