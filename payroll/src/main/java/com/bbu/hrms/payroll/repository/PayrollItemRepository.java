package com.bbu.hrms.payroll.repository;

import com.bbu.hrms.payroll.entity.PayrollItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollItemRepository extends JpaRepository<PayrollItem,Long> {

}
