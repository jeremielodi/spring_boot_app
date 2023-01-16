
package com.digipay.app.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digipay.app.models.Account;
import com.digipay.app.models.Agent;
import com.digipay.app.models.Currency;
import com.digipay.app.models.ERole;
import com.digipay.app.models.ETransactionType;
import com.digipay.app.models.Transactions;
import com.digipay.app.models.Transfert;
import com.digipay.app.models.User;
import com.digipay.app.models.Utils;
import com.digipay.app.payload.request.TransfertRequest;
import com.digipay.app.payload.request.WithdrawalRequest;
import com.digipay.app.repository.AccountRepository;
import com.digipay.app.repository.AgentRepository;
import com.digipay.app.repository.TransactionRepository;
import com.digipay.app.repository.TransfertRepository;
import com.digipay.app.repository.UserRepository;
import com.digipay.app.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  @Autowired
  TransactionRepository transactionRepository;
  @Autowired
  AgentRepository agentRepository;
  @Autowired
  UserRepository userRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  TransfertRepository transfertRepository;

  final Double gainFeePourcentage = 0.2;
  final Double commissionFeePourcentage = 0.1;

  @PostMapping("/withdrawal")
  @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
  @Transactional(rollbackOn = Exception.class)
  public ResponseEntity<?> withdrawaProcess(@Valid @RequestBody WithdrawalRequest withdrawalRequest) {

    try {
      String accountNumber = withdrawalRequest.getAccountNumber();
      String agentNumber = withdrawalRequest.getAgentNumber();
      Double amount = Double.valueOf(withdrawalRequest.getAmount());
      String transactionDescription = withdrawalRequest.getDescription();

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDetailsImpl userImpl = (UserDetailsImpl) authentication.getPrincipal();
      Long userId = userImpl.getId();

      Optional<Agent> agent = agentRepository.findByAgentNumber(agentNumber);
      Optional<User> connectedUser = userRepository.findById(userId);
      Optional<Account> userAccount = accountRepository.findByAccountNumber(accountNumber);

      Double commissionFee = commissionFeePourcentage * amount; // Frais de commission A donner a l'agent
      Double gain = gainFeePourcentage * amount; // gain de digipay pour l'operation
      Double transactionAmount = amount + commissionFee + gain;

      if (connectedUser.isEmpty() || agent.isEmpty() || userAccount.isEmpty()) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("status", 0);
        map1.put("code", 400);
        map1.put("message", "User, agent or Account not found");
        return ResponseEntity.badRequest().body(map1);
      }

      // verification si balance insuffisant
      Long accountId = userAccount.get().getId();
      Double currenctBalance = transactionRepository.accountBalance(accountId);

      if (transactionAmount > currenctBalance) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("status", 0);
        map1.put("code", 401);
        map1.put("message", "insufficient balance");
        map1.put("balance", currenctBalance);
        map1.put("transactionAmount", Utils.roundToString(transactionAmount, 2));
        return ResponseEntity.badRequest().body(map1);
      }

      // Vérifions si les comptes ont le même identifiant de devise
      // let check is the to accounts have the same currency id
      int agentAccountCurrencyId = agent.get().getAccount().getCurrency().getId();
      int userAccountCurrencyId = userAccount.get().getCurrency().getId();

      if (agentAccountCurrencyId != userAccountCurrencyId) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("status", 0);
        map1.put("code", 402);
        map1.put("message", "Accounts must have the currency");
        return ResponseEntity.badRequest().body(map1);
      }

      // seuls les administrateurs peuvent mouvementer le compte d'une autre personne
      // (user)
      // only admins can move other person's account
      if (!connectedUser.get().hasRole(ERole.ROLE_ADMIN)) {

        if (!Objects.equals(userAccount.get().getUser().getId(), connectedUser.get().getId())) {
          HashMap<String, Object> map1 = new HashMap<>();
          map1.put("status", 0);
          map1.put("code", 403);
          map1.put("message", "Only admins can move other person's account");
          return ResponseEntity.badRequest().body(map1);
        }
      }

      //
      Timestamp currenTimestamp = new Timestamp(System.currentTimeMillis());

      Transactions userTransaction = new Transactions();
      userTransaction.setAccount(userAccount.get());
      userTransaction.setUser(connectedUser.get());
      userTransaction.setAmount(transactionAmount);
      userTransaction.setDescription(transactionDescription);
      userTransaction.setCommission(commissionFee);
      userTransaction.setIsExit(1);
      userTransaction.setGain(gain);
      userTransaction.setReferenceNumber(Utils.generateRamdomString(20));
      userTransaction.setTransactionType(ETransactionType.WITHDRAWAL);
      userTransaction.setCreatedAt(currenTimestamp);

      Transactions save1 = transactionRepository.save(userTransaction);

      // enregistrement du frais de commussion pour l'agent
      Transactions agentTransaction = new Transactions();
      agentTransaction.setAccount(agent.get().getAccount());
      agentTransaction.setUser(connectedUser.get());
      agentTransaction.setAmount(commissionFee);
      agentTransaction.setDescription(ETransactionType.COMMISSION_FEE.toString());
      agentTransaction.setIsExit(0);
      agentTransaction.setGain(0.0);
      agentTransaction.setCommission(0.0);
      agentTransaction.setReferenceNumber(Utils.generateRamdomString(20));
      agentTransaction.setTransactionType(ETransactionType.COMMISSION_FEE);
      agentTransaction.setFromTransfert(1);
      agentTransaction.setCreatedAt(currenTimestamp);

      Transactions save2 = transactionRepository.save(agentTransaction);

      Transfert transfert = new Transfert();
      transfert.setFromTransaction(userTransaction);
      transfert.setToTransaction(agentTransaction);
      transfertRepository.save(transfert);

      // return la reference de la transaction cree pour l'utilisateur(user)
      HashMap<String, Object> map = new HashMap<>();
      map.put("status", 1);
      map.put("code", 200);
      map.put("transaction_reference", userTransaction.getReferenceNumber());
      map.put("transaction_amount", userTransaction.getAmount());
      map.put("transaction_date", Utils.formatDateTime(currenTimestamp));

      return ResponseEntity.ok(map);
    } catch (Exception e) {
      System.out.println("-------------------------------------------------------------------");
      System.out.println(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  // processus pour transferer l'argent d'un compte A un autre
  @PostMapping("/transfert")
  @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
  @Transactional(rollbackOn = Exception.class)
  public ResponseEntity<?> transfertProcess(@Valid @RequestBody TransfertRequest transfertRequest) {

    try {

      Double amount = Double.valueOf(transfertRequest.getAmount());
      String transactionDescription = transfertRequest.getDescription();

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDetailsImpl userImpl = (UserDetailsImpl) authentication.getPrincipal();
      Long userId = userImpl.getId();

      Optional<User> connectedUser = userRepository.findById(userId);

      Optional<Account> fromAccount = accountRepository
          .findByAccountNumber(transfertRequest.getFromAccountNumber());

      Optional<Account> toAccount = accountRepository.findByAccountNumber(transfertRequest.getToAccountNumber());

      Double gain = gainFeePourcentage * amount; // (Frais d'envois ou de retrait) le cout pour l'operation, gain
                                                 // pour digipay
      Double transactionAmount = amount + gain;

      if (connectedUser.isEmpty() || fromAccount.isEmpty() || toAccount.isEmpty()) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("status", 0);
        map1.put("code", 400);
        map1.put("message", "User or one of Accounts not found");
        return ResponseEntity.badRequest().body(map1);
      }

      // verification si balance insuffisant

      Double currenctBalance = transactionRepository.accountBalance(fromAccount.get().getId());
      if (transactionAmount > currenctBalance) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("status", 0);
        map1.put("code", 401);
        map1.put("message", "insufficient balance");
        map1.put("balance", currenctBalance);
        map1.put("transactionAmount", transactionAmount);
        return ResponseEntity.badRequest().body(map1);
      }

      // let check is the to accounts have the same currency id
      int fromCurrencyId = fromAccount.get().getCurrency().getId();
      int toCurrencyId = toAccount.get().getCurrency().getId();

      if (fromCurrencyId != toCurrencyId) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("status", 0);
        map1.put("code", 402);
        map1.put("message", "Accounts must have the currency");
        return ResponseEntity.badRequest().body(map1);
      }

      // only admins can move other person's account
      if (!connectedUser.get().hasRole(ERole.ROLE_ADMIN)) {

        if (!Objects.equals(fromAccount.get().getUser().getId(), connectedUser.get().getId())) {
          HashMap<String, Object> map1 = new HashMap<>();
          map1.put("status", 0);
          map1.put("code", 403);
          map1.put("message", "Only admins can move other person's account");
          return ResponseEntity.badRequest().body(map1);
        }
      }
      //
      Timestamp currenTimestamp = new Timestamp(System.currentTimeMillis());

      Transactions fromTransaction = new Transactions();
      fromTransaction.setAccount(fromAccount.get());
      fromTransaction.setUser(connectedUser.get());
      fromTransaction.setAmount(transactionAmount);
      fromTransaction.setDescription(transactionDescription);
      fromTransaction.setIsExit(1);
      fromTransaction.setCommission(0.0);
      fromTransaction.setGain(gain);
      fromTransaction.setReferenceNumber(Utils.generateRamdomString(20));
      fromTransaction.setTransactionType(ETransactionType.TRANSFERT);
      fromTransaction.setCreatedAt(currenTimestamp);

      transactionRepository.save(fromTransaction);

      // enregistrement du frais de commussion pour l'agent
      Transactions toTransaction = new Transactions();
      toTransaction.setAccount(toAccount.get());
      toTransaction.setUser(connectedUser.get());
      toTransaction.setAmount(amount);
      toTransaction.setDescription(ETransactionType.COMMISSION_FEE.toString());
      toTransaction.setIsExit(0);
      toTransaction.setGain(0.0);
      toTransaction.setCommission(0.0);
      toTransaction.setReferenceNumber(Utils.generateRamdomString(20));
      toTransaction.setTransactionType(ETransactionType.DEPOSIT);
      toTransaction.setFromTransfert(1);
      toTransaction.setCreatedAt(currenTimestamp);
      toTransaction = transactionRepository.save(toTransaction);

      Transfert transfert = new Transfert();
      transfert.setFromTransaction(fromTransaction);
      transfert.setToTransaction(toTransaction);
      transfertRepository.save(transfert);

      // return la reference de la transaction cree pour l'utilisateur(client)
      HashMap<String, Object> map = new HashMap<>();
      map.put("status", 1);
      map.put("code", 200);
      map.put("transaction_reference", fromTransaction.getReferenceNumber());
      map.put("transaction_amount", fromTransaction.getAmount());
      map.put("transaction_date", Utils.formatDateTime(currenTimestamp));

      return ResponseEntity.ok(map);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /*
   * Liste des transaction pour un compte donné
   * List of transactions for a given account
   */
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/listForUser/{accountId}")
  public ResponseEntity<?> getTransactionsProcess(@PathVariable String accountId) {
    try {

      Long selectedAccountId = Long.valueOf(accountId);
      List<Transactions> lists = transactionRepository.findByAccountId(selectedAccountId);
      List<HashMap<String, Object>> ListMap = new ArrayList<>();
      for (int i = 0; i < lists.size(); i++) {
        Transactions t = lists.get(i);
        Currency currency = t.getAccount().getCurrency();
        HashMap<String, Object> map = new HashMap<>();
        map.put("transactionId", t.getId());
        map.put("accountId", t.getAccount().getId());
        map.put("accountNumber", t.getAccount().getAccountNumber());
        map.put("accountTitle", t.getAccount().getAccountTitle());
        map.put("amount", t.getAmount());
        map.put("commission", t.getCommission());
        map.put("currencyId", currency.getId());
        map.put("currencySymbol", currency.getSymbol());
        map.put("transactionType", t.getTransactionType());
        map.put("fromTransfert", t.getFromTransfert() == 1);
        map.put("isExit", t.getIsExit() == 1);
        map.put("description", t.getDescription());
        map.put("date", Utils.formatDateTime(t.getCreatedAt()));
        ListMap.add(map);
      }
      return ResponseEntity.ok(ListMap);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      HashMap<String, Object> errMap = new HashMap<>();
      return ResponseEntity.badRequest().body(errMap);
    }

  }
}