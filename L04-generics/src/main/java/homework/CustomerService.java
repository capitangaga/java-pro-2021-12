package homework;


import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> map = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return getCopy(map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getCopy(map.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }

    private static Map.Entry<Customer, String> getCopy(Map.Entry<Customer, String> toCopy) {
        if (toCopy == null) {
            return null;
        }
        return Map.entry(toCopy.getKey().copy(), toCopy.getValue());
    }
}
