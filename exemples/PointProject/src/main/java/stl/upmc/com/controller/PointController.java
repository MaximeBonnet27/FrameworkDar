package stl.upmc.com.controller;


import stl.upmc.com.model.Point;
import stl.upmc.com.model.Table;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PointController {

    public PointController() {
        Table.instance = new Table();
        Table.instance.init();
        Table.instance.add(new Point(1, 2));
        Table.instance.add(new Point(3, 4));
        Table.instance.add(new Point(5, 6));
        Table.instance.add(new Point(7, 8));
        Table.instance.add(new Point(9, 0));
    }


    public List<Integer> list() {
        ArrayList<Integer> ids = new ArrayList<>();
        for(Point p:Table.instance){
            ids.add(p.getId());
        }
        return ids;
    }


    public int getX(Integer id) {
        return Table.instance.getPoint(id).x;
    }

    public int getY(Integer id) {
        return Table.instance.getPoint(id).y;
    }

    public void update(Integer id,Integer x,Integer y) {
        Point point = Table.instance.getPoint(id);
        if (x != null) {
            point.x = x;
        }
        if (y != null) {
            point.y = y;
        }
    }

    public Integer addPoint(Point point) {
        Table.instance.add(point);
        return point.getId();
    }

    public void removePoint(Integer id) {
        Table.instance.removePoint(id);
    }

}
