package hu.cubix.logistics.patrik.service;

import hu.cubix.logistics.patrik.config.LogisticsConfigurationProperties;
import hu.cubix.logistics.patrik.config.LogisticsConfigurationProperties.Income.Decrease;
import hu.cubix.logistics.patrik.dto.DelayDto;
import hu.cubix.logistics.patrik.exception.MilestoneIsNotInTheTransportPlanException;
import hu.cubix.logistics.patrik.exception.MilestoneNotFoundException;
import hu.cubix.logistics.patrik.exception.TransportPlanNotFoundException;
import hu.cubix.logistics.patrik.model.Milestone;
import hu.cubix.logistics.patrik.model.Section;
import hu.cubix.logistics.patrik.model.TransportPlan;
import hu.cubix.logistics.patrik.repository.MilestoneRepository;
import hu.cubix.logistics.patrik.repository.SectionRepository;
import hu.cubix.logistics.patrik.repository.TransportPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransportPlanServiceImpl implements TransportPlanService {

    @Autowired
    TransportPlanRepository transportPlanRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    MilestoneRepository milestoneRepository;

    @Autowired
    LogisticsConfigurationProperties config;

    @Override
    @Transactional
    public void addDelay(long transportPlanId, DelayDto delayDto) {
        TransportPlan transportPlan = transportPlanRepository.findById(transportPlanId)
                .orElseThrow(TransportPlanNotFoundException::new);

        Milestone milestone = milestoneRepository.findById(delayDto.getMilestoneId())
                .orElseThrow(MilestoneNotFoundException::new);

        if (!transportPlanRepository.doesMilestoneExistsInTransportPlanByIds(transportPlanId, delayDto.getMilestoneId())) {
            throw new MilestoneIsNotInTheTransportPlanException();
        }

        milestone.setPlannedTime(milestone.getPlannedTime().plusMinutes(delayDto.getDelayInMinutes()));

        Section section = sectionRepository.findByMilestoneId(delayDto.getMilestoneId()).get();
        if (section.getStart().getId() == delayDto.getMilestoneId()) {
            Milestone endMilestone = section.getEnd();
            endMilestone.setPlannedTime(endMilestone.getPlannedTime().plusMinutes(delayDto.getDelayInMinutes()));
        } else {
            Section nextSection = sectionRepository.findByTransportPlanIdAndOrderOfSection(transportPlanId,
                    (short) (section.getOrderOfSection() + 1)).get();

            Milestone startMilestone = nextSection.getStart();
            startMilestone.setPlannedTime(startMilestone.getPlannedTime().plusMinutes(delayDto.getDelayInMinutes()));
        }

        transportPlan.setIncome(getDecreasedIncome(transportPlan.getIncome(), delayDto.getDelayInMinutes()));
    }

    private int getDecreasedIncome(int income, int minutes) {
        return (int) (income / 100f * (100 - getDecreasePercentage(minutes)));
    }

    private int getDecreasePercentage(int minutes) {
        int percentage = 0;

        for (Decrease decrease : config.getIncome().getDecrease()) {
            if (minutes >= decrease.getMinutes()) {
                percentage = decrease.getPercentage();
            }
        }

        return percentage;
    }
}
