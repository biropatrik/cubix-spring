package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.dto.VacationExampleDto;
import hu.cubix.hr.patrik.dto.VacationInsertDto;
import hu.cubix.hr.patrik.exception.EmployeeNotFoundException;
import hu.cubix.hr.patrik.exception.VacationAlreadyExistsException;
import hu.cubix.hr.patrik.exception.VacationAlreadyProcessedException;
import hu.cubix.hr.patrik.exception.VacationNotFoundException;
import hu.cubix.hr.patrik.model.Employee;
import hu.cubix.hr.patrik.model.Vacation;
import hu.cubix.hr.patrik.model.VacationStatus;
import hu.cubix.hr.patrik.repository.EmployeeRepository;
import hu.cubix.hr.patrik.repository.VacationRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VacationServiceImpl implements VacationService {

    @Autowired
    VacationRepository vacationRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public Vacation create(VacationInsertDto vacationDto) {
        if (vacationRepository.existsById(vacationDto.getRequesterId())) {
            throw new VacationAlreadyExistsException();
        }
        Employee employee = employeeRepository.findById(vacationDto.getRequesterId())
                .orElseThrow(EmployeeNotFoundException::new);

        return vacationRepository.save(
                new Vacation(vacationDto.getStartDate(), vacationDto.getEndDate(), VacationStatus.NEW, employee));
    }

    @Override
    public List<Vacation> findAll() {
        return vacationRepository.findAll();
    }

    @Override
    public Vacation findById(long id) {
        return vacationRepository.findById(id).orElseThrow(VacationNotFoundException::new);
    }

    @Override
    @Transactional
    public Vacation modifyVacation(long vacationId, VacationInsertDto vacationDto) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(VacationNotFoundException::new);

        if (!vacation.getStatus().equals(VacationStatus.NEW)) {
            throw new VacationAlreadyProcessedException();
        }

        vacation.setStartDate(vacationDto.getStartDate());
        vacation.setEndDate(vacationDto.getEndDate());
        return vacation;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(VacationNotFoundException::new);

        if (!vacation.getStatus().equals(VacationStatus.NEW)) {
            throw new VacationAlreadyProcessedException();
        }

        vacationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Vacation manageVacation(long vacationId, VacationStatus vacationStatus, long managerOfEmployee) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(VacationNotFoundException::new);
        Employee employee = employeeRepository.findById(managerOfEmployee)
                .orElseThrow(EmployeeNotFoundException::new);

        vacation.setStatus(vacationStatus);
        vacation.setManagerOfEmployee(employee);
        return vacation;
    }

    @Override
    public Page<Vacation> findVacationsByExample(VacationExampleDto example, Pageable pageable) {

        if (example == null) {
            return vacationRepository.findAll(pageable);
        }

        Specification<Vacation> specification = Specification.where(null);

        if (example.getVacationStatus() != null) {
            specification =
                    specification.and(VacationSpecifications.vacationStatusEquals(example.getVacationStatus()));
        }

        if (StringUtils.isNotBlank(example.getRequesterNamePrefix())) {
            specification =
                    specification.and(VacationSpecifications.requesterNameStartsWith(example.getRequesterNamePrefix()));
        }

        if (StringUtils.isNotBlank(example.getManagerNamePrefix())) {
            specification =
                    specification.and(VacationSpecifications.managerNameStartsWith(example.getManagerNamePrefix()));
        }

        if (example.getInsertedDateFrom() != null && example.getInsertedDateTo() != null) {
            specification = specification.and(VacationSpecifications.insertedTimeBetween(
                    example.getInsertedDateFrom(),
                    example.getInsertedDateTo()));
        }

        if (example.getStartDate() != null || example.getEndDate() != null) {
            specification = specification.and(VacationSpecifications.vacationIntervalIn(
                    example.getStartDate(),
                    example.getEndDate()));
        }

        return vacationRepository.findAll(specification, pageable);
    }
}
