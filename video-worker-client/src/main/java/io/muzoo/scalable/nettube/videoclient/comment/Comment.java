package io.muzoo.scalable.nettube.videoclient.comment;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name= "comment_table")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "commentData")
    private String commentData;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime date;

    @Column(name = "commentAuthor")
    private String author;

    @Column(name = "commentAuthorId")
    private long author_id;
}
