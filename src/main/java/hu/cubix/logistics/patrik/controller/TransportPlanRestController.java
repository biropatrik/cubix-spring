package hu.cubix.logistics.patrik.controller;

import hu.cubix.logistics.patrik.dto.DelayDto;
import hu.cubix.logistics.patrik.service.TransportPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transportPlans")
public class TransportPlanRestController {

    @Autowired
    TransportPlanService transportPlanService;

    @PostMapping("/{id}/delay")
    public void addDelay(@PathVariable long id, @RequestBody DelayDto delayDto) {
        transportPlanService.addDelay(id, delayDto);
    }
}
