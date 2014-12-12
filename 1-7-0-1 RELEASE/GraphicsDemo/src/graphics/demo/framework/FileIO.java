package graphics.demo.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public interface FileIO 
{
	public InputStream readAsset(String fileName) throws IOException;
	public InputStream readFile(String fileName) throws IOException;
	public OutputStream writeFile(String fileName) throws IOException;
}