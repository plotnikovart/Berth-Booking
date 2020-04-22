package app.web;

import app.config.security.OperationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/test")
    public String confirm() {
        return "AccountId=" + OperationContext.accountId();
    }
}
