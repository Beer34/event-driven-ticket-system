package com.ticket.bookingservice.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ticket.bookingservice.model.Seat;

@Service
public class InventoryServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Seat> getSeats(String eventId) {

        String url = "http://localhost:8081/seats/" + eventId;

        Seat[] seats = restTemplate.getForObject(url, Seat[].class);

        return Arrays.asList(seats);
    }
}