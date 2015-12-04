package net.frebib.event;

public interface SessionEvent<E> {
    void onEvent(E data);
}
