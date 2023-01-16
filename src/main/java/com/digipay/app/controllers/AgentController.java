package com.digipay.app.controllers;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digipay.app.models.Agent;
import com.digipay.app.repository.AgentRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @Autowired
    AgentRepository agentRepository;

    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    @GetMapping("/detail/{agentNumber}")
    public ResponseEntity<?> findByNumber(@PathVariable String agentNumber) {
        try {

            Optional<Agent> agent = agentRepository.findByAgentNumber(agentNumber);

            if (!agent.isPresent()) {
                HashMap<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", 0);
                errorMap.put("code", 400);
                errorMap.put("message", "AGENT_NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
            }

            Agent currentAgent = agent.get();
            HashMap<String, Object> map = new HashMap<>();
            map.put("agentId", currentAgent.getId());
            map.put("agentNumber", currentAgent.getAgentNumber());
            map.put("agentName", currentAgent.getName());
            map.put("userId", currentAgent.getUser().getId());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}