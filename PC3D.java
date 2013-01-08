// by Peter Li
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// v1.0 - 3/4/12
// - Initial Release
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

import java.awt.*;

public class PC3D
{
    public static PCPC c;
    public static Graphics g;
    public static int cWidth;
    public static int cHeight;

    public static int num = 0;
    public static int lines = 0;
    public static new3DD obj[];
    public static new3D disp[];
    public static new3DD cam = new new3DD ();
    public static new3DD angle = new new3DD ();
    public static new3DD theta = new new3DD ();
    public static new3DD d = new new3DD ();
    public static int line1[];
    public static int line2[];
    public static double depth = 0;
    public static int fov;
    public static int fps;

    public static int frameLimit;
    public static long frameTimer;
    public static long now;

    public static String s[];

    public static void main (String[] args) throws Exception
    {
	c = new PCPC ();
	g = c.getGraphics ();
	cWidth = c.getCanvasWidth ();
	cHeight = c.getCanvasHeight ();

	cam.x = 70;
	cam.y = 70;
	cam.z = 0;

	theta.x = 0;
	theta.y = 0;
	theta.z = 0;

	getSettings ();
	run ();
    }


    public static void getSettings () throws Exception
    {
	s = c.readFile ("PC3Dsettings.ini", 1).split (" ");
	fov = Integer.parseInt (s [1]);
	s = c.readFile ("PC3Dsettings.ini", 2).split (" ");
	fps = Integer.parseInt (s [1]);
	frameLimit = 1000 / fps;
    }


    public static void get3D () throws Exception
    {
	num = Integer.parseInt (c.readFile ("PC3Dobject.txt", 1));
	lines = Integer.parseInt (c.readFile ("PC3Dobject.txt", num + 2));

	obj = new new3DD [num];
	disp = new new3D [num];
	line1 = new int [lines];
	line2 = new int [lines];

	//gets coords
	for (int i = 0 ; i < num ; i++)
	{
	    obj [i] = new new3DD ();
	    disp [i] = new new3D ();
	    String temp[] = c.readFile ("PC3Dobject.txt", i + 2).split (" ");

	    obj [i].x = Double.parseDouble (temp [0]);
	    obj [i].y = Double.parseDouble (temp [1]);
	    obj [i].z = Double.parseDouble (temp [2]);
	}

	//gets lines
	for (int i = 0 ; i < lines ; i++)
	{
	    String temp[] = c.readFile ("PC3Dobject.txt", i + num + 3).split (" ");

	    line1 [i] = Integer.parseInt (temp [0]);
	    line2 [i] = Integer.parseInt (temp [1]);
	}
    }


    public static void setBackground ()
    {
	g.setColor (Color.BLACK);
	g.fillRect (0, 0, cWidth, cHeight);

	g.setColor (Color.WHITE);
	g.drawString ("# of Points: " + Integer.toString (num), 10, 20);
	g.drawString ("Angle x: " + Double.toString (theta.x), 10, 40);
	g.drawString ("Angle y: " + Double.toString (theta.y), 100, 40);
	g.drawString ("Angle z: " + Double.toString (theta.z), 190, 40);
	g.drawString ("Cam x: " + Double.toString (cam.x), 10, 60);
	g.drawString ("Cam y: " + Double.toString (cam.y), 100, 60);
	g.drawString ("Cam z: " + Double.toString (cam.z), 190, 60);
	g.drawString ("FOV: " + Integer.toString (fov), 10, 80);
	g.drawString ("FPS: " + Integer.toString (fps), 10, 100);
    }


    public static void getInput ()
    {
	int key = 0;

	key = c.getKey ();

	if (key == 65) //a
	{
	    cam.x--;
	}
	else if (key == 68) //d
	{
	    cam.x++;
	}
	else if (key == 87) //w
	{
	    cam.z++;
	}
	else if (key == 83) //s
	{
	    cam.z--;
	}
	else if (key == 69) //e
	{
	    cam.y++;
	}
	else if (key == 81) //q
	{
	    cam.y--;
	}
	else if (key == 39) //right
	{
	    theta.y++;
	}
	else if (key == 37) //left
	{
	    theta.y--;
	}
	else if (key == 38) //up
	{
	    theta.x++;
	}
	else if (key == 40) //down
	{
	    theta.x--;
	}
	else if (key == 32) //space
	{
	    fov++;

	    if (fov > 300)
	    {
		fov = 300;
	    }
	}
	else if (key == 17) //ctrl
	{
	    fov--;

	    if (fov < 10)
	    {
		fov = 10;
	    }
	}
    }


    public static void getCommand ()
    {
	int key = 0;

	key = c.getKey ();

	if (key == 10) //enter
	{
	    String str = c.getText ();

	    s = str.split (" ");

	    if (s [0].charAt (0) == 'a' && s [0].charAt (1) == 'p')
	    {

	    }
	    else if (s [0].charAt (0) == 'a' && s [0].charAt (1) == 'l')
	    {

	    }
	    else if (s [0].charAt (0) == 'f' && s [0].charAt (1) == 'p' && s [0].charAt (2) == 's')
	    {
		fps = Integer.parseInt (s [1]);
		frameLimit = 1000 / fps;
	    }
	    else if (s [0].charAt (0) == 'p' && s [0].charAt (1) == 'o' && s [0].charAt (2) == 's')
	    {
		int i = Integer.parseInt (s [1]);
		System.out.println (obj [i].x + " " + obj [i].y + " " + obj [i].z);
	    }
	    
	    str = "";
	}
    }


    public static void draw3D ()
    {
	g.setColor (Color.WHITE);

	angle.x = Math.toRadians (theta.x % 360);
	angle.y = Math.toRadians (theta.y % 360);
	angle.z = Math.toRadians (theta.z % 360);
	depth = 2 * Math.atan (1 / (360 - fov));

	for (int i = 0 ; i < num ; i++)
	{
	    //point vectors
	    d.x = Math.cos (angle.y) * (Math.sin (angle.z) * (obj [i].y - cam.y) + Math.cos (angle.z) * (obj [i].x - cam.x)) - Math.sin (angle.y) * (obj [i].z - cam.z);
	    d.y = Math.sin (angle.x) * (Math.cos (angle.y) * (obj [i].z - cam.z) + Math.sin (angle.y) * (Math.sin (angle.z) * (obj [i].y - cam.y) + Math.cos (angle.z) * (obj [i].x - cam.x))) + Math.cos (angle.x) * (Math.cos (angle.z) * (obj [i].y - cam.y) - Math.sin (angle.z) * (obj [i].x - cam.x));
	    d.z = Math.cos (angle.x) * (Math.cos (angle.y) * (obj [i].z - cam.z) + Math.sin (angle.y) * (Math.sin (angle.z) * (obj [i].y - cam.y) + Math.cos (angle.z) * (obj [i].x - cam.x))) - Math.sin (angle.x) * (Math.cos (angle.z) * (obj [i].y - cam.y) - Math.sin (angle.z) * (obj [i].x - cam.x));

	    //3d to 2d projection
	    disp [i].x = (int) ((d.x - depth) * ((360 - fov) / d.z)) + 300;
	    disp [i].y = (int) ((d.y - depth) * ((360 - fov) / d.z)) + 300;

	    //labels vertices
	    g.drawString (Integer.toString (i), disp [i].x, disp [i].y);
	}

	//draws lines
	for (int i = 0 ; i < lines ; i++)
	{
	    g.drawLine (disp [line1 [i] - 1].x, disp [line1 [i] - 1].y, disp [line2 [i] - 1].x, disp [line2 [i] - 1].y);
	}
    }


    public static void run () throws Exception
    {
	get3D ();                                   //retrieves coords from file

	frameTimer = System.currentTimeMillis ();

	while (true)
	{
	    now = System.currentTimeMillis ();
	    if (now - frameTimer > frameLimit)      //limits fps
	    {
		frameTimer = now;

		setBackground ();                   //stat display
		getInput ();                        //movement & camera manipulation
		draw3D ();                          //draws object
		//getCommand ();                      //command console
		c.ViewUpdate ();                    //updates screen
	    }
	}
    }
}
