package com.victortavin.marmitaria.repositories.balance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victortavin.marmitaria.entities.balance.BalanceEntity;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long>{

}
