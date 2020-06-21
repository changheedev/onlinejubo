package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.BaseEntity;
import com.github.changhee_choi.jubo.core.domain.user.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@Entity
@Table(name = "oj_church")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Church extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int memberNum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "church", cascade = CascadeType.ALL)
    private List<Jubo> juboList = new ArrayList<>();

    @Builder
    public Church(String name, int memberNum, User user) {
        this.name = name;
        this.memberNum = memberNum;
        this.user = user;
        this.user.setChurch(this);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public void addJubo(Jubo jubo) {
        if (!this.juboList.contains(jubo)) {
            this.juboList.add(jubo);
        }
    }
}
