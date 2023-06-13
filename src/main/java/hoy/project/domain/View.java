package hoy.project.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "article_id")
    private Article article;

    private Long count;

    @CreatedDate
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public View(Article article) {
        this.article = article;
        this.count = 0L;
    }
}
