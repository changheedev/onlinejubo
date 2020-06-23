package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @OneToMany(mappedBy = "church", cascade = CascadeType.ALL)
    private List<Jubo> juboList = new ArrayList<>();

    @Builder
    public Church(String name, int memberNum) {
        this.name = name;
        this.memberNum = memberNum;
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
