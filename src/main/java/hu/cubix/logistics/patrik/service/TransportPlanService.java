package hu.cubix.logistics.patrik.service;

import hu.cubix.logistics.patrik.dto.DelayDto;

public interface TransportPlanService {

    public void addDelay(long transportPlanId, DelayDto delayDto);
}
