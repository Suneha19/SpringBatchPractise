package com.qiwkreport.qiwk.etl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiwkreport.qiwk.etl.domain.Olduser;

@Repository
public interface OlduserRepository extends JpaRepository<Olduser, Integer> {

}
