package app.service.converters.impl;

import app.database.entity.Account;
import app.database.entity.BerthApplication;
import app.service.berth.dto.BerthApplicationDto;
import app.service.converters.AbstractConverter;
import app.service.file.FileInfoService;
import app.service.file.dto.FileInfoDto;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class BerthApplicationConverter extends AbstractConverter<BerthApplication, BerthApplicationDto.Resp, BerthApplicationDto.Req> {

    private final FileInfoService fileInfoService;

    @Override
    public BerthApplicationDto.Resp toDto(BerthApplicationDto.Resp dto, BerthApplication entity) {
        List<FileInfoDto> attachments = StreamEx.of(entity.getAttachements())
                .map(fileInfoService::get)
                .toList();

        return (BerthApplicationDto.Resp) dto
                .setId(entity.getId())
                .setBerthId(entity.getBerth().getId())
                .setCreatedAt(entity.getCreationDate())
                .setApplicantId(entity.getApplicant().getId())
                .setStatus(entity.getStatus())
                .setDecision(entity.getDecision())
                .setModeratorId(ofNullable(entity.getModerator()).map(Account::getId).orElse(null))
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setAttachments(attachments);
    }

    @Override
    public BerthApplication toEntity(BerthApplication entity, BerthApplicationDto.Req dto) {
        List<UUID> attachments = ofNullable(dto.getAttachments())
                .map(it ->
                        StreamEx.of(it)
                                .map(FileInfoDto::getFileId)
                                .peek(fileInfoService::get)
                                .toList()
                )
                .orElse(null);

        return entity
                .setDescription(dto.getDescription())
                .setAttachments(attachments);
    }
}
