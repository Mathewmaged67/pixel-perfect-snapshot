import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryState {
  private List<GearItem> items = SeedData.copyGear();
  private Map<String, String> equipped = new HashMap<>();

  public InventoryState() {
    reset();
  }

  public List<GearItem> getItems() {
    return items;
  }

  public Map<String, String> getEquipped() {
    return equipped;
  }

  public void unlock(String id) {
    for (GearItem item : items) {
      if (item.getId().equals(id)) {
        item.setOwned(true);
        break;
      }
    }
  }

  public void equip(String slot, String id) {
    equipped.put(slot, id);
  }

  public void reset() {
    items = SeedData.copyGear();
    equipped = new HashMap<>();
    equipped.put("weapon", null);
    equipped.put("head", "g-helm");
    equipped.put("feet", "g-boots");
    equipped.put("accessory", "g-gloves");
  }
}
