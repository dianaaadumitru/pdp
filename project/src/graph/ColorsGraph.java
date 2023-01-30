package graph;

import utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorsGraph {
    private final int colorsNo;

    private List<Pair<Integer, String>> colors;

    public ColorsGraph(int colorsNo) {
        this.colorsNo = colorsNo;
        this.colors = new ArrayList<>();
    }

    public int getColorsNo() {
        return colorsNo;
    }

    public List<Pair<Integer, String>> getColors() {
        return colors;
    }

    public void setColors(List<Pair<Integer, String>> colors) {
        this.colors = colors;
    }

    public void addColor(int code, String colorName) {
        colors.add(new Pair<>(code, colorName));
    }

    public Pair<Integer, String> getValueByKey(int key) {
        for (var color : colors) {
            if (color.getKey() == key)
                return color;
        }
        return null;
    }

    public Map<Integer, String> getIndexesToColors(List<Integer> codes) {
        Map<Integer, String> map = new HashMap<>();
        for (int index = 0; index < codes.size(); index++) {
            map.put(index, getValueByKey(codes.get(index)).getValue());
        }
        return map;
    }

    public Map<Integer, Integer> getIndexesToCodes(List<Integer> codes) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int index = 0; index < codes.size(); index++) {
            System.out.println(codes.get(index));
            map.put(index, getValueByKey(codes.get(index)).getKey());
        }
        return map;
    }

    public List<Integer> getCodesFromIndexes(List<Integer> indexes) {
        List<Integer> codes = new ArrayList<>();
        for (int index : indexes) {
            codes.add(colors.get(index).getKey());
        }
        return codes;
    }

    @Override
    public String toString() {
        return "ColorsGraph{" +
                "colorsNo=" + colorsNo +
                ", colors=" + colors +
                '}';
    }
}
