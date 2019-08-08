package app.service.facade;

import app.database.entity.Convenience;
import app.database.repository.ConvenienceRepository;
import app.web.dto.ConvenienceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConvenienceFacade {

    private final ConvenienceRepository convenienceRepository;

    public List<ConvenienceDto> getAll() {
        return convenienceRepository.findAll().stream()
                .map(Convenience::getDto)
                .collect(Collectors.toList());
    }

}
