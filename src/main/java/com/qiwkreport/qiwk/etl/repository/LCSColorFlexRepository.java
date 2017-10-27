package com.qiwkreport.qiwk.etl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiwkreport.qiwk.etl.flex.domain.LCSColorFlex;

@Repository
public interface LCSColorFlexRepository extends JpaRepository<LCSColorFlex, Integer> {

}
