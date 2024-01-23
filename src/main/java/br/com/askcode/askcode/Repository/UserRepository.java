package br.com.askcode.askcode.Repository;

import br.com.askcode.askcode.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByPassword(String password);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(UUID id);

    void deleteById(UUID id);

    List<UserEntity> findAll();

}
