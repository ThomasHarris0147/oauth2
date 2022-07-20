package io.muzoo.scalable.nettube.videoclient.streaming;

public class PlayerDoesNotExistError extends RuntimeException {

    public PlayerDoesNotExistError(String message) {
        super(message);
    }

    public PlayerDoesNotExistError(String message, Throwable cause) {
        super(message, cause);
    }
}
