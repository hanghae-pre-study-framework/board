package hh99.BoardProject.registration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Data
@Table(name="USER_INFO")
public class UserInfo {

    @Id
    @Column(name="user_name", nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(name= "reg_id")
    private String regId;

    @Column(name= "reg_date")
    private String regDate;

    @Column(name="reg_ip")
    private String regIp;

}
