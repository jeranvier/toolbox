package jeranvier.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SlidingWindowInputStream extends InputStream{
	
	private static final int INFLATION_FACTOR = 5; // if we have a window of 6, we collect the elements 30 by 30 form the underlying stream
	InputStream in; //underlying stream
	private int frameSize; // the size of the window
	private int frameShift; // the amount by which the window is shifted ( allow overlapping windows)
	private int indexStartFrame; //index at which the next window is starting
	private byte[] read; // buffer
	private int offset; // amount of element that are still in the read array before reading new elements
	private int inCounter; //how many elements have been last read from the underlying stream
	
	public SlidingWindowInputStream(InputStream in, int frameSize, int frameShift) {
		this.in = in;
		this.frameSize = frameSize;
		this.frameShift = frameShift;
		indexStartFrame = 0;
		inCounter = 0;
		read = new byte[0];
	}

	@Override
	public int read() throws IOException {
		return 0;
	}

	@Override
	public int read(byte[] b) throws IOException {
		//if not enough data, read next chunk
		if(indexStartFrame + frameSize > offset + inCounter){
			if(read.length != 0){
				read = Arrays.copyOfRange(read, indexStartFrame, indexStartFrame + read.length);
				offset = read.length - indexStartFrame;
			}
			else{
				read = new byte[frameSize*INFLATION_FACTOR];
				offset = 0;
			}
			
			inCounter = in.read(read, offset, read.length - offset);
			indexStartFrame = 0;
		}
		
		//if we cannot write anymore complete frames
		if(inCounter + offset >= indexStartFrame + frameSize && inCounter == -1){
			b = new byte[0];
			return -1;
		}
		
		//if enough data, write frame, otherwise, we will have exit the method in the lines above
		else if(inCounter + offset >= indexStartFrame + frameSize){
			for(int i = indexStartFrame; i < indexStartFrame + frameSize; i++){
				b[i-indexStartFrame] = read[i];
			}
			indexStartFrame += frameShift;
			return frameSize;
		}
		else{ //to make it complete, but should never be used
			b = new byte[0];
			return -1;
		}
	}


}
