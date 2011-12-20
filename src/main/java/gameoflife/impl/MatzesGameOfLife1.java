package gameoflife.impl;

import com.google.common.base.Function;
import gameoflife.GameOfLife;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

import static com.google.common.collect.Iterables.transform;

public class MatzesGameOfLife1 implements GameOfLife {

    private Collection<Cell> _activeCells = new HashSet<Cell>();

    @Override
    public void setCellAlive(final int x, final int y) {
        _activeCells.add(new Cell(x, y));
    }

    @Override
    public boolean isCellAlive(final int x, final int y) {
        return _activeCells.contains(new Cell(x, y));
    }

    @Override
    public void calculateNextGeneration() {
        final Collection<Cell> nextgeneration = new HashSet<Cell>();
        final Collection<Cell> traversedCells = new HashSet<Cell>();
        for (final Cell activeCell : _activeCells) {
            traversedCells.add(activeCell);
            final Cell[] neighbours = getNeighbours(activeCell);
            int activeNeighbours = 0;
            for (Cell neighbour : neighbours) {
                if (_activeCells.contains(neighbour)) {
                    activeNeighbours++;
                } else {
                    if (traversedCells.add(neighbour)) {
                        int activeNeighboursOfInactiveCell = 0;
                        for (Cell neigbourOfInactiveCell : getNeighbours(neighbour)) {
                            if (_activeCells.contains(neigbourOfInactiveCell)) {
                                activeNeighboursOfInactiveCell++;
                            }
                            if(activeNeighboursOfInactiveCell > 3) {
                                break;
                            }
                        }
                        if (activeNeighboursOfInactiveCell == 3) {
                            nextgeneration.add(neighbour);
                        }
                    }
                }
            }
            if (activeNeighbours == 2 || activeNeighbours == 3) {
                nextgeneration.add(activeCell);
            }
        }
        _activeCells = nextgeneration;
    }

    private Cell[] getNeighbours(Cell cell) {
        return new Cell[]{new Cell(cell.x - 1, cell.y - 1), new Cell(cell.x, cell.y - 1), new Cell(cell.x + 1, cell.y - 1),
                new Cell(cell.x - 1, cell.y), new Cell(cell.x + 1, cell.y),
                new Cell(cell.x - 1, cell.y + 1), new Cell(cell.x, cell.y + 1), new Cell(cell.x + 1, cell.y + 1),};
    }

    @Override
    public Iterable<Point> getCoordinatesOfAliveCells() {
        return transform(_activeCells, new Function<Cell, Point>() { @Override public Point apply(final Cell input) {
            return input.toPoint();
        }});
    }

    private static final class Cell {
        private final int x;
        private final int y;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        Point toPoint() {
            return new Point(x, y);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Cell cell = (Cell) o;
            return x == cell.x && y == cell.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

}
