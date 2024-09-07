package hh99.BoardProject.registration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Data //getter, setter 자동생성
@Table(name="USER_INFO")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="seq_no")
    private Integer seqNo;

    @Column(name="user_name", nullable = false, unique = true)
    @Pattern(regexp = "[0-9a-z]+", message="Username Must Contain Only Lowered letters and Numbers")
    @Size(min=4, max=10, message="size 4~10")
    private String userName;

    @Column(nullable = false)
    @Pattern(regexp = "[0-9a-zA-Z]+", message="Username Must Contain Only Letters and Numbers")
    @Size(min=8, max=15, message="size 8~15")
    private String password;

    @Column(name= "reg_id")
    private String regId;

    @Column(name= "reg_date")
    private String regDate;

    @Column(name="reg_ip")
    private String regIp;

}
