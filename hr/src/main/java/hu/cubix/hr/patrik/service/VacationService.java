package hu.cubix.hr.patrik.service;

import hu.cubix.hr.patrik.dto.VacationExampleDto;
import hu.cubix.hr.patrik.dto.VacationInsertDto;
import hu.cubix.hr.patrik.model.Vacation;
import hu.cubix.hr.patrik.model.VacationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VacationService {

    Vacation create(VacationInsertDto vacation);

    List<Vacation> findAll();

    Vacation findById(long id);

    void delete(long id);

    Vacation modifyVacation(long vacationId, VacationInsertDto vacation);

    Vacation manageVacation(long vacationId, VacationStatus vacationStatus);

    Page<Vacation> findVacationsByExample(VacationExampleDto exampleDto, Pageable pageable);
}
