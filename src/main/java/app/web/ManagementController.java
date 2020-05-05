package app.web;

import app.service.berth.ManagementBerthApplicationService;
import app.service.berth.dto.BerthApplicationDto;
import app.service.berth.dto.BerthApplicationFilter;
import app.web.dto.response.ListCount;
import app.web.dto.response.ObjectResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
