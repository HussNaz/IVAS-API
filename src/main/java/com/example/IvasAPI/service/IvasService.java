package com.example.IvasAPI.service;

import com.example.IvasAPI.exception.InvalidBINException;
import com.example.IvasAPI.exception.InvalidNIDException;
import com.example.IvasAPI.exception.ResourceNotFoundException;
import com.example.IvasAPI.model.IvasEntity;
import com.example.IvasAPI.repository.IvasEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IvasService {

    private final IvasEntityRepository ivasEntityRepository;

    public IvasEntity findByBinNumber(String binNumber) {
        if (!isValidBinNumber(binNumber)) {
            throw new InvalidBINException("Invalid BIN number. BIN must be 13 digits");
        }
        return ivasEntityRepository.findByBinNumber(binNumber)
                .orElseThrow(() -> new ResourceNotFoundException("BIN not found"));
    }

    public boolean validateEntity(String binNumber, String nid) {
        if (!isValidBinNumber(binNumber)) {
            throw new InvalidBINException("Invalid BIN number. BIN must be 13 digits");
        }
        if (!isValidNid(nid)) {
            throw new InvalidNIDException("Invalid NID. NID must be 10, 13, or 18 digits");
        }
        return ivasEntityRepository.existsByBinNumberAndNid(binNumber, nid);
    }

    public IvasEntity createBin(IvasEntity ivasEntity) {
        return ivasEntityRepository.save(ivasEntity);
    }

    public ResponseEntity<String> updateBin(IvasEntity ivasEntity, Long id) {
        if (ivasEntityRepository.existsById(id)) {
            ivasEntity.setId(id);
            ivasEntityRepository.save(ivasEntity);

            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + " not found");
    }

    public ResponseEntity<String> deleteBin(String bin) {
        return ivasEntityRepository.findByBinNumber(bin).map(ivasEntity -> {
            ivasEntityRepository.delete(ivasEntity);

            return ResponseEntity.status(HttpStatus.OK).body("BIN with BIN number  " + bin + " Successfully deleted");
        }).orElseThrow(() -> new ResourceNotFoundException("Bin Not Found"));
    }

    public ResponseEntity<String> deactivateBin(String bin) {
        return ivasEntityRepository.findByBinNumber(bin).map(ivasEntity -> {
            ivasEntity.setActive(false);
            ivasEntityRepository.save(ivasEntity);
            return ResponseEntity.status(HttpStatus.OK).body("BIN with BIN number  " + bin + " Successfully deactivated");
        }).orElseThrow(() -> new ResourceNotFoundException("Bin Not Found"));

    }

    private boolean isValidNid(String nid) {
        return nid.matches("^(\\d{10}|\\d{13}|\\d{18})$");
    }

    private boolean isValidBinNumber(String binNumber) {
        return binNumber.matches("^\\d{13}$");
    }
}
