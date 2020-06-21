package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.BaseEntity;
import com.github.changhee_choi.jubo.core.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@Entity
@Table(name = "oj_church_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class ChurchInfo extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    private int memberNum;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ChurchInfo(String name, int memberNum, User user) {
        this.name = name;
        this.memberNum = memberNum;
        this.user = user;
        this.user.setChurchInfo(this);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }
}
