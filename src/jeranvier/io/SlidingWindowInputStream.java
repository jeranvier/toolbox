package jeranvier.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

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
	
	public static void main(String[] arg) throws UnsupportedAudioFileException, IOException{
		int frameSize = 128;
		int frameShift = 5;
		AudioInputStream audio = AudioSystem.getAudioInputStream(new File("/Users/ranvier/Desktop/tone600.wav"));
		SlidingWindowInputStream swis = new SlidingWindowInputStream(audio, frameSize, frameShift);
		byte[] read = new byte[frameSize];
		ByteBuffer bb = ByteBuffer.allocate(2);
//		bb.order(audio.getFormat().isBigEndian()?ByteOrder.BIG_ENDIAN:ByteOrder.LITTLE_ENDIAN);
		swis.read(read);
		for(int i=0; i<read.length/2; i++){
			bb.clear();
			bb.put(read[2*i]);
			bb.put(read[2*i+1]);
			System.out.println(bb.getShort(0));
//			System.out.println(read[2*i+1]);
		}
		
	}


}
