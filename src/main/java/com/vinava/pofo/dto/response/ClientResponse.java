package com.vinava.pofo.dto.response;

import com.vinava.pofo.dto.Address;
import com.vinava.pofo.dto.ContactDetail;
import com.vinava.pofo.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
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

    private static List<ClientResponse> from(List<Client> clients) {
        List<ClientResponse> clientResponses = new ArrayList<>();
        for (Client client: clients) {
            clientResponses.add(from(client, false));
        }
        return clientResponses;
    }

    public static ResponseEntity<List<ClientResponse>> getResponseEntityFrom(List<Client> clients) {
        try {
            List<ClientResponse> clientResponses = from(clients);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(clientResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(clientResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of clients: {}", clients, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
