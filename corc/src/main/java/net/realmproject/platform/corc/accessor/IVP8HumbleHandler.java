package net.realmproject.platform.corc.accessor;


import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.humble.video.Codec;
import io.humble.video.Container;
import io.humble.video.Encoder;
import io.humble.video.KeyValueBag;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;
import io.humble.video.MuxerStream;
import io.humble.video.PixelFormat;
import io.humble.video.Property;
import io.humble.video.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import io.humble.video.customio.HumbleIO;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.impl.IDeviceAccessor;
import net.realmproject.dcm.event.Logging;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.dcm.stock.camera.Frame;


public class IVP8HumbleHandler extends IHandler<HttpRequest> implements Logging {
   
    IDeviceAccessor<Frame> camera;
    
    public IVP8HumbleHandler(String id, String deviceId, DeviceEventBus bus) {
        this.camera = new IDeviceAccessor<>(id, deviceId, bus);
    }

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    // Modified from
    // https://github.com/x-nagasawa/kmkt/blob/master/src/com/github/kmkt/util/MjpegServlet.java
    @Override
    protected void onExecute(Action action, HttpRequest request) throws IOException, ServletException, InterruptedException {
        
    	long startTime = System.currentTimeMillis();
    	
    	HttpServletRequest req = request.getHttpRequest();
        HttpServletResponse resp = request.getHttpResponse();
        //OutputStream out = new BufferedOutputStream(resp.getOutputStream());
        OutputStream out = resp.getOutputStream();

        /**
         * Set up a queue to pull in Frame objects and store the image byte[].
         */
        //frame dropping by setting max queue size
        BlockingQueue<byte[]> frames = new LinkedBlockingQueue<>(3);
        frames.offer(camera.getState().image);
        frames.offer(camera.getState().image);
        Consumer<Frame> listener = (frame -> {
            if (frame.image == null) { return; }
            try {
                frames.put(frame.image);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        camera.getListeners().add(listener);
        
        logger.info("Accept HTTP connection from " + req.getRemoteAddr());
        

        //use a 1ms-based framerate, then we say that a new frame is at frame# now-start 
        Rational framerate = Rational.make(1d / 1000d);
        long frameCount = 1;
        
        
        String outputStreamURL = HumbleIO.map(out);
        
        /** 
         * First we create a muxer using the filename 
         */
        Muxer muxer = Muxer.make(outputStreamURL, null, "webm");
        muxer.setOutputBufferLength(64);
          
        System.out.println("flush_packets = " + muxer.getPropertyAsBoolean("flush_packets"));
        System.out.println("max_delay = " + muxer.getPropertyAsInt("max_delay"));
        System.out.println("rtbufsize = " + muxer.getPropertyAsInt("rtbufsize"));
        System.out.println("chunk_duration = " + muxer.getPropertyAsInt("chunk_duration"));
        System.out.println("packetsize = " + muxer.getPropertyAsInt("packetsize"));
        muxer.setProperty("flush_packets", true);
        muxer.setProperty("max_delay", 50);
        muxer.setProperty("rtbufsize", 65536);
        
        /** Now, we need to decide what type of codec to use to encode video. Muxers
         * have limited sets of codecs they can use. We're going to pick the first one that
         * works, or if the user supplied a codec name, we're going to force-fit that
         * in instead.
         */
        String codecname = null;
        final MuxerFormat format = muxer.getFormat();
        final Codec codec;
        if (codecname != null) {
          codec = Codec.findEncodingCodecByName(codecname);
        } else {
          codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());
        }
        
        System.out.println(codec);

        
        /**
         * Now that we know what codec, we need to create an encoder
         */
        Encoder encoder = Encoder.make(codec);
        
         /**
         * Video encoders need to know at a minimum:
         *   width
         *   height
         *   pixel format
         * Some also need to know frame-rate (older codecs that had a fixed rate at which video files could
         * be written needed this). There are many other options you can set on an encoder, but we're
         * going to keep it simpler here.
         */
        encoder.setWidth(512);
        encoder.setHeight(384);
        // We are going to use 420P as the format because that's what most video formats these days use
        final PixelFormat.Type pixelformat = PixelFormat.Type.PIX_FMT_YUV420P;
        encoder.setPixelFormat(pixelformat);
        encoder.setTimeBase(framerate);
        
        
        /** An annoynace of some formats is that they need global (rather than per-stream) headers,
         * and in that case you have to tell the encoder. And since Encoders are decoupled from
         * Muxers, there is no easy way to know this beyond 
         */
        if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER))
            encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);
        
        /** Open the encoder. */
        encoder.open(null, null);
        
        /** Add this stream to the muxer. */
        muxer.addNewStream(encoder);
        
        
        /** And open the muxer for business. */
        muxer.open(null, null);
        
        /** Next, we need to make sure we have the right MediaPicture format objects
         * to encode data with. Java (and most on-screen graphics programs) use some
         * variant of Red-Green-Blue image encoding (a.k.a. RGB or BGR). Most video
         * codecs use some variant of YCrCb formatting. So we're going to have to
         * convert. To do that, we'll introduce a MediaPictureConverter object later. object.
         */
        MediaPictureConverter converter = null;
        final MediaPicture picture = MediaPicture.make(
            encoder.getWidth(),
            encoder.getHeight(),
            pixelformat);
        picture.setTimeBase(framerate);
     

        
        // loop until we're interrupted or the connection is closed, waiting for
        // new frames and transmitting them
        final MediaPacket packet = MediaPacket.make();
        try {
        	while (true) {
        		
        		byte[] bytes = frames.take();
        		long time = timestamp(startTime);
        		if (time < 1) { continue; }
        		
            	/** Make the jpeg image && convert image to TYPE_3BYTE_BGR */
            	BufferedImage jpeg = ImageIO.read(new ByteArrayInputStream(bytes));
            	final BufferedImage screen = convertToType(jpeg, BufferedImage.TYPE_3BYTE_BGR);
                
            	/** This is LIKELY not in YUV420P format, so we're going to convert it using some handy utilities. */
				if (converter == null) {
					converter = MediaPictureConverterFactory.createConverter(screen, picture);
				}
				converter.toPicture(picture, screen, time);
            	
				do {
					encoder.encode(packet, picture);
					if (packet.isComplete()) {
						muxer.write(packet, false);
					}
				} while (packet.isComplete());
				out.flush();
				
				frameCount++;
				
        	}
        }
        catch (IOException | RuntimeException e) {
            // connection closed
            logger.info("Close HTTP connection from " + req.getRemoteAddr());
        }
        catch (InterruptedException e) {
            // Interrupted
            out.close();
            e.printStackTrace();
        }
        finally {
        	
        	/** Encoders, like decoders, sometimes cache pictures so it can do the right key-frame optimizations.
             * So, they need to be flushed as well. As with the decoders, the convention is to pass in a null
             * input until the output is not complete.
             */
//			do {
//				encoder.encode(packet, null);
//				if (packet.isComplete())
//					muxer.write(packet, false);
//			} while (packet.isComplete());
//			
//			muxer.close();
        	
            camera.getListeners().remove(listener);
        }
    }


	private static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
		BufferedImage image;

		// if the source image is already the target type, return the source
		// image

		if (sourceImage.getType() == targetType)
			image = sourceImage;

		// otherwise create a new image of the target type and draw the new
		// image

		else {
			image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}

		return image;
	}
    
    private long timestamp(long startTime) {
    	return (System.currentTimeMillis() - startTime);
    }
    


}

class VP8Streamer {
	
	public VP8Streamer() throws InterruptedException, IOException {
		


		
	}

}
