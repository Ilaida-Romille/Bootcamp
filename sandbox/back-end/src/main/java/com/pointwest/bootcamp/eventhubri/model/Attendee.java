public class Attendee extends User {
    private String attendeeId;

    public Attendee() {
        super();
    }

    public Attendee(String userId, String name, String email, String attendeeId) {
        super(userId, name, email);
        this.attendeeId = attendeeId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }
}