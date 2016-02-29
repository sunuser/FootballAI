package com.bitbybyte.football;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitbybyte.football.AI.Team;
import com.bitbybyte.football.Singletons.Match;
import com.bitbybyte.football.Singletons.WorldStatus;

public class Football extends ApplicationAdapter {
	SpriteBatch _batch;
	Texture _img;
	Team _homeTeam;
	Team _awayTeam;
	Match _match;
	WorldStatus _world;
	
	@Override
	public void create () {
		_batch = new SpriteBatch();
		_img = new Texture("badlogic.jpg");

		// Create instances
		_homeTeam = new Team("Liverpool");
		_awayTeam = new Team("Manchester City");
		_match = Match.getInstance();
		_world = WorldStatus.getInstance();

		_world.progressToNextMatchday();
		_match.start();
	}

	@Override
	public void render () {
		update();

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_batch.begin();
		_batch.draw(_img, 0, 0);
		_batch.end();
	}

	private void update() {

	}
}
