package ourrobots.F3_Final.Utils;

import java.util.ArrayList;

public class TwoArrayLists implements java.io.Serializable {

    private ArrayList<String> arraylist1 = new ArrayList<>();
    private ArrayList<String> arraylist2 = new ArrayList<>();

    public TwoArrayLists() {
    }

    public ArrayList<String> getArraylist1() {
        return arraylist1;
    }

    public void setArraylist1(ArrayList<String> arraylist1) {
        this.arraylist1 = arraylist1;
    }

    public ArrayList<String> getArraylist2() {
        return arraylist2;
    }

    public void setArraylist2(ArrayList<String> arraylist2) {
        this.arraylist2 = arraylist2;
    }
}
