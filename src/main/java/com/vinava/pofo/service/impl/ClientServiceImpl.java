package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.ClientRepository;
import com.vinava.pofo.dto.request.ClientRequest;
import com.vinava.pofo.dto.response.ClientResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.exception.ResourceNotFoundException;
import com.vinava.pofo.model.Client;
import com.vinava.pofo.service.ClientService;
import com.vinava.pofo.service.helper.SlugGenerationService;
import com.vinava.pofo.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private SlugGenerationService slugGenerationService;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientResponse createClient(ClientRequest clientRequest) {
        log.debug("Starting createClient with request: {}", clientRequest);
        Optional<Client> clientOptional = clientRepository.
                findByNameAndAddressPincode(clientRequest.getName(), clientRequest.getAddress().getPincode());
        if (clientOptional.isPresent()) {
            log.debug("Client already present with name : {} and pincode: {}",
                    clientRequest.getName(), clientRequest.getAddress().getPincode());
            return ClientResponse.from(clientOptional.get(), false);
        }
        Client client = clientRequest.from();
        client.setSlug(generateClientSlug(clientRequest.getName()));
        client = clientRepository.saveAndFlush(client);
        log.debug("Returning from createClient, response: {}", client);
        return ClientResponse.from(client, true);
    }

    @Override
    public ClientResponse updateClient(ClientRequest clientRequest) throws ResourceNotFoundException, ProcessException {
        log.debug("Updating client with id: {}", clientRequest.getId());
        Optional<Client> clientOptional = clientRepository.findById(clientRequest.getId());
        if (!clientOptional.isPresent()) {
            log.error("No client found with id: {}", clientRequest.getId());
            throw new ResourceNotFoundException("Client", "id", clientRequest.getId());
        }
        Client client = clientOptional.get();
        client = clientRequest.updateClientFrom(client);
        clientOptional = clientRepository.
                findByNameAndAddressPincode(client.getName(), client.getAddress().getPincode());
        if (clientOptional.isPresent()) {
            log.error("Client already present with name : {} and pincode: {}",
                    clientRequest.getName(), clientRequest.getAddress().getPincode());
            String errorMessage = String.format("Client already present with name %s and with pincode %s",
                    clientRequest.getName(), clientRequest.getAddress().getPincode());
            throw new ProcessException("Update client", errorMessage);
        }
        client = clientRepository.save(client);
        return ClientResponse.from(client, false);
    }

    @Override
    public List<ClientResponse> getClientsByName(String clientName, Integer pageNumber, Integer pageSize, String sortBy, String order) {
        log.debug("Starting getClients for clientName: {}, pageNumber: {}, pageSize: {}, sortBy: {} and order: {}",
                clientName, pageNumber, pageSize, sortBy, order);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Client> clients = clientRepository.findAllByName(clientName, pageable);
        log.debug("Returning from getClientsByName with client response: {}", clients);
        return ClientResponse.from(clients);
    }

    @Override
    public List<ClientResponse> getAllClients(Integer pageNumber, Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllClients with pageNumber: {}, pageSize: {}, sortBy: {} and order: {}",
                pageNumber, pageSize, sortBy, order);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<Client> clients = clientRepository.findAll(pageable);
        log.debug("Returning from getAllClients with client response: {}", clients);
        return ClientResponse.from(clients.getContent());
    }

    @Override
    public ClientResponse getClientById(Long id) {
        log.debug("Fetching client with id: {}", id);
        Optional<Client> clientOptional = clientRepository.findById(id);
        ClientResponse clientResponse = null;
        if (clientOptional.isPresent()) {
            clientResponse = ClientResponse.from(clientOptional.get(), false);
        }
        log.debug("Returning from getClientById with response: {}", clientResponse);
        return clientResponse;
    }

    @Override
    public ClientResponse getClientBySlug(String slug) {
        log.debug("Fetching client with slug: {}", slug);
        Optional<Client> clientOptional = clientRepository.findBySlug(slug);
        ClientResponse clientResponse = null;
        if (clientOptional.isPresent()) {
            clientResponse = ClientResponse.from(clientOptional.get(), false);
        }
        log.debug("Returning from getClientBySlug with response: {}", clientResponse);
        return clientResponse;
    }

    @Override
    public boolean deleteClient(long id) {
        log.debug("Deleting client with id: {}", id);
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (!optionalClient.isPresent()) {
            log.error("Client not present with id: {}", id);
            return false;
        }
        clientRepository.delete(optionalClient.get());
        return true;
    }


    private String generateClientSlug(String clientName) throws ProcessException {
        log.debug("Creating slug for clientName: {}", clientName);
        int retryCount = 0;
        String slug = null;
        while (true) {
            slug = slugGenerationService.generateClientSlug(clientName, retryCount);
            Optional<Client> clientOptional = clientRepository.findBySlug(slug);
            if (!clientOptional.isPresent()) {
                break;
            }
            retryCount++;
            if (retryCount > ConstantUtil.DEFAULT_MAX_RETRY_COUNT) {
                log.error("Max retry count of {} exceeded", ConstantUtil.DEFAULT_MAX_RETRY_COUNT);
                String message = String.format("Max retry count of %s exceeded for the slug: %s",
                        ConstantUtil.DEFAULT_MAX_RETRY_COUNT, slug);
                throw new ProcessException("Generate Client Slug", message);
            }
        }
        log.debug("Returning from generateClientSlug with slug: {}, and try count: {}", slug, retryCount);
        return slug;
    }

}
