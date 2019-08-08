package app.web;

import app.service.facade.ConvenienceFacade;
import app.web.dto.ConvenienceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conveniences")
@RequiredArgsConstructor
public class ConvenienceController {

    private final ConvenienceFacade convenienceFacade;

    @GetMapping
    public List<ConvenienceDto> getAll() {
        return convenienceFacade.getAll();
    }
}
