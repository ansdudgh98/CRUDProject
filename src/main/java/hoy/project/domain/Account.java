package hoy.project.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true, updatable = false,nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Builder
    public Account(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public static Account createAccount(String userId, String password, String email){
        return Account.builder()
                .userId(userId)
                .password(password)
                .email(email)
                .build();
    }


}
