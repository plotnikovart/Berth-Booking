package ru.hse.coursework.berth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.berth.ManagementBerthApplicationService;
import ru.hse.coursework.berth.service.berth.dto.BerthApplicationDto;
import ru.hse.coursework.berth.service.berth.dto.BerthApplicationFilter;
import ru.hse.coursework.berth.service.berth.dto.management.BerthApplicationDecision;
import ru.hse.coursework.berth.service.berth.dto.management.ChangeApplicationStatusResp;
import ru.hse.coursework.berth.service.berth.dto.management.StartApplicationResp;
import ru.hse.coursework.berth.service.dto.ListCount;
import ru.hse.coursework.berth.web.dto.response.ObjectResp;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementBerthApplicationService managementBerthApplicationService;

    @PostMapping("/berths/applications")
    public ObjectResp<ListCount<BerthApplicationDto.Resp>> getApplications(@RequestBody BerthApplicationFilter filter) {
        ListCount<BerthApplicationDto.Resp> resp = managementBerthApplicationService.getApplications(filter);
        return new ObjectResp<>(resp);
    }

    @PostMapping("/berths/applications/{id}/start")
    public ObjectResp<StartApplicationResp> startWorkWithApplication(@PathVariable Long id) {
        StartApplicationResp resp = managementBerthApplicationService.startApplication(id);
        return new ObjectResp<>(resp);
    }

    @PostMapping("/berths/applications/{id}/approve")
    public ObjectResp<ChangeApplicationStatusResp> approveApplication(@PathVariable Long id, @RequestBody BerthApplicationDecision decision) {
        ChangeApplicationStatusResp resp = managementBerthApplicationService.approveApplication(id, decision.getDecision());
        return new ObjectResp<>(resp);
    }

    @PostMapping("/berths/applications/{id}/reject")
    public ObjectResp<ChangeApplicationStatusResp> rejectApplication(@PathVariable Long id, @RequestBody BerthApplicationDecision decision) {
        ChangeApplicationStatusResp resp = managementBerthApplicationService.rejectApplication(id, decision.getDecision());
        return new ObjectResp<>(resp);
    }
}
