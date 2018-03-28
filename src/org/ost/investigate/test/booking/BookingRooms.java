package org.ost.investigate.test.booking;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.ArrayList;
import java.util.List;

/*
Events needs rooms to realisation. Events have start time and end time. And Events don't have to meet each other in the same room.
 */
public class BookingRooms {

    @BeforeEach
    void before(TestInfo testInfo) {
        System.out.println("-------------------------------------- Start - " + testInfo.getDisplayName());
    }

    @AfterEach
    void after(TestInfo testInfo) throws InterruptedException {
        System.out.println("-------------------------------------- Finish - " + testInfo.getDisplayName());
    }

    interface Event {
        int getStartTime();

        int getEndTime();

        String getDescription();

        String getName();
    }

    class EventImp implements Event {
        private int startTime;
        private int endTime;
        private String name;
        private String description;

        EventImp(int start, int end, String name, String description) {
            this.startTime = start;
            this.endTime = end;
            this.name = name;
            this.description = description;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public String getDescription() {
            return description;
        }

        public String getName() {

            return name;
        }
    }

    interface Room {
        List<Event> getEvents();

        boolean addEvent(Event event);
    }

    class RoomImpl implements Room {
        private List<Event> events = new ArrayList<>();

        RoomImpl(Event event) {
            events.add(event);
        }

        public List<Event> getEvents() {
            return events;
        }

        public boolean addEvent(Event event) {
            if (!events.contains(event)) {
                for (Event eventLocal : events) {
                    if ((eventLocal.getStartTime() <= event.getStartTime() && event.getStartTime() < eventLocal.getEndTime())
                            || (eventLocal.getStartTime() < event.getEndTime() && event.getEndTime() <= eventLocal.getEndTime())) {
                        return false;
                    }
                }
            }
            events.add(event);
            return true;
        }
    }

    class RoomSet {
        private List<Event> events;
        private List<Room> rooms = new ArrayList<>();

        public List<Room> getRooms() {
            return rooms;
        }

        RoomSet(List<Event> events) {
            if (events == null) throw new IllegalArgumentException();
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
                    rooms.add(new RoomImpl(event));
                }
            }
        }

        int size() {
            return rooms.size();
        }
    }

    class RoomImplAdvanced implements Room {
        private List<Event> events = new ArrayList<>();

        RoomImplAdvanced(Event event) {
            events.add(event);
        }

        public List<Event> getEvents() {
            return events;
        }

        public boolean addEvent(Event event) {
            boolean isSuitable = !events.stream().anyMatch(e -> ((e.getStartTime() <= event.getStartTime() && event.getStartTime() < e.getEndTime())
                    || (e.getStartTime() < event.getEndTime() && event.getEndTime() <= e.getEndTime()) || e.equals(event)));
            if (isSuitable) {
                events.add(event);
            }
            return isSuitable;
        }
    }

    class RoomSetAdvanced {
        private List<Event> events;
        private List<Room> rooms = new ArrayList<>();

        public List<Room> getRooms() {
            return rooms;
        }

        RoomSetAdvanced(List<Event> events) {
            if (events == null) throw new IllegalArgumentException();
            this.events = events;
            calculate();
        }

        void calculate() {
            rooms = events.stream().collect(ArrayList::new, (v, e) -> {
                boolean isAddToRoom = false;
                for (Room room : v) {
                    if (room.addEvent(e)) {
                        isAddToRoom = true;
                        break;
                    }
                }
                if (!isAddToRoom) {
                    v.add(new RoomImplAdvanced(e));
                }
            }, (v1, v2) -> {
            });
        }

        int size() {
            return rooms.size();
        }
    }

    @Test
    @DisplayName("Checks rooms for events")
    void testRoomsForEvents() {
        Event event1 = new EventImp(11, 12, "event1", "");
        Event event2 = new EventImp(11, 13, "event2", "");
        Event event3 = new EventImp(12, 13, "event3", "");
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

    @Test
    @DisplayName("Checks rooms for events using java 8")
    void testRoomsForEventsJava8() {
        Event event1 = new EventImp(11, 12, "event1", "");
        Event event2 = new EventImp(11, 13, "event2", "");
        Event event3 = new EventImp(12, 13, "event3", "");
        List events = new ArrayList() {{
            this.add(event1);
            this.add(event2);
            this.add(event3);
        }};

        RoomSetAdvanced roomSet = new RoomSetAdvanced(events);
        Assert.assertTrue(roomSet.getRooms().get(0).getEvents().contains(event1));
        Assert.assertTrue(roomSet.getRooms().get(0).getEvents().contains(event3));
        Assert.assertTrue(roomSet.getRooms().get(1).getEvents().contains(event2));
        System.out.println("Result : " + roomSet.size());

    }

    @Test
    @DisplayName("Expanded checks rooms for events using java 8")
    void testRoomsForEventsJava8Ext() {
        Event event1 = new EventImp(11, 12, "event1", "");
        Event event2 = new EventImp(11, 13, "event2", "");
        Event event3 = new EventImp(12, 13, "event3", "");
        Event event4 = new EventImp(15, 16, "event4", "");
        Event event5 = new EventImp(14, 15, "event4", "");
        List events = new ArrayList() {{
            this.add(event5);
            this.add(event1);
            this.add(event2);
            this.add(event3);
            this.add(event4);
        }};

        RoomSetAdvanced roomSet = new RoomSetAdvanced(events);
        Assert.assertTrue(roomSet.getRooms().get(0).getEvents().contains(event1));
        Assert.assertTrue(roomSet.getRooms().get(0).getEvents().contains(event3));
        Assert.assertTrue(roomSet.getRooms().get(0).getEvents().contains(event4));
        Assert.assertTrue(roomSet.getRooms().get(0).getEvents().contains(event5));
        Assert.assertTrue(roomSet.getRooms().get(1).getEvents().contains(event2));
        System.out.println("Result : " + roomSet.size());

    }
}
