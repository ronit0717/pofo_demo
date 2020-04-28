package com.vinava.pofo.dto.request;

import com.vinava.pofo.enumeration.CartStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCartStatusRequest {

    @NotNull
    private Long cartId;

    @NotNull
    private CartStatus cartStatus;

}
