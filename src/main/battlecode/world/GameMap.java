package battlecode.world;

import battlecode.common.*;

import battlecode.schema.BodyType;
import battlecode.schema.SpawnedBodyTable;
import battlecode.server.Server;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class represents the map in the game world on which
 * objects interact.
 *
 * This class is STATIC and immutable. It reflects the initial
 * condition of the map. All changes to the map are reflected in GameWorld.
 */
public class GameMap {

    /**
     * The width and height of the map.
     */
    private final float width, height;

    /**
     * The coordinates of the origin
     */
    private final MapLocation origin;

    /**
     * The random seed contained in the map file
     */
    private final int seed;

    /**
     * The maximum number of rounds in the game
     */
    private final int rounds;

    /**
     * The name of the map
     */
    private final String mapName;

    /**
     * The bodies to spawn on the map; MapLocations are in world space -
     * i.e. in game correct MapLocations that need to have the origin
     * subtracted from them to be used to index into the map arrays.
     */
    private final BodyInfo[] initialBodies;

    /**
     * Creates a deep copy of the input GameMap.
     *
     * @param gm the GameMap to copy.
     */
    public GameMap(GameMap gm) {
        this.width = gm.width;
        this.height = gm.height;

        this.origin = gm.origin;
        this.seed = gm.seed;
        this.rounds = gm.rounds;
        this.mapName = gm.mapName;
        this.initialBodies = gm.getInitialBodies();
    }

    /**
     * Creates a GameMap with the given parameters.
     *
     * The map will be initialized with a pseudorandom origin between (0, 0) and
     * (500, 500), based on the seed.
     *
     * YOU MUST NOT MODIFY ANY OF THESE OBJECTS AFTER CREATING THE MAP.
     *
     * @param schemaMap the GameMap acquired by a flatbuffer.
     */
    public GameMap(battlecode.schema.GameMap schemaMap) {
        this.width = schemaMap.maxCorner().x() - schemaMap.minCorner().x();
        this.height = schemaMap.maxCorner().y() - schemaMap.minCorner().y();
        this.origin = new MapLocation(schemaMap.minCorner().x(), schemaMap.minCorner().y());
        this.seed = schemaMap.randomSeed();
        this.mapName = schemaMap.name();
        this.rounds = GameConstants.GAME_DEFAULT_ROUNDS;

        this.initialBodies = new BodyInfo[schemaMap.bodies().robotIDsLength()];
        SpawnedBodyTable bodyTable = schemaMap.bodies();
        for(int i = 0; i < bodyTable.robotIDsLength()){
            byte bodyType = bodyTable.types(i);
            switch (bodyType){
                case BodyType.TREE_BULLET:
                    this.initialBodies[i] = new TreeInfo()
                    break;
                case BodyType.TREE_NEUTRAL:
                    break;
                case BodyType.ARCHON:
                    break;
                case BodyType.GARDENER:
                    break;
                case BodyType.LUMBERJACK:
                    break;
                case BodyType.RECRUIT:
                    break;
                case BodyType.SCOUT:
                    break;
                case BodyType.SOLDIER:
                    break;
                case BodyType.TANK:
                    break;
                default:

            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GameMap)) return false;

        return this.equals((GameMap) o);
    }

    /**
     * Returns whether two GameMaps are equal.
     *
     * @param other the other map to compare to.
     * @return whether the two maps are equivalent.
     */
    public boolean equals(GameMap other) {
        if (this.rounds != other.rounds) return false;
        if (this.width != other.width) return false;
        if (this.height != other.height) return false;
        if (this.seed != other.seed) return false;
        if (!this.mapName.equals(other.mapName)) return false;
        if (!this.origin.equals(other.origin)) return false;

        return Arrays.equals(this.initialBodies, other.initialBodies);
    }

    /**
     * Returns the width of this map.
     *
     * @return the width of this map.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of this map.
     *
     * @return the height of this map.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the name of the map.
     *
     * @return the name o the map.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Determines whether or not the location at the specified
     * coordinates is on the map. The coordinate should be a shifted one
     * (takes into account the origin).
     *
     * @param x the (shifted) x-coordinate of the location
     * @param y the (shifted) y-coordinate of the location
     * @return true if the given coordinates are on the map,
     *         false if they're not
     */
    private boolean onTheMap(float x, float y) {
        return (x >= origin.x && y >= origin.y && x < origin.x + width && y < origin.y + height);
    }

    /**
     * Determines whether or not the specified location is on the map.
     *
     * @param location the MapLocation to test
     * @return true if the given location is on the map,
     *         false if it's not
     */
    public boolean onTheMap(MapLocation location) {
        return onTheMap(location.x, location.y);
    }

    /**
     * Determines whether or not the specified circle is completely on the map.
     *
     * @param loc the center of the circle
     * @param radius the radius of the circle
     * @return true if the given circle is on the map,
     *         false if it's not
     */
    public boolean onTheMap(MapLocation loc, float radius){
        return (onTheMap(loc.translate(-radius, 0)) &&
                onTheMap(loc.translate(radius, 0)) &&
                onTheMap(loc.translate(0, -radius)) &&
                onTheMap(loc.translate(0, radius)));
    }

    /**
     * Get a list of the initial bodies on the map.
     *
     * @return the list of starting bodies on the map.
     *         MUST NOT BE MODIFIED.
     */
    public BodyInfo[] getInitialBodies() {
        return initialBodies;
    }

    /**
     * Gets the maximum number of rounds for this game.
     *
     * @return the maximum number of rounds for this game
     */
    public int getRounds() {
        return rounds;
    }

    /**
     * @return the seed of this map
     */
    public int getSeed() {
        return seed;
    }

    /**
     * Gets the origin (i.e., upper left corner) of the map
     *
     * @return the origin of the map
     */
    public MapLocation getOrigin() {
        return origin;
    }
}
