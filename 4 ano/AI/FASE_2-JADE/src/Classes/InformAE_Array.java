package Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InformAE_Array implements Serializable {
    ArrayList<InformAE> list;

    public InformAE_Array(List<InformAE> list) {
        this.list = new ArrayList<>(list);
    }

    public ArrayList<InformAE> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "[" + Arrays.deepToString(list.toArray()) + "]";
    }
}
