package Entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

public class User {
    private final String userId;
    private final LocalDate dateOfBirth;
    private final String gender;
    private final Set<String> interests;
    
    public User(String userId, LocalDate dateOfBirth, String gender) {
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.interests = new HashSet<>();
    }
    
    public void addInterest(String interest) {
        interests.add(interest);
    }
    
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    
    // Getters
    public String getUserId() { return userId; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public Set<String> getInterests() { return new HashSet<>(interests); }
}
