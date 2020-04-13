package com.vinava.pofo.dto.request;

import com.vinava.pofo.dto.Address;
import com.vinava.pofo.dto.ContactDetail;
import com.vinava.pofo.model.Client;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
public class ClientRequest {

    @NotBlank
    @Size(max = 100, message = "Client name should be of maximum length of 100")
    private String name;

    private Long logoImageId;

    private Boolean active;

    private Date subscriptionEndDate;

    private Address address;

    private ContactDetail contactDetail;

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
                .build();
    }

    public Client updateClientFrom(Client client) {
        client.setName(this.getName());
        client.setActive(this.isActive());
        client.setAddress(this.getAddress());
        client.setContactDetail(this.getContactDetail());
        client.setGstin(this.getGstin());
        client.setLogoImageId(this.getLogoImageId());
        return client;
    }

    public boolean isActive() {
        if (active == null) {
            return true;
        }
        return false;
    }

}
