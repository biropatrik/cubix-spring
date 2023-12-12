package hu.cubix.logistics.patrik.service;

import hu.cubix.logistics.patrik.model.Address;
import hu.cubix.logistics.patrik.model.Milestone;
import hu.cubix.logistics.patrik.model.Section;
import hu.cubix.logistics.patrik.model.TransportPlan;
import hu.cubix.logistics.patrik.repository.AddressRepository;
import hu.cubix.logistics.patrik.repository.MilestoneRepository;
import hu.cubix.logistics.patrik.repository.SectionRepository;
import hu.cubix.logistics.patrik.repository.TransportPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InitDbServiceImpl implements InitDbService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    MilestoneRepository milestoneRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    TransportPlanRepository transportPlanRepository;

    @Override
    @Transactional
    public void clearDb() {
        sectionRepository.deleteAllInBatch();
        transportPlanRepository.deleteAllInBatch();
        milestoneRepository.deleteAllInBatch();
        addressRepository.deleteAllInBatch();
    }

    @Override
    @Transactional
    public void initDb() {
        Address address = addressRepository.save(
                new Address("HU", "Eger", "Test ut 2", "3300", 10, 15, 25));

        Milestone start1 = milestoneRepository.save(new Milestone(address, LocalDateTime.now().plusDays(5)));
        Milestone end1 = milestoneRepository.save(new Milestone(address, LocalDateTime.now().plusDays(10)));

        Milestone start2 = milestoneRepository.save(new Milestone(address, LocalDateTime.now().plusDays(20)));
        Milestone end2 = milestoneRepository.save(new Milestone(address, LocalDateTime.now().plusDays(30)));

        Milestone start3 = milestoneRepository.save(new Milestone(address, LocalDateTime.now().plusDays(30)));
        Milestone end3 = milestoneRepository.save(new Milestone(address, LocalDateTime.now().plusDays(35)));

        TransportPlan transportPlan1 = transportPlanRepository.save(new TransportPlan(9500));
        TransportPlan transportPlan2 = transportPlanRepository.save(new TransportPlan(10000));

        Section section1 = sectionRepository.save(new Section(start1, end1, (short) 0, transportPlan1));

        Section section2_0 = sectionRepository.save(new Section(start2, end2, (short) 0, transportPlan2));
        Section section2_1 = sectionRepository.save(new Section(start3, end3, (short) 1, transportPlan2));
    }
}
