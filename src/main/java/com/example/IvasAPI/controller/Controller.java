package com.example.IvasAPI.controller;


import com.example.IvasAPI.dto.IvasEntityDto;
import com.example.IvasAPI.model.IvasEntity;
import com.example.IvasAPI.service.IvasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {

    private final IvasService ivasService;

    @GetMapping("/findAll")
    public Iterable<IvasEntity> findAll() {
        return ivasService.findAllEntities();
    }

    @GetMapping("/findAll/active")
    public List<IvasEntity> findAllActive() {
        return ivasService.findAllActiveEntities();
    }

    @PostMapping("/createBin")
    public ResponseEntity<?> createBin(@Valid @RequestBody IvasEntity ivasEntity) {
        return new ResponseEntity<>(ivasService.createBin(ivasEntity), HttpStatus.CREATED);
    }

    @GetMapping("/validate")
    public boolean validateEntity(@RequestParam String binNumber, @RequestParam String nid) {
        return ivasService.validateEntity(binNumber, nid);
    }

    @PutMapping("/updateBin")
    public ResponseEntity<String> updateBin(@Valid @RequestBody IvasEntity ivasEntity, @PathVariable Long id) {
        return ivasService.updateBin(ivasEntity, id);
    }

    @DeleteMapping("/deleteBin")
    public ResponseEntity<String> deleteBin(@RequestParam String bin) {
        return ivasService.deleteBin(bin);
    }

    @GetMapping("/deactivateBin")
    public ResponseEntity<String> deactivateBin(@RequestParam String bin) {
        return ivasService.deactivateBin(bin);
    }

    @GetMapping("/findByBinNumber")
    public IvasEntity findByBinNumber(@RequestParam String binNumber) {
        return ivasService.findByBinNumber(binNumber);
    }
}
