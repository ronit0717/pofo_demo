package com.vinava.pofo.dto.response;

import com.vinava.pofo.dto.Address;
import com.vinava.pofo.dto.ContactDetail;
import com.vinava.pofo.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {

    private Long id;
    private String name;
    private String slug;
    private boolean active;
    private boolean userExists;
    private Long logoImageId;
    private Date subscriptionEndDate;
    private String gstin;
    private Address address;
    private ContactDetail contactDetail;

    public static ClientResponse from(Client client, boolean newUser) {
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .slug(client.getSlug())
                .active(client.isActive())
                .logoImageId(client.getLogoImageId())
                .userExists(!newUser)
                .subscriptionEndDate(client.getSubscriptionEndDate())
                .address(client.getAddress())
                .contactDetail(client.getContactDetail())
                .gstin(client.getGstin())
                .build();
    }

    public static List<ClientResponse> from(List<Client> clients) {
        List<ClientResponse> clientResponses = new ArrayList<>();
        for (Client client: clients) {
            clientResponses.add(from(client, false));
        }
        return clientResponses;
    }

}
