package com.bbu.hrms.leave_request.controller;

import com.bbu.hrms.leave_request.dto.LeaveBalanceDTO;
import com.bbu.hrms.leave_request.helper.PagedResponse;
import com.bbu.hrms.leave_request.service.LeaveBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/leave-balances")
@Tag(name = "Leave Balance", description = "Leave Balance API")
public class LeaveBalanceController {

    private final LeaveBalanceService service;

    public LeaveBalanceController(LeaveBalanceService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all leave balances")
    public ResponseEntity<PagedResponse<LeaveBalanceDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<LeaveBalanceDTO> pagedResult = service.getAll(page, size);
        return ResponseEntity.ok().body(new PagedResponse<>(pagedResult));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get leave balance by id")
    public LeaveBalanceDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new leave balance")
    public LeaveBalanceDTO create(@Valid @RequestBody LeaveBalanceDTO request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a leave balance")
    public LeaveBalanceDTO update(@PathVariable Long id,
                                       @Valid @RequestBody LeaveBalanceDTO request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a leave balance")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
