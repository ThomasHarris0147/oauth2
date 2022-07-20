package io.muzoo.scalable.nettube.videoclient.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CommentDTO {

    private boolean success;
    private String message;

}

