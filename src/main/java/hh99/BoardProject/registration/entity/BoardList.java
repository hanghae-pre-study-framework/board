package hh99.BoardProject.registration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="BOARD_LIST")
public class BoardList {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="seq_no")
    private Integer seqNo;
    @Column(name="title")
    private String title;

    @Column(name="contents")
    private String contents;

    @Column(name= "password", nullable = false)
    private String password;

    @Column(name= "use_yn", nullable = false)
    private String useYn;

    @Column(name="reg_id", nullable = false)
    private String regId;

    @Column(name="reg_date")
    private String regDate;

    @Column(name="reg_ip")
    private String regIp;

    @Column(name="edit_id")
    private String editId;


    @Column(name="edit_date")
    private String editDate;

    @Column(name="edit_ip")
    private String editIp;

    public BoardList(String title, String regId, String contents, String regDate)
    {
        this.title = title;
        this.regId = regId;
        this.contents = contents;
        this.regDate = regDate;
    }
}
