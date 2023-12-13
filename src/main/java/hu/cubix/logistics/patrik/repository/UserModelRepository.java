package hu.cubix.logistics.patrik.repository;

import hu.cubix.logistics.patrik.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserModelRepository extends JpaRepository<UserModel, String> {
}
