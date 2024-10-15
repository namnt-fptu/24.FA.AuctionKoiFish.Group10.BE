package com.group10.koiauction.service;

import com.group10.koiauction.entity.Account;
import com.group10.koiauction.entity.Transaction;
import com.group10.koiauction.entity.enums.TransactionEnum;
import com.group10.koiauction.mapper.AccountMapper;
import com.group10.koiauction.model.response.AccountResponse;
import com.group10.koiauction.model.response.BalanceResponseDTO;
import com.group10.koiauction.repository.AccountRepository;
import com.group10.koiauction.repository.TransactionRepository;
import com.group10.koiauction.utilities.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    TransactionRepository transactionRepository;

    public Transaction depositFunds(Long id){
        Account account = accountUtils.getCurrentAccount();
        Transaction transaction = transactionRepository.findTransactionById(id);
        if(transaction.getType().equals(TransactionEnum.PENDING)){
            account.setBalance(account.getBalance() + transaction.getAmount());
        }else{
            throw new RuntimeException("Failed to deposit funds , transaction have been processed");
        }
        transaction.setType(TransactionEnum.DEPOSIT_FUNDS);
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public BalanceResponseDTO getCurrentUserBalance(){
        Account account = accountUtils.getCurrentAccount();
        BalanceResponseDTO balanceResponseDTO = new BalanceResponseDTO();
        balanceResponseDTO.setBalance(account.getBalance());
        balanceResponseDTO.setId(account.getUser_id());
        return balanceResponseDTO;
    }
}
