package app.web;

import app.service.berth.ManagementBerthApplicationService;
import app.service.berth.dto.BerthApplicationDto;
import app.service.berth.dto.BerthApplicationFilter;
import app.service.berth.dto.management.BerthApplicationDecision;
import app.service.berth.dto.management.ChangeApplicationStatusResp;
import app.service.berth.dto.management.StartApplicationResp;
import app.web.dto.response.ListCount;
import app.web.dto.response.ObjectResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
