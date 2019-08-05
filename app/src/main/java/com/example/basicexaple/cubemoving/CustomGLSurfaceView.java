package com.example.basicexaple.cubemoving;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.KeyEvent;

import com.example.basicexaple.simplecube.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class CustomGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private Cube mCube;
    private float mXrot;
    private float mYrot;
    private float mXspeed;
    private float mYspeed;

    public CustomGLSurfaceView(Context context) {
        super(context);
        requestFocus();
        setFocusableInTouchMode(true);
        mCube = new Cube();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -10.0f);
        gl.glRotatef(mXrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(mYrot, 0.0f, 1.0f, 0.0f);
        mCube.draw(gl);
        gl.glLoadIdentity();
        mXrot += mXspeed;
        mYrot += mYspeed;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        System.out.println("Come Here for Enhanced");
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
            mYspeed -= 0.1f;
        else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
            mYspeed += 0.1f;
        else if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
            mXspeed -= 0.1f;
        else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
            mXspeed += 0.1f;
        return true;
    }


}
