package com.bitbybyte.football.Singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.bitbybyte.football.AI.Listeners.BallListener;
import com.bitbybyte.football.Pitch.Ball;

import au.com.ds.ef.EasyFlow;
import au.com.ds.ef.EventEnum;
import au.com.ds.ef.FlowBuilder;
import au.com.ds.ef.StateEnum;
import au.com.ds.ef.StatefulContext;
import au.com.ds.ef.call.ContextHandler;
import au.com.ds.ef.err.LogicViolationError;

/**
 * Created by Mickey on 17/02/2016.
 */
public class Match {
    private static Match ourInstance = new Match();
    public static Match getInstance() {
        return ourInstance;
    }

    Ball _ball;
    BallListener _ballListener;
    float _timeLeft = 3 * 60; //starting at 3 minutes
    float _pitchLength = 35;    // sizes in metres
    float _pitchWidth = 20;
    boolean _stopMatchClock = true;

    int _homeTeamGoals = 0;
    int _awayTeamGoals = 0;

    private static class FlowContext extends StatefulContext {
//        private String pin;
//        private int invalidPinCounter;
//        private int balance = 1000;
//        private int withdrawAmt;
    }

    enum States implements StateEnum {
        BEGIN, KICK_OFF, IN_PLAY, END_MATCH
    }

    enum Events implements EventEnum {
        start, take_ko, score_goal, final_whistle
    }

    private Match() {
        EasyFlow<FlowContext> flow =
                FlowBuilder.from(States.BEGIN).transit(
                        FlowBuilder.on(Events.start).to(States.KICK_OFF).transit(
                                FlowBuilder.on(Events.take_ko).to(States.IN_PLAY).transit(
                                        FlowBuilder.on(Events.score_goal).to(States.KICK_OFF),
                                        FlowBuilder.on(Events.final_whistle).to(States.END_MATCH)
        )));

        flow.whenEnter(States.BEGIN, new ContextHandler<FlowContext>(){
            @Override
            public void call(FlowContext context) throws Exception {
                _stopMatchClock = true;

                try {
                    context.trigger(Events.start);
                } catch (LogicViolationError logicViolationError) {
                    logicViolationError.printStackTrace();
                }
            }
        });

        flow.whenEnter(States.KICK_OFF, new ContextHandler<FlowContext>(){
            @Override
            public void call(FlowContext context) throws Exception {
                _stopMatchClock = true;

                try {
                    context.trigger(Events.take_ko);
                } catch (LogicViolationError logicViolationError) {
                    logicViolationError.printStackTrace();
                }
            }
        });

        flow.whenEnter(States.IN_PLAY, new ContextHandler<FlowContext>(){
            @Override
            public void call(final FlowContext context) throws Exception {
                setBallListener(new BallListener() {
                    @Override
                    public void onEnteringGoal(boolean homeGoal) {
                        if(homeGoal)
                            _homeTeamGoals++;
                        else
                            _awayTeamGoals++;

                        try {
                            context.trigger(Events.score_goal);
                        } catch (LogicViolationError logicViolationError) {
                            logicViolationError.printStackTrace();
                        }
                    }
                });

                _stopMatchClock = false;
            }
        });
        flow.whenLeave(States.IN_PLAY, new ContextHandler<FlowContext>(){
            @Override
            public void call(final FlowContext context) throws Exception {
                setBallListener(null);
                _stopMatchClock = true;
                Thread.sleep(2000);

                // Place ball at centre spot
                _ball.position.x = 0;
                _ball.position.y = 0;
            }
        });

        flow.whenEnter(States.END_MATCH, new ContextHandler<FlowContext>(){
            @Override
            public void call(FlowContext context) throws Exception {
                _stopMatchClock = true;
            }
        });
    }

    public void start() {

    }

    public void update(){
        tickClock();

        // Check for goal
        //-----------------------
        if (_ball.position.x - _ball.radius > _pitchLength/2.0) {    // Home goal
            if(_ballListener!=null)
                _ballListener.onEnteringGoal(true);
        }
        else if (_ball.position.x + _ball.radius < -_pitchLength/2.0) {  // away goal
            if(_ballListener!=null)
                _ballListener.onEnteringGoal(false);
        }
        //-----------------------
    }

    private void tickClock(){
        if(!_stopMatchClock) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            _timeLeft -= deltaTime;
        }

        int minutes = ((int)_timeLeft) / 60;
        int seconds = ((int)_timeLeft) % 60;
    }

    public void setBallListener(BallListener eventListener) {
        _ballListener = eventListener;
    }
}
