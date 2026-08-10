import java.util.Map;

public class Example {

    public static class HotelManager {
        private Hotline hotline;

        /**
         * Single Responsibility Principle:
         * The HotelManager is responsible for supervising the hotel operations, and nothing else.
         * It does not do or know the specific tasks of the departments.
         */
        public void supervise(String dept, String request) {
            /**
             * Open-Closed Principle
             * The HotelManager is open for extension (new departments can be added)
             * but closed for modification (the HotelManager code doesn't change when
             * new departments are added).
             */
            Department department = hotline.callDepartment(dept);
            department.handleGuestRequest(request);
        }

        /**
         * Dependency Inversion:
         * The HotelManager does not instantiate Hotline itself.
         * The Hotline is injected via the setHotline method.
         */
        public void setHotline(Hotline hotline) {
            this.hotline = hotline;
        }
    }

    public static class Hotline {
        private Map<String, Department> departments;

        public Department callDepartment(String name) {
            return getDepartments().get(name);
        }

        public Map<String, Department> getDepartments() {
            if (departments == null) {
                initialize();
            }
            return departments;
        }

        private void initialize() {
            departments = Map.of(
                "Frontdesk", new Frontdesk(),
                "Housekeeping", new Housekeeping(),
                "Restaurant", new Restaurant(),
                "Accounting", new Accounting()
            );
        }
    }

    /**
     * Liskov Substitution Principle:
     * All departments must be substitutable for the Department abstract class.
     * They must all implement the handleGuestRequest method.
     */
    public static abstract class Department {
        public abstract void handleGuestRequest(String request);
    }

    public static class Frontdesk extends Department {
        @Override
        public void handleGuestRequest(String request) {
            System.out.println("[Frontdesk] handleGuestRequest: " + request);
            welcomeGuests();
        }

        private void welcomeGuests() {
            System.out.println("[Frontdesk] welcomeGuests");
        }
    }

    public static class Housekeeping extends Department {
        @Override
        public void handleGuestRequest(String request) {
            System.out.println("[Housekeeping] handleGuestRequest: " + request);
            cleanRoom();
        }

        private void cleanRoom() {
            System.out.println("[Housekeeping] cleanRoom");
        }
    }

    public static class Restaurant extends Department {
        @Override
        public void handleGuestRequest(String request) {
            System.out.println("[Restaurant] handleGuestRequest: " + request);
            cookBreakfast();
            serveBreakfast();
        }

        private void serveBreakfast() {
            System.out.println("[Restaurant] serveBreakfast");
        }

        private void cookBreakfast() {
            System.out.println("[Restaurant] cookBreakfast");
        }
    }

    public static class Accounting extends Department {
        @Override
        public void handleGuestRequest(String request) {
            System.out.println("[Accounting] handleGuestRequest: " + request);
            acceptPayment();
        }

        private void acceptPayment() {
            System.out.println("[Accounting] acceptPayment");
        }
    }
}