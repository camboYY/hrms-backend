package com.bbu.hrms.leave_request.scheduler;

import com.bbu.hrms.leave_request.model.LeaveBalance;
import com.bbu.hrms.leave_request.model.LeaveType;
import com.bbu.hrms.leave_request.repository.LeaveBalanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class LeaveBalanceScheduler {
    private static final Logger logger = LoggerFactory.getLogger(LeaveBalanceScheduler.class);

    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveBalanceScheduler(LeaveBalanceRepository leaveBalanceRepository) {
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    /**
     * Runs yearly at midnight on January 1st.
     * CRON format = sec min hour day month weekday
     */
    @Scheduled(cron = "0 0 0 1 1 *")
    @Transactional
    public void carryForwardLeave() {
        System.out.println("Running carry forward leave scheduler...");

        List<LeaveBalance> allBalances = leaveBalanceRepository.findAll();

        for (LeaveBalance balance : allBalances) {
            LeaveType leaveType = balance.getLeaveType();

            if (leaveType.isCarryForwardAllowed()) {
                // Carry forward up to the max days per year
                int carryForwardDays = Math.min(balance.getRemainingDays(), leaveType.getMaxDaysPerYear());

                balance.setRemainingDays(carryForwardDays);
            } else {
                // Reset to 0 if carry forward not allowed
                balance.setRemainingDays(0);
            }

            leaveBalanceRepository.save(balance);
        }
    }

}
