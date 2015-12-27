package latmod.core.rendering;
import java.io.InputStream;

import org.lwjgl.opengl.*;

import latmod.lib.FastMap;

/** Made by LatvianModder */
@Deprecated
public class Shader
{
	private FastMap<String, Integer> uniformMap = new FastMap<String, Integer>();
	public final String name;
	public int programID;
	public int vertexID;
	public int fragmentID;
	public boolean failed = false;
	
	protected Shader(String s) { name = s; }
	//LatCore
	public Shader(String s, InputStream vert, InputStream frag)
	{
		this(s);
		String vertS = null;
		String fragS = null;
		
		try { byte[] b = new byte[vert.available()];
		vert.read(b); vertS = new String(b); vert.close(); } catch(Exception e)
		{ Renderer.logger.warning("Failed to load Vertex shader " + name); failed = true; }
		
		try { byte[] b = new byte[frag.available()];
		frag.read(b); fragS = new String(b); frag.close(); } catch(Exception e)
		{ Renderer.logger.warning("Failed to load Fragment shader " + name); failed = true; }
		
		if(!failed) createShaders(vertS, fragS);
	}
	
	public Shader(String name, String vert, String frag)
	{ this(name); createShaders(vert, frag); }
	
	protected void createShaders(String vert, String frag)
	{
		programID = createProgramID();
		vertexID = createVertexID();
		fragmentID = createFragmentID();
		
		if(!compileShader(vertexID, vert))
		{
			Renderer.logger.warning("Failed to compile Vertex code! " + name);
			Renderer.logger.warning("Error:\n" + getVertexError());
			failed = true;
		}
		
		if(!compileShader(fragmentID, frag))
		{
			Renderer.logger.warning("Failed to compile Fragment code! " + name);
			Renderer.logger.warning("Error:\n" + getFragmentError());
			failed = true;
		}
		
		if(!failed)
		{
			attach();
			validate();
			
			if(!failed) Renderer.logger.info("Loaded shader " + name);
			else Renderer.logger.warning("Failed loading shader " + name);
		}
	}
	
	protected String getVertexError()
	{ return GL20.glGetShaderInfoLog(vertexID, 1000).trim(); }
	
	protected String getFragmentError()
	{ return GL20.glGetShaderInfoLog(fragmentID, 1000).trim(); }

	protected int createProgramID()
	{ return GL20.glCreateProgram(); }
	
	protected int createVertexID()
	{ return GL20.glCreateShader(GL20.GL_VERTEX_SHADER); }
	
	protected int createFragmentID()
	{ return GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER); }
	
	protected boolean compileShader(int id, String code)
	{
		GL20.glShaderSource(id, code);
		GL20.glCompileShader(id);
		return GL20.glGetShader(id, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE;
	}
	
	protected boolean attach()
	{
		GL20.glAttachShader(programID, vertexID);
		GL20.glAttachShader(programID, fragmentID);
		GL20.glLinkProgram(programID);
		return true;
	}
	
	protected boolean validate()
	{
		GL20.glValidateProgram(programID);
		return true;
	}
	
	public void enable()
	{ if(!failed) GL20.glUseProgram(programID); }
	
	public void disable()
	{ disableShader(); }
	
	public void delete()
	{
		GL20.glDeleteShader(vertexID);
		GL20.glDeleteShader(fragmentID);
		GL20.glDeleteProgram(programID);
	}
	
	public static void disableShader()
	{ GL20.glUseProgram(0); }
	
	public void setUniformf(String s, float... f)
	{
		if(f.length == 0) return; int id = getUID(s);
		if(f.length == 1) GL20.glUniform1f(id, f[0]);
		else if(f.length == 2) GL20.glUniform2f(id, f[0], f[1]);
		else if(f.length == 3) GL20.glUniform3f(id, f[0], f[1], f[2]);
		else if(f.length == 4) GL20.glUniform4f(id, f[0], f[1], f[2], f[3]);
	}
	
	public void setUniformi(String s, int... i)
	{
		if(i.length == 0) return; int id = getUID(s);
		if(i.length == 1) GL20.glUniform1i(id, i[0]);
		else if(i.length == 2) GL20.glUniform2i(id, i[0], i[1]);
		else if(i.length == 3) GL20.glUniform3i(id, i[0], i[1], i[2]);
		else if(i.length == 4) GL20.glUniform4i(id, i[0], i[1], i[2], i[3]);
	}
	
	private int getUID(String s)
	{
		Integer i = uniformMap.get(s);
		if(i == null)
		{
			i = Integer.valueOf(GL20.glGetUniformLocation(programID, s));
			Renderer.logger.info("Shader " + name + " Uniform ID for " + s + " is " + i);
			uniformMap.put(s, i);
		}
		
		return i.intValue();
	}
}