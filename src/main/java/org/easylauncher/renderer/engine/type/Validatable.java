package org.easylauncher.renderer.engine.type;

public interface Validatable<E extends Exception> {

    void validate() throws E;

}
