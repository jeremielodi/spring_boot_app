package com.digipay.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digipay.app.models.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    Optional<Agent> findByAgentNumber(String agentNumber);
}
