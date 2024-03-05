package com.dndoz.PosePicker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dndoz.PosePicker.Domain.Withdrawal;

public interface WithdrawalRepository extends JpaRepository<Withdrawal,Long> {
}
