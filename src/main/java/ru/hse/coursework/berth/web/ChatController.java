package ru.hse.coursework.berth.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.service.chat.ChatFacade;
import ru.hse.coursework.berth.service.chat.dto.ChatDto;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;
import ru.hse.coursework.berth.service.chat.dto.OffsetDto;
import ru.hse.coursework.berth.web.dto.resp.ListResp;
import ru.hse.coursework.berth.web.dto.resp.ObjectResp;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatFacade chatFacade;

    @PostMapping
    ObjectResp<ChatDto> startChat(@RequestBody StartChatReq body) {
        var resp = chatFacade.startChat(body.getAccountId());
        return new ObjectResp<>(resp);
    }

    @GetMapping
    ListResp<ChatDto> getAllMyChats() {
        var resp = chatFacade.getChats();
        return new ListResp<>(resp);
    }

    @GetMapping("{chatId}")
    ObjectResp<ChatDto> getChat(@PathVariable Long chatId) {
        var resp = chatFacade.getChat(chatId);
        return new ObjectResp<>(resp);
    }

    @GetMapping("{chatId}/messages")
    ListResp<MessageDto.Resp> getChatMessages(@PathVariable Long chatId,
                                              @RequestParam Long offsetStart,
                                              @RequestParam Long offsetEnd) {
        var offsetDto = new OffsetDto()
                .setOffsetStart(offsetStart)
                .setOffsetEnd(offsetEnd);

        var resp = chatFacade.getMessages(chatId, offsetDto);
        return new ListResp<>(resp);
    }


    @Data
    public static class StartChatReq {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @ApiModelProperty(required = true)
        private Long accountId;
    }
}
