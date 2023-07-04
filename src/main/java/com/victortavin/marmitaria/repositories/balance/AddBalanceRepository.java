package com.victortavin.marmitaria.repositories.balance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victortavin.marmitaria.entities.balance.Add_BalanceEntity;

public interface AddBalanceRepository extends JpaRepository<Add_BalanceEntity, Long>{
	
}
