package hh99.BoardProject.registration.repository;

import hh99.BoardProject.registration.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    UserInfo findOneByUserName(String userName); //findOne 하나 find : array
    Optional<UserInfo> findByUserName(String userName);
}
