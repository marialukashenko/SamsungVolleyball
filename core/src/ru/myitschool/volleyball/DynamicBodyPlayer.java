package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

public class DynamicBodyPlayer {

    public Body body;
    public float r;
    private float lowLevel;
    private static final int GO = 0, JUMP = 1, FALL = 2, STAY = 3;
    private int state;
    private long timeStartJump, timeJump = 200;
    private long timeLastFaza, timeFazaInterval = 50;
    public int faza, nFaz = 18, fazaStay = 17, fazaJumpLeft = 18, fazaJumpRight = 19;
    public boolean isFlip;
    public static final boolean LEFT = true, RIGHT = false;
    private boolean side;
    float targetX, targetY;

    DynamicBodyPlayer(World world, float x, float y, float radius, boolean side) {
        r = radius;
        lowLevel = y;
        this.side = side;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;

        fixtureDef.density = 5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        Fixture fixture = body.createFixture(fixtureDef);
        circle.dispose();
    }

    public void touch(float tx, float ty) {
        if (state != GO && state != STAY) return;
        targetX = tx;
        if (ty > getY() + r * 2) {
            state = JUMP;
            float a = MathUtils.atan2((tx - getX()), (ty - getY()));
            float vx = 40 * MathUtils.sin(a);
            float vy = 40 * MathUtils.cos(a);
            vy = vy < 0 ? -1 * vy : vy;
            body.applyLinearImpulse(new Vector2(vx, vy), body.getPosition(), true);
            timeStartJump = TimeUtils.millis();
            return;
        }

        if (tx > getX()) {
            float vx = (tx - getX()) * 8;
            if (vx > 10) vx = 10;
            body.setLinearVelocity(vx, 0);
        } else {
            float vx = (tx - getX()) * 8;
            if (vx < -10) vx = -10;
            body.setLinearVelocity(vx, 0);
        }
    }

    void move() {
        if (timeStartJump + timeJump < TimeUtils.millis() && state == JUMP) {
            body.setLinearVelocity(body.getLinearVelocity().x > 5 ? 5 : body.getLinearVelocity().x, -4.9f);
            state = FALL;
        }
        changeFaza();

        if (state == FALL && getY() <= lowLevel + 0.1f) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            state = GO;
        }

        if(near(getX(), targetX, 0.1f)) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
    }

    boolean near(float x1, float x2, float dx){
        return x1 > x2-dx && x1 < x2+dx;
    }

    void changeFaza() {
        if (state == GO) {
            if (Math.abs(body.getLinearVelocity().x) < 0.01) {
                isFlip = side;
                faza = fazaStay;
            } else if (timeLastFaza + timeFazaInterval < TimeUtils.millis()) {
                isFlip = body.getLinearVelocity().x > 0;
                if (++faza >= nFaz) faza = 0;
                timeLastFaza = TimeUtils.millis();
            }
        }
        if (state == JUMP && body.getLinearVelocity().x > 0) {
            if (isFlip) faza = fazaJumpRight;
            else faza = fazaJumpLeft;
        }
        if (state == JUMP && body.getLinearVelocity().x < 0) {
            if (isFlip) faza = fazaJumpLeft;
            else faza = fazaJumpRight;
        }
    }

    float getX() {
        return body.getPosition().x;
    }

    float getY() {
        return body.getPosition().y;
    }

    float scrX() {
        return body.getPosition().x - r;
    }

    float scrY() {
        return body.getPosition().y - r;
    }

    float width() {
        return r * 2;
    }

    float height() {
        return r * 2;
    }

    Vector2 getCenter() {
        return body.getPosition();
    }

    boolean overlap(DynamicBodyBall b) {
        return (getX() - b.getX()) * (getX() - b.getX()) + (getY() - b.getY()) * (getY() - b.getY()) <= (r + b.r) * (r + b.r);
    }

    void hitBall(DynamicBodyBall ball) {
        if (ball.getX() > SCR_WIDTH / 2) {
            Vector2 ballVelocity = ball.body.getLinearVelocity();
            Vector2 ballPosition = ball.body.getPosition();
            if (ballVelocity.x == 0 && ballVelocity.y == 0) {
                timeStartJump = TimeUtils.millis();
                state = JUMP;
                //body.setLinearVelocity(-8f, MathUtils.random(15f, 20f));
                body.applyLinearImpulse(new Vector2(-5f, MathUtils.random(15f, 20f)), body.getPosition(), true);
            }
            if (ballVelocity.x < 0 && ball.getX() < getX()) {
                body.setLinearVelocity(ballVelocity.x, 0);
                state = GO;
            } else if (ballVelocity.x < 0 && ball.getX() > getX()) {
                body.setLinearVelocity(-ballVelocity.x, 0);
                state = GO;
            } else if (ballVelocity.x > 0 && ball.getX() < getX()) {
                body.setLinearVelocity(-ballVelocity.x, 0);
                state = GO;
            } else if (ballVelocity.x > 0 && ball.getX() > getX()) {
                body.setLinearVelocity(ballVelocity.x, 0);
                state = GO;
            }
            if (ballPosition.y - getY() > 0 && (ballPosition.y - getY()) * (ballPosition.y - getY()) +
                    (ballPosition.x - getX()) * (ballPosition.x - getX()) <= 1 && ballVelocity.y < 0 && ballVelocity.y > -10) {
                timeStartJump = TimeUtils.millis();
                state = JUMP;
                //body.setLinearVelocity(body.getLinearVelocity().x, 20);
                body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x, 20), body.getPosition(), true);
            }
            if (timeStartJump + timeJump < TimeUtils.millis() && state == JUMP) {
                body.setLinearVelocity(body.getLinearVelocity().x > 5 ? 5 : body.getLinearVelocity().x, -10);
                state = FALL;
            }
        }
    }
}
