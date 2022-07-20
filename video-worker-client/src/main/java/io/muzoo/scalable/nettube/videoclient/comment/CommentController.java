package io.muzoo.scalable.nettube.videoclient.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @RequestMapping(value = "/api/comments", method = RequestMethod.POST)
    public CommentDTO addComment(@RequestBody Map<String, String> request) {
        Comment comment = new Comment();
        comment.setAuthor(request.get("author"));
        // comment.setAuthor_id(request.get("author_id"));
        comment.setCommentData(request.get("commentData"));
        commentRepository.save(comment);
        return CommentDTO.builder().success(true).message("Comment added").build();
    }

    @RequestMapping(value = "/api/comments/recent", method = RequestMethod.GET)
    public Stream<Comment> getRecentComments() {
        return commentRepository.findAll(Sort.by(Sort.Direction.DESC, "date")).stream().limit(100);
    }
}
