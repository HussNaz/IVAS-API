package com.example.IvasAPI.repository;

import com.example.IvasAPI.model.IvasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IvasEntityRepository extends JpaRepository<IvasEntity, Long> {
    Optional<IvasEntity> findByBinNumber(String binNumber);

    boolean existsByBinNumberAndNid(String binNumber, String nid);

}
