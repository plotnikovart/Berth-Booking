package app.service.account.dto;

import app.database.entity.enums.AccountKind;
import app.database.entity.enums.AccountRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
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
    private List<Object> permissions = new ArrayList<>();
}
