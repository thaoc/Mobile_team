package edu.csp.csc315.damascusway30;

import com.android.volley.RequestQueue;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;

public class LocalData {

    private Employee currentEmployee;
    private Round currentRound;
    private Resident currentResident;
    private CheckIn currentCheckIn;
    private static final DatabaseIO dbIO = new DatabaseIO();

    public DatabaseIO getDatabaseIO() {return this.dbIO;}

    public ArrayList<Resident> residentList = new ArrayList<>();

    public RequestQueue queue;

    public Employee getCurrentEmployee() {
        return this.currentEmployee;
    }

    public void setCurrentEmployee(Employee e)
    {
        this.currentEmployee = e;
    }

    public Round getCurrentRound(){
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public Resident getCurrentResident() {
        return currentResident;
    }

    public void setCurrentResident(Resident currentResident) {
        this.currentResident = currentResident;
    }



    public CheckIn getCurrentCheckIn()
    {
        return currentCheckIn;
    }

    public void setCurrentCheckIn(CheckIn currentCheckIn)
    {
        this.currentCheckIn = currentCheckIn;
    }

    private static final LocalData holder = new LocalData();
    public static LocalData getInstance() {return holder;}


}
