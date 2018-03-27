package org.ost.investigate.test.booking;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/*
Events needs rooms to realisation. Events have start time and end time. And Events don't have to meet each other in the same room.
 */
public class BookingRooms {

    class Event {
        private int startTime;
        private int endTime;

        Event(int start, int end) {
            this.startTime = start;
            this.endTime = end;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }
    }

    class Room {
        List<Event> events = new ArrayList<>();

        Room(Event event) {
            events.add(event);
        }

        public List<Event> getEvents() {
            return events;
        }

        public boolean addEvent(Event event) {
            if (!events.contains(event)) {
                for (Event eventLocal : events) {
                    if (eventLocal.getStartTime() >= event.getEndTime() || eventLocal.getEndTime() <= event.getStartTime()) {
                        events.add(event);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private class RoomSet {
        private List<Event> events = new ArrayList<>();
        private List<Room> rooms = new ArrayList<>();

        public List<Room> getRooms() {
            return rooms;
        }

        RoomSet(List<Event> events) {
            this.events = events;
            calculate();
        }

        void calculate() {
            for (Event event : events) {
                boolean isAddToRoom = false;
                for (Room room : rooms) {
                    if (room.addEvent(event)) {
                        isAddToRoom = true;
                        break;
                    }
                }
                if (!isAddToRoom) {
                    rooms.add(new Room(event));
                }
            }
        }

        int size() {
            return rooms.size();
        }
    }

    @Test
    @DisplayName("Checks rooms for events")
    void testRoomsForEvents() {
        Event event1 = new Event(11, 12);
        Event event2 = new Event(11, 13);
        Event event3 = new Event(12, 13);
        List events = new ArrayList() {{
            this.add(event1);
            this.add(event2);
            this.add(event3);
        }};

        RoomSet roomSet = new RoomSet(events);
        Assert.assertTrue(roomSet.getRooms().get(0).getEvents().contains(event1));
        Assert.assertTrue(roomSet.getRooms().get(0).getEvents().contains(event3));
        Assert.assertTrue(roomSet.getRooms().get(1).getEvents().contains(event2));
        System.out.println("Result : " + roomSet.size());

    }
}
