package org.easylauncher.renderer.state;

public interface Validatable<E extends Exception> {

    void validate() throws E;

}
