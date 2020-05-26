package ru.hse.coursework.berth.service.account.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.database.entity.enums.AccountKind;
import ru.hse.coursework.berth.database.entity.enums.AccountRole;

import java.util.List;

@Data
public class AccountInfo {

    @ApiModelProperty(required = true)
    private Long id;
    @ApiModelProperty(required = true)
    private AccountKind kind;
    private String email;

    @ApiModelProperty(required = true)
    private List<AccountRole> roles;

    @ApiModelProperty(required = true)
    private Boolean hasBerths;
    @ApiModelProperty(required = true)
    private Boolean hasShips;
}
