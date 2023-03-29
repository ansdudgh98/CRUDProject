package hoy.project.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Views {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "views_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "view_id")
    private View view;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
}
