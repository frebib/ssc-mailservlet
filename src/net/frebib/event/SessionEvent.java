package net.frebib.event;

/**
 * Defines an event initiated by the session
 * @param <E> Event data type
 */
public interface SessionEvent<E> {

    /**
     * Called when an event is raised
     * @param data event data
     */
    void onEvent(E data);
}
