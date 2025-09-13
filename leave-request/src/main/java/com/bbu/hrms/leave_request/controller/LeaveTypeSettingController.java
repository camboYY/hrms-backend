package com.bbu.hrms.leave_request.controller;

import com.bbu.hrms.leave_request.dto.LeaveTypeDTO;
import com.bbu.hrms.leave_request.helper.PagedResponse;
import com.bbu.hrms.leave_request.service.LeaveTypeSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/leave-types")
@Tag(name = "Leave Types", description = "Endpoints for managing leave type settings")
public class LeaveTypeSettingController {

    private final LeaveTypeSettingService service;

    public LeaveTypeSettingController(LeaveTypeSettingService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all leave type settings")
    public ResponseEntity<PagedResponse<LeaveTypeDTO>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<LeaveTypeDTO> pagedResult = service.getAllByName(name, page, size);

        return ResponseEntity.ok(new PagedResponse<>(pagedResult));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get leave type setting by id")
    public LeaveTypeDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new leave type setting")
    public LeaveTypeDTO create(@Valid @RequestBody LeaveTypeDTO request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a leave type setting")
    public LeaveTypeDTO update(@PathVariable Long id,
                                           @Valid @RequestBody LeaveTypeDTO request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a leave type setting")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
