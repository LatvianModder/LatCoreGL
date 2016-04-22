package latmod.core.rendering;

import latmod.core.LatCoreGL;
import latmod.lib.MathHelperLM;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

/**
 * Made by LatvianModder
 */
public class Renderer // Renderer3D
{
	public static int drawingLevel = 0;
	
	public static void init()
	{
	}
	
	/**
	 * Rendering for Guis, Hud
	 */
	public static void enter2D()
	{
		int w = LatCoreGL.window.getWidth();
		int h = LatCoreGL.window.getHeight();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		//FIXME: GLU.gluOrtho2D(0F, w, h, 0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GLHelper.depth.disable();
		GLHelper.cullFace.disable();
		GLHelper.alphaTest.enable();
		GLHelper.fog.disable();
		
		drawingLevel = 0;
		GLHelper.color.setDefault();
		GLHelper.bound_texture.set(0);
	}
	
	public static void vertex(double x, double y, double z)
	{ GL11.glVertex3d(x, y, z); }
	
	public static void vertex(double x, double y)
	{ GL11.glVertex2d(x, y); }
	
	/**
	 * -
	 */
	public static void end()
	{
		GL11.glEnd();
		drawingLevel--;
	}
	
	/**
	 * -
	 */
	public static void begin(int i)
	{
		GL11.glBegin(i);
		drawingLevel++;
	}
	
	/**
	 * -
	 */
	public static void beginQuads()
	{ begin(GL11.GL_QUADS); }
	
	public static void beginLines()
	{ begin(GL11.GL_LINE_STRIP); }
	
	public static void beginTriangles()
	{ begin(GL11.GL_TRIANGLES); }
	
	public static void beginPoints()
	{ begin(GL11.GL_POINTS); }
	
	/**
	 * Draws 2D rectangle. If textures are enabled, then draws textured rect
	 */
	public static void rect(double x, double y, double w, double h)
	{ rect(x, y, w, h, 0D, 0D, 1D, 1D); }
	
	public static void rect(double x, double y, double w, double h, double tx, double ty, double tw, double th)
	{
		beginQuads();
		
		if(GLHelper.texture.enabled)
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
	
	/**
	 * Draws 3D line in world
	 */
	public static void line(double x, double y, double z, double x1, double y1, double z1)
	{
		begin(GL11.GL_LINES);
		vertex(x, y, z);
		vertex(x1, y1, z1);
		end();
	}
	
	/**
	 * Draws 2D line on screen
	 */
	public static void line(double x, double y, double x1, double y1)
	{
		begin(GL11.GL_LINES);
		vertex(x, y);
		vertex(x1, y1);
		end();
	}
	
	public static ByteBuffer getScreenPixels()
	{
		int w = LatCoreGL.window.getWidth();
		int h = LatCoreGL.window.getHeight();
		ByteBuffer bb = BufferUtils.createByteBuffer(w * h * 4);
		GL11.glReadBuffer(GL11.GL_FRONT);
		GL11.glReadPixels(0, 0, w, h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		return bb;
	}
	
	public static void takeScreenshot()
	{ new ThreadScreenshot().start(); }
	
	public static void textureVertex(double x, double y)
	{ GL11.glTexCoord2d(x, y); }
	
	public static void textureVertex(double x, double y, double z)
	{ GL11.glTexCoord3d(x, y, z); }
	
	public static void vertexWithUV(double x, double y, double tx, double ty)
	{
		textureVertex(tx, ty);
		vertex(x, y);
	}
	
	public static void vertexWithUV(double x, double y, double z, double tx, double ty)
	{
		textureVertex(tx, ty);
		vertex(x, y, z);
	}
	
	/**
	 * Draws rectangle only from lines
	 */
	public static void linedRect(double x, double y, double w, double h)
	{
		GLHelper.polyMode.set(GLHelper.LINES);
		rect(x, y, w, h);
		GLHelper.polyMode.set(GLHelper.FILLED);
	}
	
	/**
	 * Draws circle with diameter 'd'
	 */
	public static void drawPoly(double x, double y, double r, double detail)
	{
		if(detail < 2D) return;
		begin(GL11.GL_TRIANGLE_FAN);
		
		double step = 360F / detail;
		for(double i = 0F; i <= 360F; i += step)
			vertex(x + MathHelperLM.sin(i * MathHelperLM.RAD) * r, y + MathHelperLM.cos(i * MathHelperLM.RAD) * r);
		
		end();
	}
	
	/**
	 * Draws circle with diameter 'd'
	 */
	public static void drawPoly(double x, double y, double r, double detail, int innerCol, int outterCol)
	{
		if(detail < 2D) return;
		
		begin(GL11.GL_TRIANGLE_FAN);
		GLHelper.color.setI(innerCol);
		vertex(x, y);
		GLHelper.color.setI(outterCol);
		double step = 360F / detail;
		for(double i = 0F; i <= 360F; i += step)
			vertex(x + MathHelperLM.sin(i * MathHelperLM.RAD) * r, y + MathHelperLM.cos(i * MathHelperLM.RAD) * r);
		end();
	}
	
	/**
	 * Draws arc with diameter 'd', rotation 'rot' and angle 'deg' <br>
	 * 'deg' must be >= 0 & <= 360
	 */
	public static void drawArc(double x, double y, double d, double deg, double rot)
	{
		deg = deg % 360F;
		rot = rot % 360F;
		if(deg < 1) return;
		
		begin(GL11.GL_POLYGON);
		vertex(x, y);
		
		for(double i = 0; i <= deg; i++)
			vertex(x - MathHelperLM.sinFromDeg(i + 180F + rot) * d / 2F, y + MathHelperLM.cosFromDeg(i + 180F + rot) * d / 2F);
		
		end();
	}
	
	/**
	 * Not finished yet!
	 */
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
	{
		begin(GL11.GL_POINTS);
		vertex(x, y);
		end();
	}
	
	public static int createListID()
	{ return GL11.glGenLists(1); }
	
	public static int createAndUpdateListID()
	{
		int id = createListID();
		updateList(id);
		return id;
	}
	
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