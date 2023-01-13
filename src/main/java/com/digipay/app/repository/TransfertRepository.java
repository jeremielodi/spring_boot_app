package com.digipay.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digipay.app.models.Transfert;

public interface TransfertRepository extends JpaRepository<Transfert, Long> {

}
