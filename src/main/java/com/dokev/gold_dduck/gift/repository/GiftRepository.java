package com.dokev.gold_dduck.gift.repository;

import com.dokev.gold_dduck.gift.domain.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftRepository extends JpaRepository<Gift, Long> {
}
