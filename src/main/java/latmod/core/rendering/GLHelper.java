package latmod.core.rendering;

import latmod.lib.LMColorUtils;
import latmod.lib.MathHelperLM;
import latmod.lib.util.VecLM;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class GLHelper extends Renderer
{
	public static final int FILLED = GL11.GL_FILL;
	public static final int POINTS = GL11.GL_POINT;
	public static final int LINES = GL11.GL_LINE;
	
	public static abstract class Capability
	{
		public abstract void onSet();
		
		public void setDefault() { }
		
		public void push() { }
		
		public void pop() { }
	}
	
	public static abstract class CapabilityBool extends Capability
	{
		public boolean enabled;
		private boolean enabled0;
		
		public void set(boolean i)
		{
			if(setN(i))
			{
				onSet();
			}
		}
		
		public boolean setN(boolean i)
		{
			if(enabled != i)
			{
				enabled = i;
				return true;
			}
			
			return false;
		}
		
		public void enable()
		{ set(true); }
		
		public void disable()
		{ set(false); }
		
		@Override
		public void setDefault()
		{ set(false); }
		
		@Override
		public void push()
		{ enabled0 = enabled; }
		
		@Override
		public void pop()
		{ set(enabled0); }
	}
	
	public static class CapabilityGL extends CapabilityBool
	{
		public final int id;
		
		public CapabilityGL(int cap)
		{ id = cap; }
		
		@Override
		public void onSet()
		{
			if(enabled) GL11.glEnable(id);
			else GL11.glDisable(id);
		}
	}
	
	public static abstract class CapabilityInt extends Capability
	{
		public int value;
		private int value0;
		
		public void set(int i)
		{
			if(value != i)
			{
				value = i;
				onSet();
			}
		}
		
		@Override
		public void setDefault()
		{ set(0); }
		
		@Override
		public void push()
		{ value0 = value; }
		
		@Override
		public void pop()
		{ set(value0); }
	}
	
	public static abstract class CapabilityInt2 extends Capability
	{
		public int value1;
		public int value2;
		private int value01;
		private int value02;
		
		public void set(int i, int j)
		{
			if(value1 != i || value2 != j)
			{
				value1 = i;
				value2 = j;
				onSet();
			}
		}
		
		@Override
		public void setDefault()
		{ set(0, 0); }
		
		@Override
		public void push()
		{
			value01 = value1;
			value02 = value2;
		}
		
		@Override
		public void pop()
		{ set(value01, value02); }
	}
	
	public static abstract class CapabilityFloat extends Capability
	{
		public float value;
		private float value0;
		
		public void set(float i)
		{
			if(value != i)
			{
				value = i;
				onSet();
			}
		}
		
		@Override
		public void setDefault()
		{ set(0F); }
		
		@Override
		public void push()
		{ value0 = value; }
		
		@Override
		public void pop()
		{ set(value0); }
	}
	
	public static abstract class Color extends Capability
	{
		public float red = 1F, green = 1F, blue = 1F, alpha = 1F;
		private float red0 = 1F, green0 = 1F, blue0 = 1F, alpha0 = 1F;
		
		@Override
		public int hashCode()
		{ return LMColorUtils.getRGBAF(red, green, blue, alpha); }
		
		public void setF(float r, float g, float b, float a)
		{
			r = MathHelperLM.clampFloat(r, 0F, 1F);
			g = MathHelperLM.clampFloat(g, 0F, 1F);
			b = MathHelperLM.clampFloat(b, 0F, 1F);
			a = MathHelperLM.clampFloat(a, 0F, 1F);
			
			if(red != r || green != g || blue != b || alpha != a)
			{
				red = r;
				green = g;
				blue = b;
				alpha = a;
				onSet();
			}
		}
		
		public void setI(int r, int g, int b, int a)
		{ setF(r / 255F, g / 255F, b / 255F, a / 255F); }
		
		public void setI(int c, int a)
		{ setF(LMColorUtils.getRedF(c), LMColorUtils.getGreenF(c), LMColorUtils.getBlueF(c), a / 255F); }
		
		public void setI(int c)
		{ setI(c, LMColorUtils.getAlpha(c)); }
		
		@Override
		public void setDefault()
		{ setF(1F, 1F, 1F, 1F); }
		
		@Override
		public void push()
		{
			red0 = red;
			green0 = green;
			blue0 = blue;
			alpha0 = alpha;
		}
		
		@Override
		public void pop()
		{ setF(red0, green0, blue0, alpha0); }
	}
	
	public static final Color color = new Color()
	{
		@Override
		public void onSet()
		{ GL11.glColor4f(red, green, blue, alpha); }
	};
	
	/**
	 * Sets background color (also clears screen)
	 */
	public static final Color background = new Color()
	{
		@Override
		public void onSet()
		{ GL11.glClearColor(red, green, blue, 1F); }
	};
	
	public static final CapabilityGL texture = new CapabilityGL(GL11.GL_TEXTURE_2D);
	public static final CapabilityGL blending = new CapabilityGL(GL11.GL_BLEND);
	public static final CapabilityGL fog = new CapabilityGL(GL11.GL_FOG);
	public static final CapabilityGL alphaTest = new CapabilityGL(GL11.GL_ALPHA_TEST);
	public static final CapabilityGL depth = new CapabilityGL(GL11.GL_DEPTH_TEST);
	public static final CapabilityGL cullFace = new CapabilityGL(GL11.GL_CULL_FACE);
	
	public static final CapabilityInt polyMode = new CapabilityInt()
	{
		@Override
		public void onSet()
		{ GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, value); }
	};
	
	public static final CapabilityBool smooth = new CapabilityBool()
	{
		@Override
		public void onSet()
		{
			if(enabled)
			{
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glEnable(GL11.GL_POINT_SMOOTH);
				GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
			}
			else
			{
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glDisable(GL11.GL_POINT_SMOOTH);
				GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
			}
		}
	};
	
	public static final CapabilityBool depthMask = new CapabilityBool()
	{
		@Override
		public void onSet()
		{ GL11.glDepthMask(enabled); }
	};
	
	/**
	 * Sets line width
	 */
	public static final CapabilityFloat lineWidth = new CapabilityFloat()
	{
		@Override
		public void onSet()
		{
			GL11.glLineWidth(value);
			GL11.glPointSize(value);
		}
		
		@Override
		public void setDefault()
		{ set(1F); }
	};
	
	/**
	 * Sets current texture id for GL
	 */
	public static final CapabilityInt bound_texture = new CapabilityInt()
	{
		@Override
		public void onSet()
		{ GL11.glBindTexture(GL11.GL_TEXTURE_2D, value); }
		
		@Override
		public void setDefault()
		{ set(0); }
	};
	
	public static final CapabilityInt2 blendFunc = new CapabilityInt2()
	{
		@Override
		public void onSet()
		{ GL11.glBlendFunc(value1, value2); }
		
		@Override
		public void setDefault()
		{ set(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); }
	};
	
	public static final CapabilityBool warp_texture = new CapabilityBool()
	{
		@Override
		public void onSet()
		{
			int i = enabled ? GL11.GL_REPEAT : GL12.GL_CLAMP_TO_EDGE;
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, i);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, i);
		}
		
		@Override
		public void setDefault()
		{ set(true); }
	};
	
	public static final CapabilityBool blured_texture = new CapabilityBool()
	{
		@Override
		public void onSet()
		{
			int i = enabled ? GL11.GL_LINEAR : GL11.GL_NEAREST;
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, i);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, i);
		}
		
		@Override
		public void setDefault()
		{ set(false); }
	};
	
	/**
	 * Clears screen and prepares screen for rendering
	 * <br>(must to be called before all rendering)
	 */
	public static void clear()
	{ GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); }
	
	public static void normal(double x, double y, double z)
	{ GL11.glNormal3d(x, y, z); }
	
	/**
	 * Pushes matrix
	 */
	public static void push()
	{ GL11.glPushMatrix(); }
	
	/**
	 * Pushes enable bit
	 */
	public static void pushAttrib()
	{ GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT); }
	
	/**
	 * Pops matrix
	 */
	public static void pop()
	{ GL11.glPopMatrix(); }
	
	/**
	 * Pops attrib
	 */
	public static void popAttrib()
	{ GL11.glPopAttrib(); }
	
	public static void scale(double x, double y, double z)
	{ GL11.glScaled(x, y, 1D); }
	
	public static void scale(double s)
	{ scale(s, s, s); }
	
	public static void rotate(double x, double y, double z)
	{
		rotateY(y);
		rotateX(x);
		rotateZ(z);
	}
	
	public static void rotate(double yaw, double pitch)
	{
		rotateY(yaw);
		rotateY(pitch);
	}
	
	public static void rotateX(double f)
	{ GL11.glRotated(f, 1D, 0D, 0D); }
	
	public static void rotateY(double f)
	{ GL11.glRotated(f, 0D, 1D, 0D); }
	
	public static void rotateZ(double f)
	{ GL11.glRotated(f, 0D, 0D, 1D); }
	
	public static void translate(double x, double y)
	{ GL11.glTranslated(x, y, 0D); }
	
	public static void translate(VecLM v, double scale)
	{ translate(v.x * scale, v.y * scale); }
}