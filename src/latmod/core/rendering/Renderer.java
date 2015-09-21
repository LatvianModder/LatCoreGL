package latmod.core.rendering;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.lwjgl.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import latmod.core.*;
import latmod.core.util.*;

/** Made by LatvianModder */
public class Renderer // Renderer3D
{
	public static final Logger logger = Logger.getLogger("Renderer");
	
	static
	{
		logger.setParent(LatCoreGL.logger);
		logger.info("LWJGL version: " + Sys.getVersion());
	}
	
	private static int startWidth, startHeight;
	private static boolean textureEnabled;
	
	public static final int FILLED = GL11.GL_FILL;
	public static final int POINTS = GL11.GL_POINT;
	public static final int LINES = GL11.GL_LINE;

	/** Clears screen and prepares screen for rendering
	 * <br>(must to be called before all rendering) */
	public static void clear()
	{ GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); }
	
	public static void background(float r, float g, float b)
	{ GL11.glClearColor(r, g, b, 1F); }
	
	/** Sets background color (also clears screen) */
	public static void background(int c)
	{
		if(c >= 0 && c <= 255) background(c / 255F, c / 255F, c / 255F); else
		background(LMColorUtils.getRed(c) / 255F, LMColorUtils.getGreen(c) / 255F, LMColorUtils.getBlue(c) / 255F);
	}
	
	public static void init(int w, int h)
	{
		startWidth = w;
		startHeight = h;
		GL11.glViewport(0, 0, w, h);
		enter2D();
	}
	
	/** Rendering for Guis, Hud */
	public static void enter2D()
	{
		int w = LatCoreGL.getWidth();
		int h = LatCoreGL.getHeight();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluOrtho2D(0F, w, h, 0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		//FIXME
		//Renderer3D.disableDepth();
		//Renderer3D.disableCulling();
		//Renderer3D.disable3DAlpha();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		currentTexture = -1;
	}
	
	public static void normal(double x, double y, double z)
	{ GL11.glNormal3d(x, y, z); }
	
	/** Pushes matrix */
	public static void push()
	{ GL11.glPushMatrix(); }
	
	/** Pushes enable bit */
	public static void pushEnableAttrib()
	{ GL11.glPushAttrib(GL11.GL_ENABLE_BIT); }
	
	/** Pops matrix */
	public static void pop()
	{ GL11.glPopMatrix(); }
	
	/** Pops attrib */
	public static void popAttrib()
	{ GL11.glPopAttrib(); }
	
	public static void scale(double x, double y)
	{ GL11.glScaled(x, y, 1D); }
	
	public static void scale(double s)
	{ scale(s, s); }
	
	public static void scale(Vertex v, double s)
	{ scale(v.x * s, v.y * s); }
	
	public static void vertex(double x, double y, double z)
	{ GL11.glVertex3d(x, y, z); }
	
	public static void vertex(double x, double y)
	{ GL11.glVertex2d(x, y); }
	
	public static void rotate(double x, double y, double z)
	{ rotateY(y); rotateX(x); rotateZ(z); }
	
	public static void rotate(double yaw, double pitch)
	{ rotateY(yaw); rotateY(pitch); }
	
	public static void rotateX(double f)
	{ GL11.glRotated(f, 1D, 0D, 0D); }
	
	public static void rotateY(double f)
	{ GL11.glRotated(f, 0D, 1D, 0D); }
	
	public static void rotateZ(double f)
	{ GL11.glRotated(f, 0D, 0D, 1D); }
	
	public static void translate(double x, double y)
	{ GL11.glTranslated(x, y, 0D); }
	
	public static void translate(Vertex v, double scale)
	{ translate(v.x * scale, v.y * scale); }
	
	/** - */
	public static void end()
	{ GL11.glEnd(); }//drawingLevel--; }
	
	/** - */
	public static void begin(int i)
	{ GL11.glBegin(i); }//drawingLevel++; }
	
	/** - */
	public static void beginQuads()
	{ begin(GL11.GL_QUADS); }
	
	public static void beginLines()
	{ begin(GL11.GL_LINE_STRIP); }
	
	public static void beginTriangles()
	{ begin(GL11.GL_TRIANGLES); }
	
	public static void beginPoints()
	{ begin(GL11.GL_POINTS); }
	
	/** Draws 2D rectangle. If textures are enabled, then draws textured rect*/
	public static void rect(double x, double y, double w, double h)
	{ rect(x, y, w, h, 0D, 0D, 1D, 1D); }
	
	public static void rect(double x, double y, double w, double h, double tx, double ty, double tw, double th)
	{
		beginQuads();
		
		if(textureEnabled)
		{
			vertexWithUV(x, y, tx, ty);
			vertexWithUV(x + w, y, tx + tw, ty);
			vertexWithUV(x + w, y + h, tx + tw, ty + th);
			vertexWithUV(x, y + h, tx, ty + th);
		}
		else
		{
			vertex(x, y);
			vertex(x + w, y);
			vertex(x + w, y + h);
			vertex(x, y + h);
		}
		
		end();
	}
	
	/** Draws 3D line in world */
	public static void line(double x, double y, double z, double x1, double y1, double z1)
	{ begin(GL11.GL_LINES); vertex(x, y, z); vertex(x1, y1, z1); end(); }
	
	/** Draws 2D line on screen */
	public static void line(double x, double y, double x1, double y1)
	{ begin(GL11.GL_LINES); vertex(x, y); vertex(x1, y1); end(); }
	
	public static ByteBuffer getScreenPixels()
	{
		int w = LatCoreGL.getWidth();
		int h = LatCoreGL.getHeight();
		ByteBuffer bb = BufferUtils.createByteBuffer(w * h * 4);
		GL11.glReadBuffer(GL11.GL_FRONT);
		GL11.glReadPixels(0, 0, w, h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		return bb;
	}
	
	public static void takeScreenshot()
	{ new ThreadScreenshot().start(); }
	
	private static int currentTexture = -1;
	
	/** Sets current texture id for GL */
	public static void bind(int i)
	{
		if(currentTexture == -1 || currentTexture != i)
		{
			currentTexture = i;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i);
		}
	}
	
	public static void textureVertex(double x, double y)
	{ GL11.glTexCoord2d(x, y); }
	
	public static void textureVertex(double x, double y, double z)
	{ GL11.glTexCoord3d(x, y, z); }
	
	public static void vertexWithUV(double x, double y, double tx, double ty)
	{ textureVertex(tx, ty); vertex(x, y); }
	
	public static void vertexWithUV(double x, double y, double z, double tx, double ty)
	{ textureVertex(tx, ty); vertex(x, y, z); }
	
	/** Disables textures */
	public static void disableTexture()
	{ GL11.glDisable(GL11.GL_TEXTURE_2D);
	textureEnabled = false; }
	
	/** Enables textures */
	public static void enableTexture()
	{ GL11.glEnable(GL11.GL_TEXTURE_2D);
	textureEnabled = true; }
	
	public static boolean textureEnabled()
	{ return textureEnabled; }
	
	public static void enableAlphaBlending()
	{ GL11.glEnable(GL11.GL_BLEND); }
	
	public static void enableSmooth()
	{
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_POINT_SMOOTH);
		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
	}
	
	public static void disableSmooth()
	{
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_POINT_SMOOTH);
		GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
	}
	
	public static boolean isEnabled(int i)
	{ return GL11.glIsEnabled(i); }
	
	/** Sets line width */
	public static void lineWidth(float f)
	{ GL11.glLineWidth(f); GL11.glPointSize(f); }

	public static int getStartHeight()
	{ return startHeight; }
	
	public static int getStartWidth()
	{ return startWidth; }
	
	/** Draws rectangle only from lines */
	public static void linedRect(double x, double y, double w, double h)
	{
		polyMode(LINES);
		rect(x, y, w, h);
		polyMode(FILLED);
	}
	
	public static void polyMode(int m)
	{ GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, m); }
	
	/** Draws circle with diameter 'd' */
	public static void drawPoly(double x, double y, double r, double detail)
	{
		if(detail < 2D) return;
		begin(GL11.GL_TRIANGLE_FAN);
		
		double step = 360F / detail;
		for(double i = 0F; i <= 360F; i += step)
		vertex(x + MathHelperLM.sin(i * MathHelperLM.RAD) * r, y + MathHelperLM.cos(i * MathHelperLM.RAD) * r);
		
		end();
	}
	
	/** Draws circle with diameter 'd' */
	public static void drawPoly(double x, double y, double r, double detail, int innerCol, int outterCol)
	{
		if(detail < 2D) return;
		
		begin(GL11.GL_TRIANGLE_FAN);
		LMColorUtils.setGLColor(innerCol);
		vertex(x, y);
		LMColorUtils.setGLColor(outterCol);
		double step = 360F / detail;
		for(double i = 0F; i <= 360F; i += step)
		vertex(x + MathHelperLM.sin(i * MathHelperLM.RAD) * r, y + MathHelperLM.cos(i * MathHelperLM.RAD) * r);
		end();
	}
	
	/** Draws arc with diameter 'd', rotation 'rot' and angle 'deg' <br>
	 * 'deg' must be >= 0 & <= 360 */
	public static void drawArc(double x, double y, double d, double deg, double rot)
	{
		deg = deg % 360F;
		rot = rot % 360F;
		if(deg < 1) return;
		
		begin(GL11.GL_POLYGON);
		vertex(x, y);
		
		for(double i = 0; i <= deg; i++)
		vertex(x - MathHelperLM.sinFromDeg(i + 180F + rot) * d/2F,
		y + MathHelperLM.cosFromDeg(i + 180F + rot) * d/2F);
		
		end();
	}
	
	/** Not finished yet! */
	public static void drawArc(double x, double y, double inD, double outD, double deg, double rot)
	{
		deg = MathHelperLM.clamp(deg, 0F, 360F);
		rot = (-rot - 90F) % 360F;
		if(deg == 0F || inD >= outD) return;
		
		begin(GL11.GL_POLYGON);
		
		double step = 10F;
		
		for(double i = 0; i <= deg; i += step)
		{
			double vx = x + MathHelperLM.sinFromDeg(i + rot) * outD / 2F;
			double vy = y + MathHelperLM.cosFromDeg(i + rot) * outD / 2F;
			vertex(vx, vy);
		}
		
		for(double i = deg; i > 0; i -= step)
		{
			double vx = x + MathHelperLM.sinFromDeg(i + rot) * inD / 2F;
			double vy = y + MathHelperLM.cosFromDeg(i + rot) * inD / 2F;
			vertex(vx, vy);
		}
		
		end();
	}
	
	public static void point(double x, double y)
	{ begin(GL11.GL_POINTS); vertex(x, y); end();}
	
	public static int createListID()
	{ return GL11.glGenLists(1); }
	
	public static int createAndUpdateListID()
	{ int id = createListID(); updateList(id); return id; }
	
	public static void updateList(int id)
	{ GL11.glNewList(id, GL11.GL_COMPILE); }
	
	public static void renderList(int id)
	{ GL11.glCallList(id); }

	public static void endList()
	{ GL11.glEndList(); }
	
	public static void deleteList(int id)
	{ GL11.glDeleteLists(id, 1); }

	public static void listMode(int mode)
	{ GL11.glListBase(mode); }
}