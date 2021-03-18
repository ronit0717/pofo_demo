package com.vinava.pofo.dto.response;

import com.vinava.pofo.model.Store;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Data
@Builder
@Slf4j
public class StoreResponse {

    private long id;
    private long clientId;
    private String name;
    private long locationId;
    private String description;
    private String address;
    private String gstin;
    private Date createdOn;
    private Date updatedOn;

    public static StoreResponse from(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .clientId(store.getClientId())
                .name(store.getName())
                .description(store.getDescription())
                .address(store.getAddress())
                .gstin(store.getGstin())
                .createdOn(store.getCreatedOn())
                .updatedOn(store.getUpdatedOn())
                .build();
    }

    private static List<StoreResponse> from(List<Store> stores) {
        List<StoreResponse> storeResponses = new LinkedList<>();
        for (Store store : stores) {
            storeResponses.add(from(store));
        }
        return storeResponses;
    }

    public static ResponseEntity<List<StoreResponse>> getResponseEntityFrom(List<Store> stores) {
        try {
            List<StoreResponse> storeResponses = from(stores);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(storeResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(storeResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of stores: {}", stores, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
