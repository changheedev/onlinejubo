package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@Entity
@Table(name = "oj_jubo_content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = {"jubo", "attachments"})
@ToString(exclude = "jubo")
public class JuboContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jubo_id")
    private Jubo jubo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "oj_jubo_content_attachment",
            joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id", referencedColumnName = "id"))
    private Set<Attachment> attachments = new HashSet<>();

    public JuboContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void setJubo(Jubo jubo) {
        this.jubo = jubo;
        if (!this.jubo.getContents().contains(this)) {
            this.jubo.getContents().add(this);
        }
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }
}
