package dto;

public enum PlantStatus {
    OK,     //No error
    FAULT,  //Small error
    DEFECT, //Big error
    OFFLINE, //dead
    STARTING
}
