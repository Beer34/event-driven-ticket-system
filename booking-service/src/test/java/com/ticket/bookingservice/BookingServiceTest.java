package com.ticket.bookingservice;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.ticket.bookingservice.model.Event;
import com.ticket.bookingservice.service.EventService;

public class BookingServiceTest {

    private EventService eventService = new EventService();

    @Test
    void testGetEventsReturnsTwoMovies() {

        List<Event> events = eventService.getEvents();

        assertEquals(2, events.size());
    }

    @Test
    void testFirstEventIsAvengers() {

        List<Event> events = eventService.getEvents();

        assertEquals("Avengers", events.get(0).getName());
    }

    @Test
    void testSecondEventIsBatman() {

        List<Event> events = eventService.getEvents();

        assertEquals("Batman", events.get(1).getName());
    }
    @Test
    void testEventDetails() {

        List<Event> events = eventService.getEvents();

        assertEquals("E1", events.get(0).getEventId());
        assertEquals("18:00", events.get(0).getShowtime());
    }
}