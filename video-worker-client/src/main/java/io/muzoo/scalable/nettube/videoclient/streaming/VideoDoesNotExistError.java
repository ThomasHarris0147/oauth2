package io.muzoo.scalable.nettube.videoclient.streaming;

public class VideoDoesNotExistError extends RuntimeException{
    public VideoDoesNotExistError(String message) {
        super(message);
    }

    public VideoDoesNotExistError(String message, Throwable cause) {
        super(message, cause);
    }
}
