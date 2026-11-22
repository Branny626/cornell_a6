package graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import graph.MazeGraph.Direction;
import graph.MazeGraph.MazeEdge;
import graph.MazeGraph.IPair;
import graph.MazeGraph.MazeVertex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.GameMap;
import util.MazeGenerator.TileType;

public class MazeGraphTest {

    /* Note, to conform to the precondition of the `MazeGraph` constructor, make sure that any
     * TileType arrays that you construct contain a `PATH` tile at index [2][2] and represent a
     * single, orthogonally connected component of `PATH` tiles. */

    /**
     * Create a game map with tile types corresponding to the letters on each line of `template`.
     * 'w' = WALL, 'p' = PATH, and 'g' = GHOSTBOX.  The letters of `template` must form a rectangle.
     * Elevations will be a gradient from the top-left to the bottom-right corner with a horizontal
     * slope of 2 and a vertical slope of 1.
     */
    GameMap createMap(String template) {
        Scanner lines = new Scanner(template);
        ArrayList<ArrayList<TileType>> lineLists = new ArrayList<>();

        while (lines.hasNextLine()) {
            ArrayList<TileType> lineList = new ArrayList<>();
            for (char c : lines.nextLine().toCharArray()) {
                switch (c) {
                    case 'w' -> lineList.add(TileType.WALL);
                    case 'p' -> lineList.add(TileType.PATH);
                    case 'g' -> lineList.add(TileType.GHOSTBOX);
                }
            }
            lineLists.add(lineList);
        }

        int height = lineLists.size();
        int width = lineLists.getFirst().size();

        TileType[][] types = new TileType[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                types[i][j] = lineLists.get(j).get(i);
            }
        }

        double[][] elevations = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                elevations[i][j] = (2.0 * i + j);
            }
        }
        return new GameMap(types, elevations);
    }

    /**
     * An Assert statement checking that `edge` is non-null, has tail `tail`, head `head`, and direction `dir`,
     * and has the weight given by `MazeGraph.edgeWeight()`, which is evaluated on the elevations of its
     * tail and head.
     */
    private void assertEdge(MazeEdge edge, MazeVertex tail, MazeVertex head, Direction dir,
                            GameMap map) {
        assertNotNull(edge);
        assertEquals(tail, edge.tail());
        assertEquals(head, edge.head());
        assertEquals(dir, edge.direction());
        double tailElev = map.elevations()[tail.loc().i()][tail.loc().j()];
        double headElev = map.elevations()[head.loc().i()][head.loc().j()];
        assertEquals(MazeGraph.edgeWeight(tailElev, headElev), edge.weight());
    }

    @DisplayName("WHEN a GameMap with exactly one path tile in position [2][2] is passed into the "
            + "MazeGraph constructor, THEN a graph with one vertex is created.")
    @Test
    void testOnePathCell() {
        GameMap map = createMap("""
                wwwww
                wwwww
                wwpww
                wwwww
                wwwww""");
        MazeGraph graph = new MazeGraph(map);
        Map<IPair, MazeVertex> vertices = new HashMap<>();
        graph.vertices().forEach(v -> vertices.put(v.loc(), v));

        assertEquals(1, vertices.size());
        assertTrue(vertices.containsKey(new IPair(2, 2)));
    }

    @DisplayName("WHEN a GameMap with exactly two horizontally adjacent path tiles is passed into "
            + "the MazeGraph constructor, THEN a graph with two vertices is created in which the two "
            + "vertices are connected by two directed edges with weights determined by evaluating "
            + "`MazeGraph.edgeWeight` on their elevations.")
    @Test
    void testTwoPathCellsHorizontal() {
        GameMap map = createMap("""
                wwwww
                wwwww
                wwppw
                wwwww
                wwwww""");
        MazeGraph graph = new MazeGraph(map);
        Map<IPair, MazeVertex> vertices = new HashMap<>();
        graph.vertices().forEach(v -> vertices.put(v.loc(), v));

        // graph contains two vertices with the correct locations
        assertEquals(2, vertices.size());
        IPair left = new IPair(2, 2);
        IPair right = new IPair(3, 2);
        assertTrue(vertices.containsKey(left));
        assertTrue(vertices.containsKey(right));

        MazeVertex vl = vertices.get(left);
        MazeVertex vr = vertices.get(right);

        // left vertex has one edge to the vertex to its right
        assertNull(vl.edgeInDirection(Direction.LEFT));
        assertNull(vl.edgeInDirection(Direction.UP));
        assertNull(vl.edgeInDirection(Direction.DOWN));
        MazeEdge l2r = vl.edgeInDirection(Direction.RIGHT);
        assertNotNull(l2r);

        // edge from left to right has the correct fields
        double lElev = map.elevations()[2][2];
        double rElev = map.elevations()[3][2];
        assertEquals(vl, l2r.tail());
        assertEquals(vr, l2r.head());
        assertEquals(Direction.RIGHT, l2r.direction());
        assertEquals(MazeGraph.edgeWeight(lElev, rElev), l2r.weight());

        // right vertex has one edge to the vertex to its left with the correct fields
        assertNull(vr.edgeInDirection(Direction.RIGHT));
        assertNull(vr.edgeInDirection(Direction.UP));
        assertNull(vr.edgeInDirection(Direction.DOWN));
        MazeEdge r2l = vr.edgeInDirection(Direction.LEFT);
        assertNotNull(r2l);
        assertEquals(vr, r2l.tail());
        assertEquals(vl, r2l.head());
        assertEquals(Direction.LEFT, r2l.direction());
        assertEquals(MazeGraph.edgeWeight(rElev, lElev), r2l.weight());
    }



    @DisplayName("WHEN a GameMap with exactly two vertically adjacent path tiles is passed into "
            + "the MazeGraph constructor, THEN a graph with two vertices is created in which the "
            + "two vertices are connected by two directed edges with weights determined by "
            + "evaluating `MazeGraph.edgeWeight` on their elevations.")
    @Test
    void testTwoPathCellsVertical() {
        GameMap map = createMap("""
                wwwww
                wwwww
                wwpww
                wwpww
                wwwww""");
        MazeGraph graph = new MazeGraph(map);
        Map<IPair, MazeVertex> vertices = new HashMap<>();
        graph.vertices().forEach(v -> vertices.put(v.loc(), v));

        // graph should contains two vertices with the correct locations
        assertEquals(2, vertices.size());
        IPair top = new IPair(2, 2);
        IPair bottom = new IPair(2, 3);
        assertTrue(vertices.containsKey(top));
        assertTrue(vertices.containsKey(bottom));

        MazeVertex vt = vertices.get(top);
        MazeVertex vb = vertices.get(bottom);

        // top vertex should have exactly one edge, pointing down to the bottom vertex
        assertNull(vt.edgeInDirection(Direction.LEFT));
        assertNull(vt.edgeInDirection(Direction.RIGHT));
        assertNull(vt.edgeInDirection(Direction.UP));
        assertEdge(vt.edgeInDirection(Direction.DOWN), vt, vb, Direction.DOWN, map);

        // bottom vertex should have exactly one edge, pointing up to the top vertex
        assertNull(vb.edgeInDirection(Direction.LEFT));
        assertNull(vb.edgeInDirection(Direction.RIGHT));
        assertNull(vb.edgeInDirection(Direction.DOWN));
        assertEdge(vb.edgeInDirection(Direction.UP), vb, vt, Direction.UP, map);
    }


    @DisplayName("WHEN a GameMap includes two path tiles in the first and last column of the same "
            + "row, THEN (tunnel) edges are created between these tiles with the correct properties.")
    @Test
    void testHorizontalTunnelEdgeCreation() {
        // (0,2) and (4,2) should be connected through the tunnel, because row 2 is a path.
        GameMap map = createMap("""
                wwwww
                wwwww
                ppppp
                wwwww
                wwwww""");
        MazeGraph graph = new MazeGraph(map);
        Map<IPair, MazeVertex> vertices = new HashMap<>();
        graph.vertices().forEach(v -> vertices.put(v.loc(), v));

        // graph contains one vertex per path tile
        assertEquals(5, vertices.size());
        IPair leftEnd = new IPair(0, 2);
        IPair rightEnd = new IPair(4, 2);
        assertTrue(vertices.containsKey(leftEnd));
        assertTrue(vertices.containsKey(rightEnd));

        MazeVertex vl = vertices.get(leftEnd);
        MazeVertex vr = vertices.get(rightEnd);

        // the leftmost vertex's left edge should tunnel to the rightmost vertex
        assertEdge(vl.edgeInDirection(Direction.LEFT), vl, vr, Direction.LEFT, map);

        // the rightmost vertex's right edge should tunnel to the leftmost vertex
        assertEdge(vr.edgeInDirection(Direction.RIGHT), vr, vl, Direction.RIGHT, map);

        // neither end vertex has vertical edges (the tiles above and below are walls)
        assertNull(vl.edgeInDirection(Direction.UP));
        assertNull(vl.edgeInDirection(Direction.DOWN));
        assertNull(vr.edgeInDirection(Direction.UP));
        assertNull(vr.edgeInDirection(Direction.DOWN));
    }

    @DisplayName("WHEN a GameMap includes a cyclic connected component of path tiles with a "
            + "non-path tile in the middle, THEN its graph includes edges between all adjacent "
            + "pairs of vertices.")
    @Test
    void testCyclicPaths() {
        GameMap map = createMap("""
                wwwwwww
                wwwwwww
                wwpppww
                wwpwpww
                wwpppww
                wwwwwww""");
        MazeGraph graph = new MazeGraph(map);
        Map<IPair, MazeVertex> vertices = new HashMap<>();
        graph.vertices().forEach(v -> vertices.put(v.loc(), v));

        // graph contains exactly the 8 ring tiles, and not the wall in the middle
        assertEquals(8, vertices.size());
        IPair[] ring = {new IPair(2, 2), new IPair(3, 2), new IPair(4, 2), new IPair(2, 3),
                new IPair(4, 3), new IPair(2, 4), new IPair(3, 4), new IPair(4, 4)};
        for (IPair loc : ring) {
            assertTrue(vertices.containsKey(loc));
        }
        assertFalse(vertices.containsKey(new IPair(3, 3)));

        // Check every direction from every vertex, as there should be an edge only if the
        // neighbor is part of the ring. The ring isn't on the border, so no tunnels.
        int edgeCount = 0;
        for (MazeVertex v : vertices.values()) {
            int i = v.loc().i();
            int j = v.loc().j();
            for (Direction d : Direction.values()) {
                IPair neighborLoc = switch (d) {
                    case LEFT -> new IPair(i - 1, j);
                    case RIGHT -> new IPair(i + 1, j);
                    case UP -> new IPair(i, j - 1);
                    case DOWN -> new IPair(i, j + 1);
                };
                MazeVertex neighbor = vertices.get(neighborLoc);
                MazeEdge edge = v.edgeInDirection(d);
                if (neighbor == null) {
                    assertNull(edge);
                } else {
                    assertEdge(edge, v, neighbor, d, map);
                    edgeCount++;
                }
            }
        }

        // 8 adjacent pairs around the ring, each connected by 2 directed edges
        assertEquals(16, edgeCount);
    }

    @DisplayName("WHEN a path column spans the top and bottom rows next to a ghost box tile, "
            + "THEN tunnel edges connect its ends and the ghost box gets no vertex.")
    @Test
    void testVerticalTunnelAndGhostBox() {
        GameMap map = createMap("""
                wwpww
                wwpgw
                wwpww
                wwpww
                wwpww""");
        MazeGraph graph = new MazeGraph(map);
        Map<IPair, MazeVertex> vertices = new HashMap<>();
        graph.vertices().forEach(v -> vertices.put(v.loc(), v));

        // only the 5 path tiles in column 2 become vertices, the ghost box does not
        assertEquals(5, vertices.size());
        assertFalse(vertices.containsKey(new IPair(3, 1)));

        MazeVertex top = vertices.get(new IPair(2, 0));
        MazeVertex bottom = vertices.get(new IPair(2, 4));
        assertNotNull(top);
        assertNotNull(bottom);

        // the top vertex's up edge tunnels to the bottom. Vice versa
        assertEdge(top.edgeInDirection(Direction.UP), top, bottom, Direction.UP, map);
        assertEdge(bottom.edgeInDirection(Direction.DOWN), bottom, top, Direction.DOWN, map);

        // the path tile next to the ghost box has no edge pointing into it
        MazeVertex besideGhostBox = vertices.get(new IPair(2, 1));
        assertNull(besideGhostBox.edgeInDirection(Direction.RIGHT));
        assertNull(besideGhostBox.edgeInDirection(Direction.LEFT));
    }

    @DisplayName("WHEN path tiles form a plus shape, THEN the center connects to all four "
            + "neighbors in both directions, and uphill edges weigh more than downhill ones.")
    @Test
    void testFourWayIntersectionAndSlope() {
        GameMap map = createMap("""
                wwwww
                wwpww
                wpppw
                wwpww
                wwwww""");
        MazeGraph graph = new MazeGraph(map);
        Map<IPair, MazeVertex> vertices = new HashMap<>();
        graph.vertices().forEach(v -> vertices.put(v.loc(), v));

        assertEquals(5, vertices.size());
        MazeVertex center = vertices.get(new IPair(2, 2));
        MazeVertex left = vertices.get(new IPair(1, 2));
        MazeVertex right = vertices.get(new IPair(3, 2));
        MazeVertex up = vertices.get(new IPair(2, 1));
        MazeVertex down = vertices.get(new IPair(2, 3));

        // the center has an edge in every direction
        assertEdge(center.edgeInDirection(Direction.LEFT), center, left, Direction.LEFT, map);
        assertEdge(center.edgeInDirection(Direction.RIGHT), center, right, Direction.RIGHT, map);
        assertEdge(center.edgeInDirection(Direction.UP), center, up, Direction.UP, map);
        assertEdge(center.edgeInDirection(Direction.DOWN), center, down, Direction.DOWN, map);

        // each arm connects back to the center
        assertEdge(left.edgeInDirection(Direction.RIGHT), left, center, Direction.RIGHT, map);
        assertEdge(right.edgeInDirection(Direction.LEFT), right, center, Direction.LEFT, map);
        assertEdge(up.edgeInDirection(Direction.DOWN), up, center, Direction.DOWN, map);
        assertEdge(down.edgeInDirection(Direction.UP), down, center, Direction.UP, map);

        // Elevation increases to the right, so center -> right is uphill  and
        // right -> center is downhill (weight 0.25). 
        assertEquals(1.75, center.edgeInDirection(Direction.RIGHT).weight());
        assertEquals(0.25, right.edgeInDirection(Direction.LEFT).weight());
    }


}
