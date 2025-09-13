package com.bbu.hrms.user_service.repository;

import com.bbu.hrms.user_service.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {}