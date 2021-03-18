package com.vinava.pofo.dto.request;

import com.vinava.pofo.dto.Address;
import com.vinava.pofo.dto.ContactDetail;
import com.vinava.pofo.enumeration.ClientType;
import com.vinava.pofo.model.Client;
import com.vinava.pofo.util.DateUtil;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
public class ClientRequest {

    @NotBlank
    @Size(max = 100, message = "Client name should be of maximum length of 100")
    private String name;

    private String logoImage;

    private Boolean active;

    private Date subscriptionEndDate;

    private Address address;

    private ContactDetail contactDetail;

    @NotNull
    private ClientType clientType;

    @Size(min = 15, max = 15, message = "Invalid GSTIN")
    private String gstin;

    public Client from() {
        return Client.builder()
                .name(this.getName())
                .subscriptionEndDate(this.subscriptionEndDate)
                .active(this.isActive())
                .address(this.address)
                .contactDetail(this.contactDetail)
                .gstin(this.gstin)
                .clientType(this.clientType)
                .build();
    }

    public Client updateClientFrom(Client client) {
        client.setName(this.getName());
        if (this.isActive() && !client.isActive()) {
            client.setActivationDate(DateUtil.getCurrentDate());
        }
        client.setActive(this.isActive());
        client.setAddress(this.getAddress());
        client.setContactDetail(this.getContactDetail());
        client.setGstin(this.getGstin());
        client.setLogoImage(this.getLogoImage());
        client.setClientType(this.getClientType());
        return client;
    }

    private boolean isActive() {
        if (this.active == null) {
            return true;
        }
        return this.active;
    }

}
