package hu.cubix.logistics.patrik.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.cubix.logistics.patrik.dto.DelayDto;
import hu.cubix.logistics.patrik.exception.MilestoneIsNotInTheTransportPlanException;
import hu.cubix.logistics.patrik.exception.MilestoneNotFoundException;
import hu.cubix.logistics.patrik.exception.TransportPlanNotFoundException;
import hu.cubix.logistics.patrik.model.Address;
import hu.cubix.logistics.patrik.model.Milestone;
import hu.cubix.logistics.patrik.model.Section;
import hu.cubix.logistics.patrik.model.TransportPlan;
import hu.cubix.logistics.patrik.repository.AddressRepository;
import hu.cubix.logistics.patrik.repository.MilestoneRepository;
import hu.cubix.logistics.patrik.repository.SectionRepository;
import hu.cubix.logistics.patrik.repository.TransportPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TransportPlanRestControllerIT {

    private static final String API_TRANSPORT_PLANS_DELAY = "/api/transportPlans/{id}/delay";
    private static final String TRANSPORT_PLAN_NOT_FOUND_EXP = "TransportPlan does not exists!";
    private static final String MILESTONE_NOT_FOUND_EXP = "Milestone does not exists!";
    private static final String MILESTONE_NOT_IN_THE_TRANSPORT_PLAN_EXP = "The milestone is not included in the current transport plan!";
    private static final int DECREASED_PERCENTAGE = 10;
    private static final short DELAY_IN_MINUTES = 30;

    private static long transportPlanId;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    MilestoneRepository milestoneRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    TransportPlanRepository transportPlanRepository;

    @BeforeEach
    void init() {
        clearDb();
        initDb();
    }

    @Test
    void testAddDelayToStart() {
        TransportPlan transportPlan = transportPlanRepository.findByIdWithFetch(transportPlanId).get();
        Section section = sectionRepository.findByTransportPlanIdAndOrderOfSection(transportPlanId, (short) 0).get();
        Milestone milestoneStart = section.getStart();
        Milestone milestoneEnd = section.getEnd();

        int income = transportPlan.getIncome();
        DelayDto delayDto = new DelayDto(milestoneStart.getId(), DELAY_IN_MINUTES);

        LocalDateTime expectedStartTime = milestoneStart.getPlannedTime().plusMinutes(DELAY_IN_MINUTES);
        LocalDateTime expectedEndTime = milestoneEnd.getPlannedTime().plusMinutes(DELAY_IN_MINUTES);

        webTestClient
                .post()
                .uri(API_TRANSPORT_PLANS_DELAY, transportPlanId)
                .bodyValue(delayDto)
                .exchange()
                .expectStatus().isOk();

        transportPlan  = transportPlanRepository.findById(transportPlanId).get();
        milestoneStart = milestoneRepository.findById(milestoneStart.getId()).get();
        milestoneEnd = milestoneRepository.findById(milestoneEnd.getId()).get();

        assertEquals(expectedStartTime, milestoneStart.getPlannedTime());
        assertEquals(expectedEndTime, milestoneEnd.getPlannedTime());
        assertEquals(getDecreasedIncome(income, DECREASED_PERCENTAGE), transportPlan.getIncome());
    }

    @Test
    void testAddDelayToEnd() {
        TransportPlan transportPlan = transportPlanRepository.findByIdWithFetch(transportPlanId).get();
        Section section = sectionRepository.findByTransportPlanIdAndOrderOfSection(transportPlanId, (short) 0).get();
        Milestone milestoneEnd = section.getEnd();

        Section nextSection = sectionRepository.findByTransportPlanIdAndOrderOfSection(transportPlanId, (short) 1).get();
        Milestone nextMilestoneStart = nextSection.getStart();

        int income = transportPlan.getIncome();
        DelayDto delayDto = new DelayDto(milestoneEnd.getId(), DELAY_IN_MINUTES);

        LocalDateTime expectedEndTime = milestoneEnd.getPlannedTime().plusMinutes(DELAY_IN_MINUTES);
        LocalDateTime expectedNextStartTime = nextMilestoneStart.getPlannedTime().plusMinutes(DELAY_IN_MINUTES);

        webTestClient
                .post()
                .uri(API_TRANSPORT_PLANS_DELAY, transportPlanId)
                .bodyValue(delayDto)
                .exchange()
                .expectStatus().isOk();

        transportPlan  = transportPlanRepository.findById(transportPlanId).get();
        milestoneEnd = milestoneRepository.findById(milestoneEnd.getId()).get();
        nextMilestoneStart = milestoneRepository.findById(nextMilestoneStart.getId()).get();

        assertEquals(expectedEndTime, milestoneEnd.getPlannedTime());
        assertEquals(expectedNextStartTime, nextMilestoneStart.getPlannedTime());
        assertEquals(getDecreasedIncome(income, DECREASED_PERCENTAGE), transportPlan.getIncome());
    }

    @Test
    void testAddDelayTransportPlanNotFound() {
        clearDb();

        DelayDto delayDto = new DelayDto(1L, DELAY_IN_MINUTES);

        webTestClient
                .post()
                .uri(API_TRANSPORT_PLANS_DELAY, transportPlanId)
                .bodyValue(delayDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(HashMap.class)
                .consumeWith(exp -> assertEquals(TRANSPORT_PLAN_NOT_FOUND_EXP,
                        Objects.requireNonNull(exp.getResponseBody()).get(TransportPlanNotFoundException.class.getSimpleName())));
    }

    @Test
    void testAddDelayMilestoneNotFound() {
        clearDb();
        long transportPlanId = transportPlanRepository.save(new TransportPlan()).getId();

        DelayDto delayDto = new DelayDto(1L, DELAY_IN_MINUTES);

        webTestClient
                .post()
                .uri(API_TRANSPORT_PLANS_DELAY, transportPlanId)
                .bodyValue(delayDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(HashMap.class)
                .consumeWith(exp -> assertEquals(MILESTONE_NOT_FOUND_EXP,
                        Objects.requireNonNull(exp.getResponseBody()).get(MilestoneNotFoundException.class.getSimpleName())));
    }

    @Test
    void testAddDelayMilestoneIsNotInTheTransportPlan() {
        long transportPlanId = transportPlanRepository.save(new TransportPlan()).getId();
        long milestoneId = milestoneRepository.findAll().get(0).getId();

        DelayDto delayDto = new DelayDto(milestoneId, DELAY_IN_MINUTES);

        webTestClient
                .post()
                .uri(API_TRANSPORT_PLANS_DELAY, transportPlanId)
                .bodyValue(delayDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(HashMap.class)
                .consumeWith(exp -> assertEquals(MILESTONE_NOT_IN_THE_TRANSPORT_PLAN_EXP,
                        Objects.requireNonNull(exp.getResponseBody()).get(MilestoneIsNotInTheTransportPlanException.class.getSimpleName())));
    }

    private int getDecreasedIncome(int income, int percentage) {
        return (int) (income / 100f * (100 - percentage));
    }

    private void clearDb() {
        sectionRepository.deleteAllInBatch();
        transportPlanRepository.deleteAllInBatch();
        milestoneRepository.deleteAllInBatch();
        addressRepository.deleteAllInBatch();
    }

    private void initDb() {
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

        transportPlanId = transportPlan2.getId();
    }
}
