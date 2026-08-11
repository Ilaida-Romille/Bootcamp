import java.util.List;

public class UpdateRegistrationRequestDto {
    private String dietaryRestrictions;
    private List<String> selectedSessionIds;

    public UpdateRegistrationRequestDto() {
    }

    public UpdateRegistrationRequestDto(String dietaryRestrictions, List<String> selectedSessionIds) {
        this.dietaryRestrictions = dietaryRestrictions;
        this.selectedSessionIds = selectedSessionIds;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public List<String> getSelectedSessionIds() {
        return selectedSessionIds;
    }

    public void setSelectedSessionIds(List<String> selectedSessionIds) {
        this.selectedSessionIds = selectedSessionIds;
    }
}