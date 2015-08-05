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

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IStreamCoder.Direction;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.impl.IDeviceAccessor;
import net.realmproject.dcm.event.Logging;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.dcm.stock.camera.Frame;
import net.realmproject.platform.util.RealmResponse;


public class IVP8Handler extends IHandler<HttpRequest> implements Logging {


    private IContainer container;
    private IContainerFormat format; 
    private IStream stream;
    private IStreamCoder coder;
    private ICodec codec;
    
    private long startTime = System.currentTimeMillis();
    private long frameCount = 0;
    
    IDeviceAccessor<Frame> camera;

    public IVP8Handler(String id, String deviceId, DeviceEventBus bus) {
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
    protected void onExecute(Action action, HttpRequest request) throws IOException, ServletException {
        HttpServletRequest req = request.getHttpRequest();
        HttpServletResponse resp = request.getHttpResponse();
        OutputStream out = new BufferedOutputStream(resp.getOutputStream());

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

        container = IContainer.make();
        format = webm();
        coder = IStreamCoder.make(Direction.ENCODING, format.getOutputDefaultVideoCodec());
        coder.open();
        stream = container.addNewStream(coder);
        codec = coder.getCodec();
        
        
        if (container.open(out, format) < 0) { 
        	getLog().error("Could not open container");
        	return;
        }
        if (container.writeHeader() < 0) { 
        	getLog().error("Could not write header");
        	return;
        }


        // loop until we're interrupted or the connection is closed, waiting for
        // new frames and transmitting them
        try {
            while (true) {
                sendFrame(frames.take());
            }
        }
        catch (IOException e) {
            // connection closed
            logger.info("Close HTTP connection from " + req.getRemoteAddr());
        }
        catch (InterruptedException e) {
            // Interrupted
            out.close();
            container.close();
            e.printStackTrace();
        }
        finally {
            camera.getListeners().remove(listener);
        }
    }

    private void sendFrame(byte[] frame) throws IOException {

        if (frame == null) return;

        IPacket packet = IPacket.make();
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(frame));
        IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);
        long timestamp = (System.currentTimeMillis() - startTime) * 1000;
        IVideoPicture picture = converter.toPicture(image, timestamp);
        
        if (frameCount % 150 == 0) {
        	picture.setKeyFrame(true);
        }
        picture.setQuality(0);
        
        coder.encodeVideo(packet, picture, 10240);
        picture.delete();
        
        if (packet.isComplete()) {
            container.writePacket(packet);
        }
        
        frameCount++;
    }
    
    public static void main(String[] args) {
		
    	IContainerFormat webm = webm();

    	System.out.println(webm.getOutputDefaultVideoCodec());
	}
    
    private static IContainerFormat webm() {
		
    	for (IContainerFormat cf : IContainerFormat.getInstalledOutputFormats()) {
    		if ("webm".equals(cf.getOutputFormatShortName())) { return cf; }
    	}
    	throw new IllegalArgumentException();
    	
	}

}
