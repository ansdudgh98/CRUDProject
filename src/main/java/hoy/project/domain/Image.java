package hoy.project.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String filename;

    private String url;

    private Long capacity;

    @Builder
    public Image(String filename, String url, Long capacity, Account account) {
        this.filename = filename;
        this.url = url;
        this.capacity = capacity;
        this.account = account;
    }


}
