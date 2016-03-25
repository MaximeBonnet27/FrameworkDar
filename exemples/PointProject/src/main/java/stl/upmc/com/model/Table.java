package stl.upmc.com.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Table implements Iterable<Point> {

    public static Table instance;

    private final ArrayList<Point> points;

    /**
     * "AutoIncrement" id
     */
    private int count = 0;

    public Table() {
        this.points = new ArrayList<>();
    }

    public void init(){

    }

    /**
     * Ajoute un point dans la table
     * Incrémente également le compteur d'ids
     * @param point le point à ajouter
     */
    public void add(Point point){
        point.id = count;
        points.add(point);
        count++;
    }

    public int getId(Point point){

        if(point.id != - 1){
            return point.id;
        }
        throw new IllegalArgumentException("Le point passé en paramètre n'est pas dans la table");
    }

    public Point getPoint(int id){
        for(Point p : this){
            if(p.id == id){
                return p;
            }
        }
        throw new IllegalArgumentException("Le point passé en paramètre n'est pas dans la table");
    }

    public void removePoint(int id){
        points.remove(getPoint(id));
    }

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }

    @Override
    public void forEach(Consumer<? super Point> consumer) {
        points.forEach(consumer);
    }

    @Override
    public Spliterator<Point> spliterator() {
        return points.spliterator();
    }

    @Override
    public String toString() {
        return "Table{" +
                "points=" + points +
                '}';
    }
}
