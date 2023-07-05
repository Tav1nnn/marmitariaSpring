package com.victortavin.marmitaria.repositories.demand;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victortavin.marmitaria.entities.demand.DemandEntity;

public interface DemandRepository extends JpaRepository<DemandEntity, Long>{

}
