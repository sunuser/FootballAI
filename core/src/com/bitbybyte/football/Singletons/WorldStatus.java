package com.bitbybyte.football.Singletons;

import com.badlogic.gdx.math.Vector2;
import com.bitbybyte.football.Pitch.Ball;

import java.awt.geom.Point2D;

/**
 * Created by Mickey on 17/02/2016.
 */
public class WorldStatus {
    private static WorldStatus ourInstance = new WorldStatus();

    public static WorldStatus getInstance() {
        return ourInstance;
    }

    private WorldStatus() {
    }

    public void progressToNextMatchday() {
        matchDay++;
    }

    Ball ball;
    float matchDay = 0;
}
