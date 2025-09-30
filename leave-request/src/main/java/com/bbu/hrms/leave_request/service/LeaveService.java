package com.bbu.hrms.leave_request.service;


import com.bbu.hrms.leave_request.dto.LeaveBalanceDTO;
import com.bbu.hrms.leave_request.dto.LeaveRequestDTO;
import com.bbu.hrms.leave_request.dto.LeaveTypeDTO;
import com.bbu.hrms.leave_request.model.LeaveRequest;

import java.util.List;

public interface LeaveService {
    // Leave Types
    LeaveTypeDTO createLeaveType(LeaveTypeDTO dto);
    List<LeaveTypeDTO> listLeaveTypes();

    List<LeaveRequest> getPendingRequestsForManager(Long managerId);

    // Leave Requests
    LeaveRequestDTO requestLeave(LeaveRequestDTO dto);
    LeaveRequestDTO approveLeave(Long requestId, Long approverId);
    LeaveRequestDTO rejectLeave(Long requestId, Long approverId);
    List<LeaveRequestDTO> listRequests(Long employeeId);

    // Leave Balances
    LeaveBalanceDTO allocateLeave(LeaveBalanceDTO dto);
    LeaveBalanceDTO getBalance(Long employeeId, Long leaveTypeId);

    LeaveRequestDTO cancelRequest(Long id);

    List<LeaveRequestDTO> getRecentRequests(Long employeeId);
}
