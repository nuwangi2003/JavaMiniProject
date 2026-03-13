package model;

public class Lecturer extends User{
    private String specialization;
    private String designation;

    public void setSpecialization(String specialization){
        this.specialization = specialization;
    }

    public String getSpecialization(){
        return this.specialization;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
