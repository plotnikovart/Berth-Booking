package ru.hse.coursework.berth.service.file;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.FileInfo;
import ru.hse.coursework.berth.database.repository.FileInfoRepository;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileInfoService {

    private static final int CODE_LENGTH = 16;

    private final TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();
    private final FileInfoRepository fileInfoRepository;
//    private final Ehcache ehcache;

    public FileInfoDto save(String fileName) {
        Generators.timeBasedGenerator();
        var fileInfo = new FileInfo()
                .setId(uuidGenerator.generate())
                .setCode(RandomStringUtils.random(CODE_LENGTH, true, true))
                .setName(extractName(fileName))
                .setExt(extractExt(fileName));

        fileInfoRepository.save(fileInfo);

        return toDto(fileInfo);
    }

    public FileInfoDto get(UUID fileId) {
        FileInfo fileInfo = fileInfoRepository.findById(fileId).orElseThrow(NotFoundException::new);
        return toDto(fileInfo);
    }

    public List<FileInfoDto> get(List<UUID> filesIds) {
        if (filesIds.isEmpty()) {
            return List.of();
        }

        return List.of();
//        Map<UUID, FileInfo> uuidFileInfoMap = StreamEx.of(fileInfoRepository.findAllById(filesIds)).toMap(FileInfo::getId, Function.identity());
    }

    public FileInfoDto getWithCheck(UUID fileId, String code) throws NotFoundException {
        FileInfo fileInfo = fileInfoRepository.findById(fileId).orElseThrow(NotFoundException::new);
        if (!fileInfo.getCode().equals(code)) {
            throw new NotFoundException();
        }

        return toDto(fileInfo);
    }


    private FileInfoDto toDto(FileInfo fileInfo) {
        return new FileInfoDto(
                fileInfo.getId(),
                fileInfo.getName() + fileInfo.getExtWithDot(),
                String.format("/api/files/%s/%s", fileInfo.getCode(), fileInfo.getId().toString())
        );
    }


    private String extractName(String fileName) {
        return fileName.contains(".") ? StringUtils.substringBeforeLast(fileName, ".") : fileName;
    }

    private String extractExt(String fileName) {
        return fileName.contains(".") ? StringUtils.substringAfterLast(fileName, ".") : null;
    }
}
