package hh99.BoardProject.registration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="BOARD_LIST")
public class BoardList {

    @Id
    @Column(name="title", nullable = false)
    private String title;

    @Column(name="contents", nullable = false)
    private String contents;

    @Column(name= "user_name", nullable = false)
    private String userName;

    @Column(name= "password")
    private String password;

    @Column(name= "use_yn")
    private String useYn;

    @Column(name="reg_id")
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

}
