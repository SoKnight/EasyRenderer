package org.easylauncher.renderer.state;

public interface Bindable {

    void bind();

    default void unbind() {};

}
