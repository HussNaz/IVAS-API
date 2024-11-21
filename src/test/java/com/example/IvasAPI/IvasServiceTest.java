package com.example.IvasAPI;

import com.example.IvasAPI.exception.ResourceNotFoundException;
import com.example.IvasAPI.model.IvasEntity;
import com.example.IvasAPI.repository.IvasEntityRepository;
import com.example.IvasAPI.service.IvasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IvasServiceTest {

    @Mock
    private IvasEntityRepository ivasEntityRepository;

    @InjectMocks
    private IvasService ivasService;

    private IvasEntity testIvasEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);


        testIvasEntity = new IvasEntity();
        testIvasEntity.setId(1L);
        testIvasEntity.setBinNumber("1234567890123");
        testIvasEntity.setNid("1234567890");
        testIvasEntity.setPhoneNumber("+88012345678");
        testIvasEntity.setAreaOfService("Chattogram");
        testIvasEntity.setActive(true);
    }

    @Test
    public void testFindAllEntities() {
        List<IvasEntity> entities = Arrays.asList(testIvasEntity, new IvasEntity());

        when(ivasEntityRepository.findAll()).thenReturn(entities);

        List<IvasEntity> result = ivasService.findAllEntities();

        assertEquals(2, result.size());
        verify(ivasEntityRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllActiveEntities() {
        IvasEntity inactiveEntity = new IvasEntity();
        inactiveEntity.setActive(false);
        List<IvasEntity> entities = Arrays.asList(testIvasEntity, inactiveEntity);

        when(ivasEntityRepository.findAll()).thenReturn(entities);

        List<IvasEntity> result = ivasService.findAllActiveEntities();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
        verify(ivasEntityRepository, times(1)).findAll();
    }

    @Test
    public void testFindByBinNumber_Success() {
        when(ivasEntityRepository.findByBinNumber(testIvasEntity.getBinNumber())).thenReturn(Optional.of(testIvasEntity));

        IvasEntity result = ivasService.findByBinNumber(testIvasEntity.getBinNumber());

        assertNotNull(result);
        assertEquals(testIvasEntity.getBinNumber(), result.getBinNumber());
        verify(ivasEntityRepository, times(1)).findByBinNumber(testIvasEntity.getBinNumber());
    }

    @Test
    public void testFindByBinNumber_NotFound() {
        String binNumber = "1234567890123";

        when(ivasEntityRepository.findByBinNumber(binNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ivasService.findByBinNumber(binNumber));
        verify(ivasEntityRepository, times(1)).findByBinNumber(binNumber);
    }

    @Test
    public void testValidateEntity_Success() {
        when(ivasEntityRepository.existsByBinNumberAndNid(testIvasEntity.getBinNumber(), testIvasEntity.getNid())).thenReturn(true);

        boolean result = ivasService.validateEntity(testIvasEntity.getBinNumber(), testIvasEntity.getNid());

        assertTrue(result);
        verify(ivasEntityRepository, times(1)).existsByBinNumberAndNid(testIvasEntity.getBinNumber(), testIvasEntity.getNid());
    }

    @Test
    public void testCreateBin() {
        when(ivasEntityRepository.save(testIvasEntity)).thenReturn(testIvasEntity);

        IvasEntity result = ivasService.createBin(testIvasEntity);

        assertNotNull(result);
        verify(ivasEntityRepository, times(1)).save(testIvasEntity);
    }

    @Test
    public void testUpdateBin_Success() {
        when(ivasEntityRepository.existsById(testIvasEntity.getId())).thenReturn(true);
        when(ivasEntityRepository.save(testIvasEntity)).thenReturn(testIvasEntity);

        ResponseEntity<String> response = ivasService.updateBin(testIvasEntity, testIvasEntity.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Successfully", response.getBody());
        verify(ivasEntityRepository, times(1)).existsById(testIvasEntity.getId());
        verify(ivasEntityRepository, times(1)).save(testIvasEntity);
    }

    @Test
    public void testUpdateBin_NotFound() {
        Long id = 1L;

        when(ivasEntityRepository.existsById(id)).thenReturn(false);

        ResponseEntity<String> response = ivasService.updateBin(testIvasEntity, id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product with ID " + id + " not found", response.getBody());
        verify(ivasEntityRepository, times(1)).existsById(id);
    }

    @Test
    public void testDeleteBin_Success() {
        when(ivasEntityRepository.findByBinNumber(testIvasEntity.getBinNumber())).thenReturn(Optional.of(testIvasEntity));

        ResponseEntity<String> response = ivasService.deleteBin(testIvasEntity.getBinNumber());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BIN with BIN number  " + testIvasEntity.getBinNumber() + " Successfully deleted", response.getBody());
        verify(ivasEntityRepository, times(1)).findByBinNumber(testIvasEntity.getBinNumber());
        verify(ivasEntityRepository, times(1)).delete(testIvasEntity);
    }

    @Test
    public void testDeleteBin_NotFound() {
        String binNumber = "1234567890123";

        when(ivasEntityRepository.findByBinNumber(binNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ivasService.deleteBin(binNumber));
        verify(ivasEntityRepository, times(1)).findByBinNumber(binNumber);
    }

    @Test
    public void testDeactivateBin_Success() {
        when(ivasEntityRepository.findByBinNumber(testIvasEntity.getBinNumber())).thenReturn(Optional.of(testIvasEntity));
        when(ivasEntityRepository.save(testIvasEntity)).thenReturn(testIvasEntity);

        ResponseEntity<String> response = ivasService.deactivateBin(testIvasEntity.getBinNumber());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BIN with BIN number  " + testIvasEntity.getBinNumber() + " Successfully deactivated", response.getBody());
        assertFalse(testIvasEntity.isActive());
        verify(ivasEntityRepository, times(1)).findByBinNumber(testIvasEntity.getBinNumber());
        verify(ivasEntityRepository, times(1)).save(testIvasEntity);
    }

    @Test
    public void testDeactivateBin_NotFound() {
        String binNumber = "1234567890123";

        when(ivasEntityRepository.findByBinNumber(binNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ivasService.deactivateBin(binNumber));
        verify(ivasEntityRepository, times(1)).findByBinNumber(binNumber);
    }

}
