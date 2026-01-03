package com.pm.patient_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.IPatientRepository;

@Service
public class PatientService {
    private IPatientRepository iPatientRepository;

    public PatientService(IPatientRepository iPatientRepository) {
        this.iPatientRepository = iPatientRepository;
    }

    public List<PatientResponseDTO> getPatient(){
        List<Patient> patients = iPatientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(PatientMapper::toDTO)
                .toList();

        // 22 - 24 similar to 27 - 31
        // List<PatientResponseDTO> patientDTOs = new ArrayList<>();
        // for (Patient patient : patients) {
        //     patientDTOs.add(PatientMapper.toDTO(patient));
        // }
        return patientResponseDTOs;

        // Can simply return like this also
        // return patients.stream()
        //         .map(PatientMapper::toDTO)
        //         .toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(iPatientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("Email already exists: " 
            + patientRequestDTO.getEmail());
        }
        
        Patient newPatient = iPatientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        // Get the patient by id, if not throw exception
        Patient patient = iPatientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id.toString()));
        
        if(iPatientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("Email already exists: " 
            + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth() != null ? 
            java.time.LocalDate.parse(patientRequestDTO.getDateOfBirth()) : patient.getDateOfBirth());
        
        Patient updatedPatient = iPatientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }
}
