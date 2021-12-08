package com.dokev.gold_dduck.member.repository;

import com.dokev.gold_dduck.member.domain.Group;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);
}
