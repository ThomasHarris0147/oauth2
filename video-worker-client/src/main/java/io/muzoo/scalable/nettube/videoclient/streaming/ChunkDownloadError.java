package io.muzoo.scalable.nettube.videoclient.streaming;

public class ChunkDownloadError extends RuntimeException {

    public ChunkDownloadError(String message) {
        super(message);
    }

    public ChunkDownloadError(String message, Throwable cause) {
        super(message, cause);
    }
}
